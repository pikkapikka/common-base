/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: WechatPayTask.java
 * Author:   qxf
 * Date:     2016年12月20日 下午5:14:14
 */
package com.smeyun.payment.paytest.app;

import org.apache.commons.lang3.RandomUtils;

import com.alibaba.fastjson.JSONObject;
import com.smeyun.payment.wechatpay.entity.WechatPayRequest;
import com.smeyun.platform.util.common.encrypt.Md5Encrypt;
import com.smeyun.platform.util.common.http.HttpClientUtil;
import com.smeyun.platform.util.common.http.bean.SmeHttpResponse;
import com.smeyun.platform.util.common.util.PlatformUtil;

/**
 * 〈功能详细描述〉
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class WechatPayTask implements Runnable
{
    private String threadName;
    
    public WechatPayTask(int id)
    {
        threadName = "thread-" + id;
    }
    
    /**
     * @see java.lang.Runnable#run()<br>
     * 
     * @author qxf 
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        WechatPayRequest request = new WechatPayRequest();
        request.setClientType(1);
        request.setDetail("aaa阿大大");
        request.setNotifyUrl("http://127.0.0.1:8083/platform/payment/test/notify");
        request.setOrderCode("order" + PlatformUtil.generateRandomString(20));
        request.setProductIds("bbbb");
        request.setRandom("aaa");
        request.setTitle("test data");
        request.setTotalFee("5.63");
        
        StringBuilder builder = new StringBuilder();
        builder.append(request.getClientType());
        builder.append(request.getOrderCode());
        builder.append(request.getProductIds());
        builder.append(request.getTotalFee());
        builder.append(request.getNotifyUrl());
        builder.append(request.getRandom());
        
        request.setSalt(Md5Encrypt.md5(builder.toString()));
        
        try
        {
            System.out.println(threadName + ": request=" + request.toString());
            SmeHttpResponse ret = HttpClientUtil.postJsonDataToUrl(
                    "http://devpayment.smeyun.com/wechatpay/platform/payment/wechatpay/pay",
                    JSONObject.toJSONString(request));
            
            System.out.println(threadName + ": httpCode=" + ret.getStatus());
            //            System.out
            //                    .println(threadName + ": httpCode=(" + ret.getStatus() + "), content=(" + ret.getContent() + ").");
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
