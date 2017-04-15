/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: WechatConstant.java
 * Author:   qxf
 * Date:     2016年11月23日 上午9:28:32
 */
package com.smeyun.payment.wechatpay.util;

/**
 * 常量
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class WechatConstant
{
    // 成功返回值
    public static final String RET_SUCCESS = "SUCCESS";
    
    // 失败的返回值
    public static final String RET_FAIL = "FAIL";
    
    // 格式化字符串
    public static final String RETURN_FORMAT_XML = "<xml><return_code><![CDATA[%s]]></return_code>"
            + "<return_msg><![CDATA[%s]]></return_msg></xml>";
    
    // 响应成功
    public static final String RESPONSE_SUCCESS = "0";
    
    // 响应失败
    public static final String RESPONSE_FAILED = "1";
}
