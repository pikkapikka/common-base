/*
 * Copyright (C), 2002-2016, 重庆锋云汇智科技有限公司
 * FileName: ReceiveMsgTask.java
 * Author:   qxf
 * Date:     2016年12月6日 下午4:58:16
 * Description: //模块目的、功能描述      
 */
package com.sme.shortmsg.platform.service.impl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.huawei.insa2.util.Args;

/**
 * 〈功能详细描述〉
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service("receiveMsgTask")
public class ReceiveMsgTask implements Runnable
{
    @Value("${short.message.plat.send.server.host}")
    private String serverHost;
    
    @Value("${short.message.plat.send.server.port}")
    private int serverPort;
    
    // 绑定IP地址
    @Value("${short.message.plat.receive.bind.ip}")
    private String bindAddr;
    
    // 绑定端口地址
    @Value("${short.message.plat.receive.bind.port}")
    private int bindPort;
    
    @Value("${short.message.plat.receive.login.name}")
    private String loginName;
    
    @Value("${short.message.plat.receive.login.pwd}")
    private String loginPwd;
    
    @Value("${short.message.plat.send.company.id}")
    private String companyId;
    
    /**
     * 线程运行类
     * @see java.lang.Runnable#run()<br>
     * 
     * @author qxf 2016年12月6日
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public void run()
    {
        Args argstr = new Args();
        argstr.set("serHost", serverHost);
        argstr.set("serviceport", serverPort);
        argstr.set("localhost", bindAddr);
        argstr.set("localport", bindPort);
        argstr.set("transaction-timeout", 10); // 操作超时时间(单位：秒)
        argstr.set("read-timeout", 15); // 物理连接读操作超时时间(单位：秒)
        
        argstr.set("login-name", loginName);
        argstr.set("login-pass", loginPwd);
        argstr.set("debug", true);
        argstr.set("source-addr", new BigInteger("30000" + companyId).intValue()); // SP…ID(最大为六位字符)
        
        // 启动服务
        new ShortMsgRevceiveService(argstr, bindAddr, bindPort);
    }
}
