/*
 * Copyright (C), 2002-2016, 苏宁易购电子商务有限公司
 * FileName: ShortMsgSendServiceImpl.java
 * Author:   qxf
 * Date:     2016年9月6日 下午5:49:41
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
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
 * 联通渠道发送短信
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
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
        // 尝试发送短消息
        for (int idx = 0; idx <= maxSendTimes; idx++)
        {
            try
            {
                // 如果短信发送者没有发送
                connector.send(content, StringUtils.isEmpty(chargeNum) ? defaultCharge : chargeNum, receivers);
                return;
            }
            catch (ShortMsgUncheckedException e)
            {
                LOGGER.error("send short message catch exception:", e);
                // 休眠一点时间
                Common.sleep(sleepTime);
            }
        }
        
        throw new ShortMsgUncheckedException("send short message failed");
    }
    
}
