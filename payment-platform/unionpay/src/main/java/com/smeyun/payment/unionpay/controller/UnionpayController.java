/**
* Copyright © 2016 SME Corporation. All rights reserved
* @Title: UnionpayController.java
* @Description: TODO
* @Author liuyaoshen
* @Date 2017年1月22日
* @Version v1.0
*/

package com.smeyun.payment.unionpay.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smeyun.payment.unionpay.entity.BizType;
import com.smeyun.payment.unionpay.entity.PayClientType;
import com.smeyun.payment.unionpay.entity.UnionpayRequest;
import com.smeyun.payment.unionpay.service.IUnionpayService;
import com.smeyun.payment.unionpay.util.SDKConfig;
import com.smeyun.platform.util.common.encrypt.Md5Encrypt;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;


/**
* JDK version used:      <JDK1.7> 
* @ClassName: UnionpayController
* @Description: TODO(What does this class do?)
* @author liuyaoshen
* @date 2017年1月22日
*/
@RestController
@RequestMapping("/platform/payment/unionpay")
public class UnionpayController
{
    // 日志打印对象
    private static final Logger LOGGER = LoggerFactory.getLogger(UnionpayController.class);
    
    @Autowired
    private IUnionpayService unionpayService;
    @Autowired
    private SDKConfig sdkConfig;
    
    
    @ResponseBody
    @RequestMapping(value = "pay", method = RequestMethod.POST, consumes = "application/json")
    public String pay(@RequestBody UnionpayRequest request){
        //校验参数
        if (null == request || request.isParamEmpty() || !validateParam(request))
        {
            LOGGER.error("支付参数错误！" + request.toString());
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID);
        }
        if (BizType.B2B.getType() == request.getBizType().intValue())
        {
            return unionpayService.pay_B2B(request);
        }
        else {
            
            return unionpayService.pay_B2C(request);
        }
    }
    
    
   
    /**
     * @Title: validateParam
     * @Description: 检验请求参数
     * @param @param request
     * @param @return    param
     * @return boolean    returnType
     * @throws
     */
    private boolean validateParam(UnionpayRequest request)
    {
        if(!PayClientType.judge(request.getClientType()) || !BizType.judge(request.getBizType()))
        {
            return false;
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append(request.getTxnAmt());
        builder.append(request.getOrderCode());
        builder.append(request.getBizType());
        builder.append(request.getClientType());
        builder.append(request.getReturnUrl());
        builder.append(request.getNotifyUrl());
        builder.append(request.getOrderDes());
        builder.append(sdkConfig.getPrivateKey());
        
        
        return request.getSalt().equals(Md5Encrypt.md5(builder.toString()));
    }
    
    
}
