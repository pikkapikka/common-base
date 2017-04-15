/*
 * Copyright (C), 2002-2016, �����׹������������޹�˾
 * FileName: Common.java
 * Author:   qxf
 * Date:     2016��9��6�� ����6:03:27
 * Description: //ģ��Ŀ�ġ���������      
 * History: //�޸ļ�¼
 * <author>      <time>      <version>    <desc>
 * �޸�������             �޸�ʱ��            �汾��                  ����
 */
package com.sme.shortmsg.platform.util;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sme.shortmsg.platform.exception.ShortMsgUncheckedException;

/**
 * ����
 *
 * @author qxf
 * @see [�����/����]����ѡ��
 * @since [��Ʒ/ģ��汾] ����ѡ��
 */
public final class Common
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Common.class);
    
    private Common()
    {
        
    }
    
    public static void sleep(long milliSeconds)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(milliSeconds);
        }
        catch (InterruptedException e)
        {
            LOGGER.error("thread sleep catch exception:", e);
        }
    }
    
    /**
     * תΪ16���Ʒ���
     * @param str
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String paraTo16(String str) throws UnsupportedEncodingException
    {
        String hs = "";
        
        byte[] byStr = str.getBytes("UTF-8");
        for (int i = 0; i < byStr.length; i++)
        {
            String temp = (Integer.toHexString(byStr[i] & 0xFF));
            if (temp.length() == 1)
            {
                temp = "%0" + temp;
            }
            else
            {
                temp = "%" + temp;
            }
            hs = hs + temp;
        }
        return hs.toUpperCase();
        
    }
    
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
            throw new ShortMsgUncheckedException("parameter invalid.");
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
     * �ж��Ƿ�Ϊ�ֻ�����
     *  
     * @param number ����
     * @return boolean ���ֻ�����Ϊtrue�����򷵻�Ϊfalse
     * @see   [�ࡢ��#��������#��Ա]
     * @since [��ʼ�汾]
     */
    public static boolean isMobileNum(String number)
    {
        /*
         * 130 131 132 133 134 135 136 137 138 139 145 147 150 151 152 153 155 156 157 
         * 158 159 170 171 173 176 177 178 180 181 182 183 184 185 186 187 188 189
         */
        String regTxt = "^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0,1,3,6-8])|(18[0-9]))\\d{8}$";
        return regMatch(number, regTxt);
    }
    
    /**
     * �ж��Ƿ�Ϊ�ֻ�����
     *  
     * @param number ����
     * @return boolean ���ֻ�����Ϊtrue�����򷵻�Ϊfalse
     * @see   [�ࡢ��#��������#��Ա]
     * @since [��ʼ�汾]
     */
    public static boolean isUnionNum(String number)
    {
        /*
         * 130,131,132,155,156,185,186,145,176 
         */
        String regTxt = "^((13[0-2])|(14[5])|(15[5,6])|(17[6])|(18[5,6]))\\d{8}$";
        return regMatch(number, regTxt);
    }
    
    /**
     * �ж��Ƿ�ΪIP��ַ
     *  
     * @param ipAddr IP��ַ
     * @return boolean �Ƿ���true�����Ƿ���false
     * 
     * @see   [�ࡢ��#��������#��Ա]
     * @since [��ʼ�汾]
     */
    public static boolean isIpAddress(String ipAddr)
    {
        /*
         * 130,131,132,155,156,185,186,145,176 
         */
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
}
