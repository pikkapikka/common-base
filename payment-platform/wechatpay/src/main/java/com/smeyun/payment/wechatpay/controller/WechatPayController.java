/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: WechatPayController.java
 * Author:   qxf
 * Date:     2016年11月23日 上午11:50:01
 */
package com.smeyun.payment.wechatpay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smeyun.payment.wechatpay.entity.PayClientType;
import com.smeyun.payment.wechatpay.entity.WechatPayRequest;
import com.smeyun.payment.wechatpay.service.IWechatPayService;
import com.smeyun.payment.wechatpay.util.WechatConstant;
import com.smeyun.payment.wechatpay.util.WechatPayUtil;
import com.smeyun.platform.util.common.encrypt.Md5Encrypt;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;
import com.smeyun.platform.util.common.util.PlatformUtil;

/**
 * 微信支付控制器
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Controller
@RequestMapping("/platform/payment/wechatpay")
public class WechatPayController
{
    // 日志打印对象
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatPayController.class);
    
    @Autowired
    private IWechatPayService payService;
    
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
    public @ResponseBody String pay(@RequestBody WechatPayRequest payReq, HttpServletRequest httpReq)
    {
        LOGGER.debug("enter WechatPayController:pay, request=" + payReq);
        
        if ((null == payReq) || payReq.isParamEmpty() || !requestValid(payReq))
        {
            LOGGER.error("parameter invalid, request=" + payReq);
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        // 生成支付的二维码
        return payService.payDone(payReq, httpReq);
        
    }
    
    private boolean requestValid(WechatPayRequest payReq)
    {
        if (!PayClientType.judge(payReq.getClientType()))
        {
            return false;
        }
        
        try
        {
            Double.parseDouble(payReq.getTotalFee());
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        
        /*
         * 为了防止支付数据被篡改, 增加了盐值校验。
         * 校验方式  MD5(客户端类型 + 订单编码 + 产品ID + 金额 + 通知URL + 随机数)
         */
        StringBuilder builder = new StringBuilder();
        builder.append(payReq.getClientType());
        builder.append(payReq.getOrderCode());
        builder.append(payReq.getProductIds());
        builder.append(payReq.getTotalFee());
        builder.append(payReq.getNotifyUrl());
        builder.append(payReq.getRandom());
        
        return payReq.getSalt().equals(Md5Encrypt.md5(builder.toString()));
    }
    
    /**
     * 支付响应
     * 
     * @param httpReq http请求消息
     * @param httpResp http响应消息
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @RequestMapping(value = "/notify")
    public void notify(HttpServletRequest httpReq, HttpServletResponse httpResp)
    {
        LOGGER.debug("enter WechatPayController:notify.");
        BufferedReader br = null;
        PrintWriter writer = null;
        try
        {
            br = new BufferedReader(new InputStreamReader((ServletInputStream) httpReq.getInputStream()));
            writer = httpResp.getWriter();
            
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            
            String notifyMsg = sb.toString();
            if (StringUtils.isEmpty(notifyMsg))
            {
                LOGGER.error("the notification message is empty.");
                String xmlMsg = String.format(WechatConstant.RETURN_FORMAT_XML,
                        WechatConstant.RET_FAIL,
                        WechatConstant.RET_FAIL);
                writer.write(xmlMsg);
                return;
            }
            
            LOGGER.info("the notification message is ({}).", notifyMsg);
            
            // 处理通知消息
            String ret = payService.dealNotification(WechatPayUtil.doXMLParse(notifyMsg));
            String xmlMsg = String.format(WechatConstant.RETURN_FORMAT_XML, ret, ret);
            
            LOGGER.info("the notification result is ({}).", xmlMsg);
            writer.write(xmlMsg);
            
        }
        catch (IOException e)
        {
            LOGGER.error("deal the notification message failed:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR);
        }
        finally
        {
            PlatformUtil.close(br);
            PlatformUtil.close(writer);
        }
    }
}
