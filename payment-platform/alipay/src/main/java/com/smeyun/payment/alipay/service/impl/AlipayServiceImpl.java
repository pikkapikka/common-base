/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: AlipayServiceImpl.java
 * Author:   qxf
 * Date:     2016年11月16日 下午5:44:01
 */
package com.smeyun.payment.alipay.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.smeyun.payment.alipay.dao.IAlipayDao;
import com.smeyun.payment.alipay.entity.AlipayRequest;
import com.smeyun.payment.alipay.entity.PayNotifyData;
import com.smeyun.payment.alipay.entity.PayRequestData;
import com.smeyun.payment.alipay.entity.PayResponseData;
import com.smeyun.payment.alipay.service.IAlipayService;
import com.smeyun.payment.alipay.util.AlipayCore;
import com.smeyun.payment.alipay.util.CommonUtil;
import com.smeyun.payment.alipay.util.Constant;
import com.smeyun.platform.util.base.entity.BaseResponse;
import com.smeyun.platform.util.common.constant.PlatformConstant;
import com.smeyun.platform.util.common.encrypt.Md5Encrypt;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;
import com.smeyun.platform.util.common.http.HttpClientUtil;
import com.smeyun.platform.util.common.http.HttpMethod;
import com.smeyun.platform.util.common.http.bean.SmeHttpResponse;
import com.smeyun.platform.util.common.util.PlatformUtil;

