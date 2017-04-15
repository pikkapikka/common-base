/*
 * Copyright (C), 2002-2016, ������ƻ��ǿƼ����޹�˾
 * FileName: ShortMsgRequest.java
 * Author:   qxf
 * Date:     2016��9��7�� ����2:16:49
 * Description: //ģ��Ŀ�ġ���������      
 */
package com.sme.shortmsg.platform.entity;

import java.util.Arrays;

/**
 * ������Ϣ��
 *
 * @author qxf
 * @see [�����/����]����ѡ��
 * @since [��Ʒ/ģ��汾] ����ѡ��
 */
public class ShortMsgRequest
{
    // ���ͺ��룬Ŀǰ��δʹ��
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
