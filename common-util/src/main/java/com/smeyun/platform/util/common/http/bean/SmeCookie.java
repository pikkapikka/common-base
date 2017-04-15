/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: SmeCookie.java
 * Author:   qxf
 * Date:     2016年11月17日 下午3:09:35
 */
package com.smeyun.platform.util.common.http.bean;

/**
 * cookie
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class SmeCookie
{
    /**
     * 构造函数
     */
    public SmeCookie()
    {
    }
    
    /**
     * 构造函数
     * @param name
     * @param value
     * @param domain 域名
     */
    public SmeCookie(String name, String value, String domain)
    {
        this.domain = domain;
        this.name = name;
        this.value = value;
    }
    
    private String name;
    
    private String value;
    
    private String domain;
    
    private String path;
    
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
    
    public String getDomain()
    {
        return domain;
    }
    
    public void setDomain(String domain)
    {
        this.domain = domain;
    }
    
    public String getPath()
    {
        return path;
    }
    
    public void setPath(String path)
    {
        this.path = path;
    }
}
