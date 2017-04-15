/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: AlipayRequest.java
 * Author:   qxf
 * Date:     2016年11月16日 下午2:48:47
 */
package com.smeyun.payment.gopay.entity;

import org.apache.commons.lang3.StringUtils;

import com.smeyun.platform.util.base.entity.BaseRequest;

/**
 * 国付宝支付的请求消息
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class GopayRequest extends BaseRequest
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
    private String goodsName;
    
    /**
     * 商品描述
     */
    private String goodsDetail;
    
    /**
     * 订单价格
     */
    private String totalFee;
    
    /**
     * 佣金
     */
    private String feeAmt;
    
    /**
     * 支付成功的通知地址
     */
    private String notifyUrl;
    
    /**
     * 支付失败的通知地址
     */
    private String returnUrl;
    
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
   
    public String getGoodsName()
    {
        return goodsName;
    }

    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getGoodsDetail()
    {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail)
    {
        this.goodsDetail = goodsDetail;
    }

    public String getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(String totalFee)
    {
        this.totalFee = totalFee;
    }

    public String getFeeAmt()
    {
        return feeAmt;
    }

    public void setFeeAmt(String feeAmt)
    {
        this.feeAmt = feeAmt;
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
        builder.append(", goodsName=");
        builder.append(goodsName);
        builder.append(", goodsDetail=");
        builder.append(goodsDetail);
        builder.append(", totalFee=");
        builder.append(totalFee);
        builder.append(", feeAmt=");
        builder.append(feeAmt);
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
        return StringUtils.isAnyBlank(orderCode, goodsName, totalFee, notifyUrl, returnUrl, random, salt);
    }
}
