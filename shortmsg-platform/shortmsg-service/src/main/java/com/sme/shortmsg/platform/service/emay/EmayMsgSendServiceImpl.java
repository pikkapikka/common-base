/*
 * Copyright (C), 2002-2017, 重庆锋云汇智科技有限公司
 * FileName: EmayMsgSendServiceImpl.java
 * Author:   qxf
 * Date:     2017年1月4日 下午2:25:04
 * Description: //模块目的、功能描述      
 */
package com.sme.shortmsg.platform.service.emay;

import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sme.shortmsg.platform.exception.ShortMsgUncheckedException;
import com.sme.shortmsg.platform.service.IShortMsgSendService;
import com.sme.shortmsg.platform.util.Common;
import com.sme.shortmsg.platform.util.HttpSend;

/**
 * 使用亿美软通使用的接口发送短信
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service("emayMsgSendServiceImpl")
public class EmayMsgSendServiceImpl implements IShortMsgSendService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EmayMsgSendServiceImpl.class);
    
    @Value("${short.message.plat.emay.url}")
    private String url;
    
    @Value("${short.message.plat.emay.username}")
    private String username;
    
    @Value("${short.message.plat.emay.passwd}")
    private String password;
    
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
        try
        {
            StringBuilder builder = new StringBuilder();
            builder.append(url).append("?");
            builder.append("username=").append(username);
            builder.append("&password=").append(password);
            builder.append("&mobile=").append(getPhoneNumbers(receivers));
            builder.append("&content=").append(URLEncoder.encode(content, "UTF-8"));
            
            trySendMsg(builder.toString());
        }
        catch (Exception e)
        {
            LOGGER.error("send short message failed:", e);
            throw new ShortMsgUncheckedException(e);
        }
    }
    
    private String getPhoneNumbers(String[] receivers)
    {
        StringBuilder builder = new StringBuilder();
        for (String number : receivers)
        {
            builder.append(number);
            builder.append(",");
        }
        
        // 去掉最后一个,
        String tempNums = builder.toString();
        if (!StringUtils.isEmpty(tempNums))
        {
            tempNums = tempNums.substring(0, tempNums.length() - 1);
        }
        
        return tempNums;
    }
    
    private void trySendMsg(String url)
    {
        String ret = null;
        
        // 尝试发送短消息
        for (int idx = 0; idx <= maxSendTimes; idx++)
        {
            try
            {
                // 发送信息
                ret = HttpSend.getSend(url, null);
                LOGGER.debug("send ret = {}.", ret);
                break;
            }
            catch (Exception e)
            {
                LOGGER.error("send short message catch exception:", e);
                // 休眠一点时间
                Common.sleep(sleepTime);
            }
        }
        
        if (null != ret)
        {
            // 解析发送结果
            int retCode = getSendRet(ret);
            if (retCode >= 0)
            {
                return;
            }
            else
            {
                LOGGER.error("send short message faied, xml={}, url={}.", ret, url);
                throw new ShortMsgUncheckedException(String.valueOf(retCode));
            }
        }
        
        throw new ShortMsgUncheckedException("send short message failed");
    }
    
    private int getSendRet(String xmlRet)
    {
        try
        {
            Document doc = DocumentHelper.parseText(xmlRet);
            Element rootEle = doc.getRootElement();
            String retCode = rootEle.elementText("result");
            if (null != retCode)
            {
                return Integer.parseInt(retCode);
            }
            else
            {
                LOGGER.error("get send result is null, xml={}.", xmlRet);
                throw new ShortMsgUncheckedException("ret is null.");
            }
        }
        catch (DocumentException e)
        {
            LOGGER.error("parse xml failed, xml={}.", xmlRet);
            throw new ShortMsgUncheckedException(e);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("convert send result to int failed, xml={}.", xmlRet);
            throw new ShortMsgUncheckedException(e);
        }
    }
}
