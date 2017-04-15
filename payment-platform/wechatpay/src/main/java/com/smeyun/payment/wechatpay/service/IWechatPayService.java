/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: AlipayService.java
 * Author:   qxf
 * Date:     2016年11月16日 下午5:40:00
 */
package com.smeyun.payment.wechatpay.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.smeyun.payment.wechatpay.entity.WechatPayRequest;

/**
 * 支付宝支付的服务实现类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface IWechatPayService
{
    /**
     * 支付操作
     * 
     * @param request 请求地址
     * @param httpReq http请求
     * @return 二维码地址
     * 
     * @author     [作者]（必须，使用汉语）
     * @see        [相关类/方法]（可选）
     * @since      [产品/模块版本] （可选）
     */
    String payDone(WechatPayRequest request, HttpServletRequest httpReq);
    
    /**
     * 处理支付的回调数据
     * 
     * @param notifyParams 通知参数
     * @return  String 成功返回 success， 失败返回 fail
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    String dealNotification(Map<String, String> notifyParams);
}
