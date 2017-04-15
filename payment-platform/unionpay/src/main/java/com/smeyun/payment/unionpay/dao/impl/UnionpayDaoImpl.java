/**
* Copyright © 2016 SME Corporation. All rights reserved
* @Title: UnionpayDaoImpl.java
* @Description: TODO
* @Author liuyaoshen
* @Date 2016年12月26日
* @Version v1.0
*/

package com.smeyun.payment.unionpay.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smeyun.payment.unionpay.dao.IUnionpayDao;
import com.smeyun.payment.unionpay.entity.PayNotifyData;
import com.smeyun.payment.unionpay.entity.PayRequestData;
import com.smeyun.platform.util.base.dao.MybatisDao;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
* JDK version used:      <JDK1.8> 
* @ClassName: UnionpayDaoImpl
* @Description: TODO
* @author liuyaoshen
* @date 2016年12月26日
*/
@Service 
public class UnionpayDaoImpl implements IUnionpayDao
{
    
 // 日志访问对象
    private static final Logger LOG = LoggerFactory.getLogger(UnionpayDaoImpl.class);
    
    @Autowired
    private MybatisDao mybatisDao;
    
    /**
     * @see com.smeyun.payment.alipay.dao.IAlipayDao#saveRequestData(com.smeyun.payment.alipay.entity.PayRequestData)<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public long saveRequestData(PayRequestData data)
    {
        try
        {
            mybatisDao.insert("insertRequestData", data);
            return data.getId();
        }
        catch (SmeyunUncheckedException e)
        {
            LOG.error("save pay infomation failed, data=" + data, e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("save pay infomation exception, data=" + data, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, e);
        }
    }
    
    /**
     * @see com.smeyun.payment.alipay.dao.IAlipayDao#queryRequestDataByOrderCode(java.lang.String)<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PayRequestData> queryRequestDataByOrderCode(String orderCode)
    {
        try
        {
            return mybatisDao.queryList("queryRequestDataByOrderCode", orderCode);
        }
        catch (SmeyunUncheckedException e)
        {
            LOG.error("query pay infomation failed, orderCode=" + orderCode, e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("query pay infomation exception, orderCode=" + orderCode, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, e);
        }
    }
    
    /**
     * @see com.smeyun.payment.alipay.dao.IAlipayDao#queryRequestDataById(long)<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public PayRequestData queryRequestDataById(long id)
    {
        try
        {
            return (PayRequestData) mybatisDao.queryData("queryRequestDataById", id);
        }
        catch (SmeyunUncheckedException e)
        {
            LOG.error("query pay infomation failed, id=" + id, e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("query pay infomation exception, id=" + id, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, e);
        }
    }
    
    /**
     * @see com.smeyun.payment.alipay.dao.IAlipayDao#saveNotifyData(com.smeyun.payment.alipay.entity.PayNotifyData)<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public long saveNotifyData(PayNotifyData data)
    {
        try
        {
            mybatisDao.insert("insertNotifyData", data);
            return data.getId();
        }
        catch (SmeyunUncheckedException e)
        {
            LOG.error("save notify infomation failed, data=" + data, e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("save notify infomation exception, data=" + data, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, e);
        }
    }
    
    /**
     * @see com.smeyun.payment.alipay.dao.IAlipayDao#queryNotifyDataByReqId(long)<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public PayNotifyData queryNotifyDataByReqId(long reqId)
    {
        try
        {
            return (PayNotifyData) mybatisDao.queryData("queryNotifyDataByReqId", reqId);
        }
        catch (SmeyunUncheckedException e)
        {
            LOG.error("query notify infomation failed, reqId=" + reqId, e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("query notify infomation exception, reqId=" + reqId, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, e);
        }
    }
    
    /**
     * @see com.smeyun.payment.alipay.dao.IAlipayDao#updateNotifyData(com.smeyun.payment.alipay.entity.PayNotifyData)<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public void updateNotifyData(PayNotifyData data)
    {
        try
        {
            mybatisDao.update("updateNotifyData", data);
        }
        catch (SmeyunUncheckedException e)
        {
            LOG.error("update notify infomation failed, data=" + data, e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("update notify infomation exception, data=" + data, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, e);
        }
        
    }
}
