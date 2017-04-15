/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: MailConfigration.java
 * Author:   qxf
 * Date:     2016年12月14日 上午11:23:53
 */
package com.smeyun.platform.util.base.mail;

/**
 * 邮件配置
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MailConfigration
{
    // 邮件发送服务器
    private String emailHost;
    
    // 发送者的鉴权用户名
    private int emailPort;
    
    // 发送者的鉴权密码
    private String password;
    
    // 发送者邮件地址
    private String senderMail;
    
    public String getEmailHost()
    {
        return emailHost;
    }
    
    public void setEmailHost(String emailHost)
    {
        this.emailHost = emailHost;
    }
    
    public int getEmailPort()
    {
        return emailPort;
    }
    
    public void setEmailPort(int emailPort)
    {
        this.emailPort = emailPort;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getSenderMail()
    {
        return senderMail;
    }
    
    public void setSenderMail(String senderMail)
    {
        this.senderMail = senderMail;
    }
    
    /**
     * @see java.lang.Object#toString()<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("MailConfigration [emailHost=");
        builder.append(emailHost);
        builder.append(", emailPort=");
        builder.append(emailPort);
        builder.append(", password=");
        builder.append(password);
        builder.append(", senderMail=");
        builder.append(senderMail);
        builder.append("]");
        return builder.toString();
    }
    
}
