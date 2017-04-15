/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: GopayUtil.java
 * Author:   qxf
 * Date:     2016年11月24日 下午4:43:02
 */
package com.smeyun.payment.gopay.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.payment.gopay.entity.PayClientType;
import com.smeyun.platform.util.common.constant.PlatformConstant;
import com.smeyun.platform.util.common.encrypt.Md5Encrypt;
import com.smeyun.platform.util.common.encrypt.SHAEncrypt;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;
import com.smeyun.platform.util.common.util.PlatformUtil;

/**
 * 工具类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class GopayUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GopayUtil.class);
    
    /**
     * Convenience method to get the IP Address from client.
     * 
     * @param request the current request
     * @return IP to application
     */
    public static String getIpAddr(HttpServletRequest request)
    {
        if (request == null)
        {
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * 解析客户类型
     * 
     * @author     [作者]（必须，使用汉语）
     * @see        [相关类/方法]（可选）
     * @since      [产品/模块版本] （可选）
     */
    public static PayClientType parseClientType(int type)
    {
        if (type == PayClientType.PC.getType())
        {
            return PayClientType.PC;
        }
        else if (type == PayClientType.PHONE.getType())
        {
            return PayClientType.PHONE;
        }
        else
        {
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
    }
    
    /**
     * 获取国付宝服务器时间 用于时间戳
     * 
     * @param url URL地址
     * @return 格式YYYYMMDDHHMMSS
     */
    public static String getGopayServerTime(String url)
    {
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 10000);
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, PlatformConstant.CHARSET_GBK);
        
        // 执行getMethod
        try
        {
            int statusCode = httpClient.executeMethod(getMethod);
            if (PlatformUtil.isHttpSuc(statusCode))
            {
                return StringUtils.trim((new String(getMethod.getResponseBody(), PlatformConstant.CHARSET_GBK)));
            }
            else
            {
                LOGGER.error("get gopay server time failed, url=({}), httpCode=({}).", url, statusCode);
                throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get gopay server time exception, url=" + url, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
        finally
        {
            getMethod.releaseConnection();
        }
    }
    
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildRequestMysign(String text)
    {
        String mysign = "";
        // 1 MD5 ，2 SHA
        if (text != null && !"".equals(text))
        {
            if ("1".equals(GopayConstant.SIGN_TYPE))
            {
                mysign = Md5Encrypt.md5(text);
            }
            if ("2".equals(GopayConstant.SIGN_TYPE))
            {
                mysign = SHAEncrypt.sha(text);
            }
        }
        return mysign;
    }
    
    /**
     * 组装返回的字符串
     * 
     * @param retCode 返回码
     * @param jumpUrl 跳转地址
     * @return  String
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static String createRetMsg(String retCode, String jumpUrl, Map<String, String> notifyParams)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("RespCode=");
        builder.append(retCode);
        builder.append("|JumpURL=");
        builder.append(jumpUrl);
        if (!StringUtils.isEmpty(jumpUrl))
        {
            builder.append(jumpUrl.contains("?") ? "&" : "?");
            builder.append("orderCode=");
            builder.append(notifyParams.get("merOrderNum"));
            builder.append("&payTime=");
            builder.append(notifyParams.get("tranFinishTime"));
        }
        
        return builder.toString();
    }
}
