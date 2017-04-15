/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PayNotofyData.java
 * Author:   qxf
 * Date:     2016年11月22日 上午11:21:48
 */
package com.smeyun.payment.unionpay.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;


@Setter @Getter
public class PayNotifyData
{
    // ID
    private long id;
    
    // 支付请求ID
    private long reqId;
    
    // 订单编码
    private String orderCode;
    
    // 交易金额
    private Long txnAmount;
    
    // 消费交易的的流水号，供后续查询用.相当于银联支付(消费交易)的流水号)
    private String queryId;
    
    // 应答码
    private String resCode;
    // 应答消息
    private String resMsg;
    
    // 订单推送时间(相当于支付申请时间)
    private Date txnTime;
    
    // 银联返回接收时间
    private Date revTime;
    
    // 结算金额
    private Long settleAmount;
    
    // 结算日期
    private Date settleDate;
    
    //时间戳
    private Long version;

   

    
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
