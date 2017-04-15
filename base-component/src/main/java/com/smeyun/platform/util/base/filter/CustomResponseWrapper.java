/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: SmeyunUncheckedException.java
 * Author:   qxf
 * Date:     2016年11月16日 上午11:19:44
 */
package com.smeyun.platform.util.base.filter;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author Administrator
 *
 */
public class CustomResponseWrapper extends HttpServletResponseWrapper
{
    private CustomPrintWriter myWriter;  
    private CustomServletOutputStream myOutputStream;

    public CustomResponseWrapper(HttpServletResponse response)
    {
        super(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override  
    public CustomPrintWriter getWriter() throws IOException {  
        myWriter = new CustomPrintWriter(super.getWriter());  
        return myWriter;  
    }  
    
    /**
     * {@inheritDoc}
     */
    @Override  
    public ServletOutputStream getOutputStream() throws IOException {  
        myOutputStream = new CustomServletOutputStream(super.getOutputStream());  
        return myOutputStream;  
    }  
  
    /**
     * 获取writer
     * @return writer
     */
    public CustomPrintWriter getMyWriter() {  
        return myWriter;  
    }  
  
    /**
     * 获取输出流
     * @return 输出流
     */
    public CustomServletOutputStream getMyOutputStream() {  
        return myOutputStream;  
    }  

}
