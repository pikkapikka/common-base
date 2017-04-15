/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PlatformUtil.java
 * Author:   qxf
 * Date:     2016年11月22日 下午2:41:02
 */
package com.smeyun.platform.util.common.util;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 基本工具类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class PlatformUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformUtil.class);
    
    private static final int RANDOM_STRING_LEN = 32;
    
    /*
     * 正则表达式：验证邮箱
     */
    private static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    
    // 私有构造方法
    private PlatformUtil()
    {
        
    }
    
    /**
     * 生成32位随机字符串
     * 
     * @return String 随机字符串
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static String generateRandomString()
    {
        return RandomStringUtils.random(RANDOM_STRING_LEN, true, true);
    }
    
    /**
     * 生成随机字符串
     * 
     * @param count 字符串位数
     * @return String 随机字符串
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static String generateRandomString(int count)
    {
        return RandomStringUtils.random(count, true, true);
    }
    
    /**
     * 生成随机字符串(只包含数据)
     * 
     * @param count 字符串位数
     * @return String 随机字符串
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static String generateRandomNumber(int count)
    {
        return RandomStringUtils.random(count, false, true);
    }
    
    /**
     * 判断是否为成功的http错误码
     * 
     * @param httpStatus http状态码
     * @return boolean 如果状态是200,201,202，返回true，其余返回false
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static boolean isHttpSuc(int httpStatus)
    {
        return httpStatus == HttpStatus.SC_OK || httpStatus == HttpStatus.SC_CREATED
                || httpStatus == HttpStatus.SC_ACCEPTED;
    }
    
    /**
     * 休眠
     * 
     * @param milliSeconds 时间（毫秒）
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static void sleep(long milliSeconds)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(milliSeconds);
        }
        catch (InterruptedException e)
        {
            LOGGER.error("system sleep failed, milliSeconds=" + milliSeconds, e);
            throw new SmeyunUncheckedException(e);
        }
    }
    
    /**
     * 关闭流
     * 
     * @param closeable 可关闭的流
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static void close(Closeable closeable)
    {
        if (null != closeable)
        {
            try
            {
                closeable.close();
            }
            catch (IOException e)
            {
                LOGGER.error("system close stream failed.", e);
                throw new SmeyunUncheckedException(e);
            }
        }
        
    }
    
    /**
     * 获取当前日期 yyyyMMdd
     * 
     * @param date 日期
     * @return String
     */
    public static String formatDate(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String strDate = formatter.format(date);
        return strDate;
    }
    
    /**
     * 获取当前日期的格式化字符串
     * 
     * @param date 日期
     * @param format 格式化字符串
     * @return String
     */
    public static String formatDate(Date date, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String strDate = formatter.format(date);
        return strDate;
    }
    
    /**
     * 判断是否为手机号码
     *  
     * @param number 号码
     * @return boolean 是手机号码为true，否则返回为false
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static boolean isMobileNum(String number)
    {
        return PhoneNumberUtil.isPhoneNum(number);
    }
    
    /**
     * 判断是否为手机号码
     *  
     * @param number 号码
     * @return boolean 是手机号码为true，否则返回为false
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static boolean isUnionNum(String number)
    {
        return PhoneNumberUtil.isChinaUnionNum(number);
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
        return IpAddrUtil.isIpAddress(ipAddr);
    }
    
    /**
     * 判断是否为email地址
     *  
     * @param mail 邮件地址
     * @return boolean 是返回true，不是返回false
     * 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static boolean isMail(String mail)
    {
        return StringUtils.isBlank(mail) ? false : match(REGEX_EMAIL, mail);
    }
     
    private static boolean match(String pat, String str) {  
        Pattern pattern = Pattern.compile(pat);  
        Matcher match = pattern.matcher(str);  
        return match.find();  
    }
}
