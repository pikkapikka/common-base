/*
 * Copyright (C), 2002-2016, 重庆锋云汇智科技有限公司
 * FileName: ShortMsgRevceiveService.java
 * Author:   qxf
 * Date:     2016年12月6日 下午4:28:18
 * Description: //模块目的、功能描述      
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
 * 接收消息服务
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ShortMsgRevceiveService extends SGIPSMProxy
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortMsgRevceiveService.class);
    
    /**
     * 构造函数
     * @param args
     */
    public ShortMsgRevceiveService(Args args, String bindAddr, int bindPort)
    {
        super(args);
        LOGGER.info("start receive short message service.");
        
        // 启动服务
        startService(bindAddr, bindPort);
    }
    
    /**
     * 接收从联通网关传递的消息
     * @see com.huawei.smproxy.SGIPSMProxy#onDeliver(com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage)<br>
     * 
     * @author qxf 2016年12月6日
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
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
            //是否是类的实例
            LOGGER.warn("receive the down message.");
            return;
        }
        
        if (msg instanceof SGIPDeliverMessage)
        {
            // 收到用户发送的短信(上行)
            SGIPDeliverMessage deliverMsg = (SGIPDeliverMessage) msg;
            LOGGER.debug("receive short message ：" + convertMsg(deliverMsg));
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
