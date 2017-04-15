/**
* Copyright © 2016 SME Corporation. All rights reserved
* @Title: UnionpayServiceImpl.java
* @Description: TODO
* @Author liuyaoshen
* @Date 2016年12月26日
* @Version v1.0
*/

package com.smeyun.payment.unionpay.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.smeyun.payment.unionpay.api.UnionpayApi;
import com.smeyun.payment.unionpay.dao.IUnionpayDao;
import com.smeyun.payment.unionpay.entity.BizType;
import com.smeyun.payment.unionpay.entity.PayNotifyData;
import com.smeyun.payment.unionpay.entity.PayRequestData;
import com.smeyun.payment.unionpay.entity.PayResponseData;
import com.smeyun.payment.unionpay.entity.UnionpayRequest;
import com.smeyun.payment.unionpay.service.IUnionpayService;
import com.smeyun.payment.unionpay.util.CommonUtil;
import com.smeyun.payment.unionpay.util.SDKConfig;
import com.smeyun.payment.unionpay.util.SDKConstants;
import com.smeyun.payment.unionpay.util.UnionPayBase;
import com.smeyun.payment.unionpay.util.enums.RespCodeEnum;
import com.smeyun.payment.unionpay.util.enums.ResponseTypeEnum;
import com.smeyun.platform.util.common.encrypt.Md5Encrypt;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;
import com.smeyun.platform.util.common.http.HttpClientUtil;
import com.smeyun.platform.util.common.http.bean.SmeHttpResponse;
import com.smeyun.platform.util.common.util.DateUtil;
import com.smeyun.platform.util.common.util.PlatformUtil;

