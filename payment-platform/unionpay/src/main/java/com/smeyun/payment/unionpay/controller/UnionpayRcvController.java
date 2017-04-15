/** 
* @ClassName: UnionpayRcvController 
* @Description:  
* @author: liuyaoshen@smeyun.com 
* @date 2017年2月4日 上午10:49:34 
* @version V1.0
*/
package com.smeyun.payment.unionpay.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smeyun.payment.unionpay.service.IUnionpayService;
import com.smeyun.payment.unionpay.util.SDKConstants;
import com.smeyun.payment.unionpay.util.enums.ResponseTypeEnum;

/**
 * <p>:<p/>
 * @author liuyaoshen@smeyun.com
 * @date 2017年2月4日 上午10:49:34 
 */
@Controller
@RequestMapping("rcv")
public class UnionpayRcvController
{
 // 日志打印对象
    private static final Logger LOGGER = LoggerFactory.getLogger(UnionpayRcvController.class);
    
    @Autowired
    private IUnionpayService unionpayService;
    /**
     * 银联同步应答请求
     * @param request 银联请求request
     * @return
     */
    @RequestMapping("frontRcvResponse") 
    public void syncNotify(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String url = this.notify(request, ResponseTypeEnum.SYNC);
            response.sendRedirect(url);
        }
        catch (Exception e)
        {
            LOGGER.error("syncNotify error!", e);
        }
    }
    
    /**
     * 银联异步应答请求
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("backRcvResponse") 
    public String asyncNotify(HttpServletRequest request){
        try
        {
            this.notify(request, ResponseTypeEnum.ASYNC);
            return SDKConstants.RESP_OK;
        }
        catch (Exception e)
        {
            LOGGER.error("asyncNotify error!", e);
            return SDKConstants.RESP_FAILED;
        }
    }
    
    private String notify(HttpServletRequest request, ResponseTypeEnum responseTypeEnum) throws Exception{
        Map<String, String> respParam = getAllRequestParam(request);
        String encoding = request.getParameter(SDKConstants.param_encoding);
        return unionpayService.dealNotify(respParam, encoding, responseTypeEnum);
    }
    
    /**
     * 获取请求参数中所有的信息
     * 
     * @param request
     * @return
     */
    public static Map<String, String> getAllRequestParam(final HttpServletRequest request)  throws IOException {
        Map<String, String> res = new HashMap<String, String>();
        String encoding = request.getParameter(SDKConstants.param_encoding);
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                //在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if(StringUtils.isNotBlank(value)){
                    value = new String(value.getBytes(encoding), encoding);
                    res.put(en, value);
                }
            }
        }
        
        return res;
    }
    
}
