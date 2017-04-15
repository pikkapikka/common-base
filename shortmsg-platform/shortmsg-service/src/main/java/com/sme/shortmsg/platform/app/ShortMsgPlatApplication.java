/**
 * 
 */
package com.sme.shortmsg.platform.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.sme.shortmsg.platform.exception.ShortMsgUncheckedException;
import com.sme.shortmsg.platform.filter.CustomErrorFilter;
import com.sme.shortmsg.platform.util.Common;

/**
 * 短信平台系统启动函数
 * @author qxf
 * @since 2016-09-06
 *
 */
@SpringBootApplication(scanBasePackages = "com.sme")
public class ShortMsgPlatApplication
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortMsgPlatApplication.class);
    
    @Value("${short.message.plat.receive.trust.ips}")
    private String trustIps;
    
    /**
     * 实例化过滤器
     * 
     * @return 过滤器
     */
    @Bean
    public Filter customFilter()
    {
        CustomErrorFilter filter = new CustomErrorFilter();
        if (StringUtils.isEmpty(trustIps))
        {
            return filter;
        }
        
        List<String> ipAddrs = new ArrayList<String>();
        String[] tempIps = trustIps.split(",");
        for (String ip : tempIps)
        {
            if (Common.isIpAddress(ip))
            {
                ipAddrs.add(ip);
            }
            else
            {
                throw new ShortMsgUncheckedException("ipaddress config failed.");
            }
        }
        
        filter.setTrustIps(ipAddrs);
        return filter;
        
    }
    
    /**
     * 注册过滤器
     * 
     * @return 注册器
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean()
    {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(customFilter());
        bean.addUrlPatterns("/sme/*");
        bean.setName("CustomFilter");
        return bean;
    }
    
    /**
     * 启动函数
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        
        SpringApplication.run(ShortMsgPlatApplication.class);
        LOGGER.debug("start short message platform success.");
    }
    
}
