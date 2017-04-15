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
     * ��ʼ����SMG������(������)
     *  
     * @param isForce �Ƿ�ǿ��
     * @see   [�ࡢ��#��������#��Ա]
     * @since [��ʼ�汾]
     */
    private SGIPSMProxy init()
    {
        try
        {
            Args argstr = new Args();
            argstr.set("host", serverHost);
            argstr.set("port", serverPort);
            argstr.set("transaction-timeout", 10); // ������ʱʱ��(��λ����)
            argstr.set("read-timeout", 15); // �������Ӷ�������ʱʱ��(��λ����)
            argstr.set("source-addr", new BigInteger("30000" + companyId).intValue()); // SP��ID(���Ϊ��λ�ַ�)
            argstr.set("login-name", loginName);
            argstr.set("login-pass", loginPwd);
            argstr.set("heartbeat-interval", 15); //�������ʱ��
            argstr.set("reconnect-interval", 5);
            argstr.set("clientid", spNumber);
            argstr.set("debug", true);
            
            SGIPSMProxy sgipsmp = new SGIPSMProxy(argstr);
            
            // Ϊ������
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
            SGIPSubmitMessage sgipsubmit = new SGIPSubmitMessage(spNumber, // SP�Ľ������
                    chargeNum, // ���Ѻ��� string
                    receivers, // ���ոö���Ϣ���ֻ��ţ����100������ string[]
                    companyId, // ��ҵ���룬ȡֵ��ΧΪ0��99999 string
                    serviceType, // ҵ����룬��SP���� stirng
                    0, // �Ʒ����� int
                    "0", // ��������Ϣ���շ�ֵ stirng
                    "0", // �����û��Ļ��� string
                    0, // ���շѱ�־0��Ӧ��1��ʵ�� int
                    0, // ����MT��Ϣ��ԭ�� int
                    0, // ���ȼ�0��9�ӵ� ���ߣ�Ĭ��Ϊ0 int
                    null, // ����Ϣ��������ֹʱ�� date
                    null, // ����Ϣ��ʱ���͵�ʱ�� date
                    1, // ״̬������ int
                    0, // GSMЭ������ int
                    0, // GSMЭ������ int
                    15, // ����Ϣ�ı����ʽ ,15��ʾGBK
                    0, // ��Ϣ���� ��Ŀǰ��ֻ֧��Ϊ0�����
                    MessageContent.length, // ����Ϣ���ݳ��� int
                    MessageContent, // ����Ϣ������ btye[]
                    "0" // ��������չ�� string
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
