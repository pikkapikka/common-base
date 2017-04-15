/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: AlipayRequest.java
 * Author:   qxf
 * Date:     2016年11月16日 下午2:48:47
 */
package com.smeyun.payment.wechatpay.entity;

import org.apache.commons.lang3.StringUtils;

import com.smeyun.platform.util.base.entity.BaseRequest;

/**
 * 微信支付的请求消息
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class WechatPayRequest extends BaseRequest
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
     * 商品IDs
     */
    private String productIds;
    
    /**
     * 订单标题
     */
    private String title;
    
    /**
     * 商品描述
     */
    private String detail;
    
    /**
     * 支付价格
     */
    private String totalFee;
    
    /**
     * 支付成功的通知地址
     */
    private String notifyUrl;
    
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
    
    public String getProductIds()
    {
        return productIds;
    }
    
    public void setProductIds(String productIds)
    {
        this.productIds = productIds;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getDetail()
    {
        return detail;
    }
    
    public void setDetail(String detail)
    {
        this.detail = detail;
    }
    
    public String getTotalFee()
    {
        return totalFee;
    }
    
    public void setTotalFee(String totalFee)
    {
        this.totalFee = totalFee;
    }
    
    public String getNotifyUrl()
    {
        return notifyUrl;
    }
    
    public void setNotifyUrl(String notifyUrl)
    {
        this.notifyUrl = notifyUrl;
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
        builder.append("WechatPayRequest [clientType=");
        builder.append(clientType);
        builder.append(", orderCode=");
        builder.append(orderCode);
        builder.append(", productIds=");
        builder.append(productIds);
        builder.append(", title=");
        builder.append(title);
        builder.append(", detail=");
        builder.append(detail);
        builder.append(", totalFee=");
        builder.append(totalFee);
        builder.append(", notifyUrl=");
        builder.append(notifyUrl);
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
        return StringUtils.isAnyBlank(orderCode, totalFee, title, notifyUrl, productIds, random, salt);
    }
}
