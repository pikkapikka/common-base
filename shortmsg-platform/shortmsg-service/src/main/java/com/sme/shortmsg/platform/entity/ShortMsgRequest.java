/*
 * Copyright (C), 2002-2016, 重庆锋云汇智科技有限公司
 * FileName: ShortMsgRequest.java
 * Author:   qxf
 * Date:     2016年9月7日 下午2:16:49
 * Description: //模块目的、功能描述      
 */
package com.sme.shortmsg.platform.entity;

import java.util.Arrays;

/**
 * 请求消息包
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ShortMsgRequest
{
    // 发送号码，目前暂未使用
    private String sender;
    
    private String content;
    
    private String[] receivers;

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String[] getReceivers()
    {
        return receivers;
    }

    public void setReceivers(String[] receivers)
    {
        this.receivers = receivers;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final int maxLen = 10;
        StringBuilder builder = new StringBuilder();
        builder.append("ShortMsgRequest [content=");
        builder.append(content);
        builder.append(", sender=");
        builder.append(sender);
        builder.append(", receivers=");
        builder.append(receivers != null ? Arrays.asList(receivers).subList(0,
                Math.min(receivers.length, maxLen)) : null);
        builder.append("]");
        return builder.toString();
    }
}
