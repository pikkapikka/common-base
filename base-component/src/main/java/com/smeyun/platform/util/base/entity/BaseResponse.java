/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: AlipayResponse.java
 * Author:   qxf
 * Date:     2016年11月16日 下午4:26:13
 */
package com.smeyun.platform.util.base.entity;

/**
 * 支付返回对象
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class BaseResponse
{
    /**
     * 数据内容
     */
    private Object data;
    
    /**
     * 构造函数
     */
    public BaseResponse()
    {
        
    }
    
    /**
     * 构造函数
     * @param data 数据
     */
    public BaseResponse(Object data)
    {
        this.data = data;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }
}
