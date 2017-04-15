/*
 * Copyright (C), 2002-2017, 重庆锋云汇智数据科技有限公司
 * FileName: PhoneNumberUtil.java
 * Author:   qxf
 * Date:     2017年1月10日 下午1:05:25
 */
package com.smeyun.platform.util.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 〈功能详细描述〉
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class PhoneNumberUtil
{
    private PhoneNumberUtil()
    {
        
    }
    
    /**
     * 判断是否为手机号码
     *  
     * @param number 号码
     * @return boolean 是手机号码为true，否则返回为false
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static boolean isPhoneNum(String number)
    {
        /*
         * 130 131 132 133 134 135 136 137 138 139 145 147 149 150 151 152 153 155 156 157 
         * 158 159 170 171 173 176 177 178 180 181 182 183 184 185 186 187 188 189
         */
        String regTxt = "^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,6-8])|(18[0-9]))\\d{8}$";
        return StringUtils.isEmpty(number) ? false : regMatch(number, regTxt);
    }
    
    /**
     * 判断是否为联通手机号码
     *  
     * @param number 号码
     * @return boolean 是手机号码为true，否则返回为false
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static boolean isChinaUnionNum(String number)
    {
        /*
         * 130 131 132 145 155 156 171 175 176 185 186
         */
        String regTxt = "^((13[0-2])|(14[5])|(15[5,6])|(17[1,5,6])|(18[5,6]))\\d{8}$";
        return StringUtils.isEmpty(number) ? false : regMatch(number, regTxt);
    }
    
    /**
     * 判断是否为中国电信手机号码
     *  
     * @param number 号码
     * @return boolean 是手机号码为true，否则返回为false
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static boolean isChinaTelNum(String number)
    {
        /*
        * 中国电信号码格式验证 手机段： 133 149 153 173 177 180 181 189 
        */  
        String regTxt = "^((13[3])|(14[9])|(15[3])|(17[3,7])|(18[0,1,9]))\\d{8}$";
        return StringUtils.isEmpty(number) ? false : regMatch(number, regTxt);
    }
    
    /**
     * 判断是否为中国移动手机号码
     *  
     * @param number 号码
     * @return boolean 是手机号码为true，否则返回为false
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static boolean isChinaMobileNum(String number)
    {
        /*
         * 134 135 136 137 138 139 147 150 151 152 157 158 159 178 182 183 184 187 188
         */
        String regTxt = "^((13[4-9])|(14[7])|(15[0,1,2,7,8,9])|(17[8])|(18[2,3,4,7,8]))\\d{8}$";
        return StringUtils.isEmpty(number) ? false : regMatch(number, regTxt);
    }
    
    private static boolean regMatch(String source, String regTxt)
    {
        Pattern pattern = Pattern.compile(regTxt);
        Matcher matcher = pattern.matcher(source);
        return matcher.matches();
    }
}
