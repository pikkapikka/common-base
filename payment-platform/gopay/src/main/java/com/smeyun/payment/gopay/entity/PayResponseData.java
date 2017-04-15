/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PayResponseData.java
 * Author:   qxf
 * Date:     2016年11月22日 下午1:38:33
 */
package com.smeyun.payment.gopay.entity;

/**
 * 支付响应消息
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class PayResponseData
{
    // 订单编码
    private String orderCode;
    
    // 支付宝交易编号
    private String gopayTradeNo;
    
    // 支付结果
    private String result;
    
    // 支付总价
    private String totalPrice;
    
    // 随机数
    private String random;
    
    // 校验盐值
    // 值为：MD5(gopayTradeNo + orderCode + result + totalPrice + random)
    private String salt;

    public String getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(String orderCode)
    {
        this.orderCode = orderCode;
    }

    public String getGopayTradeNo()
    {
        return gopayTradeNo;
    }

    public void setGopayTradeNo(String gopayTradeNo)
    {
        this.gopayTradeNo = gopayTradeNo;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice)
    {
        this.totalPrice = totalPrice;
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
        builder.append("PayResponseData [orderCode=");
        builder.append(orderCode);
        builder.append(", gopayTradeNo=");
        builder.append(gopayTradeNo);
        builder.append(", result=");
        builder.append(result);
        builder.append(", totalPrice=");
        builder.append(totalPrice);
        builder.append("]");
        return builder.toString();
    }
}
