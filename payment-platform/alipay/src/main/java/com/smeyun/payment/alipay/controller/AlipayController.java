/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: AlipayController.java
 * Author:   qxf
 * Date:     2016年11月16日 下午2:49:27
 */
package com.smeyun.payment.alipay.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smeyun.payment.alipay.entity.AlipayRequest;
import com.smeyun.payment.alipay.entity.PayClientType;
import com.smeyun.payment.alipay.service.IAlipayService;
import com.smeyun.payment.alipay.util.Constant;
import com.smeyun.platform.util.common.encrypt.Md5Encrypt;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 支付的控制器。
 * 提供支付宝支付的接口
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Controller
@RequestMapping("/platform/payment/alipay")
public class AlipayController
{
    // 日志打印对象
    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayController.class);
    
    @Autowired
    private IAlipayService alipayService;
    
    /**
     * 支付的接口
     * 
     * @param request 支付请求
     * @author qxf
     * @see  [相关类/方法]（可选）
     * @since  [产品/模块版本] （可选）
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody String pay(@RequestBody AlipayRequest request)
    {
        LOGGER.debug("enter AlipayController:pay, request=" + request);
        
        // 参数校验
        if ((null == request) || request.isParamEmpty() || !isParamValid(request))
        {
            LOGGER.error("parameter is invalid, request=" + request);
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        return alipayService.payDone(request);
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
    public @ResponseBody String payNotifyMsg(HttpServletRequest request)
    {
        if ((null == request) || MapUtils.isEmpty(getPramsFromRequest(request)))
        {
            LOGGER.error("parameter of payNotifyMsg is null or empty.");
            return Constant.RET_FAIL;
        }
        
        // 获取支付宝的通知消息
        Map<String, String> params = getPramsFromRequest(request);
        LOGGER.info("receive the notification from alipay, out_trade_no:({}), trade_no:({}), "
                        + "total_price:({}), trade_status:{{}}.",
                params.get("out_trade_no"),
                params.get("trade_no"),
                params.get("total_fee"),
                params.get("trade_status"));
        
        try
        {
            // 处理通知消息
            return alipayService.dealNotification(params);
        }
        catch (SmeyunUncheckedException e)
        {
            return Constant.RET_FAIL;
        }
        catch (Exception e)
        {
            LOGGER.error("deal notification catch exception:", e);
            return Constant.RET_FAIL;
        }
        
    }
    
    private boolean isParamValid(AlipayRequest request)
    {
        if (!PayClientType.judge(request.getClientType()) || (request.getQuantity() <= 0))
        {
            return false;
        }
        
        try
        {
            // 价格必须为数字
            Double.parseDouble(request.getPayPrice());
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        
        /*
         * 为了防止支付数据被篡改, 增加了盐值校验。
         * 校验方式  MD5(客户端类型 + 订单编码 + 金额 + 通知URL + 随机数)
         */
        StringBuilder builder = new StringBuilder();
        builder.append(request.getClientType());
        builder.append(request.getOrderCode());
        builder.append(request.getQuantity());
        builder.append(request.getPayPrice());
        builder.append(request.getNotifyUrl());
        builder.append(request.getReturnUrl());
        builder.append(request.getRandom());
        
        return request.getSalt().equals(Md5Encrypt.md5(builder.toString()));
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
