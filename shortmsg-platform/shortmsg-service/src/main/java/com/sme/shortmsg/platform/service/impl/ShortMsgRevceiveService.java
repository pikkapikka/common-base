/*
 * Copyright (C), 2002-2016, ������ƻ��ǿƼ����޹�˾
 * FileName: ShortMsgRevceiveService.java
 * Author:   qxf
 * Date:     2016��12��6�� ����4:28:18
 * Description: //ģ��Ŀ�ġ���������      
 */
package com.sme.shortmsg.platform.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;
import com.huawei.insa2.comm.sgip.message.SGIPSubmitRepMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUserReportMessage;
import com.huawei.insa2.util.Args;
import com.huawei.smproxy.SGIPSMProxy;

/**
 * ������Ϣ����
 *
 * @author qxf
 * @see [�����/����]����ѡ��
 * @since [��Ʒ/ģ��汾] ����ѡ��
 */
public class ShortMsgRevceiveService extends SGIPSMProxy
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortMsgRevceiveService.class);
    
    /**
     * ���캯��
     * @param args
     */
    public ShortMsgRevceiveService(Args args, String bindAddr, int bindPort)
    {
        super(args);
        LOGGER.info("start receive short message service.");
        
        // ��������
        startService(bindAddr, bindPort);
    }
    
    /**
     * ���մ���ͨ���ش��ݵ���Ϣ
     * @see com.huawei.smproxy.SGIPSMProxy#onDeliver(com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage)<br>
     * 
     * @author qxf 2016��12��6��
     * @see   [�ࡢ��#��������#��Ա]
     * @since [��ʼ�汾]
     */
    @Override
    public SGIPMessage onDeliver(SGIPDeliverMessage msg)
    {
        ProcessRecvDeliverMsg(msg);
        LOGGER.debug("waiting for receive.......");
        return super.onDeliver(msg);
    }
    
    @Override
    public SGIPMessage onReport(SGIPReportMessage msg)
    {
        LOGGER.debug("on report message is {}.", getReportMsg(msg));
        return super.onReport(msg);
    }
    
    @Override
    public SGIPMessage onUserReport(SGIPUserReportMessage msg)
    {
        LOGGER.debug("on user report message is {}.", convertMsg(msg));
        return super.onUserReport(msg);
    }
    
    private void ProcessRecvDeliverMsg(SGIPMessage msg)
    {
        if (msg instanceof SGIPSubmitRepMessage)
        {
            //�Ƿ������ʵ��
            LOGGER.warn("receive the down message.");
            return;
        }
        
        if (msg instanceof SGIPDeliverMessage)
        {
            // �յ��û����͵Ķ���(����)
            SGIPDeliverMessage deliverMsg = (SGIPDeliverMessage) msg;
            LOGGER.debug("receive short message ��" + convertMsg(deliverMsg));
        }
    }
    
    private String convertMsg(SGIPMessage msg)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("message:src_node_Id=");
        builder.append(msg.getSrcNodeId());
        builder.append(", time_Stamp=");
        builder.append(msg.getTimeStamp());
        builder.append(", sequence_Id=");
        builder.append(msg.getSequenceId());
        builder.append(", content=");
        builder.append(new String(msg.getBytes()));
        return builder.toString();
    }
    
    private String getReportMsg(SGIPMessage msg)
    {
        byte[] srcs = msg.getBytes();
        
        byte[] submitSqs = new byte[12];
        System.arraycopy(srcs, 0, submitSqs, 0, submitSqs.length);
        byte[] reportType = new byte[1];
        System.arraycopy(srcs, 12, reportType, 0, reportType.length);
        byte[] userNum = new byte[21];
        System.arraycopy(srcs, 13, userNum, 0, userNum.length);
        
        byte[] state = new byte[1];
        System.arraycopy(srcs, 34, state, 0, state.length);
        
        byte[] errCode = new byte[1];
        System.arraycopy(srcs, 35, errCode, 0, errCode.length);
        
        StringBuilder builder = new StringBuilder();
        builder.append("message:submitSQ=");
        builder.append(new String(submitSqs));
        builder.append(", reportType=");
        builder.append(new String(reportType));
        builder.append(", userNum=");
        builder.append(new String(userNum));
        builder.append(", state=");
        builder.append(new String(state));
        builder.append(", errCode=");
        builder.append(new String(errCode));
        return builder.toString();
    }
}
