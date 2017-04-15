/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: SoftHttpResponse.java
 * Author:   qxf
 * Date:     2016年11月17日 下午3:09:35
 */
package com.smeyun.platform.util.common.http.bean;

import java.util.Map;

/**
 * 消息响应
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class SmeHttpResponse
{
    
    /*
     * 返回的HTTP状态码
     */
    private int status;
    
    /*
     * 返回的内容
     */
    private String content;
    
    /*
     * 返回的headers
     */
    private Map<String, String> headers;
    
    public SmeHttpResponse()
    {
    }
    
    public SmeHttpResponse(int status, String content)
    {
        this.status = status;
        this.content = content;
    }
    
    public SmeHttpResponse(int status, String content, Map<String, String> headers)
    {
        this.status = status;
        this.content = content;
        this.headers = headers;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public Map<String, String> getHeaders()
    {
        return headers;
    }
    
    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }
}
