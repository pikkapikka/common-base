/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PayRequestData.java
 * Author:   qxf
 * Date:     2016年11月21日 上午10:43:12
 */
package com.smeyun.payment.unionpay.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;


/**
* JDK version used:   1.8
* @ClassName: PayRequestData
* @Description: 支付请求参数传输对象
* @author liuyaoshen
* @date 2017年1月22日
*/
@Setter @Getter
public class PayRequestData
{
    /**
     * ID值
     */
    private long id;
    
    /**
     * 订单编码
     */
    private String orderCode;
    
    /**
     * 客户端类型
     */
    private Integer clientType;
    
    
    /**
     * 支付价格
     */
    private Long txnAmount;
    
    /**
     * 通知URL
     */
    private String notifyUrl;
    
    /**
     * 返回的URL
     */
    private String returnUrl;
    
    /**
     * 支付时间
     */
    private Date payTime;
    /**
    * 业务类型:201-B2C,202-B2B
    */
    private Integer bizType;
    
    /**
     * 构造方法
     */
    public PayRequestData()
    {
        
    }
    
    /**
     * 构造方法
     * @param request 请求消息
     */
    public PayRequestData(UnionpayRequest request)
    {
        this.orderCode = request.getOrderCode();
        this.notifyUrl = request.getNotifyUrl();
        this.returnUrl = request.getReturnUrl();
        this.txnAmount = request.getTxnAmt();
        this.clientType = request.getClientType();
        this.bizType = request.getBizType();
        this.payTime = request.getPayTime();
    }
    
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

   
}