/**
* JDK version used:      <JDK1.8> 
* @ClassName: UnionpayServiceImpl
* @Description: TODO
* @author liuyaoshen
* @date 2016年12月26日
*/
@Service
public class UnionpayServiceImpl implements IUnionpayService
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UnionpayServiceImpl.class);
    
    @Autowired
    private SDKConfig sdkConfig;
    
    @Autowired
    private UnionpayApi unionpayApi;
    
    @Autowired
    private IUnionpayDao unionpayDao;
    
    @Override
    public String pay_B2C(UnionpayRequest payRequest)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean validateRcvReq(Map<String, String> valideData, String encoding)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Transactional
    @Override
    public String pay_B2B(UnionpayRequest unionpayRequest)
    {
        // 保存到数据库
        long id = unionpayDao.saveRequestData(new PayRequestData(unionpayRequest));
        switch (CommonUtil.parseClientType(unionpayRequest.getClientType()))
        {
            case PC:
            {
                return buildPayForm(unionpayRequest, id);
            }
            case PHONE:
            {
                return buildPayForm(unionpayRequest, id);
            }
            default:
            {
                return null;
            }
        }
    }
    
    @Transactional
    @Override
    public String dealNotify(Map<String, String> valideData, String encoding, ResponseTypeEnum responseTypeEnum)
    {
        if (!unionpayApi.validate(valideData, encoding))
        {
            LOGGER.error("pay response sign error");
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR_INT);
        }
        
        long id = getReqId(valideData);
        PayRequestData payRequestData = unionpayDao.queryRequestDataById(id);
        if (payRequestData == null)
        {
            LOGGER.error("can not find the request information, reqId={}.", id);
            throw new SmeyunUncheckedException(ErrorCodeConstant.PARAM_INVALID_INT);
        }
        //保存
        saveNotifyData(payRequestData, valideData);
        //通知客户端
        return notifyCustom(payRequestData, valideData, responseTypeEnum);
    }
    
    /**
     * 获取返回的请求信息记录ID
     * @param valideData
     * @return
     */
    private long getReqId(Map<String, String> valideData)
    {
        String reqReserved = valideData.get("reqReserved");
        LOGGER.info("reqReserved is {}", reqReserved);
        try
        {
            return Long.parseLong(reqReserved);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("get request ID failed, param=" + reqReserved, e);
           throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR_INT);
        }
       
    }
    
    private void saveNotifyData(PayRequestData payRequestData, Map<String, String> valideData){
        PayNotifyData notifyData = unionpayDao.queryNotifyDataByReqId(payRequestData.getId());
        //
        if (null == notifyData)
        {
            notifyData = createSyncNotifyData(payRequestData.getId(), valideData);
            unionpayDao.saveNotifyData(notifyData);
        } else {
            notifyData.setResCode(valideData.get("respCode"));
            notifyData.setResMsg(valideData.get("respMsg"));
            unionpayDao.updateNotifyData(notifyData);
        }
    }
    
    /**
     * 创建同步应答数据
     * @param reqId
     * @param valideData
     * @return
     */
    private PayNotifyData createSyncNotifyData(long reqId, Map<String, String> valideData){
        PayNotifyData payNotifyData = new PayNotifyData();
        
        payNotifyData.setOrderCode(valideData.get("orderId"));
        payNotifyData.setQueryId(valideData.get("queryId"));
        payNotifyData.setReqId(reqId);
        payNotifyData.setResCode(valideData.get("respCode"));
        payNotifyData.setResMsg(valideData.get("respMsg"));
        payNotifyData.setRevTime(new Date());
        payNotifyData.setTxnAmount(Long.parseLong(valideData.get("txnAmt")));
        payNotifyData.setTxnTime(DateUtil.parseYmdhms(valideData.get("txnTime")));
        payNotifyData.setVersion(System.currentTimeMillis());
        
        return payNotifyData;
    }
    
    /**
     * 通知客户端
     * @param data
     * @param notifyParams
     */
    private String notifyCustom(PayRequestData data, Map<String, String> notifyParams, ResponseTypeEnum responseTypeEnum)
    {
        String respCode = notifyParams.get("respCode");
        String ret = RespCodeEnum.SUCESS.getCode().equals(respCode) ? SDKConstants.PAY_SUCESS : SDKConstants.PAY_FAILED;
        PayResponseData payResponseData = new PayResponseData();
        payResponseData = createReponseData(notifyParams, ret);
        if (responseTypeEnum == ResponseTypeEnum.SYNC)
        {
            return createUrl(payResponseData, data.getReturnUrl());
        }
        else{
          // 异步回调通知接口
            tryCallRemote(data.getNotifyUrl(), JSONObject.toJSONString(payResponseData));
            return "";
        }
        
    }
    
    private String createUrl(PayResponseData payResponseData, String url){
        StringBuilder builder = new StringBuilder(url);
        builder.append("?orderCode=" + payResponseData.getOrderCode());
        builder.append("&queryId=" + payResponseData.getQueryId());
        builder.append("&result=" + payResponseData.getResult());
        builder.append("&salt=" + payResponseData.getSalt());
        builder.append("&txnAmt=" + payResponseData.getTxnAmt());
        return builder.toString();
    }
    
    /**
     * 向客户端通知结果
     * <p>MD5值:MD5(orderCode + queryId + result + txnAmt + key)</p>
     * @param notifyParams
     * @param ret
     * @return
     */
    private PayResponseData createReponseData(Map<String, String> notifyParams, String ret)
    {
        PayResponseData responseData = new PayResponseData();
        
        responseData.setOrderCode(notifyParams.get("orderId"));
        responseData.setQueryId(notifyParams.get("queryId"));
        responseData.setResult(ret);
        responseData.setTxnAmt(notifyParams.get("txnAmt"));
        
        // 生成校验盐值
        StringBuilder builder = new StringBuilder();
        builder.append(responseData.getOrderCode());
        builder.append(responseData.getQueryId());
        builder.append(responseData.getResult());
        builder.append(responseData.getTxnAmt());
        builder.append(sdkConfig.getPrivateKey());
        
        responseData.setSalt(Md5Encrypt.md5(builder.toString()));
        
        return responseData;
    }
    /**
     * @Title: buildPayForm
     * @Description: 构建支付表单(支持B2C、B2B)
     * @param @param unionpayRequest
     * @param @return    param
     * @return String    returnType
     * @throws
     */
    private String buildPayForm(UnionpayRequest unionpayRequest, long id)
    {
        String htmlForm = "";
        try
        {
            
            Map<String, String> requestData = new HashMap<String, String>();
            
            /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
            requestData.put("version", UnionPayBase.VERSION); //版本号，全渠道默认值
            requestData.put("encoding", UnionPayBase.ENCODING_UTF8); //字符集编码，可以使用UTF-8,GBK两种方式
            requestData.put("signMethod", SDKConstants.SIGN_RSA); //签名方法，只支持 01：RSA方式证书加密
            requestData.put("txnType", SDKConstants.TXN_CONSUME); //交易类型 ，01：消费
            requestData.put("txnSubType", SDKConstants.TXN_SUB_CONSUME); //交易子类型， 01：自助消费
            //业务类型 000202: B2B, 000201: B2C
            requestData.put("bizType",
                    unionpayRequest.getBizType().intValue() == BizType.B2B.getType() ? SDKConstants.BIZ_B2B
                            : SDKConstants.BIZ_B2C);
            requestData.put("channelType", SDKConstants.CHNANEL_PC); //渠道类型 固定07
            requestData.put("reqReserved", String.valueOf(id)); //自定义值，交易应答时原样返回
            
            /***商户接入参数***/
            requestData.put("merId", sdkConfig.getMerId()); //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
            requestData.put("accessType", SDKConstants.ACCESS_MERCHANT); //接入类型，0：直连商户 
            requestData.put("orderId", unionpayRequest.getOrderCode()); //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则     
            requestData.put("txnTime", DateUtil.dtSimpleYmdhhmmss(unionpayRequest.getPayTime())); //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
            requestData.put("currencyCode", "156"); //交易币种（境内商户一般是156 人民币）        
            requestData.put("txnAmt", String.valueOf(unionpayRequest.getTxnAmt())); //交易金额，单位分，不要带小数点
            
            //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
            //如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
            //异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
            requestData.put("frontUrl", sdkConfig.getFrontUrl());
            
            //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
            //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
            //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  
            //    3.收单后台通知后需要10秒内返回http200或302状态码 
            //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
            //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
            requestData.put("backUrl", sdkConfig.getBackUrl());
            
            /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
            Map<String, String> reqData = unionpayApi.sign(requestData, UnionPayBase.ENCODING_UTF8); //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
            htmlForm = unionpayApi.createAutoFormHtml(sdkConfig.getFrontRequestUrl(),
                    reqData,
                    UnionPayBase.ENCODING_UTF8); //生成自动跳转的Html表单
        }
        catch (Exception e)
        {
            LOGGER.error("build unionpay form error!reason:{}", unionpayRequest.toString(), e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
        return htmlForm;
    }
    
    
    private void tryCallRemote(String url, String param)
    {
        for (int idx = 0; idx < sdkConfig.getMaxTimes(); idx++)
        {
            try
            {
                SmeHttpResponse resp = HttpClientUtil.postJsonDataToUrl(url, param);
                if (PlatformUtil.isHttpSuc(resp.getStatus()))
                {
                    LOGGER.info("call remote site success, url=" + url);
                    return;
                }
                else
                {
                    LOGGER.error("call remote site failed, url={}, httpstatus={}.", url, resp.getStatus());
                    throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR_INT);
                }
                
            }
            catch (Exception e)
            {
                LOGGER.error("call remote site failed, url=" + url, e);
            }
            
            PlatformUtil.sleep(sdkConfig.getSleepTime());
        }
    }
    
}
