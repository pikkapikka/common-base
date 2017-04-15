/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: CuratorFrameworkUtil.java
 * Author:   qxf
 * Date:     2016年12月14日 下午4:45:32
 */
package com.smeyun.platform.util.base.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * CuratorFramework框架工具类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class CuratorFrameworkUtil
{
    /**
     * 创建client
     * @param connectionString 连接字符串
     * @return
     */
    public static CuratorFramework createSimple(String connectionString)
    {
        
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        
        return CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
    }
    
    /**
     * 创建带参数的client
     * @param connectionString 链接字符换
     * @param retryPolicy 重试策略
     * @param connectionTimeoutMs 连接超时时间
     * @param sessionTimeoutMs session超时时间
     * @return
     */
    public static CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy,
            int connectionTimeoutMs, int sessionTimeoutMs)
    {
        // using the CuratorFrameworkFactory.builder() gives fine grained control
        // over creation options. See the CuratorFrameworkFactory.Builder javadoc
        // details
        return CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }
}
