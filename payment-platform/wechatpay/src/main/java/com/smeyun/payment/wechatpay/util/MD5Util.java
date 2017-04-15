/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: MD5Util.java
 * Author:   qxf
 * Date:     2016年11月23日 上午9:28:32
 */
package com.smeyun.payment.wechatpay.util;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 微信专用的MD5工具类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MD5Util
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MD5Util.class);
    
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};
    
    private static String byteArrayToHexString(byte b[])
    {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
        {
            resultSb.append(byteToHexString(b[i]));
        }
        
        return resultSb.toString();
    }
    
    private static String byteToHexString(byte b)
    {
        int n = b;
        if (n < 0)
        {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    
    /**
     * MD5加密
     * 
     * @param origin 要加密的字符串
     * @param charsetname 字符集
     * @return  String 加密后的值
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static String MD5Encode(String origin, String charsetname)
    {
        String resultString = null;
        try
        {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
            {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            }
            else
            {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        }
        catch (Exception exception)
        {
            LOGGER.error("md5 encode failed.", exception);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, exception);
        }
        return resultString;
    }
    
}
