/*
 * Copyright (C), 2002-2016, 重庆锋云汇智科技有限公司
 * FileName: IShortMsgSendService.java
 * Author:   qxf
 * Date:     2016年9月6日 下午4:36:43
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.sme.shortmsg.platform.service;

/**
 * 短信发送的接口
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface IShortMsgSendService
{
    /**
     * 发送消息
     * 
     * @param chargeNum 付费号码
     * @param content 内容
     * @param receivers 消息接受者
     * @author qxf
     * @see        [相关类/方法]（可选）
     * @since      [产品/模块版本] （可选）
     */
    void send(String chargeNum, String content, String[] receivers);
}
