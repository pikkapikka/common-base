/**
 * 
 */
package com.sme.shortmsg.platform.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sme.shortmsg.platform.exception.ShortMsgUncheckedException;
import com.sme.shortmsg.platform.util.Common;

/**
 * @author Administrator
 *
 */
public class CustomErrorFilter implements Filter
{
    private static final Logger LOG = LoggerFactory.getLogger(CustomErrorFilter.class);
    
    private List<String> trustIps;

    /** 
     * {@inheritDoc}
     */
    @Override
    public void destroy()
    {
        // TODO Auto-generated method stub

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        // TODO Auto-generated method stub
        
        CustomResponseWrapper responseWrapper = new CustomResponseWrapper((HttpServletResponse) response); 
        
        /*
         * IP������У�飬���������У��IP����ֻ����ָ��IP�ķ���������
         */
        if (!CollectionUtils.isEmpty(trustIps))
        {
            HttpServletRequest httpReq = (HttpServletRequest)request;
            String remoteIp = Common.getIpAddr(httpReq);
            if (!trustIps.contains(remoteIp))
            {
                LOG.error("the request ip is not trust, remoteIp=", remoteIp);
                responseWrapper.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"errorMsg\":\"invalid request.\"}");
                return;
            }
        }
        
        
        try
        {
            chain.doFilter(request, responseWrapper);
        }
        catch (ServletException e)
        {
            if (e.getCause() instanceof ShortMsgUncheckedException)
            {
                LOG.error("filter catch exception:", e);
                responseWrapper.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"errorMsg\":\"" + e.getCause().getMessage() + "\"}");
            }
        }
    }
    
    /**
     * ��������IP
     *  
     * @param [����1]     [����1˵��]
     * @return  void
     * @exception/throws [�쳣����] [�쳣˵��]
     * @see   [�ࡢ��#��������#��Ա]
     * @since [��ʼ�汾]
     */
    public void setTrustIps(List<String> trustIps)
    {
        this.trustIps = trustIps;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig arg0) throws ServletException
    {
        // TODO Auto-generated method stub

    }

}
