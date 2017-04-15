/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: AlipayRequest.java
 * Author:   qxf
 * Date:     2016年11月16日 下午2:48:47
 */
package com.smeyun.payment.alipay.entity;

import org.apache.commons.lang3.StringUtils;

import com.smeyun.platform.util.base.entity.BaseRequest;

/**
 * 支付宝支付的请求消息
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class AlipayRequest extends BaseRequest
{
    /**
     * 客户端类型：
     * 1：PC
     * 2：手机
     */
    private int clientType;
    
    /**
     * 订单编码
     */
    private String orderCode;
    
    /**
     * 订单标题
     */
    private String subject;
    
    /**
     * 商品描述
     */
    private String body;
    
    /**
     * 支付价格
     */
    private String payPrice;
    
    /**
     * 支付成功的通知地址
     */
    private String notifyUrl;
    
    /**
     * 支付失败的通知地址
     */
    private String returnUrl;
    
    /**
     * 订单数量
     */
    private int quantity;
    
    /**
     * 随机数
     */
    private String random;
    
    /**
     * 校验盐值
     */
    private String salt;
    
    public int getClientType()
    {
        return clientType;
    }
    
    public void setClientType(int clientType)
    {
        this.clientType = clientType;
    }
    
    public String getOrderCode()
    {
        return orderCode;
    }
    
    public void setOrderCode(String orderCode)
    {
        this.orderCode = orderCode;
    }
    
    public String getPayPrice()
    {
        return payPrice;
    }
    
    public void setPayPrice(String payPrice)
    {
        this.payPrice = payPrice;
    }
    
    public String getNotifyUrl()
    {
        return notifyUrl;
    }
    
    public void setNotifyUrl(String notifyUrl)
    {
        this.notifyUrl = notifyUrl;
    }
    
    public String getReturnUrl()
    {
        return returnUrl;
    }
    
    public void setReturnUrl(String returnUrl)
    {
        this.returnUrl = returnUrl;
    }
    
    public String getRandom()
    {
        return random;
    }
    
    public void setRandom(String random)
    {
        this.random = random;
    }
    
    public String getSalt()
    {
        return salt;
    }
    
    public void setSalt(String salt)
    {
        this.salt = salt;
    }
    
    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    /**
     * 
     * @see java.lang.Object#toString()<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("AlipayRequest [clientType=");
        builder.append(clientType);
        builder.append(", orderCode=");
        builder.append(orderCode);
        builder.append(", subject=");
        builder.append(subject);
        builder.append(", body=");
        builder.append(body);
        builder.append(", quantity=");
        builder.append(quantity);
        builder.append(", payPrice=");
        builder.append(payPrice);
        builder.append(", notifyUrl=");
        builder.append(notifyUrl);
        builder.append(", returnUrl=");
        builder.append(returnUrl);
        builder.append("]");
        return builder.toString();
    }
    
    /**
     * 
     * 判断参数是否为空
     * 
     * @return boolean 有任何一个参数为空，则返回true， 否则返回false
     */
    public boolean isParamEmpty()
    {
        return StringUtils.isAnyBlank(orderCode, subject, payPrice, notifyUrl, random, salt);
    }
}
