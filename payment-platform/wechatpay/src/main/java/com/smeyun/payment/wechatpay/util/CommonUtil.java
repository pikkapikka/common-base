/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: CommonUtil.java
 * Author:   qxf
 * Date:     2016年11月21日 上午11:37:45
 */
package com.smeyun.payment.wechatpay.util;

import javax.servlet.http.HttpServletRequest;

import com.smeyun.payment.wechatpay.entity.PayClientType;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 工具类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class CommonUtil
{    
    private CommonUtil()
    {
        
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
     * Convenience method to get the IP Address from client.
     * 
     * @param request the current request
     * @return IP to application
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) return "";
        
        String ip = request.getHeader("X-Forwarded-For"); 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        } 
        return ip; 
    }
}
