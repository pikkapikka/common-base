/*
 * Copyright (C), 2002-2016, �����׹������������޹�˾
 * FileName: HttpSend.java
 * Author:   qxf
 * Date:     2016��9��6�� ����6:03:27
 * Description: //ģ��Ŀ�ġ���������      
 * History: //�޸ļ�¼
 * <author>      <time>      <version>    <desc>
 * �޸�������             �޸�ʱ��            �汾��                  ����
 */
package com.sme.shortmsg.platform.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ���ͺ�����http��Ϣ����
 *
 * @author qxf
 * @see [�����/����]����ѡ��
 * @since [��Ʒ/ģ��汾] ����ѡ��
 */
public final class HttpSend
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSend.class);
    
    private HttpSend()
    {
        
    }
    
    public static String getSend(String strUrl, String param)
    {
        URL url = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        
        String getUrl = strUrl + (StringUtils.isEmpty(param) ? "" : "?" + param);
        
        try
        {
            url = new URL(getUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.connect();
            
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line);
            }
            
            return buffer.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
            
            close(reader);
        }
    }
    
    public static String postSend(String strUrl, String param)
    {
        
        URL url = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        DataOutputStream out = null;
        
        try
        {
            url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.connect();
            
            //POST����ʱʹ��
            out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(param);
            out.flush();
            
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line);
            }
            
            return buffer.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
            
            close(out);
            close(reader);
            
        }
        
    }
    
    /**
     * תΪ16���Ʒ���
     * @param str
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String paraTo16(String str) throws UnsupportedEncodingException
    {
        String hs = "";
        
        byte[] byStr = str.getBytes("UTF-8");
        for (int i = 0; i < byStr.length; i++)
        {
            String temp = "";
            temp = (Integer.toHexString(byStr[i] & 0xFF));
            if (temp.length() == 1)
                temp = "%0" + temp;
            else
                temp = "%" + temp;
            hs = hs + temp;
        }
        return hs.toUpperCase();
        
    }
    
    
    
    private static void close(Closeable stream)
    {
        if (null != stream)
        {
            try
            {
                stream.close();
            }
            catch (IOException e)
            {
                LOGGER.error("close execption:", e);
            }
        }
    }
    
}
