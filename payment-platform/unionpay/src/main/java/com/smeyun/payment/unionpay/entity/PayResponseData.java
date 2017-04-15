/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PayResponseData.java
 * Author:   qxf
 * Date:     2016年11月22日 下午1:38:33
 */
package com.smeyun.payment.unionpay.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付响应消息
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Setter @Getter
public class PayResponseData
{
    /**
     * 
     */

    // 订单编码
    private String orderCode;
    
    // 支付宝交易编号
    private String queryId;
    
    // 支付结果
    private String result;
    
    // 支付总价
    private String txnAmt;
    
    
    // 校验盐值
    // 值为：MD5(orderCode + queryId + result + txnAmt + key)
    private String salt;

   
}
