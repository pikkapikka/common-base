/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: Md5Encrypt.java
 * Author:   qxf
 * Date:     2016年11月16日 下午3:14:42
 */
package com.smeyun.platform.util.common.encrypt;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.platform.util.common.constant.PlatformConstant;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * MD5加密的
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class Md5Encrypt
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Md5Encrypt.class);
    
    private Md5Encrypt()
    {
        
    }
    
    /**
     * MD5加密
     * @param text 要加密的字符串
     * 
     * @author qxf
     * @see [相关类/方法]（可选）
     */
    public static String md5(String text)
    {
        if (StringUtils.isEmpty(text))
        {
            LOGGER.error("parameter text is invalid.");
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        return DigestUtils.md5Hex(getContentBytes(text, PlatformConstant.CHARSET_UTF8));
    }
    
    /**
     * MD5加密
     * @param text 要加密的字符串
     * @param charset 字符集
     * 
     * @author qxf
     * @see [相关类/方法]（可选）
     */
    public static String md5(String text, String charset)
    {
        if (StringUtils.isEmpty(text))
        {
            LOGGER.error("parameter text is invalid.");
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        return DigestUtils.md5Hex(getContentBytes(text, PlatformConstant.CHARSET_UTF8));
    }
    
    /**
     * 校验MD5加密值
     * 
     * @param text 要加密的字符串
     * @param result 要验证的加密后的值
     * 
     * @author qxf
     * @see [相关类/方法]（可选）
     */
    public static boolean vertify(String text, String result)
    {
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(result))
        {
            LOGGER.error("parameter text or result is invalid.");
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        return DigestUtils.md5Hex(getContentBytes(text, PlatformConstant.CHARSET_UTF8)).equals(result);
    }
    
    private static byte[] getContentBytes(String content, String charset)
    {
        if (StringUtils.isEmpty(charset))
        {
            return content.getBytes();
        }
        
        try
        {
            return content.getBytes(charset);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("convert to bytes failed, content=" + content, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    public static void main(String[] args)
    {
        System.out.println(md5("1ordercode410.01http://devpayment.smeyun.com/alipay/platform/payment/test/notifyhttp://devpayment.smeyun.com/alipay/platform/payment/test/returnaa"));
        System.out.println(md5("1ordercode5bbbb0.01http://devpayment.smeyun.com/alipay/platform/payment/test/notifyaa"));
        System.out.println(md5("1ordercode6990.01http://192.168.11.17:9090/gopay/platform/payment/test/notifyhttp://devwww.smeservice.com.cnaa"));
    }
}
