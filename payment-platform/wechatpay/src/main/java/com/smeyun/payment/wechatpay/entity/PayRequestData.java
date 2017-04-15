/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PayRequestData.java
 * Author:   qxf
 * Date:     2016年11月21日 上午10:43:12
 */
package com.smeyun.payment.wechatpay.entity;

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
    private String productIds;
    
    /**
     * 支付价格
     */
    private String totalFee;
    
    /**
     * 通知URL
     */
    private String notifyUrl;
    
    /**
     * 随机字符串
     */
    private String randomStr;
    
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
    public PayRequestData(WechatPayRequest request)
    {
        this.clientType = request.getClientType();
        this.orderCode = request.getOrderCode();
        this.productIds = request.getProductIds();
        this.totalFee = request.getTotalFee();
        this.notifyUrl = request.getNotifyUrl();
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

    public String getNotifyUrl()
    {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl)
    {
        this.notifyUrl = notifyUrl;
    }

    public Date getTime()
    {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

    public String getProductIds()
    {
        return productIds;
    }

    public void setProductIds(String productIds)
    {
        this.productIds = productIds;
    }

    public String getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(String totalFee)
    {
        this.totalFee = totalFee;
    }

    public String getRandomStr()
    {
        return randomStr;
    }

    public void setRandomStr(String randomStr)
    {
        this.randomStr = randomStr;
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
        builder.append(", productIds=");
        builder.append(productIds);
        builder.append(", totalFee=");
        builder.append(totalFee);
        builder.append(", notifyUrl=");
        builder.append(notifyUrl);
        builder.append(", randomStr=");
        builder.append(randomStr);
        builder.append(", time=");
        builder.append(time);
        builder.append("]");
        return builder.toString();
    }
    
}
