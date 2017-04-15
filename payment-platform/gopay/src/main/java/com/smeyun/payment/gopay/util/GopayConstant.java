/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: GopayConstant.java
 * Author:   qxf
 * Date:     2016年11月24日 下午4:42:41
 */
package com.smeyun.payment.gopay.util;

/**
 * 常量类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class GopayConstant
{
    /*
     * 网关版本，必须为2.2
     */
    public static final String VERSION = "2.2";
    
    /*
     * 字符编码格式，目前支持 1:GBK 或 2:UTF-8
     */
    public static final String INPUT_CHARSET = "2";
    
    /*
     * 网关语言，目前支持 1:中文 或 2:英文
     */
    public static final String LANGUAGE = "1";
    
    /*
     * 报文签名域signValue的加密方式，1 MD5 ，2 SHA
     */
    public static final String SIGN_TYPE = "1";
    
    /*
     * 交易代码，必须为8888
     */
    public static final String TRAN_CODE = "8888";
    
    /*
     * 币种，目前只支持156代表人民币
     */
    public static final String CURRENCY_TYPE = "156";
    
    /*
     * 国付宝交易成功
     */
    public static final String TRADE_SUCCESS = "0000";
    
    /*
     * 国付宝交易失败
     */
    public static final String TRADE_FAIL = "9999";
    
    // 响应成功
    public static final String RESPONSE_SUCCESS = "0";
    
    // 响应失败
    public static final String RESPONSE_FAILED = "1";
}
