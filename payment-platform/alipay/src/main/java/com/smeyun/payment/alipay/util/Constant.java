/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: Constant.java
 * Author:   qxf
 * Date:     2016年11月17日 上午9:26:14
 */
package com.smeyun.payment.alipay.util;

/**
 * 常量
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class Constant
{
    private Constant()
    {
        
    }
    
    /*
     * 支付类型："1"
     */
    public static final String PAYMENT_TYPE = "1";
    
    public static final String RET_FAIL = "fail";
    
    public static final String RET_SUCCESS = "success";
    
    // 签名方式
    public static final String SIGN_TYPE = "MD5";
    
    // 调用的接口名，无需修改
    public static final String SERVICE_PC = "create_direct_pay_by_user";
    
    // 手机网页即时到账交易接口
    public static final String SERVICE_PHONE = "alipay.wap.trade.create.direct";
    
    // 手机网页即时到账授权接口
    public static final String SERVICE_PHONE_AUTH = "alipay.wap.auth.authandexecute";
    
    // 支付宝提供给商户的服务接入网关URL(PC)
    public static final String PAY_GATEWAY_PC = "https://mapi.alipay.com/gateway.do?";
    
    /*
     * 支付宝提供给商户的服务接入网关URL(手机端)
     */
    public static final String PAY_GATEWAY_PHONE = "http://wappaygw.alipay.com/service/rest.htm?";
    
    // 支付宝通知支付成功
    public static final String VAL_PAY_SUCCESS = "TRADE_SUCCESS";
    
    // 支付宝通知支付完成
    public static final String VAL_PAY_FINISH = "TRADE_FINISH";
    
    // 响应成功
    public static final String RESPONSE_SUCCESS = "0";
    
    // 响应失败
    public static final String RESPONSE_FAILED = "1";
    
    // 数据ID的KEY
    public static final String KEY_DATAID = "DATAID";
    
    // 字符串拼接的符号
    public static final String KEY_SPLITCHAR = ",";
    
}
