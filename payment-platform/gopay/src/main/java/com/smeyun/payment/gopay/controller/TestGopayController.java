/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: TestAliController.java
 * Author:   qxf
 * Date:     2016年11月22日 下午4:46:01
 */
package com.smeyun.payment.gopay.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 〈功能详细描述〉
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Controller
@RequestMapping("/platform/payment/test")
public class TestGopayController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestGopayController.class);
    
    /**
     *〈一句话功能简述〉
     *〈功能详细描述〉
     * @author     [作者]（必须，使用汉语）
     * @see        [相关类/方法]（可选）
     * @since      [产品/模块版本] （可选）
     */
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public @ResponseBody String payNotifyMsg(HttpServletRequest request)
    {
        LOGGER.error("notify enter.");
        return "OK";
    }
    
    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public @ResponseBody String returnUrl(HttpServletRequest request)
    {
        LOGGER.error("return enter.");
        return "OK";
    }
}
