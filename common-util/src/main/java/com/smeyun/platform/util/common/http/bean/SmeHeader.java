/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: SmeHeader.java
 * Author:   qxf
 * Date:     2016年11月17日 下午3:09:35
 */
package com.smeyun.platform.util.common.http.bean;

/**
 * 消息头部
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class SmeHeader
{
    /**
     * 构造函数
     */
    public SmeHeader()
    {
    }
    
    /**
     * 构造函数
     * @param name 消息头部的KEY
     * @param value 消息头部的VALUE
     */
    public SmeHeader(String name, String value)
    {
        this.name = name;
        this.value = value;
    }
    
    private String name;
    
    private String value;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
}
