/**
 * 
 */
package com.sme.shortmsg.platform.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.huawei.insa2.comm.sgip.message.SGIPSubmitMessage;
import com.huawei.insa2.comm.sgip.message.SGIPSubmitRepMessage;
import com.huawei.insa2.util.Args;
import com.huawei.smproxy.SGIPSMProxy;
import com.sme.shortmsg.platform.exception.ShortMsgUncheckedException;

/**
 * 
 * @author qxf
 *
 */
@Service("sgipConnenctor")
public class SgipConnenctor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SgipConnenctor.class);
    
    @Value("${short.message.plat.send.server.host}")
    private String serverHost;
    
    @Value("${short.message.plat.send.server.port}")
    private int serverPort;
    
    @Value("${short.message.plat.send.login.name}")
    private String loginName;
    
    @Value("${short.message.plat.send.login.pwd}")
    private String loginPwd;
    
    @Value("${short.message.plat.send.sp.number}")
    private String spNumber;
    
    @Value("${short.message.plat.send.company.id}")
    private String companyId;
    
    @Value("${short.message.plat.send.service.type}")
    private String serviceType;
    
    /**
     * 初始化与SMG的链接(短连接)
     *  
     * @param isForce 是否强制
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    private SGIPSMProxy init()
    {
        try
        {
            Args argstr = new Args();
            argstr.set("host", serverHost);
            argstr.set("port", serverPort);
            argstr.set("transaction-timeout", 10); // 操作超时时间(单位：秒)
            argstr.set("read-timeout", 15); // 物理连接读操作超时时间(单位：秒)
            argstr.set("source-addr", new BigInteger("30000" + companyId).intValue()); // SP…ID(最大为六位字符)
            argstr.set("login-name", loginName);
            argstr.set("login-pass", loginPwd);
            argstr.set("heartbeat-interval", 15); //心跳间隔时间
            argstr.set("reconnect-interval", 5);
            argstr.set("clientid", spNumber);
            argstr.set("debug", true);
            
            SGIPSMProxy sgipsmp = new SGIPSMProxy(argstr);
            
            // 为短连接
            boolean state = sgipsmp.connect(loginName, loginPwd);
            if (!state)
            {
                LOGGER.error("connect to SMG failed, host={}, port={}.", serverHost, serverPort);
                close(sgipsmp);
                throw new ShortMsgUncheckedException("connection exception");
            }
            else
            {
                LOGGER.debug("connect to SMG success, host={}, port={}.", serverHost, serverPort);
                return sgipsmp;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("connect to SMG exception, host={}, port={}.", serverHost, serverPort);
            LOGGER.error("connect to SMG exception.", e);
            throw new ShortMsgUncheckedException("connection exception");
        }
    }
    
    public void send(String content, String chargeNum, String[] receivers)
    {
        LOGGER.debug("short message chargeNumber={}, receivers={}, content={}.", chargeNum, receivers, content);
        
        SGIPSMProxy sgipsmp = null;
        
        try
        {
            byte[] MessageContent = content.getBytes("GB2312");
            SGIPSubmitMessage sgipsubmit = new SGIPSubmitMessage(spNumber, // SP的接入号码
                    chargeNum, // 付费号码 string
                    receivers, // 接收该短消息的手机号，最多100个号码 string[]
                    companyId, // 企业代码，取值范围为0～99999 string
                    serviceType, // 业务代码，由SP定义 stirng
                    0, // 计费类型 int
                    "0", // 该条短消息的收费值 stirng
                    "0", // 赠送用户的话费 string
                    0, // 代收费标志0：应收1：实收 int
                    0, // 引起MT消息的原因 int
                    0, // 优先级0～9从低 到高，默认为0 int
                    null, // 短消息寿命的终止时间 date
                    null, // 短消息定时发送的时间 date
                    1, // 状态报告标记 int
                    0, // GSM协议类型 int
                    0, // GSM协议类型 int
                    15, // 短消息的编码格式 ,15表示GBK
                    0, // 信息类型 ，目前的只支持为0的情况
                    MessageContent.length, // 短消息内容长度 int
                    MessageContent, // 短消息的内容 btye[]
                    "0" // 保留，扩展用 string
            );
            
            sgipsmp = init();
            SGIPSubmitRepMessage msg = (SGIPSubmitRepMessage) sgipsmp.send(sgipsubmit);
            if ((msg != null) && msg.getResult() == 0)
            {
                LOGGER.debug("send short message success, msg={}.", msg);
                return;
            }
            
            LOGGER.error("send short message failed, msg={}, connection state={}.", msg, sgipsmp.getConnState());
            throw new ShortMsgUncheckedException("send message failed");
            
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("send short message catch exception:", e);
            throw new ShortMsgUncheckedException(e);
        }
        catch (ShortMsgUncheckedException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("send short message catch exception:", e);
            throw new ShortMsgUncheckedException(e);
        }
        finally
        {
            close(sgipsmp);
        }
    }
    
    private void close(SGIPSMProxy sgipsmp)
    {
        if (null != sgipsmp && null != sgipsmp.getConn())
        {
            sgipsmp.close();
        }
    }
}
