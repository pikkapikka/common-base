/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: MybatisDao.java
 * Author:   qxf
 * Date:     2016年11月18日 下午4:01:50
 */
package com.smeyun.platform.util.base.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 数据库操作DAO层
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MybatisDao
{
    private static final Logger LOG = LoggerFactory.getLogger(MybatisDao.class);
    
    private final SqlSessionFactory sqlSessionFactory;
    
    /**
     * 构造方法
     * @param sqlSessionFactoryBean
     */
    public MybatisDao(SqlSessionFactory sqlSessionFactory)
    {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    private SqlSession getSqlSession()
    {
        return this.sqlSessionFactory.openSession();
    }
    
    protected void closeSqlSession(SqlSession sqlSession)
    {
        sqlSession.close();
    }
    
    /**
     * 向数据库添加数据
     * 
     * @param statementName SQL语句名称
     * @param obj 参数对象
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public int insert(String statementName, Object obj) throws SmeyunUncheckedException
    {
        SqlSession sqlSession = getSqlSession();
        
        try
        {
            LOG.debug("insert(" + statementName + "," + obj + ")");
            return sqlSession.insert(statementName, obj);
        }
        catch (Exception ex)
        {
            LOG.error("insert object to DB failed:", ex);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, ex);
        }
        finally
        {
            closeSqlSession(sqlSession);
        }
    }
    
    /**
     * 从数据库删除对象
     * 
     * @param statementName SQL语句名称
     * @param obj 参数对象
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public void delete(String statementName, Object obj) throws SmeyunUncheckedException
    {
        SqlSession sqlSession = getSqlSession();
        try
        {
            if (obj != null)
            {
                LOG.debug("remove(" + statementName + "," + obj + ")");
                sqlSession.delete(statementName, obj);
                return;
            }
            
            LOG.debug("remove(" + statementName + ")");
        }
        catch (Exception ex)
        {
            LOG.error("delete object from DB failed:", ex);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, ex);
        }
        finally
        {
            closeSqlSession(sqlSession);
        }
    }
    
    /**
     * 向数据库更新对象
     * 
     * @param statementName SQL语句名称
     * @param obj 参数对象
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public void update(String statementName, Object obj) throws SmeyunUncheckedException
    {
        SqlSession sqlSession = getSqlSession();
        try
        {
            LOG.debug("update(" + statementName + ")");
            if (obj == null)
            {
                LOG.error("parameter obj is null.");
                throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
            }
            
            LOG.debug("update(" + statementName + "," + obj + ")");
            if (0 >= sqlSession.update(statementName, obj))
            {
                LOG.error("update failed, result not more than zero, statementName=" + statementName);
                throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR);
            }
            return;
        }
        catch (Exception ex)
        {
            LOG.error("update object to DB failed:", ex);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, ex);
        }
        finally
        {
            closeSqlSession(sqlSession);
        }
    }
    
    /**
     * 查询对象列表
     * 
     * @param statementName SQL语句名称
     * @param obj 参数对象
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @SuppressWarnings("rawtypes")
    public List queryList(String statementName, Object obj) throws SmeyunUncheckedException
    {
        SqlSession sqlSession = getSqlSession();
        try
        {
            List localList;
            if (obj != null)
            {
                LOG.debug("queryForList(" + statementName + "," + obj + ")");
                localList = sqlSession.selectList(statementName, obj);
                return localList;
            }
            LOG.debug("queryForList(" + statementName + ")");
            return sqlSession.selectList(statementName);
        }
        catch (Exception ex)
        {
            LOG.error("query objects from DB failed:", ex);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, ex);
        }
        finally
        {
            closeSqlSession(sqlSession);
        }
    }
    
    /**
     * 查询指定对象
     * 
     * @param statementName SQL语句名称
     * @param obj 参数对象
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public Object queryData(String statementName, Object obj) throws SmeyunUncheckedException
    {
        SqlSession sqlSession = getSqlSession();
        try
        {
            Object localObject2;
            if (obj != null)
            {
                LOG.debug("queryForObject(" + statementName + "," + obj + ")");
                localObject2 = sqlSession.selectOne(statementName, obj);
                return localObject2;
            }
            LOG.debug("queryForObject(" + statementName + ")");
            return sqlSession.selectOne(statementName);
        }
        catch (Exception ex)
        {
            LOG.error("query object from DB failed:", ex);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, ex);
        }
        finally
        {
            closeSqlSession(sqlSession);
        }
    }
    
    /**
     * 分页查询指定对象
     * 
     * @param statementName SQL语句名称
     * @param obj 参数对象
     * @param start 起始页
     * @param limit 每页限制条数
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @SuppressWarnings("rawtypes")
    public List queryListRowBounds(String statementName, Object obj, int start, int limit)
            throws SmeyunUncheckedException
    {
        SqlSession sqlSession = getSqlSession();
        try
        {
            LOG.debug("queryListWithStartAndLimit(" + statementName + "," + obj + ")");
            return sqlSession.selectList(statementName, obj, new RowBounds(start, limit));
        }
        catch (Exception ex)
        {
            LOG.error("query object from DB by page failed:", ex);
            throw new SmeyunUncheckedException(ErrorCodeConstant.DB_ACCESS_ERROR, ex);
        }
        finally
        {
            closeSqlSession(sqlSession);
        }
    }
}
