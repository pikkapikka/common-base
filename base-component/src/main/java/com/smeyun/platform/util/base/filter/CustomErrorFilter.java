/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: SmeyunUncheckedException.java
 * Author:   qxf
 * Date:     2016年11月16日 上午11:19:44
 */
package com.smeyun.platform.util.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 异常过滤器。
 * 这里会捕捉SmeyunUncheckedException异常，捕获后，会返回http 500错误，并且返回错误码。
 * 
 * @author qxf
 */
public class CustomErrorFilter implements Filter
{
    private static final Logger LOG = LoggerFactory.getLogger(CustomErrorFilter.class);

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        // TODO Auto-generated method stub
        CustomResponseWrapper responseWrapper = new CustomResponseWrapper((HttpServletResponse) response); 
        
        try
        {
            chain.doFilter(request, responseWrapper);
        }
        catch (ServletException e)
        {
            if (e.getCause() instanceof SmeyunUncheckedException)
            {
                SmeyunUncheckedException smeEx = (SmeyunUncheckedException) e.getCause();
                LOG.error("filter catch exception:", e);
                responseWrapper.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"errorCode\":\"" + smeEx.getErrorCode() + "\"}");
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig arg0) throws ServletException
    {
        // TODO Auto-generated method stub

    }

}
