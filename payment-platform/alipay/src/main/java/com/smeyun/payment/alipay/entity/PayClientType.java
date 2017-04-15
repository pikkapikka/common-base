/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: PayClientType.java
 * Author:   qxf
 * Date:     2016年11月16日 下午4:56:33
 */
package com.smeyun.payment.alipay.entity;

/**
 * 客户端类型
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public enum PayClientType
{
    PC(1), PHONE(2);
    
    private int type;
    
    private PayClientType(int type)
    {
        this.type = type;
    }
    
    /**
     * 获取类型
     * 
     * @return  int
     */
    public int getType()
    {
        return this.type;
    }
    
    /**
     * 检验某个值是否有效
     * 
     * @param type 类型
     * @return  boolean
     */
    public static boolean judge(int type)
    {
        return type == PC.getType() || type == PHONE.type;
    }
}
