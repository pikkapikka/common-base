/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PayNotofyData.java
 * Author:   qxf
 * Date:     2016年11月22日 上午11:21:48
 */
package com.smeyun.payment.alipay.entity;

import java.util.Date;

/**
 * 通知消息
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class PayNotifyData
{
    // ID
    private long id;
    
    // 支付请求ID
    private long reqId;
    
    // 订单编码
    private String orderCode;
    
    // 总共支付费用
    private String totalFee;
    
    // 支付宝内部交易编码
    private String aliTradeNo;
    
    // 支付宝内部通知ID
    private String notifyId;
    
    // 打折价格
    private String discount;
    
    // 交易状态
    private String tradeStatus;
    
    // 通知时间
    private String notifyTime;
    
    // 支付时间
    private String payTime;
    
    // 消息接收时间
    private Date receiveTime;
    
    // 是否成功
    private boolean payStatus = true;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getReqId()
    {
        return reqId;
    }

    public void setReqId(long reqId)
    {
        this.reqId = reqId;
    }

    public String getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(String orderCode)
    {
        this.orderCode = orderCode;
    }

    public String getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(String totalFee)
    {
        this.totalFee = totalFee;
    }

    public String getAliTradeNo()
    {
        return aliTradeNo;
    }

    public void setAliTradeNo(String aliTradeNo)
    {
        this.aliTradeNo = aliTradeNo;
    }

    public String getNotifyId()
    {
        return notifyId;
    }

    public void setNotifyId(String notifyId)
    {
        this.notifyId = notifyId;
    }

    public String getDiscount()
    {
        return discount;
    }

    public void setDiscount(String discount)
    {
        this.discount = discount;
    }

    public String getTradeStatus()
    {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus)
    {
        this.tradeStatus = tradeStatus;
    }

    public String getNotifyTime()
    {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime)
    {
        this.notifyTime = notifyTime;
    }

    public String getPayTime()
    {
        return payTime;
    }

    public void setPayTime(String payTime)
    {
        this.payTime = payTime;
    }

    public Date getReceiveTime()
    {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime)
    {
        this.receiveTime = receiveTime;
    }

    public boolean isPayStatus()
    {
        return payStatus;
    }

    public void setPayStatus(boolean payStatus)
    {
        this.payStatus = payStatus;
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
        builder.append("PayNotifyData [id=");
        builder.append(id);
        builder.append(", reqId=");
        builder.append(reqId);
        builder.append(", orderCode=");
        builder.append(orderCode);
        builder.append(", totalFee=");
        builder.append(totalFee);
        builder.append(", aliTradeNo=");
        builder.append(aliTradeNo);
        builder.append(", notifyId=");
        builder.append(notifyId);
        builder.append(", discount=");
        builder.append(discount);
        builder.append(", tradeStatus=");
        builder.append(tradeStatus);
        builder.append(", notifyTime=");
        builder.append(notifyTime);
        builder.append(", payTime=");
        builder.append(payTime);
        builder.append(", receiveTime=");
        builder.append(receiveTime);
        builder.append(", payStatus=");
        builder.append(payStatus);
        builder.append("]");
        return builder.toString();
    }
}
