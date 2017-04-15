/**
* Copyright © 2016 SME Corporation. All rights reserved
* @Title: IUnionpayService.java
* @Description: TODO
* @Author liuyaoshen
* @Date 2016年12月26日
* @Version v1.0
*/

package com.smeyun.payment.unionpay.service;

import java.util.Map;

import com.smeyun.payment.unionpay.entity.UnionpayRequest;
import com.smeyun.payment.unionpay.util.enums.ResponseTypeEnum;

/**
* JDK version used:      <JDK1.8> 
* @ClassName: IUnionpayService
* @Description: TODO
* @author liuyaoshen
* @date 2016年12月26日
*/
public interface IUnionpayService
{
    /**
     * @Title: pay
     * @Description: B2C网关支付提交进行加密签名生成form表单
     * @param @param payRequest
     * @param @return    param
     * @return PayResponse    returnType
     * @throws
     */
    public String pay_B2C(UnionpayRequest payRequest);
    
    /**
     * @Title: validateRcvReq
     * @Description: 验签-银联通知服务器向商户服务器发送通知的参数验证
     * @param @param valideData
     * @param @param encoding
     * @param @return    param
     * @return boolean    returnType
     * @throws
     */
    public boolean validateRcvReq(Map<String, String> valideData, String encoding);
    
    /**
     * @Title: pay_B2B
     * @Description: B2B支付生成form表单
     * @param @param unionpayRequest
     * @param @return    param
     * @return String    returnType
     * @throws
     */
    public String pay_B2B(UnionpayRequest unionpayRequest);
    
    /**
     * @param valideData 银联应答返回的参数
     * @param encoding
     * @param responseTypeEnum 应答类型(同步/异常)
     */
    public String dealNotify(Map<String, String> valideData, String encoding, ResponseTypeEnum responseTypeEnum);
}
