/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: SmeyunUncheckedException.java
 * Author:   qxf
 * Date:     2016年11月16日 上午11:19:44
 */
package com.smeyun.platform.util.common.exception;

/**
 * 异常类，
 * 推荐使用SmeUncheckedException
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class SmeyunUncheckedException extends RuntimeException
{ 
    /**
     * 序列号
     */
    private static final long serialVersionUID = 3872905123680141825L;
    
    // 错误码
    private String errorCode; 
    
    /**
     * 构造函数
     * @param t 异常消息
     */
    public SmeyunUncheckedException(Throwable t)
    {
        super(t);
    }
    
    /**
     * 构造函数
     * @param errerCode 错误码
     */
    public SmeyunUncheckedException(String errorCode)
    {
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数
     * @param errerCode 错误码
     */
    public SmeyunUncheckedException(int errorCode)
    {
        this.errorCode = String.valueOf(errorCode);
    }
    
    /**
     * 构造函数
     * @param message 异常消息
     * @param errerCode 错误码
     */
    public SmeyunUncheckedException(String errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数
     * @param message 异常消息
     * @param errerCode 错误码
     */
    public SmeyunUncheckedException(int errorCode, String message)
    {
        super(message);
        this.errorCode = String.valueOf(errorCode);
    }
    
    /**
     * 构造函数
     * @param message 异常消息
     * @param t 异常栈
     */
    public SmeyunUncheckedException(String errorCode, Throwable t)
    {
        super(t);
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数
     * @param message 异常消息
     * @param t 异常栈
     */
    public SmeyunUncheckedException(int errorCode, Throwable t)
    {
        super(t);
        this.errorCode = String.valueOf(errorCode);
    }
    
    /**
     * 构造函数
     * @param message 异常消息
     * @param errorCode 错误码
     * @param t 异常栈
     */
    public SmeyunUncheckedException(String errorCode, String message, Throwable t)
    {
        super(message, t);
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数
     * @param message 异常消息
     * @param errorCode 错误码
     * @param t 异常栈
     */
    public SmeyunUncheckedException(int errorCode, String message, Throwable t)
    {
        super(message, t);
        this.errorCode = String.valueOf(errorCode);
    }
    
    /**
     * 获取错误码
     * 
     * @return  错误码
     *
     * @author qxf
     */
    public String getErrorCode()
    {
        return errorCode;
    }

    /**
     * @see java.lang.Throwable#getMessage()<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public String getMessage()
    {
        return "ErrorCode:" + errorCode + ", detail:" + super.getMessage();
    }
}
