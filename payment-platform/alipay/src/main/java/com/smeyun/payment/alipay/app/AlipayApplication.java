/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: AlipayApplication.java
 * Author:   qxf
 * Date:     2016年11月16日 上午10:42:44
 */
package com.smeyun.payment.alipay.app;

import javax.servlet.Filter;

import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.smeyun.platform.util.base.dao.MybatisDao;
import com.smeyun.platform.util.base.filter.CustomErrorFilter;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 支付宝支付的服务
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@SpringBootApplication(scanBasePackages = "com.smeyun")
public class AlipayApplication
{
    private static final Logger LOG = LoggerFactory.getLogger(AlipayApplication.class);
    
    /**
     * 实例化数据库连接池
     * 
     * @return 数据库连接池
     */
    @Bean
    @ConfigurationProperties(prefix = "tomcat.datasource")
    public PoolConfiguration getPoolProperties()
    {
        return new PoolProperties();
    }

    /**
     * 实例化数据源
     * 
     * @return 数据源
     */
    @Bean
    @ConfigurationProperties(prefix = "tomcat.datasource")
    public javax.sql.DataSource dataSource()
    {
        final PoolConfiguration poolProperties = getPoolProperties();
        final org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource(
                poolProperties);
        return dataSource;
    }
    
    /**
     * 实例化mybatis实例
     * 
     * @return mybatis实例
     */
    @Bean
    public MybatisDao mybatisDao()
    {
        try
        {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource());
            bean.setMapperLocations(resolver.getResources("classpath:mybatis/*.xml"));
            return new MybatisDao(bean.getObject());
        }
        catch (Exception e)
        {
            LOG.error("initialize mybatis bean failed:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /**
     * 实例化过滤器
     * 
     * @return 过滤器
     */
    @Bean
    public Filter customFilter()
    {
        return new CustomErrorFilter();
    }

    /**
     * 注册过滤器
     * 
     * @return 注册器
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean()
    {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(customFilter());
        bean.addUrlPatterns("/platform/*");
        bean.setName("CustomFilter");
        return bean;
    }
    
    /**
     * 启动函数
     * 
     * @param [参数1]     [参数1说明]
     * @return  void
     * @exception/throws [异常类型] [异常说明]
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static void main(String[] args)
    {
        SpringApplication.run(AlipayApplication.class);
        LOG.info("alipay application start success.");
    }
}
