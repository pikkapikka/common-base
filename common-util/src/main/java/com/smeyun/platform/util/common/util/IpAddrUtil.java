/*
 * Copyright (C), 2002-2017, 重庆锋云汇智数据科技有限公司
 * FileName: IpAddrUtil.java
 * Author:   qxf
 * Date:     2017年1月10日 上午9:21:59
 */
package com.smeyun.platform.util.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * IP地址的工具类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class IpAddrUtil
{
    private IpAddrUtil()
    {
        
    }
    
    /**
     * 判断是否为IP地址
     *  
     * @param ipAddr IP地址
     * @return boolean 是返回true，不是返回false
     * 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static boolean isIpAddress(String ipAddr)
    {
        if (StringUtils.isEmpty(ipAddr))
        {
            return false;
        }
        
        String regTxt = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d"
                + "|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
        return regMatch(ipAddr, regTxt);
    }
    
    private static boolean regMatch(String source, String regTxt)
    {
        Pattern pattern = Pattern.compile(regTxt);
        Matcher matcher = pattern.matcher(source);
        return matcher.matches();
    }
    
    /** 
     * 将IP地址转换为long
     * 
     * @param ipAddr ip地址，比如：127.0.0.1
     * @return long 以long表示的IP地址，比如：2130706433
     * @throws
     */
    public static long ip2Long(String ipAddr)
    {
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = ipAddr.indexOf(".");
        int position2 = ipAddr.indexOf(".", position1 + 1);
        int position3 = ipAddr.indexOf(".", position2 + 1);
        
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(ipAddr.substring(0, position1));
        ip[1] = Long.parseLong(ipAddr.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(ipAddr.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(ipAddr.substring(position3 + 1));
        
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }
    
    /**
     *  将long转换为IP字符串
     *  
     * @param ipAddr 以long格式表示的IP地址, 比如：2130706433
     * @return String IP地址，比如：127.0.0.1
     * @throws
     */
    public static String long2IP(long ipAddr)
    {
        StringBuilder sb = new StringBuilder();
        
        //直接右移24位
        sb.append(String.valueOf((ipAddr >>> 24)));
        sb.append(".");
        
        //将高8位置0，然后右移16位
        sb.append(String.valueOf((ipAddr & 0x00FFFFFF) >>> 16));
        sb.append(".");
        
        //将高16位置0，然后右移8位
        sb.append(String.valueOf((ipAddr & 0x0000FFFF) >>> 8));
        sb.append(".");
        
        //将高24位置0
        sb.append(String.valueOf((ipAddr & 0x000000FF)));
        return sb.toString();
    }
    
    /** 
     * 将IP地址转换为long
     * 
     * @param request http请求
     * @return long 以long表示的IP地址，比如：2130706433
     * @throws
     */
    public static long getLongIp(HttpServletRequest request)
    {
        try
        {
            String ipAddr = getIpAddr(request);
            if (StringUtils.isBlank(ipAddr))
            {
                return 0l;
            }
            else
            {
                return ip2Long(ipAddr);
            }
        }
        catch (Exception e)
        {
            return 0l;
        }
    }
    
    /**
     * 从http请求获取客户端IP地址
     * 
     * @param request http请求
     * @return String IP地址
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
