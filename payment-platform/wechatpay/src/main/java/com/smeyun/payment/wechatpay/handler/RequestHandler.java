/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: AlipayRequest.java
 * Author:   qxf
 * Date:     2016年11月16日 下午2:48:47
 */
package com.smeyun.payment.wechatpay.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smeyun.payment.wechatpay.util.MD5Util;
import com.smeyun.payment.wechatpay.util.WechatPayUtil;

/**
 * 支付请求
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@SuppressWarnings("rawtypes")
public class RequestHandler
{
    
    private String gateUrl;
    
    private String key;
    
    private SortedMap parameters;
    
    private String debugInfo;
    
    protected HttpServletRequest request;
    
    protected HttpServletResponse response;
    
    public RequestHandler(HttpServletRequest request, HttpServletResponse response)
    {
        this.request = request;
        this.response = response;
        
        this.key = "";
        this.parameters = new TreeMap();
        this.debugInfo = "";
    }
    
    public void init()
    {
        //nothing to do
    }
    
    public String getGateUrl()
    {
        return gateUrl;
    }
    
    public void setGateUrl(String gateUrl)
    {
        this.gateUrl = gateUrl;
    }
    
    public String getKey()
    {
        return key;
    }
    
    public void setKey(String key)
    {
        this.key = key;
    }
    
    public String getParameter(String parameter)
    {
        String s = (String) this.parameters.get(parameter);
        return (null == s) ? "" : s;
    }
    
    @SuppressWarnings("unchecked")
    public void setParameter(String parameter, String parameterValue)
    {
        String v = "";
        if (null != parameterValue)
        {
            v = parameterValue.trim();
        }
        this.parameters.put(parameter, v);
    }
    
    public void removeParameter(String parameter)
    {
        this.parameters.remove(parameter);
    }
    
    public SortedMap getAllParameters()
    {
        return this.parameters;
    }
    
    public String getDebugInfo()
    {
        return debugInfo;
    }
    
    public String getRequestURL() throws UnsupportedEncodingException
    {
        
        this.createSign();
        
        StringBuffer sb = new StringBuffer();
        String enc = WechatPayUtil.getCharacterEncoding(this.request, this.response);
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            
            if (!"spbill_create_ip".equals(k))
            {
                sb.append(k + "=" + URLEncoder.encode(v, enc) + "&");
            }
            else
            {
                sb.append(k + "=" + v.replace("\\.", "%2E") + "&");
            }
        }
        
        String reqPars = sb.substring(0, sb.lastIndexOf("&"));
        
        return this.getGateUrl() + "?" + reqPars;
        
    }
    
    public void doSend() throws UnsupportedEncodingException, IOException
    {
        this.response.sendRedirect(this.getRequestURL());
    }
    
    protected void createSign()
    {
        StringBuffer sb = new StringBuffer();
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
            {
                sb.append(k + "=" + v + "&");
            }
        }
        
        sb.append("key=" + this.getKey());
        
        String enc = WechatPayUtil.getCharacterEncoding(this.request, this.response);
        String sign = MD5Util.MD5Encode(sb.toString(), enc).toUpperCase();
        
        this.setParameter("sign", sign);
        
        this.setDebugInfo(sb.toString() + " => sign:" + sign);
        
    }
    
    protected void setDebugInfo(String debugInfo)
    {
        this.debugInfo = debugInfo;
    }
    
    protected HttpServletRequest getHttpServletRequest()
    {
        return this.request;
    }
    
    protected HttpServletResponse getHttpServletResponse()
    {
        return this.response;
    }
    
}
