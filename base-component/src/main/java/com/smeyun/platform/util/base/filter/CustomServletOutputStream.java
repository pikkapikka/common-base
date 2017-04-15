/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: SmeyunUncheckedException.java
 * Author:   qxf
 * Date:     2016年11月16日 上午11:19:44
 */
package com.smeyun.platform.util.base.filter;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * @author Administrator
 *
 */
public class CustomServletOutputStream extends ServletOutputStream
{
    private ServletOutputStream byteStream;
    
    public CustomServletOutputStream(ServletOutputStream byteStream)
    {
        this.byteStream = byteStream;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#isReady()
     */
    @Override
    public boolean isReady()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#setWriteListener(javax.servlet.WriteListener)
     */
    @Override
    public void setWriteListener(WriteListener paramWriteListener)
    {
        
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(int date) throws IOException
    {
        byteStream.write(date);
    }

}