/**
 * 支付服务实现类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service("alipayService")
public class AlipayServiceImpl implements IAlipayService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayServiceImpl.class);
    
    /*
     * 支付宝收款账号
     */
    @Value("${payment.alipay.account}")
    private String account;
    
    /*
     * 身份ID
     */
    @Value("${payment.alipay.partner}")
    private String partner;
    
    /*
     * 密钥
     */
    @Value("${payment.alipay.key}")
    private String key;
    
    /*
     * 回调URL
     */
    @Value("${payment.alipay.notifyUrl}")
    private String notifyUrl;
    
    /*
     * 重试次数
     */
    @Value("${payment.alipay.notify.max.times}")
    private int maxTimes;
    
    /*
     * 休眠时间
     */
    @Value("${payment.alipay.notify.sleep.milliseconds}")
    private long sleepTime;
    
    // 数据库操作实例
    @Autowired
    private IAlipayDao payDao;
    
    /**
     * @see com.smeyun.payment.alipay.service.IAlipayService#payDone(com.smeyun.payment.alipay.entity.AlipayRequest)<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public String payDone(AlipayRequest request)
    {
        
        // 保存到数据库
        long id = payDao.saveRequestData(new PayRequestData(request));
        
        switch (CommonUtil.parseClientType(request.getClientType()))
        {
            case PC:
            {
                return tryPayOnPc(request, id);
            }
            
            case PHONE:
            {
                //TODO
                return null;
            }
            
            default:
            {
                return null;
            }
        }
    }
    
    private String tryPayOnPc(AlipayRequest request, long id)
    {
        try
        {
            // 处理PC端的请求
            return payOnPc(request, id);
        }
        catch (SmeyunUncheckedException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("create alipay form falied:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /**
     * @see com.smeyun.payment.alipay.service.IAlipayService#dealNotification(java.util.Map)<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public String dealNotification(Map<String, String> notifyParams)
    {
        if (!checkAccountCorrect(notifyParams))
        {
            LOGGER.error("accout match failed, receive account:({}), correct accout:({}).",
                    notifyParams.get("seller_email"),
                    account);
            return Constant.RET_FAIL;
        }
        
        // 检查交易记录是否更改是否有效
        long id = getRequestId(notifyParams);
        PayRequestData reqData = checkTradeCorrect(id, notifyParams);
        
        // 保存到数据库
        saveNotifyData(reqData, notifyParams);
        
        // 远程通知回调地址
        notifyCustom(reqData, notifyParams);
        return Constant.RET_SUCCESS;
    }
    
    private long getRequestId(Map<String, String> notifyParams)
    {
        String extraParam = notifyParams.get("extra_common_param");
        LOGGER.debug("extra parameter is {}.", extraParam);
        
        try
        {
            return Long.parseLong(extraParam);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("get request ID failed, param=" + extraParam, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /*
     * 在PC端进行支付
     * 
     * @param request 请求消息
     * @param id 请求ID
     * @return  String 支付的form数据
     * 
     * @author qxf
     */
    private String payOnPc(AlipayRequest request, long id)
    {
        Map<String, String> sParaTemp = new HashMap<String, String>();
        
        //增加基本配置
        sParaTemp.put("seller_email", account);
        sParaTemp.put("partner", partner);
        sParaTemp.put("keys", key);
        sParaTemp.put("agent", partner);
        sParaTemp.put("body", request.getBody()); // 填写在跳到支付宝页面上显示的付款内容信息
        sParaTemp.put("subject", request.getSubject()); // 填写在跳到支付宝页面上显示的付款标题信息  
        sParaTemp.put("out_trade_no", request.getOrderCode()); // 外部交易号,最好具有唯一性,在获取支付宝发来的付款信息时使用. 
        sParaTemp.put("payment_type", Constant.PAYMENT_TYPE);
        sParaTemp.put("price", request.getPayPrice());// 订单金额信息    
        sParaTemp.put("quantity", String.valueOf(request.getQuantity())); // 订单商品数量 
        
        sParaTemp.put("service", Constant.SERVICE_PC); // 即时到账
        sParaTemp.put("return_url", request.getReturnUrl());
        sParaTemp.put("notify_url", notifyUrl);
        sParaTemp.put("extra_common_param", String.valueOf(id));
        sParaTemp.put("_input_charset", PlatformConstant.CHARSET_UTF8);
        return buildForm(sParaTemp, Constant.PAY_GATEWAY_PC, HttpMethod.POST, key);
    }
    
    private String buildForm(Map<String, String> paraTemp, String gateway, HttpMethod method, String key)
    {
        Map<String, String> sPara = buildRequestPara(paraTemp, key);
        StringBuilder sbHtml = new StringBuilder();
        
        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + gateway + "_input_charset="
                + PlatformConstant.CHARSET_UTF8 + "\" method=\"" + method.getValue() + "\">");
        
        for (Entry<String, String> entity : sPara.entrySet())
        {
            String name = entity.getKey();
            String value = entity.getValue();
            
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
        
        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"submit\" style=\"display:none;\"></form>");
        return sbHtml.toString();
    }
    
    private Map<String, String> buildRequestPara(Map<String, String> params, String key)
    {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(params);
        
        //生成签名结果
        String mysign = AlipayCore.buildMysign(sPara);
        
        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        if (!sPara.get("service").equals(Constant.SERVICE_PHONE)
                && !sPara.get("service").equals(Constant.SERVICE_PHONE_AUTH))
        {
            sPara.put("sign_type", Constant.SIGN_TYPE);
        }
        
        return sPara;
    }
    
    private boolean checkAccountCorrect(Map<String, String> notifyParams)
    {
        return account.equals(notifyParams.get("seller_email"));
    }
    
    private PayRequestData checkTradeCorrect(long id, Map<String, String> notifyParams)
    {
        PayRequestData data = payDao.queryRequestDataById(id);
        if (null == data)
        {
            LOGGER.error("can not find the request information, reqId={}.", id);
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        if (data.getPrice().equals(notifyParams.get("price"))
                && String.valueOf(data.getQuantity()).equals(notifyParams.get("quantity")))
        {
            return data;
        }
        else
        {
            LOGGER.error("the notify information is not match the request. request={}.", data);
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
    }
    
    /*
     * 创建通知入库数据
     * 
     * @param reqId 请求ID
     * @param notifyParams 响应参数
     * @return  PayNotifyData
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    private PayNotifyData createNotifyData(long reqId, Map<String, String> notifyParams)
    {
        PayNotifyData data = new PayNotifyData();
        
        data.setAliTradeNo(notifyParams.get("trade_no"));
        data.setDiscount(notifyParams.get("discount"));
        data.setNotifyId(notifyParams.get("notify_id"));
        data.setNotifyTime(notifyParams.get("notify_time"));
        data.setOrderCode(notifyParams.get("out_trade_no"));
        
        data.setPayStatus(true);
        data.setPayTime(notifyParams.get("gmt_payment"));
        data.setReceiveTime(new Date());
        data.setReqId(reqId);
        data.setTotalFee(notifyParams.get("total_fee"));
        data.setTradeStatus(notifyParams.get("trade_status"));
        
        return data;
    }
    
    /*
    * 保存消息到数据库中
    * 
    * @param reqId 请求ID
    * @param notifyParams 参数
    *
    * @author qxf
    * @see   [类、类#方法、类#成员]
    * @since [起始版本]
    */
    private void saveNotifyData(PayRequestData data, Map<String, String> notifyParams)
    {
        PayNotifyData notifyData = payDao.queryNotifyDataByReqId(data.getId());
        if (null == notifyData)
        {
            notifyData = createNotifyData(data.getId(), notifyParams);
            payDao.saveNotifyData(notifyData);
        }
        else
        {
            // TODO 暂时都为true
            notifyData.setPayStatus(true);
            notifyData.setReceiveTime(new Date());
            notifyData.setTradeStatus(notifyParams.get("trade_status"));
            
            // 更新到数据库
            payDao.updateNotifyData(notifyData);
        }
    }
    
    private void notifyCustom(PayRequestData data, Map<String, String> notifyParams)
    {
        String tradeStatus = notifyParams.get("trade_status");
        LOGGER.info("the status of pay request ({}) is ({}), orderCode is ({}), tradeNo is ({}).",
                data.getId(),
                tradeStatus,
                notifyParams.get("out_trade_no"),
                notifyParams.get("trade_no"));
        
        // 支付成功时的处理
        if (Constant.VAL_PAY_SUCCESS.equals(tradeStatus))
        {
            BaseResponse response = new BaseResponse();
            response.setData(createReponseData(notifyParams, Constant.RESPONSE_SUCCESS));
            
            // 回调通知接口
            tryCallRemote(data.getNotifyUrl(), response);
            return;
        }
        else if (!Constant.VAL_PAY_FINISH.equals(tradeStatus))
        {
            LOGGER.error("alipay failed, notification is ({}).", notifyParams);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR);
        }
    }
    
    private PayResponseData createReponseData(Map<String, String> notifyParams, String ret)
    {
        PayResponseData data = new PayResponseData();
        
        data.setAliTradeNo(notifyParams.get("trade_no"));
        data.setDiscount(notifyParams.get("discount"));
        data.setOrderCode(notifyParams.get("out_trade_no"));
        data.setPrice(notifyParams.get("price"));
        data.setQuantity(notifyParams.get("quantity"));
        data.setResult(ret);
        data.setTotalPrice(notifyParams.get("total_fee"));
        data.setRandom(PlatformUtil.generateRandomString());
        
        // 生成校验盐值
        StringBuilder builder = new StringBuilder();
        builder.append(data.getAliTradeNo());
        builder.append(data.getDiscount());
        builder.append(data.getOrderCode());
        builder.append(data.getPrice());
        builder.append(data.getQuantity());
        builder.append(data.getResult());
        builder.append(data.getTotalPrice());
        builder.append(data.getRandom());
        
        // 添加校验值
        data.setSalt(Md5Encrypt.md5(builder.toString()));
        return data;
    }
    
    private void tryCallRemote(String url, BaseResponse response)
    {
        for (int idx = 0; idx < maxTimes; idx++)
        {
            try
            {
                SmeHttpResponse resp = HttpClientUtil.postJsonDataToUrl(url, JSONObject.toJSONString(response));
                if (PlatformUtil.isHttpSuc(resp.getStatus()))
                {
                    LOGGER.info("call remote site success, url=" + url);
                    return;
                }
                else
                {
                    LOGGER.error("call remote site failed, url={}, httpstatus={}.", url, resp.getStatus());
                }
                
            }
            catch (Exception e)
            {
                LOGGER.error("call remote site failed, url=" + url, e);
            }
            
            PlatformUtil.sleep(sleepTime);
        }
    }
}
