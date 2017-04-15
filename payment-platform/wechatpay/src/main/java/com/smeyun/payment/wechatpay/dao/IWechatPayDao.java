/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: IWechatPayDao.java
 * Author:   qxf
 * Date:     2016年11月23日 上午11:36:03
 */
package com.smeyun.payment.wechatpay.dao;

import com.smeyun.payment.wechatpay.entity.PayNotifyData;
import com.smeyun.payment.wechatpay.entity.PayRequestData;

/**
 * 数据库访问接口
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface IWechatPayDao
{
    /**
     * 保存请求数据
     *
     * @param data 数据
     * @author [作者]（必须，使用汉语）
     * @see  [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    long saveRequestData(PayRequestData data);
    
    /**
     * 根据ID查询请求数据
     * 
     * @param reqId 请求ID
     * @return  PayRequestData 请求数据
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    PayRequestData queryRequestDataById(long reqId);
    
    /**
     * 保存通知的消息
     * 
     * @param data 通知数据
     * @return  long
     * @exception/throws [异常类型] [异常说明]
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    long saveNotifyData(PayNotifyData data);
    
    /**
     * 根据ID查询请求通知响应数据
     * 
     * @param reqId 请求ID
     * @return  PayRequestData 请求数据
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    PayNotifyData queryNotifyDataByReqId(long reqId);
    
    /**
     * 更新请求数据
     * 
     * @param data 数据信息
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    void updateNotifyData(PayNotifyData data);
}
