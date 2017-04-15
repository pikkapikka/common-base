/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: ShortMsgController.java
 * Author:   qxf
 * Date:     2016年9月6日 下午6:19:25
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
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
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
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
     * 发送发送短信消息
     * 
     * @author qxf
     *  
     * @param request 请求消息体
     * @param receivers 短信接收号码，可以是多个
     * @return  String 成功返回OK
     * @exception 异常抛出ShortMsgUncheckedException
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody String send(@RequestBody ShortMsgRequest request)
    {
        LOGGER.debug("enter send short message, request={}.", request);
        
        // 参数检查
        if (StringUtils.isEmpty(request.getContent()) || checkReceiveNumber(request.getReceivers()))
        {
            LOGGER.error("parameter content or receivers invalid.");
            throw new ShortMsgUncheckedException("parameter invalid");
        }
        
        List<String> unionRecvs = new ArrayList<String>();
        List<String> otherRecvs = new ArrayList<String>();
        
        // 对号码进行拆分，联通的SGIP端口只能发送联通手机号码的短信
        splitReceiverNumber(request.getReceivers(), unionRecvs, otherRecvs);
        
        try
        {
            // 发送非联通短信
            if (!otherRecvs.isEmpty())
            {
                // 发送短信消息
                String[] strs = new String[otherRecvs.size()];
                otherSendService.send(request.getSender(), request.getContent(), otherRecvs.toArray(strs));
            }
            
            // 发送联通短信, 如果联通渠道发送不成功，采用第三方渠道
            if (!unionRecvs.isEmpty())
            {
                // 发送短信消息
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
        // 一次最多只能发送100个号码
        return StringUtils.isAnyEmpty(receivers) || MAX_SEND_NUMBER < receivers.length;
    }
    
    /**
     * 对号码按照联通号码和非联通号码进行区分
     *  
     * @param receivers 接收号码
     * @param unionNumbers 联通号码集合
     * @param otherNumbers 非联通号码集合
     * 
     * @exception/throws [异常类型] [异常说明]
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    private void splitReceiverNumber(String[] receivers, List<String> unionNumbers, List<String> otherNumbers)
    {
        for (String revNum : receivers)
        {
            // 获取原生号码，去掉号码前的 "86"， "+86"
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
     * 获取原生的号码，剔除号码前的 "86" 和 "+86"
     * 
     * @param receiver 手机号码
     * @return  String
     * @exception/throws [异常类型] [异常说明]
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
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
     * 启动监听任务
     * 
     * @return  void
     * @exception/throws [异常类型] [异常说明]
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @PostConstruct
    public void startReceiveTask()
    {
        Thread thread = new Thread(receivetask);
        thread.start();
        
        LOGGER.debug("start receive thread success.");
    }
}
