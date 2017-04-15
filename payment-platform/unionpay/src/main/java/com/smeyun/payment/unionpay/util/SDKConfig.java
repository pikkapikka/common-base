
package com.smeyun.payment.unionpay.util;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
* JDK version used:      <JDK1.8> 
* @ClassName: SDKConfig
* @Description: sdk配置文件读取
* @author liuyaoshen
* @date 2016年12月20日
*/
@Component @Setter @Getter
public class SDKConfig
{
    
    /** 前台请求URL. */
    @Value("${acpsdk.frontTransUrl}")
    private String frontRequestUrl;
    
    /** 后台请求URL. */
    @Value("${acpsdk.backTransUrl}")
    private String backRequestUrl;
    
    /** 单笔查询 */
    @Value("${acpsdk.singleQueryUrl}")
    private String singleQueryUrl;
    
    /** 批量查询 */
    @Value("${acpsdk.batchQueryUrl}")
    private String batchQueryUrl;
    
    /** 批量交易 */
    @Value("${acpsdk.batchTransUrl}")
    private String batchTransUrl;
    
    /** 文件传输 */
    @Value("${acpsdk.fileTransUrl}")
    private String fileTransUrl;
    
    /** 签名证书路径. */
    @Value("${acpsdk.signCert.path}")
    private String signCertPath;
    
    /** 签名证书密码. */
    @Value("${acpsdk.signCert.pwd}")
    private String signCertPwd;
    
    /** 签名证书类型. */
    @Value("${acpsdk.signCert.type}")
    private String signCertType;
    
    /** 加密公钥证书路径. */
    @Value("${acpsdk.encryptCert.path}")
    private String encryptCertPath;
    
    /** 验证签名公钥证书目录. */
    @Value("${acpsdk.validateCert.dir}")
    private String validateCertDir;
    
    /** 有卡交易. */
    @Value("${acpsdk.cardTransUrl}")
    private String cardRequestUrl;
    
    /** app交易 */
    @Value("${acpsdk.appTransUrl}")
    private String appRequestUrl;
    
    /** 证书使用模式(单证书/多证书) */
    @Value("${acpsdk.singleMode}")
    private String singleMode;
    
    /** 前台通知地址 */
    @Value("${front.notify.url}")
    private String frontUrl;
    
    /** 后台通知地址 */
    @Value("${back.nofity.url}")
    private String backUrl;
    
    /**商户号ID*/
    @Value("${acpsdk.merId}")
    private String merId;
    
    /**币种 156-人民币*/
    @Value("${currency.zh}")
    private String currency;
    
    @Value("${payment.client.key}")
    private String privateKey;
    /*
     * 重试次数
     */
    @Value("${payment.unionpay.notify.max.times}")
    private int maxTimes;
    
    /*
     * 休眠时间
     */
    @Value("${payment.unionpay.notify.sleep.milliseconds}")
    private long sleepTime;
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
}
