/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: WechatPayDaoImpl.java
 * Author:   qxf
 * Date:     2016年11月23日 上午11:48:40
 */
package com.smeyun.payment.wechatpay.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smeyun.payment.wechatpay.dao.IWechatPayDao;
import com.smeyun.payment.wechatpay.entity.PayNotifyData;
import com.smeyun.payment.wechatpay.entity.PayRequestData;
import com.smeyun.platform.util.base.dao.MybatisDao;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 数据库操作实现类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service("wechatPayDaoImpl")
public class WechatPayDaoImpl implements IWechatPayDao
{
    // 日志打印对象
    private static final Logger LOG = LoggerFactory.getLogger(WechatPayDaoImpl.class);
    
    @Autowired
    private MybatisDao mybatisDao;
    
    /**
     * @see com.smeyun.payment.wechatpay.dao.IWechatPayDao#saveRequestData(com.smeyun.payment.wechatpay.entity.PayRequestData)<br>
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
     * @see com.smeyun.payment.wechatpay.dao.IWechatPayDao#queryRequestDataById(java.lang.String)<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public PayRequestData queryRequestDataById(long reqId)
    {
        try
        {
            return (PayRequestData) mybatisDao.queryData("queryRequestDataById", reqId);
        }
        catch (SmeyunUncheckedException e)
        {
            LOG.error("query pay infomation failed, reqId=" + reqId, e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("query pay infomation exception, reqId=" + reqId, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, e);
        }
    }
    
    /**
     * @see com.smeyun.payment.wechatpay.dao.IWechatPayDao#saveNotifyData(com.smeyun.payment.wechatpay.entity.PayNotifyData)<br>
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
     * @see com.smeyun.payment.wechatpay.dao.IWechatPayDao#queryNotifyDataByReqId(java.lang.String)<br>
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
     * @see com.smeyun.payment.wechatpay.dao.IWechatPayDao#updateNotifyData(com.smeyun.payment.wechatpay.entity.PayNotifyData)<br>
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
