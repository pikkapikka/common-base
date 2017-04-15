/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: WechatPayServiceImpl.java
 * Author:   qxf
 * Date:     2016年11月23日 上午11:25:20
 */
package com.smeyun.payment.wechatpay.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.smeyun.payment.wechatpay.dao.IWechatPayDao;
import com.smeyun.payment.wechatpay.entity.PayNotifyData;
import com.smeyun.payment.wechatpay.entity.PayRequestData;
import com.smeyun.payment.wechatpay.entity.PayResponseData;
import com.smeyun.payment.wechatpay.entity.WechatPayRequest;
import com.smeyun.payment.wechatpay.handler.PrepayIdRequestHandler;
import com.smeyun.payment.wechatpay.service.IWechatPayService;
import com.smeyun.payment.wechatpay.util.CommonUtil;
import com.smeyun.payment.wechatpay.util.QRCodeUtil;
import com.smeyun.payment.wechatpay.util.WechatConstant;
import com.smeyun.payment.wechatpay.util.WechatPayUtil;
import com.smeyun.platform.util.base.entity.BaseResponse;
import com.smeyun.platform.util.common.encrypt.Md5Encrypt;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;
import com.smeyun.platform.util.common.http.HttpClientUtil;
import com.smeyun.platform.util.common.http.bean.SmeHttpResponse;
import com.smeyun.platform.util.common.util.PlatformUtil;

