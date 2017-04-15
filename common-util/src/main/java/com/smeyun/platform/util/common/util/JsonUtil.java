/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: JsonUtil.java
 * Author:   qxf
 * Date:     2016年12月22日 下午2:28:24
 */
package com.smeyun.platform.util.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * JSON转换工具
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class JsonUtil
{
    private JsonUtil()
    {
        
    }
    
    /**
     * 将JSON字符串转换为JSONObject。
     * 
     * @param text json字符串
     * @return  JSONObject
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static JSONObject parseObject(String text)
    {
        return JSONObject.parseObject(text);
    }
    
    public static <T> T parseObject(String text, Class<T> clazz)
    {
        return JSONObject.parseObject(text, clazz);
    }
    
    public static String toJSONString(Object object)
    {
        return JSONObject.toJSONString(object);
    }
    
    public static String toJSONStringUseDefaultDateFormat(Object object)
    {
        return JSONObject.toJSONString(object, new SerializerFeature[] {SerializerFeature.WriteDateUseDateFormat});
    }
    
    public static String toJSONStringUseDateFormat(Object object, String format)
    {
        return JSONObject.toJSONStringWithDateFormat(object,
                format,
                new SerializerFeature[] {SerializerFeature.WriteDateUseDateFormat});
    }
}
