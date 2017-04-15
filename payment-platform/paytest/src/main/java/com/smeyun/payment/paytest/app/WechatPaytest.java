/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: WechatPaytest.java
 * Author:   qxf
 * Date:     2016年12月20日 下午5:12:19
 */
package com.smeyun.payment.paytest.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈功能详细描述〉
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class WechatPaytest
{
    /**
     *〈一句话功能简述〉
     *〈功能详细描述〉
     * @author     [作者]（必须，使用汉语）
     * @see        [相关类/方法]（可选）
     * @since      [产品/模块版本] （可选）
     */
    public void testPay()
    {
        ExecutorService executor = Executors.newFixedThreadPool(200);
        for (int idx = 0; idx < 100; idx++)
        {
            WechatPayTask task = new WechatPayTask(idx);
            executor.execute(task);
        }
    }
}
