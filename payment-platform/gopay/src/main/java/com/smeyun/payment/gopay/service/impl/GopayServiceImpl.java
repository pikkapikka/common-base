/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: GopayServiceImple.java
 * Author:   qxf
 * Date:     2016年11月24日 下午5:10:21
 */
package com.smeyun.payment.gopay.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.smeyun.payment.gopay.dao.IGopayDao;
import com.smeyun.payment.gopay.entity.GopayRequest;
import com.smeyun.payment.gopay.entity.PayNotifyData;
import com.smeyun.payment.gopay.entity.PayRequestData;
import com.smeyun.payment.gopay.entity.PayResponseData;
import com.smeyun.payment.gopay.service.IGopayService;
import com.smeyun.payment.gopay.util.GopayConstant;
import com.smeyun.payment.gopay.util.GopayUtil;
import com.smeyun.platform.util.base.entity.BaseResponse;
import com.smeyun.platform.util.common.encrypt.Md5Encrypt;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;
import com.smeyun.platform.util.common.http.HttpClientUtil;
import com.smeyun.platform.util.common.http.HttpMethod;
import com.smeyun.platform.util.common.http.bean.SmeHttpResponse;
import com.smeyun.platform.util.common.util.PlatformUtil;

/**
 * 服务实现类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service("gopayServiceImpl")
public class GopayServiceImpl implements IGopayService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GopayServiceImpl.class);
    
    @Value("${payment.gopay.merchatId}")
    private String merchantId;
    
    @Value("${payment.gopay.virCardNoIn}")
    private String virCardNoIn;
    
    @Value("${payment.gopay.verficationCode}")
    private String verficationCode;
    
    @Value("${payment.gopay.server.time.url}")
    private String serverTimeUrl;
    
    @Value("${payment.gopay.gateway.url}")
    private String gatewayUrl;
    
    @Value("${payment.gopay.notify.url}")
    private String notifyUrl;
    
    /*
     * 重试次数
     */
    @Value("${payment.gopay.notify.max.times}")
    private int maxTimes;
    
    /*
     * 休眠时间
     */
    @Value("${payment.gopay.notify.sleep.milliseconds}")
    private long sleepTime;
    
    @Autowired
    private IGopayDao payDao;
    
    /**
     * @see com.smeyun.payment.gopay.service.IGopayService#payDone(com.smeyun.payment.gopay.entity.GopayRequest, javax.servlet.http.HttpServletRequest)<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public String payDone(GopayRequest payReq, HttpServletRequest httpReq)
    {
        // 保存到数据库
        long id = payDao.saveRequestData(new PayRequestData(payReq));
        
        String remoteIp = GopayUtil.getIpAddr(httpReq);
        switch (GopayUtil.parseClientType(payReq.getClientType()))
        {
            case PC:
            {
                return tryPayOnPc(payReq, id, remoteIp);
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
    
    private String tryPayOnPc(GopayRequest request, long id, String remoteIp)
    {
        try
        {
            // 处理PC端的请求
            return payOnPc(request, id, remoteIp);
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
    
    /*
     * 在PC端进行支付
     * 
     * @param request 请求消息
     * @param id 请求ID
     * @return  String 支付的form数据
     * 
     * @author qxf
     */
    private String payOnPc(GopayRequest request, long id, String remoteIp)
    {
        HashMap<String, String> sParaTemp = new HashMap<String, String>();
        StringBuffer sb = new StringBuffer();
        
        // 增加基本配置
        sParaTemp.put("version", GopayConstant.VERSION);
        sParaTemp.put("charset", GopayConstant.INPUT_CHARSET);
        sParaTemp.put("language", GopayConstant.LANGUAGE);
        sParaTemp.put("signType", GopayConstant.SIGN_TYPE);
        sParaTemp.put("tranCode", GopayConstant.TRAN_CODE);
        sParaTemp.put("merchantID", merchantId);
        sParaTemp.put("merOrderNum", request.getOrderCode());
        sParaTemp.put("tranAmt", request.getTotalFee());
        sParaTemp.put("feeAmt", StringUtils.isEmpty(request.getFeeAmt()) ? "" : request.getFeeAmt());
        
        // 交易时间
        String tranDateTime = PlatformUtil.formatDate(new Date(), "yyyyMMddHHmmss");
        sParaTemp.put("tranDateTime", tranDateTime);
        sParaTemp.put("currencyType", GopayConstant.CURRENCY_TYPE);
        sParaTemp.put("backgroundMerUrl", notifyUrl);
        sParaTemp.put("virCardNoIn", virCardNoIn);
        sParaTemp.put("tranIP", remoteIp);
        
        // 服务器时间，付款时间不能在服务器时间之前
        String serverTime = GopayUtil.getGopayServerTime(serverTimeUrl);
        sParaTemp.put("gopayServerTime", serverTime);
        sParaTemp.put("goodsName", request.getGoodsName());
        sParaTemp.put("goodsDetail", request.getGoodsDetail());
        
        // 订单是否允许重复提交【0 不允许； 1 允许】【可空】
        sParaTemp.put("isRepeatSubmit", "1");
        sParaTemp.put("merRemark1", String.valueOf(id));
        
        sb.append("version=[" + GopayConstant.VERSION + "]")
                .append("tranCode=[" + GopayConstant.TRAN_CODE + "]")
                .append("merchantID=[" + merchantId + "]")
                .append("merOrderNum=[" + request.getOrderCode() + "]")
                .append("tranAmt=[" + request.getTotalFee() + "]")
                .append("feeAmt=[" + (StringUtils.isEmpty(request.getFeeAmt()) ? "" : request.getFeeAmt()) + "]")
                .append("tranDateTime=[" + tranDateTime + "]")
                .append("frontMerUrl=[]")
                .append("backgroundMerUrl=[" + notifyUrl + "]")
                .append("orderId=[]gopayOutOrderId=[]")
                .append("tranIP=[" + remoteIp + "]")
                .append("respCode=[]")
                .append("gopayServerTime=[" + serverTime + "]")
                .append("VerficationCode=[" + verficationCode + "]");
        
        String signTemp = sb.toString();
        return buildForm(sParaTemp, signTemp, gatewayUrl, HttpMethod.POST);
    }
    
    /**
     * @see com.smeyun.payment.gopay.service.IGopayService#dealNotification(java.util.Map)<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public String dealNotification(Map<String, String> notifyParams)
    {
        try
        {
            String url = tryDealNotification(notifyParams);
            return GopayUtil.createRetMsg(GopayConstant.TRADE_SUCCESS, url, notifyParams);
        }
        catch (SmeyunUncheckedException e)
        {
            return GopayUtil.createRetMsg(GopayConstant.TRADE_FAIL, null, null);
        }
        catch (Exception e)
        {
            LOGGER.error("deal notification exception, paramters=" + notifyParams, e);
            return GopayUtil.createRetMsg(GopayConstant.TRADE_FAIL, null, null);
        }
    }
    
    /*
     * 构造提交表单HTML数据
     * @param sParaTemp 请求参数数组
     * @param signTemp 待加密明文串
     * @param gateway 网关地址
     * @param method 提交方式。两个值可选：post、get
     * @return 提交表单HTML文本
     */
    private String buildForm(Map<String, String> sParaTemp, String signTemp, String gateway, HttpMethod method)
    {
        // 生成密文串
        String signVlaue = GopayUtil.buildRequestMysign(signTemp);
        sParaTemp.put("signValue", signVlaue);
        List<String> keys = new ArrayList<String>(sParaTemp.keySet());
        
        // 返回的HTML表单代码
        StringBuffer sbHtml = new StringBuffer();
        sbHtml.append("<form id=\"gopaysubmit\" name=\"gopaysubmit\" action=\"" + gateway + "\" method=\""
                + method.getValue() + "\" accept-charset=\"utf-8\" onsubmit=\"document.charset='utf-8';\">");
        for (int i = 0; i < keys.size(); i++)
        {
            String name = (String) keys.get(i);
            String value = (String) sParaTemp.get(name);
            
            sbHtml.append("<input type=\"hidden\" id=\"" + name + "\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
        
        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"submit\" style=\"display:none;\"></form>");
        return sbHtml.toString();
    }
    
    /*
     * 尝试处理通知消息
     * 
     * @param notifyParams 通知参数
     * @return  void
     * @exception/throws [异常类型] [异常说明]
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    private String tryDealNotification(Map<String, String> notifyParams)
    {
        // 检查通知消息的合法性
        checkNotifyCorrect(notifyParams);
        
        // 获取并校验请求数据
        String reqId = notifyParams.get("merRemark1");
        PayRequestData data = checkRequstData(reqId, notifyParams);
        
        // 保存通知数据
        saveNotifyData(data, notifyParams);
        
        // 通知响应消息
        notifyCustom(data, notifyParams);
        
        return data.getReturnUrl();
    }
    
    private void checkNotifyCorrect(Map<String, String> notifyParams)
    {
        // 检查通知消息应用ID和商户ID都是对应的
        if (!merchantId.equals(notifyParams.get("merchantID")))
        {
            LOGGER.error("the notify message is not correct, notifyParams=({})", notifyParams);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR);
        }
    }
    
    /*
     * 检查通知数据和请求数据是否一致
     */
    private PayRequestData checkRequstData(String reqId, Map<String, String> notifyParams)
    {
        PayRequestData data = payDao.queryRequestDataById(Long.parseLong(reqId));
        if ((null == data) || !data.getTotalFee().equals(notifyParams.get("tranAmt")))
        {
            LOGGER.error("the notification can not match the request data, notifyParams=({}).", notifyParams);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR);
        }
        
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
            notifyData.setTradeStatus(notifyParams.get("respCode"));
            notifyData.setCompleteTime(notifyParams.get("tranFinishTime"));
            
            // 更新到数据库
            payDao.updateNotifyData(notifyData);
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
        
        data.setCompleteTime(notifyParams.get("tranFinishTime"));
        data.setFeeAmt(notifyParams.get("feeAmt"));
        data.setOrderCode(notifyParams.get("merOrderNum"));
        data.setTotalFee(notifyParams.get("tranAmt"));
        
        //TODO
        data.setPayStatus(true);
        data.setReceiveTime(new Date());
        data.setReqId(reqId);
        data.setTranTime(notifyParams.get("tranDateTime"));
        data.setGopayTradeNo(notifyParams.get("orderId"));
        data.setTradeStatus(notifyParams.get("respCode"));
        
        return data;
    }
    
    private void notifyCustom(PayRequestData data, Map<String, String> notifyParams)
    {
        String tradeStatus = notifyParams.get("respCode");
        LOGGER.info("the status of pay request ({}) is ({}), orderCode is ({}), tradeNo is ({}).",
                data.getId(),
                tradeStatus,
                notifyParams.get("merOrderNum"),
                notifyParams.get("orderId"));
        
        // 支付成功时的处理
        if (GopayConstant.TRADE_SUCCESS.equals(tradeStatus))
        {
            BaseResponse response = new BaseResponse();
            response.setData(createReponseData(notifyParams, GopayConstant.RESPONSE_SUCCESS));
            
            // 回调通知接口
            tryCallRemote(data.getNotifyUrl(), response);
        }
        else
        {
            LOGGER.error("the wechat pay bussiness failed, notifyParams=({}).", notifyParams);
            throw new SmeyunUncheckedException(tradeStatus);
        }
    }
    
    /*
     * 生成响应通知参数
     * 
     * @param [参数1]     [参数1说明]
     * @return  PayResponseData
     * @exception/throws [异常类型] [异常说明]
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    private PayResponseData createReponseData(Map<String, String> notifyParams, String ret)
    {
        PayResponseData data = new PayResponseData();
        
        data.setGopayTradeNo(notifyParams.get("orderId"));
        data.setOrderCode(notifyParams.get("merOrderNum"));
        data.setResult(ret);
        data.setTotalPrice(notifyParams.get("tranAmt"));
        data.setRandom(PlatformUtil.generateRandomString());
        
        // 生成校验盐值
        StringBuilder builder = new StringBuilder();
        builder.append(data.getGopayTradeNo());
        builder.append(data.getOrderCode());
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
