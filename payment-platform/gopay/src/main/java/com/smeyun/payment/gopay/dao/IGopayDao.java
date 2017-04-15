/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: IGopayDao.java
 * Author:   qxf
 * Date:     2016年11月24日 下午4:23:13
 */
package com.smeyun.payment.gopay.dao;

import com.smeyun.payment.gopay.entity.PayNotifyData;
import com.smeyun.payment.gopay.entity.PayRequestData;


/**
 * 数据库操作接口
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface IGopayDao
{
    /**
     * 保存支付请求数据
     * 
     * @param data 请求数据
     * @return 主键ID
     * @author qxf
     * @see        [相关类/方法]（可选）
     * @since      [产品/模块版本] （可选）
     */
    long saveRequestData(PayRequestData data);
    
    /**
     * 根据请求ID的请求信息
     * 
     * @param id 订单编码
     * @return PayRequestData 请求记录
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    PayRequestData queryRequestDataById(long id);
    
    /**
     * 保存支付请求数据
     * 
     * @param data 请求数据
     * @return 主键ID
     * @author qxf
     * @see        [相关类/方法]（可选）
     * @since      [产品/模块版本] （可选）
     */
    long saveNotifyData(PayNotifyData data);
    
    /**
     * 根据请求ID查询响应信息
     * 
     * @param reqId 请求ID
     * @return PayNotifyData 请求记录
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    PayNotifyData queryNotifyDataByReqId(long reqId);
    
    /**
     * 更新状态
     * 
     * @param data 数据
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    void updateNotifyData(PayNotifyData data);
}
