/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: IGopayService.java
 * Author:   qxf
 * Date:     2016年11月24日 下午4:57:38
 */
package com.smeyun.payment.gopay.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.smeyun.payment.gopay.entity.GopayRequest;

/**
 * 国付宝支付的服务接口
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface IGopayService
{
    /**
     * 支付实现，生成form表单
     * 
     * @param [参数1]     [参数1说明]
     * @return  String
     * @exception/throws [异常类型] [异常说明]
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    String payDone(GopayRequest payReq, HttpServletRequest httpReq);
    
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
