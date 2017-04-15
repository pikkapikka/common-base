/*
 * Copyright (C), 2002-2016, ������ƻ������ݿƼ����޹�˾
 * FileName: ShortMsgController.java
 * Author:   qxf
 * Date:     2016��9��6�� ����6:19:25
 * Description: //ģ��Ŀ�ġ���������      
 * History: //�޸ļ�¼
 * <author>      <time>      <version>    <desc>
 * �޸�������             �޸�ʱ��            �汾��                  ����
 */
package com.sme.shortmsg.platform.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sme.shortmsg.platform.entity.ShortMsgRequest;
import com.sme.shortmsg.platform.exception.ShortMsgUncheckedException;
import com.sme.shortmsg.platform.service.IShortMsgSendService;
import com.sme.shortmsg.platform.service.impl.ReceiveMsgTask;
import com.sme.shortmsg.platform.util.Common;

/**
 * ��һ�仰���ܼ�����<br> 
 * ��������ϸ������
 *
 * @author qxf
 * @see [�����/����]����ѡ��
 * @since [��Ʒ/ģ��汾] ����ѡ��
 */
@Controller
@RequestMapping("/sme/platform/shortmsg")
public class ShortMsgController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortMsgController.class);
    
    private static final int MAX_SEND_NUMBER = 100;
    
    private static final String NUM_PRE_CHINA = "86";
    
    @Autowired
    @Qualifier("unionMsgSendService")
    private IShortMsgSendService unionSendService;
    
    @Autowired
    @Qualifier("emayMsgSendServiceImpl")
    private IShortMsgSendService otherSendService;
    
    @Autowired
    private ReceiveMsgTask receivetask;
    
    /**
     * 
     * ���ͷ��Ͷ�����Ϣ
     * 
     * @author qxf
     *  
     * @param request ������Ϣ��
     * @param receivers ���Ž��պ��룬�����Ƕ��
     * @return  String �ɹ�����OK
     * @exception �쳣�׳�ShortMsgUncheckedException
     * @see   [�ࡢ��#��������#��Ա]
     * @since [��ʼ�汾]
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody String send(@RequestBody ShortMsgRequest request)
    {
        LOGGER.debug("enter send short message, request={}.", request);
        
        // �������
        if (StringUtils.isEmpty(request.getContent()) || checkReceiveNumber(request.getReceivers()))
        {
            LOGGER.error("parameter content or receivers invalid.");
            throw new ShortMsgUncheckedException("parameter invalid");
        }
        
        List<String> unionRecvs = new ArrayList<String>();
        List<String> otherRecvs = new ArrayList<String>();
        
        // �Ժ�����в�֣���ͨ��SGIP�˿�ֻ�ܷ�����ͨ�ֻ�����Ķ���
        splitReceiverNumber(request.getReceivers(), unionRecvs, otherRecvs);
        
        try
        {
            // ���ͷ���ͨ����
            if (!otherRecvs.isEmpty())
            {
                // ���Ͷ�����Ϣ
                String[] strs = new String[otherRecvs.size()];
                otherSendService.send(request.getSender(), request.getContent(), otherRecvs.toArray(strs));
            }
            
            // ������ͨ����, �����ͨ�������Ͳ��ɹ������õ���������
            if (!unionRecvs.isEmpty())
            {
                // ���Ͷ�����Ϣ
                String[] strs = new String[unionRecvs.size()];
                try
                {
                    unionSendService.send(request.getSender(), request.getContent(), unionRecvs.toArray(strs));
                }
                catch (ShortMsgUncheckedException e)
                {
                    otherSendService.send(request.getSender(), request.getContent(), unionRecvs.toArray(strs));
                }
            }
        }
        catch (ShortMsgUncheckedException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("send short message failed, request={}.", request);
            LOGGER.error("the exception of sending short message is:", e);
            throw new ShortMsgUncheckedException(e);
        }
        
        LOGGER.debug("send short message end.");
        return "OK";
    }
    
    private boolean checkReceiveNumber(String[] receivers)
    {
        // һ�����ֻ�ܷ���100������
        return StringUtils.isAnyEmpty(receivers) || MAX_SEND_NUMBER < receivers.length;
    }
    
    /**
     * �Ժ��밴����ͨ����ͷ���ͨ�����������
     *  
     * @param receivers ���պ���
     * @param unionNumbers ��ͨ���뼯��
     * @param otherNumbers ����ͨ���뼯��
     * 
     * @exception/throws [�쳣����] [�쳣˵��]
     * @see   [�ࡢ��#��������#��Ա]
     * @since [��ʼ�汾]
     */
    private void splitReceiverNumber(String[] receivers, List<String> unionNumbers, List<String> otherNumbers)
    {
        for (String revNum : receivers)
        {
            // ��ȡԭ�����룬ȥ������ǰ�� "86"�� "+86"
            String orgNum = getOriginalNum(revNum);
            if (Common.isUnionNum(orgNum))
            {
                unionNumbers.add(NUM_PRE_CHINA + orgNum);
            }
            else
            {
                otherNumbers.add(orgNum);
            }
        }
    }
    
    /**
     * ��ȡԭ���ĺ��룬�޳�����ǰ�� "86" �� "+86"
     * 
     * @param receiver �ֻ�����
     * @return  String
     * @exception/throws [�쳣����] [�쳣˵��]
     * @see   [�ࡢ��#��������#��Ա]
     * @since [��ʼ�汾]
     */
    private String getOriginalNum(String receiver)
    {
        String phoneNum = receiver;
        if (receiver.startsWith(NUM_PRE_CHINA))
        {
            phoneNum = receiver.substring(NUM_PRE_CHINA.length());
        }
        
        if (receiver.startsWith("+" + NUM_PRE_CHINA))
        {
            phoneNum = receiver.substring(NUM_PRE_CHINA.length() + 1);
        }
        
        if (!Common.isMobileNum(phoneNum))
        {
            LOGGER.error("phone number invalid, number={}.", receiver);
            throw new ShortMsgUncheckedException("parameter invalid");
        }
        
        return phoneNum;
    }
    
    /**
     * ������������
     * 
     * @return  void
     * @exception/throws [�쳣����] [�쳣˵��]
     * @see   [�ࡢ��#��������#��Ա]
     * @since [��ʼ�汾]
     */
    @PostConstruct
    public void startReceiveTask()
    {
        Thread thread = new Thread(receivetask);
        thread.start();
        
        LOGGER.debug("start receive thread success.");
    }
}
