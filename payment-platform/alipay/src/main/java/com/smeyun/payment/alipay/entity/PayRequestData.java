/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PayRequestData.java
 * Author:   qxf
 * Date:     2016年11月21日 上午10:43:12
 */
package com.smeyun.payment.alipay.entity;

import java.util.Date;

/**
 * 支付请求数据，入库
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
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
    private int clientType;
    
    /**
     * 数量
     */
    private int quantity;
    
    /**
     * 支付价格
     */
    private String price;
    
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
    private Date time;
    
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
    public PayRequestData(AlipayRequest request)
    {
        this.clientType = request.getClientType();
        this.orderCode = request.getOrderCode();
        this.quantity = request.getQuantity();
        this.price = request.getPayPrice();
        this.notifyUrl = request.getNotifyUrl();
        this.returnUrl = request.getReturnUrl();
        this.time = new Date();
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(String orderCode)
    {
        this.orderCode = orderCode;
    }

    public int getClientType()
    {
        return clientType;
    }

    public void setClientType(int clientType)
    {
        this.clientType = clientType;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
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

    public Date getTime()
    {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

    /**
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
        builder.append("PayRequestData [id=");
        builder.append(id);
        builder.append(", orderCode=");
        builder.append(orderCode);
        builder.append(", clientType=");
        builder.append(clientType);
        builder.append(", quantity=");
        builder.append(quantity);
        builder.append(", price=");
        builder.append(price);
        builder.append(", notifyUrl=");
        builder.append(notifyUrl);
        builder.append(", returnUrl=");
        builder.append(returnUrl);
        builder.append(", time=");
        builder.append(time);
        builder.append("]");
        return builder.toString();
    }
}
