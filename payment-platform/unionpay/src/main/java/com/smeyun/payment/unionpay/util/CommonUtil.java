/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: CommonUtil.java
 * Author:   qxf
 * Date:     2016年11月21日 上午11:37:45
 */
package com.smeyun.payment.unionpay.util;

import com.smeyun.payment.unionpay.entity.PayClientType;
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
    
   
}
