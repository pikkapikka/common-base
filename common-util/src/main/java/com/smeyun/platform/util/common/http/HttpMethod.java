/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: HttpMethod.java
 * Author:   qxf
 * Date:     2016年11月17日 下午3:09:35
 */
package com.smeyun.platform.util.common.http;

/**
 * http方法值
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public enum HttpMethod
{
    GET("GET"), HEAD("HEAD"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE"), OPTIONS("OPTIONS"),
    TRACE("TRACE");
    
    private String value;
    
    private HttpMethod(String value)
    {
        this.value = value;
    }
    
    /**
     * 
     * 获取http方法的值
     * 
     * @return String HTTP方法
     *
     * @author qxf
     */
    public String getValue()
    {
        return value;
    }
}