/**
 * 〈功能详细描述〉
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service("wechatPayServiceImpl")
public class WechatPayServiceImpl implements IWechatPayService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatPayServiceImpl.class);
    
    private static final String MONEY_CONVERT_MUTLI = "100";
    
    private static final int RANDOM_STRING_LEN = 30;
    
    @Value("${payment.wechatpay.appId}")
    private String appId;
    
    @Value("${payment.wechatpay.merchatId}")
    private String merchatId;
    
    @Value("${payment.wechatpay.notifyUrl}")
    private String notifyUrl;
    
    @Value("${payment.wechatpay.merchatKey}")
    private String merchatKey;
    
    @Value("${payment.wechatpay.unifiedOrderUrl}")
    private String unifiedOrderUrl;
    
    @Value("${payment.wechatpay.ercode.local}")
    private String localPath;
    
    @Value("${payment.wechatpay.ercode.url}")
    private String urlPath;
    
    /*
     * 重试次数
     */
    @Value("${payment.wechatpay.notify.max.times}")
    private int maxTimes;
    
    /*
     * 休眠时间
     */
    @Value("${payment.wechatpay.notify.sleep.milliseconds}")
    private long sleepTime;
    
    @Autowired
    private IWechatPayDao payDao;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String payDone(WechatPayRequest request, HttpServletRequest httpReq)
    {
        // 产生随机字符串
        String randomStr = PlatformUtil.generateRandomString(RANDOM_STRING_LEN);
        PayRequestData payData = new PayRequestData(request);
        payData.setRandomStr(randomStr);
        
        // 保存数据到数据库
        long reqId = payDao.saveRequestData(payData);
        
        switch (CommonUtil.parseClientType(request.getClientType()))
        {
            case PC:
            {
                return tryPayOnPc(request, httpReq, randomStr, reqId);
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
    
    private String tryPayOnPc(WechatPayRequest request, HttpServletRequest httpReq, String randomStr, long reqId)
    {
        try
        {
            // 处理PC端的请求
            return doPayOnPc(request, httpReq, randomStr, reqId);
        }
        catch (SmeyunUncheckedException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("create wechat code image falied:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String dealNotification(Map<String, String> notifyParams)
    {
        try
        {
            tryDealNofiycation(notifyParams);
            return WechatConstant.RET_SUCCESS;
        }
        catch (SmeyunUncheckedException e)
        {
            LOGGER.error("handle notification catch SmeyunUncheckedException, notifyParams=" + notifyParams, e);
            return WechatConstant.RET_FAIL;
        }
        catch (Exception e)
        {
            LOGGER.error("handle notification exception, notifyParams=" + notifyParams, e);
            return WechatConstant.RET_FAIL;
        }
    }
    
    private String doPayOnPc(WechatPayRequest request, HttpServletRequest httpReq, String randomStr, long reqId)
    {
        // 获取支付链接报文
        String content = generateCodeUrl(request, randomStr, getRemoteIp(httpReq), reqId);
        
        // 解析报文
        Map<String, String> retMap = WechatPayUtil.doXMLParse(content);
        if (!WechatConstant.RET_SUCCESS.equals(retMap.get("return_code"))
                || !WechatConstant.RET_SUCCESS.equals(retMap.get("result_code")))
        {
            LOGGER.error("create link failed, request is ({}), result=({}).", request, content);
            String errorCode = retMap.get("err_code");
            throw new SmeyunUncheckedException(
                    StringUtils.isEmpty(errorCode) ? ErrorCodeConstant.INNER_ERROR : errorCode);
        }
        
        // 检查支付链接是否有效
        String codeUrl = retMap.get("code_url");
        if (StringUtils.isEmpty(codeUrl))
        {
            LOGGER.error("the code link is invalid, request is ({}), result=({}).", request, content);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR);
        }
        
        // 创建二维码图片
        return createCodeImage(codeUrl, request.getOrderCode());
        
    }
    
    /*
     * 获取支付的链接地址
     * 
     * @param request 支付请求
     * @param randomStr 随机字符串
     * @param remoteIp 客户端IP
     * @return String 响应报文
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    private String generateCodeUrl(WechatPayRequest request, String randomStr, String remoteIp, long reqId)
    {
        PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(null, null);// 获取prepayid的请求类
        
        prepayReqHandler.setParameter("appid", appId); /*微信分配的公众账号ID*/
        prepayReqHandler.setParameter("mch_id", merchatId); /*微信支付分配的商户号*/
        prepayReqHandler.setParameter("nonce_str", randomStr); /*随机字符串，不长于32位*/
        prepayReqHandler.setParameter("body", request.getTitle()); /* 商品或支付单简要描述 */
        prepayReqHandler.setParameter("detail", request.getDetail()); /*商品名称明细列表 */
        prepayReqHandler.setParameter("out_trade_no", request.getOrderCode());/*商户系统内部的订单号,32个字符内、可包含字母*/
        prepayReqHandler.setParameter("total_fee", getTotalFee(request.getTotalFee())); /*  订单总金额，单位为分，详见支付金额*/
        prepayReqHandler.setParameter("spbill_create_ip",
                remoteIp); /*APP和网页支付提交用户端ip，例如192.168.1.1 Native支付填调用微信支付API的机器IP。*/
        
        prepayReqHandler.setParameter("notify_url", notifyUrl);/*通知地址*/
        prepayReqHandler.setParameter("trade_type", "NATIVE"); /*交易类型 取值如下：JSAPI，NATIVE，APP*/
        prepayReqHandler.setParameter("product_id",
                request.getProductIds()); /*trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。*/
        prepayReqHandler.setParameter("attach", String.valueOf(reqId)); // 此数据通知时会原样返回
        
        String sign = prepayReqHandler.createMD5(createSignStr(prepayReqHandler.getAllParameters()));
        prepayReqHandler.setParameter("sign", sign); /*签名*/
        prepayReqHandler.setGateUrl(unifiedOrderUrl);
        
        // send()发送 ，获取code_url
        String sendResult = prepayReqHandler.send();
        
        LOGGER.info(
                "wechat pay order code ({}), product tile ({}), total price({}),"
                        + " and unified order infomation is ({})",
                request.getOrderCode(),
                request.getTitle(),
                request.getTotalFee(),
                sendResult);
        return sendResult;
    }
    
    /*
     * 把金额从元转向分
     * 
     * @param srcFee 原始金额，单元：元
     * @return String
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    private String getTotalFee(String srcFee)
    {
        BigDecimal intoMoney = new BigDecimal(srcFee);
        BigDecimal multiplier = new BigDecimal(MONEY_CONVERT_MUTLI);
        long totalFee = intoMoney.multiply(multiplier).longValue();
        return String.valueOf(totalFee);
    }
    
    private String getDivTotalFee(String srcFee)
    {
        BigDecimal intoMoney = new BigDecimal(srcFee);
        BigDecimal multiplier = new BigDecimal(MONEY_CONVERT_MUTLI);
        double totalFee = intoMoney.divide(multiplier).doubleValue();
        return String.valueOf(totalFee);
    }
    
    @SuppressWarnings("rawtypes")
    private String createSignStr(Map parameter)
    {
        StringBuffer sb = new StringBuffer();
        Set es = parameter.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + v + "&");
        }
        
        sb.append("key=" + merchatKey);
        return sb.toString();
    }
    
    // 获取客户端IP
    private String getRemoteIp(HttpServletRequest httpReq)
    {
        return CommonUtil.getIpAddr(httpReq);
    }
    
    /*
     * 创建二维码图片
     * 
     * @param codeUrl 支付链接
     * @param orderCode 订单号
     * @return  String
     * @exception/throws [异常类型] [异常说明]
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    private String createCodeImage(String codeUrl, String orderCode)
    {
        String imageName = createImageName(orderCode);
        String dateStr = PlatformUtil.formatDate(new Date());
        
        try
        {
            QRCodeUtil.buildQrCodeWithLogo(codeUrl, "", localPath + dateStr, imageName, true);
        }
        catch (Exception e)
        {
            LOGGER.error("create code image failed, codeUrl=" + codeUrl + ", orderCode=" + orderCode, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
        
        return urlPath + dateStr + "/" + imageName;
    }
    
    /*
     * 组装图片的名称
     */
    private String createImageName(String orderCode)
    {
        return orderCode + "_" + System.currentTimeMillis() + ".gif";
    }
    
    private void checkNotifyCorrect(Map<String, String> notifyParams)
    {
        // 检查通知消息是否为SUCCESS，而且应用ID和商户ID都是对应的
        if (!WechatConstant.RET_SUCCESS.equals(notifyParams.get("return_code"))
                || !appId.equals(notifyParams.get("appid")) || !merchatId.equals(notifyParams.get("mch_id")))
        {
            LOGGER.error("the notify message is not correct, notifyParams=({})", notifyParams);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR);
        }
    }
    
    /*
     * 处理通知响应消息
     * 
     * @param notifyParams 通知参数
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    private void tryDealNofiycation(Map<String, String> notifyParams)
    {
        // 检查通知消息的合法性
        checkNotifyCorrect(notifyParams);
        
        // 获取并校验请求数据
        String reqId = notifyParams.get("attach");
        PayRequestData data = checkRequstData(reqId, notifyParams);
        
        // 保存通知数据
        saveNotifyData(data, notifyParams);
        
        // 通知响应消息
        notifyCustom(data, notifyParams);
    }
    
    /*
     * 检查通知数据和请求数据是否一致
     */
    private PayRequestData checkRequstData(String reqId, Map<String, String> notifyParams)
    {
        PayRequestData data = payDao.queryRequestDataById(Long.parseLong(reqId));
        if ((null == data) || !data.getRandomStr().equals(notifyParams.get("nonce_str")))
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
            notifyData.setTradeRet(notifyParams.get("result_code"));
            notifyData.setErrorCode(notifyParams.get("err_code"));
            
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
        
        data.setCompleteTime(notifyParams.get("time_end"));
        data.setErrorCode(notifyParams.get("err_code"));
        data.setOrderCode(notifyParams.get("out_trade_no"));
        data.setTotalPay(getDivTotalFee(notifyParams.get("cash_fee")));
        
        //TODO
        data.setPayStatus(true);
        data.setTradeRet(notifyParams.get("result_code"));
        data.setReceiveTime(new Date());
        data.setReqId(reqId);
        data.setTradeType(notifyParams.get("trade_type"));
        data.setWechatTradeNo(notifyParams.get("transaction_id"));
        
        return data;
    }
    
    private void notifyCustom(PayRequestData data, Map<String, String> notifyParams)
    {
        String tradeStatus = notifyParams.get("result_code");
        LOGGER.info("the status of pay request ({}) is ({}), orderCode is ({}), tradeNo is ({}).",
                data.getId(),
                tradeStatus,
                notifyParams.get("out_trade_no"),
                notifyParams.get("transaction_id"));
        
        // 支付成功时的处理
        if (WechatConstant.RET_SUCCESS.equals(tradeStatus))
        {
            BaseResponse response = new BaseResponse();
            response.setData(createReponseData(notifyParams, WechatConstant.RESPONSE_SUCCESS));
            
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
        
        data.setWechatTradeNo(notifyParams.get("transaction_id"));
        data.setOrderCode(notifyParams.get("out_trade_no"));
        data.setResult(ret);
        
        String totalFee = notifyParams.get("total_fee");
        data.setTotalPrice(getDivTotalFee(totalFee));
        data.setRandom(PlatformUtil.generateRandomString());
        
        // 生成校验盐值
        StringBuilder builder = new StringBuilder();
        builder.append(data.getWechatTradeNo());
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
