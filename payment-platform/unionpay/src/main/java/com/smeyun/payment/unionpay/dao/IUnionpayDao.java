/**
* Copyright © 2016 SME Corporation. All rights reserved
* @Title: IUnionpayDao.java
* @Description: TODO
* @Author liuyaoshen
* @Date 2016年12月26日
* @Version v1.0
*/

package com.smeyun.payment.unionpay.dao;

import java.util.List;

import com.smeyun.payment.unionpay.entity.PayNotifyData;
import com.smeyun.payment.unionpay.entity.PayRequestData;


/**
* JDK version used:      <JDK1.8> 
* @ClassName: IUnionpayDao
* @Description: TODO
* @author liuyaoshen
* @date 2016年12月26日
*/
public interface IUnionpayDao
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
     * 根据订单编码查询支付的请求信息
     * 
     * @param orderCode 订单编码
     * @return  List<PayRequestData> 请求记录
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    List<PayRequestData> queryRequestDataByOrderCode(String orderCode);
    
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
