/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: Sha1Util.java
 * Author:   qxf
 * Date:     2016年11月23日 上午9:28:32
 */
package com.smeyun.payment.wechatpay.util;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.platform.util.common.constant.PlatformConstant;

/**
 * SHA签名类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class Sha1Util
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Sha1Util.class);
    
    public static String getNonceStr()
    {
        Random random = new Random();
        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), PlatformConstant.CHARSET_UTF8);
    }
    
    public static String getTimeStamp()
    {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
    
    // 创建签名SHA1
    @SuppressWarnings("rawtypes")
    public static String createSHA1Sign(SortedMap<String, String> signParams) throws Exception
    {
        StringBuffer sb = new StringBuffer();
        Set es = signParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + v + "&");
            // 要采用URLENCODER的原始值！
        }
        String params = sb.substring(0, sb.lastIndexOf("&"));
        // System.out.println("sha1之前:" + params);
        // System.out.println("SHA1签名为："+getSha1(params));
        return getSha1(params);
    }
    
    // Sha1签名
    public static String getSha1(String str)
    {
        if (str == null || str.length() == 0)
        {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        
        try
        {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes(PlatformConstant.CHARSET_UTF8));
            
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        }
        catch (Exception e)
        {
            LOGGER.error("get SHA encode failed.", e);
            return null;
        }
    }
}
