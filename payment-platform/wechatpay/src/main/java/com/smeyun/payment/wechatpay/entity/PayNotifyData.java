/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PayNotofyData.java
 * Author:   qxf
 * Date:     2016年11月22日 上午11:21:48
 */
package com.smeyun.payment.wechatpay.entity;

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
    private String totalPay;
    
    // 支付宝内部交易编码
    private String wechatTradeNo;
    
    // 支付类型
    private String tradeType;
    
    // 错误码
    private String errorCode;
    
    // 交易结果
    private String tradeRet;
    
    // 交易完成时间
    private String completeTime;
    
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

    public String getTotalPay()
    {
        return totalPay;
    }

    public void setTotalPay(String totalPay)
    {
        this.totalPay = totalPay;
    }

    public String getWechatTradeNo()
    {
        return wechatTradeNo;
    }

    public void setWechatTradeNo(String wechatTradeNo)
    {
        this.wechatTradeNo = wechatTradeNo;
    }

    public String getTradeType()
    {
        return tradeType;
    }

    public void setTradeType(String tradeType)
    {
        this.tradeType = tradeType;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getTradeRet()
    {
        return tradeRet;
    }

    public void setTradeRet(String tradeRet)
    {
        this.tradeRet = tradeRet;
    }

    public String getCompleteTime()
    {
        return completeTime;
    }

    public void setCompleteTime(String completeTime)
    {
        this.completeTime = completeTime;
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
        builder.append(", totalPay=");
        builder.append(totalPay);
        builder.append(", wechatTradeNo=");
        builder.append(wechatTradeNo);
        builder.append(", tradeType=");
        builder.append(tradeType);
        builder.append(", errorCode=");
        builder.append(errorCode);
        builder.append(", tradeRet=");
        builder.append(tradeRet);
        builder.append(", completeTime=");
        builder.append(completeTime);
        builder.append(", receiveTime=");
        builder.append(receiveTime);
        builder.append(", payStatus=");
        builder.append(payStatus);
        builder.append("]");
        return builder.toString();
    }
}
