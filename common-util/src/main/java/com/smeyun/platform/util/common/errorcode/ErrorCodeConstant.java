/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: ErrorCodeConstant.java
 * Author:   qxf
 * Date:     2016年11月16日 下午3:31:14
 */
package com.smeyun.platform.util.common.errorcode;

/**
 * 错误码定义常量
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ErrorCodeConstant
{
    private ErrorCodeConstant()
    {
        
    }
    
    /**
     * 内部错误
     */
    public static final String INNER_ERROR = "1000001";    
    public static final int INNER_ERROR_INT = 1000001;
    
    /** 参数非法 */
    public static final String PARAM_INVALID = "1000002";    
    public static final int PARAM_INVALID_INT = 1000002;
    
    /** 数据库访问异常 */
    public static final String DB_ACCESS_ERROR = "1000003";
    public static final int DB_ACCESS_ERROR_INT = 1000003;
    
    /** 网络异常 */
    public static final String NETWORK_ACCESS_ERROR = "1000004";
    public static final int NETWORK_ACCESS_ERROR_INT = 1000004;
    
    /** 对象不存在 */
    public static final int OBJECT_NOT_EXIST_INT = 1000005;
    public static final String OBJECT_NOT_EXIST = "1000005";
    
    /** REDIS访问异常 */
    public static final int REDIS_ACCESS_ERROR = 1000006;
    
    /** 手机格式不正确 */
    public static final int PHONE_FORMAT_ERROR = 1000007;
    
    /** 邮件格式不正确 */
    public static final int EMAIL_FORMAT_ERROR = 1000008;
    
    /** 短信验证码不正确 */
    public static final int SHORT_MSG_VERIFY_ERROR = 1000009;
    
    /** 图片验证码不正确 */
    public static final int IMAGE_VERIFY_ERROR = 1000010;
    
    /** 密码长度不够 */
    public static final int PASSWD_LEN_INVALID = 1000011;
    
    /** 用户名或密码错误 */
    public static final int USERNAME_PASSWD_ERROR = 1000012;
    
    /** 相同号短信发送间隔太短 */
    public static final int SEND_SMG_FREQUENT_ERROR = 1000013;
    
    /** 相同号短信发送次数超过限制 */
    public static final int SEND_SMG_OVERRUN_ERROR = 1000014;
    
    /** 重复提交 */
    public static final int DUPLICATE_COMMIT_ERROR = 1000015;
    
    /** 昵称重复 */
    public static final int DUPLICATE_NICKNAME_ERROR = 1000016;
    
    /** 手机号重复 */
    public static final int DUPLICATE_PHONE_ERROR = 1000017;
}
