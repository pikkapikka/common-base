/*
 * Copyright (C), 2002-2016, ������ƻ��ǿƼ����޹�˾
 * FileName: ReceiveMsgTask.java
 * Author:   qxf
 * Date:     2016��12��6�� ����4:58:16
 * Description: //ģ��Ŀ�ġ���������      
 */
package com.sme.shortmsg.platform.service.impl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.huawei.insa2.util.Args;

/**
 * ��������ϸ������
 *
 * @author qxf
 * @see [�����/����]����ѡ��
 * @since [��Ʒ/ģ��汾] ����ѡ��
 */
@Service("receiveMsgTask")
public class ReceiveMsgTask implements Runnable
{
    @Value("${short.message.plat.send.server.host}")
    private String serverHost;
    
    @Value("${short.message.plat.send.server.port}")
    private int serverPort;
    
    // ��IP��ַ
    @Value("${short.message.plat.receive.bind.ip}")
    private String bindAddr;
    
    // �󶨶˿ڵ�ַ
    @Value("${short.message.plat.receive.bind.port}")
    private int bindPort;
    
    @Value("${short.message.plat.receive.login.name}")
    private String loginName;
    
    @Value("${short.message.plat.receive.login.pwd}")
    private String loginPwd;
    
    @Value("${short.message.plat.send.company.id}")
    private String companyId;
    
    /**
     * �߳�������
     * @see java.lang.Runnable#run()<br>
     * 
     * @author qxf 2016��12��6��
     * @see   [�ࡢ��#��������#��Ա]
     * @since [��ʼ�汾]
     */
    @Override
    public void run()
    {
        Args argstr = new Args();
        argstr.set("serHost", serverHost);
        argstr.set("serviceport", serverPort);
        argstr.set("localhost", bindAddr);
        argstr.set("localport", bindPort);
        argstr.set("transaction-timeout", 10); // ������ʱʱ��(��λ����)
        argstr.set("read-timeout", 15); // �������Ӷ�������ʱʱ��(��λ����)
        
        argstr.set("login-name", loginName);
        argstr.set("login-pass", loginPwd);
        argstr.set("debug", true);
        argstr.set("source-addr", new BigInteger("30000" + companyId).intValue()); // SP��ID(���Ϊ��λ�ַ�)
        
        // ��������
        new ShortMsgRevceiveService(argstr, bindAddr, bindPort);
    }
}
