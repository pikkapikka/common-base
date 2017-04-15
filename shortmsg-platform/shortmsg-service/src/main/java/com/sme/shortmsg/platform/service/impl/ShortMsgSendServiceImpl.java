/*
 * Copyright (C), 2002-2016, �����׹������������޹�˾
 * FileName: ShortMsgSendServiceImpl.java
 * Author:   qxf
 * Date:     2016��9��6�� ����5:49:41
 * Description: //ģ��Ŀ�ġ���������      
 * History: //�޸ļ�¼
 * <author>      <time>      <version>    <desc>
 * �޸�������             �޸�ʱ��            �汾��                  ����
 */
package com.sme.shortmsg.platform.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sme.shortmsg.platform.exception.ShortMsgUncheckedException;
import com.sme.shortmsg.platform.service.IShortMsgSendService;
import com.sme.shortmsg.platform.util.Common;

/**
 * ��ͨ�������Ͷ���
 *
 * @author qxf
 * @see [�����/����]����ѡ��
 * @since [��Ʒ/ģ��汾] ����ѡ��
 */
@Service("unionMsgSendService")
public class ShortMsgSendServiceImpl implements IShortMsgSendService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortMsgSendServiceImpl.class);
    
    @Value("${short.message.plat.default.charge.number}")
    private String defaultCharge;
    
    @Autowired
    private SgipConnenctor connector;
    
    @Value("${short.message.plat.send.times}")
    private int maxSendTimes;
    
    @Value("${short.message.plat.sleep.time}")
    private long sleepTime;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void send(String chargeNum, String content, String[] receivers)
    {
        // ���Է��Ͷ���Ϣ
        for (int idx = 0; idx <= maxSendTimes; idx++)
        {
            try
            {
                // ������ŷ�����û�з���
                connector.send(content, StringUtils.isEmpty(chargeNum) ? defaultCharge : chargeNum, receivers);
                return;
            }
            catch (ShortMsgUncheckedException e)
            {
                LOGGER.error("send short message catch exception:", e);
                // ����һ��ʱ��
                Common.sleep(sleepTime);
            }
        }
        
        throw new ShortMsgUncheckedException("send short message failed");
    }
    
}
