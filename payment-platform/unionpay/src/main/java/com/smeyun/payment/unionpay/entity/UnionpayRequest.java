package com.smeyun.payment.unionpay.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UnionpayRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2685392757460271367L;
	
  //提交到支付方时以分为单位
    private Long txnAmt;
    /**渠道类型 默认为PC*/
    /**业务类型 201: B2C||202: B2B*/
    private Integer bizType;
    //客户端类型
    private Integer clientType;
    /**商户订单号，8-40位数字字母*/
    private String orderCode;
    //同步通知地址
    private String returnUrl;
    //异步通知地址
    private String notifyUrl;
   
    //md5签名盐值
    private String salt;
    
    /**
    * 订单描述
    */
    private String orderDes;
    private Date payTime = new Date();
    
    /*(Non Javadoc)
     * <p>Title: toString</p>
     * <p>Description: </p>
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
    public boolean isParamEmpty()
    {
        return StringUtils.isAnyBlank(orderCode, notifyUrl, salt);
    }
}
