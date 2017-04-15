/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: CommonUtil.java
 * Author:   qxf
 * Date:     2016年11月21日 上午11:37:45
 */
package com.smeyun.payment.alipay.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smeyun.payment.alipay.entity.PayClientType;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 工具类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class CommonUtil
{
    private static final int MIN_VALS_SIZE = 2;
    
    private CommonUtil()
    {
        
    }
    
    /**
     * 解析客户类型
     * 
     * @author     [作者]（必须，使用汉语）
     * @see        [相关类/方法]（可选）
     * @since      [产品/模块版本] （可选）
     */
    public static PayClientType parseClientType(int type)
    {
        if (type == PayClientType.PC.getType())
        {
            return PayClientType.PC;
        }
        else if (type == PayClientType.PHONE.getType())
        {
            return PayClientType.PHONE;
        }
        else
        {
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
    }
    
    /**
     * 分割参数字符串值。
     * 字符串格式 key1=val1,keys=val2
     * 
     * @param src 原始字符串
     * @return  Map<String,String>
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static Map<String, String> splitCommonParam(String src)
    {
        if (StringUtils.isEmpty(src))
        {
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        Map<String, String> retMap = new HashMap<String, String>();
        String[] tempKeys = src.split(Constant.KEY_SPLITCHAR);
        for (String tempVal : tempKeys)
        {
            String[] vals = tempVal.split("=");
            if (MIN_VALS_SIZE > vals.length)
            {
                throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
            }
            
            retMap.put(vals[0], vals[1]);
        }
        
        return retMap;
    }
}
