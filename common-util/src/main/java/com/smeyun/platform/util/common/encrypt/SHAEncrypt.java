/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: SHAEncrypt.java
 * Author:   qxf
 * Date:     2016年11月24日 下午2:38:20
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
 * 〈功能详细描述〉
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class SHAEncrypt
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SHAEncrypt.class);
    
    private SHAEncrypt()
    {
        
    }
    
    /**
     * 对字符串进行SHA签名
     * 
     * @param text 明文
     * @return 密文
     */
    public static String sha(String text)
    {
        if (StringUtils.isEmpty(text))
        {
            LOGGER.error("parameter text is invalid.");
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        return DigestUtils.sha1Hex(getContentBytes(text, PlatformConstant.CHARSET_UTF8));
    }
    
    /**
     * 校验SHA加密值
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
        
        return DigestUtils.sha1Hex(getContentBytes(text, PlatformConstant.CHARSET_UTF8)).equals(result);
    }
    
    /**
     * 对字符串进行SHA签名
     * 
     * @param text 明文
     * @param charset 字符集
     * @return 密文
     */
    public static String sha(String text, String charset)
    {
        if (StringUtils.isEmpty(text))
        {
            LOGGER.error("parameter text is invalid.");
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        return DigestUtils.sha1Hex(getContentBytes(text, charset));
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
}
