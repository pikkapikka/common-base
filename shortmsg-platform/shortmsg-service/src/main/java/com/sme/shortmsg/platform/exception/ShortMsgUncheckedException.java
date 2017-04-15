/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: ShortMsgUncheckedException.java
 * Author:   qxf
 * Date:     2016年9月7日 上午8:44:51
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.sme.shortmsg.platform.exception;

/**
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ShortMsgUncheckedException extends RuntimeException
{
    
    /**
     * 序列号
     */
    private static final long serialVersionUID = -861189309849712261L;
    
    /**
     * 构造函数
     * @param message 异常消息
     */
    public ShortMsgUncheckedException(String message)
    {
        super(message);
    }
    
    /**
     * 构造函数
     * @param t 异常消息
     */
    public ShortMsgUncheckedException(Throwable t)
    {
        super(t);
    }
}
