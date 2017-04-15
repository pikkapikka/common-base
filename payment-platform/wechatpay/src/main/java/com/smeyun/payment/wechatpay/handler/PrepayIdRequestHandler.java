/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: AlipayRequest.java
 * Author:   qxf
 * Date:     2016年11月16日 下午2:48:47
 */
package com.smeyun.payment.wechatpay.handler;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.payment.wechatpay.util.MD5Util;
import com.smeyun.payment.wechatpay.util.Sha1Util;
import com.smeyun.platform.util.common.constant.PlatformConstant;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;
import com.smeyun.platform.util.common.http.HttpClientUtil;
import com.smeyun.platform.util.common.http.bean.SmeHttpResponse;

/**
 * 支付前请求
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@SuppressWarnings("rawtypes")
public class PrepayIdRequestHandler extends RequestHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PrepayIdRequestHandler.class);
    
    public PrepayIdRequestHandler(HttpServletRequest request, HttpServletResponse response)
    {
        super(request, response);
    }
    
    public String createMD5(String str)
    {
        return MD5Util.MD5Encode(str, PlatformConstant.CHARSET_UTF8).toUpperCase();
    }
    
    /**
     * SHA1
     * 
     * @return
     * @throws Exception
     */
    
    public String createSHA1Sign()
    {
        StringBuffer sb = new StringBuffer();
        Set es = super.getAllParameters().entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + v + "&");
        }
        String params = sb.substring(0, sb.lastIndexOf("&"));
        String appsign = Sha1Util.getSha1(params);
        this.setDebugInfo(this.getDebugInfo() + "\r\n" + "sha1 sb:" + params);
        this.setDebugInfo(this.getDebugInfo() + "\r\n" + "app sign:" + appsign);
        return appsign;
    }
    
    public String send()
    {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>\n");
        Set es = super.getAllParameters().entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            
            if ("body".equals(entry.getKey()) || "sign".equals(entry.getKey()) || "detail".equals(entry.getKey()))
            {
                xml.append("<" + entry.getKey() + "><![CDATA[")
                        .append(entry.getValue())
                        .append("]]></" + entry.getKey() + ">\n");
            }
            else
            {
                xml.append("<" + entry.getKey() + ">").append(entry.getValue()).append("</" + entry.getKey() + ">\n");
            }
        }
        xml.append("</xml>");
        String requestUrl = super.getGateUrl();
        String resContent = "";
        
        LOGGER.error("request information is ({}).", xml.toString());
        try
        {
            SmeHttpResponse response = HttpClientUtil.postJsonDataToUrl(requestUrl, xml.toString());
            resContent = response.getContent();
        }
        catch (Exception e)
        {
            LOGGER.error("create link of two-dimension code failed, url=" + requestUrl + ", xml=" + xml.toString(), e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.NETWORK_ACCESS_ERROR, e);
        }
        
        return resContent;
        
    }
    // �ύԤ֧��
    //	public String sendPrepay() throws JSONException {
    //		String prepayid = "";
    //		StringBuffer sb = new StringBuffer("{");
    //		Set es = super.getAllParameters().entrySet();
    //		Iterator it = es.iterator();
    //		while (it.hasNext()) {
    //			Map.Entry entry = (Map.Entry) it.next();
    //			String k = (String) entry.getKey();
    //			String v = (String) entry.getValue();
    //			if (null != v && !"".equals(v) && !"appkey".equals(k)) {
    //				sb.append("\"" + k + "\":\"" + v + "\",");
    //			}
    //		}
    //		String params = sb.substring(0, sb.lastIndexOf(","));
    //		params += "}";
    //
    //		String requestUrl = super.getGateUrl();
    //		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "requestUrl:"
    //				+ requestUrl);
    //		TenpayHttpClient httpClient = new TenpayHttpClient();
    //		httpClient.setReqContent(requestUrl);
    //		String resContent = "";
    //		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "post data:" + params);
    //		if (httpClient.callHttpPost(requestUrl, params)) {
    //			resContent = httpClient.getResContent();
    //			if (2 == resContent.indexOf("prepayid")) {
    //				prepayid = JsonUtil.getJsonValue(resContent, "prepayid");
    //			}
    //			this.setDebugInfo(this.getDebugInfo() + "\r\n" + "resContent:"
    //					+ resContent);
    //		}
    //		return prepayid;
    //	}
    
    //    public String sendAccessToken()
    //    {
    //        String accesstoken = "";
    //        StringBuffer sb = new StringBuffer("{");
    //        Set es = super.getAllParameters().entrySet();
    //        Iterator it = es.iterator();
    //        while (it.hasNext())
    //        {
    //            Map.Entry entry = (Map.Entry) it.next();
    //            String k = (String) entry.getKey();
    //            String v = (String) entry.getValue();
    //            if (null != v && !"".equals(v) && !"appkey".equals(k))
    //            {
    //                sb.append("\"" + k + "\":\"" + v + "\",");
    //            }
    //        }
    //        String params = sb.substring(0, sb.lastIndexOf(","));
    //        params += "}";
    //        
    //        String requestUrl = super.getGateUrl();
    //        //		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "requestUrl:"
    //        //				+ requestUrl);
    //        TenpayHttpClient httpClient = new TenpayHttpClient();
    //        httpClient.setReqContent(requestUrl);
    //        String resContent = "";
    //        //		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "post data:" + params);
    //        if (httpClient.callHttpPost(requestUrl, params))
    //        {
    //            resContent = httpClient.getResContent();
    //            if (2 == resContent.indexOf("errcode"))
    //            {
    //                accesstoken = resContent.substring(11, 16);//��ȡ��Ӧ��errcode��ֵ
    //            }
    //        }
    //        return accesstoken;
    //    }
}
