/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: GopayDaoImpl.java
 * Author:   qxf
 * Date:     2016年11月24日 下午4:25:17
 */
package com.smeyun.payment.gopay.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smeyun.payment.gopay.dao.IGopayDao;
import com.smeyun.payment.gopay.entity.PayNotifyData;
import com.smeyun.payment.gopay.entity.PayRequestData;
import com.smeyun.platform.util.base.dao.MybatisDao;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 〈功能详细描述〉
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service("gopayDaoImpl")
public class GopayDaoImpl implements IGopayDao
{
    // 日志访问对象
    private static final Logger LOG = LoggerFactory.getLogger(GopayDaoImpl.class);
    
    @Autowired
    private MybatisDao mybatisDao;
    
    /**
     * @see com.smeyun.payment.gopay.dao.IGopayDao#saveRequestData(com.smeyun.payment.gopay.entity.PayRequestData)<br>
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
     * @see com.smeyun.payment.gopay.dao.IGopayDao#queryRequestDataById(long)<br>
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
     * @see com.smeyun.payment.gopay.dao.IGopayDao#saveNotifyData(com.smeyun.payment.gopay.entity.PayNotifyData)<br>
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
     * @see com.smeyun.payment.gopay.dao.IGopayDao#queryNotifyDataByReqId(long)<br>
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
     * @see com.smeyun.payment.gopay.dao.IGopayDao#updateNotifyData(com.smeyun.payment.gopay.entity.PayNotifyData)<br>
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
