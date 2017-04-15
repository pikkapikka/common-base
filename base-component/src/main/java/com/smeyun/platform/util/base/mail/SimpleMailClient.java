/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: MailClient.java
 * Author:   qxf
 * Date:     2016年12月14日 上午10:51:24
 */
package com.smeyun.platform.util.base.mail;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;
import com.smeyun.platform.util.common.util.PlatformUtil;

/**
 * 邮件客户端，只能发送纯文本邮件，不支持附件功能。
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class SimpleMailClient
{
    private static final Logger log = LoggerFactory.getLogger(SimpleMailClient.class);
    
    // 发送邮件的超时时间
    private static final int SEND_MAIL_TIMEOUT = 30000;
    
    // 启用鉴权属性
    private static final String KEY_AUTH_PROP = "mail.smtp.auth";
    
    // 设置超时时间属性
    private static final String KEY_TIMETOUT_PROP = "mail.smtp.timeout";
    
    // 启动SSL属性
    private static final String KEY_SSL_ENABLE_PROP = "mail.smtp.starttls.enable";
    
    private static final String KEY_SSL_REQUIRED_PROP = "mail.smtp.starttls.required";
    
    // 邮件发送服务器
    private String emailHost;
    
    // 发送者的鉴权用户名
    private int emailPort = 0;
    
    // 发送者的鉴权密码
    private String password;
    
    // 发送者邮件地址
    private String senderMail;
    
    /*
     * 邮件发送器
     */
    private JavaMailSenderImpl mailSender;
    
    /**
     * 构造函数
     */
    public SimpleMailClient()
    {
        mailSender = new JavaMailSenderImpl();
    }
    
    /**
     * 构造函数
     * @param config 邮件配置
     */
    public SimpleMailClient(MailConfigration config)
    {
        this.emailHost = config.getEmailHost();
        this.emailPort = config.getEmailPort();
        this.password = config.getPassword();
        this.senderMail = config.getSenderMail();
        
        mailSender = new JavaMailSenderImpl();
    }
    
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
     * 按照指定的标题和内容发送邮件。
     * 
     * @param title  邮件标题
     * @param content 邮件内容
     * @param reciverMail 邮件接受者
     * @throws Exception，当出现异常时，抛出
     * 
     */
    public void sendEmail(String title, String content, String... reciverMails)
    {
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || (null == reciverMails))
        {
            log.error("parameter title or content is empty.");
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        // 构建纯文本的邮件消息
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(getSenderMail());
        mailMessage.setTo(reciverMails);
        mailMessage.setSubject(title);
        mailMessage.setText(content);
        
        // 设置属性，比如启动鉴权和启动SSL
        Properties properties = new Properties();
        properties.put(KEY_AUTH_PROP, true);
        properties.put(KEY_TIMETOUT_PROP, SEND_MAIL_TIMEOUT);
        properties.put(KEY_SSL_ENABLE_PROP, true);
        properties.put(KEY_SSL_REQUIRED_PROP, true);
        
        mailSender.setHost(getEmailHost());
        if (0 != getEmailPort())
        {
            mailSender.setPort(getEmailPort());
        }
        
        mailSender.setUsername(getSenderMail());
        mailSender.setPassword(getPassword());
        mailSender.setJavaMailProperties(properties);
        
        try
        {
            mailSender.send(mailMessage);
            log.debug("send email success, sender is {}, reciver is {}, title is {}.",
                    getSenderMail(),
                    reciverMails,
                    title);
        }
        catch (MailException e)
        {
            log.error("send email failed, sender is {}, reciver is {}.", getSenderMail(), reciverMails);
            log.error("send email catch faield.", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /**
     * 发送邮件。 如果发送失败，会按照指定次数进行重试。</br>
     * 如果邮件发送给多个接收者，那么有的接收者可能会重复收到邮件。
     * 
     * @param retryTimes 重试次数，必须大于0
     * @param title 邮件标题
     * @param content 邮件内容
     * @param reciverMails 邮件接收者，不能为空
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public void sendEmail(int retryTimes, String title, String content, String... reciverMails)
    {
        if (retryTimes <= 0)
        {
            log.error("parameter retryTimes invalid, retryTimes={}.", retryTimes);
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        
        int times = 0;
        while (times++ < retryTimes)
        {
            try
            {
                sendEmail(title, content, reciverMails);
                return;
            }
            catch (Exception e)
            {
                log.error("send email exce", e);
                PlatformUtil.sleep(800);
            }
        }
    }
    
//    public static void main(String[] args)
//    {
//        SimpleMailClient client = new SimpleMailClient();
//        client.setEmailHost("smtp.126.com");
//        client.setEmailPort(25);
//        client.setSenderMail("qxf13328234023@126.com");
//        client.setPassword("qinxiaofei1!");
//        client.sendEmail("测试", "asd测试sda", "5120554@qq.com");
//    }
}
