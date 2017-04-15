/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PayResponseData.java
 * Author:   qxf
 * Date:     2016年11月22日 下午1:38:33
 */
package com.smeyun.payment.alipay.entity;

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
    private String aliTradeNo;
    
    // 支付结果
    private String result;
    
    // 支付总价
    private String totalPrice;
    
    // 单价
    private String price;
    
    // 打折价格
    private String discount;
    
    // 数量
    private String quantity;
    
    // 随机数
    private String random;
    
    // 校验盐值
    // 值为：MD5(aliTradeNo + discount + orderCode + price + quantity + result + totalPrice + random)
    private String salt;

    public String getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(String orderCode)
    {
        this.orderCode = orderCode;
    }

    public String getAliTradeNo()
    {
        return aliTradeNo;
    }

    public void setAliTradeNo(String aliTradeNo)
    {
        this.aliTradeNo = aliTradeNo;
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

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getDiscount()
    {
        return discount;
    }

    public void setDiscount(String discount)
    {
        this.discount = discount;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
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
        builder.append(", aliTradeNo=");
        builder.append(aliTradeNo);
        builder.append(", result=");
        builder.append(result);
        builder.append(", totalPrice=");
        builder.append(totalPrice);
        builder.append(", price=");
        builder.append(price);
        builder.append(", discount=");
        builder.append(discount);
        builder.append(", quantity=");
        builder.append(quantity);
        builder.append("]");
        return builder.toString();
    }
}
