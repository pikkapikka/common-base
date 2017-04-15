/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PayNotofyData.java
 * Author:   qxf
 * Date:     2016年11月22日 上午11:21:48
 */
package com.smeyun.payment.gopay.entity;

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
    private String gopayTradeNo;
    
    // 手续费金额
    private String feeAmt;
    
    // 交易状态
    private String tradeStatus;
    
    // 支付时间
    private String completeTime;
    
    // 交易时间
    private String tranTime;
    
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

    public String getTradeStatus()
    {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus)
    {
        this.tradeStatus = tradeStatus;
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

    public String getGopayTradeNo()
    {
        return gopayTradeNo;
    }

    public void setGopayTradeNo(String gopayTradeNo)
    {
        this.gopayTradeNo = gopayTradeNo;
    }

    public String getFeeAmt()
    {
        return feeAmt;
    }

    public void setFeeAmt(String feeAmt)
    {
        this.feeAmt = feeAmt;
    }

    public String getCompleteTime()
    {
        return completeTime;
    }

    public void setCompleteTime(String completeTime)
    {
        this.completeTime = completeTime;
    }

    public String getTranTime()
    {
        return tranTime;
    }

    public void setTranTime(String tranTime)
    {
        this.tranTime = tranTime;
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
        builder.append(", gopayTradeNo=");
        builder.append(gopayTradeNo);
        builder.append(", feeAmt=");
        builder.append(feeAmt);
        builder.append(", completeTime=");
        builder.append(completeTime);
        builder.append(", tradeStatus=");
        builder.append(tradeStatus);
        builder.append(", tranTime=");
        builder.append(tranTime);
        builder.append(", receiveTime=");
        builder.append(receiveTime);
        builder.append(", payStatus=");
        builder.append(payStatus);
        builder.append("]");
        return builder.toString();
    }
}
