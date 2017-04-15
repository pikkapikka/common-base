/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: AlipayCore.java
 * Author:   qxf
 * Date:     2016年11月17日 上午9:26:14
 */
package com.smeyun.payment.alipay.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smeyun.platform.util.common.encrypt.Md5Encrypt;

/**
 * 类名：AlipayFunction
 * 功能：支付宝接口公用函数类
 * 详细：该类是请求、通知返回两个文件所调用的公用函数核心处理文件，不需要修改
 */
public class AlipayCore
{
    
    /**
     * 生成签名结果
     * @param sArray 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildMysign(Map<String, String> sArray)
    {
        String prestr = createLinkString(sArray); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        prestr = prestr + sArray.get("keys"); //把拼接后的字符串再与安全校验码直接连接起来
        String mysign = Md5Encrypt.md5(prestr);
        return mysign;
    }
    
    /** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray)
    {
        
        Map<String, String> result = new HashMap<String, String>();
        
        if (sArray == null || sArray.size() <= 0)
        {
            return result;
        }
        
        for (String key : sArray.keySet())
        {
            String value = sArray.get(key);
            if (StringUtils.isEmpty(value) || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type"))
            {
                continue;
            }
            result.put(key, value);
        }
        
        return result;
    }
    
    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params)
    {
        
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        
        String prestr = "";
        
        for (int i = 0; i < keys.size(); i++)
        {
            String key = keys.get(i);
            String value = params.get(key);
            
            if (i == keys.size() - 1)
            {
                //拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            }
            else
            {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        
        return prestr;
    }
}
