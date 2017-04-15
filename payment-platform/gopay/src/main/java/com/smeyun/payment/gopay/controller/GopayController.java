/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: GopayController.java
 * Author:   qxf
 * Date:     2016年11月24日 下午4:49:26
 */
package com.smeyun.payment.gopay.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smeyun.payment.gopay.entity.GopayRequest;
import com.smeyun.payment.gopay.entity.PayClientType;
import com.smeyun.payment.gopay.service.IGopayService;
import com.smeyun.payment.gopay.util.GopayConstant;
import com.smeyun.payment.gopay.util.GopayUtil;
import com.smeyun.platform.util.common.encrypt.Md5Encrypt;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 国付宝支付控制器
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Controller
@RequestMapping("/platform/payment/gopay")
public class GopayController
{
    // 日志打印对象
    private static final Logger LOGGER = LoggerFactory.getLogger(GopayController.class);
    
    @Autowired
    private IGopayService payService;
    
    /**
     * 支付请求
     * 
     * @param payReq 支付请求消息
     * @param httpReq http请求
     * 
     * @author     [作者]（必须，使用汉语）
     * @see        [相关类/方法]（可选）
     * @since      [产品/模块版本] （可选）
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody String pay(@RequestBody GopayRequest payReq, HttpServletRequest httpReq)
    {
        LOGGER.debug("enter GopayController:pay, request=" + payReq);
        
        if ((null == payReq) || payReq.isParamEmpty() || !requestValid(payReq))
        {
            LOGGER.error("parameter invalid, request=" + payReq);
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        // 生成支付的二维码
        return payService.payDone(payReq, httpReq);
    }
    
    private boolean requestValid(GopayRequest payReq)
    {
        if (!PayClientType.judge(payReq.getClientType()))
        {
            return false;
        }
        
        try
        {
            double totalFee = Double.parseDouble(payReq.getTotalFee());
            if (!StringUtils.isEmpty(payReq.getFeeAmt()))
            {
                double feeAmt = Double.parseDouble(payReq.getFeeAmt());
                if (feeAmt > totalFee)
                {
                    return false;
                }
            }
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        
        /*
         * 为了防止支付数据被篡改, 增加了盐值校验。
         * 校验方式  MD5(客户端类型 + 订单编码 + 金额 + 佣金 + 通知URL + 返回URL + 随机数)
         */
        StringBuilder builder = new StringBuilder();
        builder.append(payReq.getClientType());
        builder.append(payReq.getOrderCode());
        builder.append(payReq.getTotalFee());
        builder.append(StringUtils.isEmpty(payReq.getFeeAmt()) ? "" : payReq.getFeeAmt());
        builder.append(payReq.getNotifyUrl());
        builder.append(payReq.getReturnUrl());
        builder.append(payReq.getRandom());
        
        return payReq.getSalt().equals(Md5Encrypt.md5(builder.toString()));
    }
    
    /**
     * 回调接口
     * 
     * @param request http请求
     * @return  String
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void payNotifyMsg(HttpServletRequest request, HttpServletResponse response)
    {
        if ((null == request) || MapUtils.isEmpty(getPramsFromRequest(request)))
        {
            LOGGER.error("parameter of payNotifyMsg is null or empty.");
            writeResponseData(response, GopayUtil.createRetMsg(GopayConstant.TRADE_FAIL, null, null));
            return;
        }
        
        // 获取支付宝的通知消息
        Map<String, String> params = getPramsFromRequest(request);
        LOGGER.info("receive the notification from gopay, out_trade_no:({}), trade_no:({}), "
                        + "total_price:({}), trade_status:{{}}.",
                params.get("merOrderNum"),
                params.get("orderId"),
                params.get("tranAmt"),
                params.get("respCode"));
        
        String retUrl = payService.dealNotification(params);
        writeResponseData(response, retUrl);
    }
    
    private void writeResponseData(HttpServletResponse response, String url)
    {
        try
        {
            response.getWriter().write(url);
        }
        catch (IOException e)
        {
            LOGGER.error("write result message failed, url=" + url, e);
        }
    }
    
    /*
     * 封装支付宝回调的传入参数
     *
     * @param request 请求消息
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Map<String, String> getPramsFromRequest(HttpServletRequest request)
    {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();)
        {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++)
            {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }
    
}
