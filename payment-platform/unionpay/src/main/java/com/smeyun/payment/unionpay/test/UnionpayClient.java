/**
* Copyright © 2016 SME Corporation. All rights reserved
* @Title: UnionpayClient.java
* @Description: TODO
* @Author liuyaoshen
* @Date 2017年1月22日
* @Version v1.0
*/

package com.smeyun.payment.unionpay.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.smeyun.payment.unionpay.entity.BizType;
import com.smeyun.payment.unionpay.entity.PayClientType;
import com.smeyun.payment.unionpay.entity.UnionpayRequest;
import com.smeyun.platform.util.common.encrypt.Md5Encrypt;
import com.smeyun.platform.util.common.http.HttpClientUtil;
import com.smeyun.platform.util.common.http.bean.SmeHttpResponse;
import com.smeyun.platform.util.common.util.PlatformUtil;

/**
* JDK version used:      <JDK1.7> 
* @ClassName: UnionpayClient
* @Description: api客户端调用方式测试
* @author liuyaoshen
* @date 2017年1月22日
*/
public class UnionpayClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UnionpayClient.class);
    
    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();
        System.out.println("unionpay start:" + String.valueOf(startTime));
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 1000; i++)
        {
            executorService.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    pay();
                }
            });
        }
        executorService.shutdown();
        while (true)
        {
            if (executorService.isTerminated())
            {
                long endTime = System.currentTimeMillis();
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println("unipay end:" + String.valueOf(endTime));
                System.out.println("tims:"+ String.valueOf(endTime - startTime));
                break;
            }
            
        }
       
      
    }
    
    public static void pay()
    {
        
        try
        {
           
            UnionpayRequest request = new UnionpayRequest();
            request.setTxnAmt(10000l);
            request.setOrderCode(PlatformUtil.generateRandomNumber(20));
            request.setBizType(BizType.B2B.getType());
            request.setClientType(PayClientType.PC.getType());
            request.setReturnUrl("www.smeyun.com/payment/return");
            request.setNotifyUrl("www.smeyun.com/payment/notify");
            request.setOrderDes("测试unionpay");
            
            StringBuilder builder = new StringBuilder();
            builder.append(request.getTxnAmt());
            builder.append(request.getOrderCode());
            builder.append(request.getBizType());
            builder.append(request.getClientType());
            builder.append(request.getReturnUrl());
            builder.append(request.getNotifyUrl());
            builder.append(request.getOrderDes());
            builder.append("0c1c469ad574ff86d5c9c10c00ebbd68");
            
            String salt = Md5Encrypt.md5(builder.toString());
            request.setSalt(salt);
            SmeHttpResponse response = HttpClientUtil.postJsonDataToUrl("http://localhost:8082//platform/payment/unionpay/pay", JSONObject.toJSONString(request));
            System.out.println("结果：" + response.getContent());
        }
        catch (Exception e)
        {
            LOGGER.error("支付失败", e);
        }
    }
}
