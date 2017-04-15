/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: HttpClientUtil.java
 * Author:   qxf
 * Date:     2016年11月17日 下午3:09:35
 */
package com.smeyun.platform.util.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;
import com.smeyun.platform.util.common.http.bean.SmeCookie;
import com.smeyun.platform.util.common.http.bean.SmeHeader;
import com.smeyun.platform.util.common.http.bean.SmeHttpResponse;

/**
 * http客户端工具类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class HttpClientUtil
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
    
    public static final String SunX509 = "SunX509";
    
    public static final String JKS = "JKS";
    
    public static final String PKCS12 = "PKCS12";
    
    public static final String TLS = "TLS";
    
    /**
     * 默认：请求获取数据的超时时间，单位毫秒。
     */
    private static final int DEFAULT_DATA_TIMEOUT = 20000;
    
    /**
     * 默认：设置连接超时时间，单位毫秒。
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    
    private static final int LEN_HOST_PORT = 2;
    
    private static final String SOFT_USER_AGENT = "Softisland SOA-Agent/1.0";
    
    /**
     * 通过GET方式获取指定URL的内容
     * @param url
     * @return
     * @throws Exception
     */
    public static SmeHttpResponse getUrlContent(String url, SmeCookie[] softCookies) throws Exception
    {
        CloseableHttpClient httpclient = getHttpClient(url, softCookies);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", SOFT_USER_AGENT);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        
        return getResponseContent(response);
    }
    
    /**
     * 通过GET方式获取指定URL的内容
     * @param url
     * @return
     * @throws Exception
     */
    public static SmeHttpResponse getUrlContent(String url) throws Exception
    {
        return getUrlContent(url, null);
    }
    
    /**
     * 获取header和返回内容
     * @param url
     * @return
     * @throws Exception
     */
    public static SmeHttpResponse getHeadersAndContent(String url, SmeCookie[] softCookies) throws Exception
    {
        CloseableHttpClient httpclient = getHttpClient(url, softCookies);
        
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", SOFT_USER_AGENT);
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(DEFAULT_DATA_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        return getResponseHeaderAndContent(response);
        
    }
    
    /**
     * 获取header和返回内容
     * @param response
     * @return
     * @throws IOException
     */
    private static SmeHttpResponse getResponseHeaderAndContent(CloseableHttpResponse response) throws IOException
    {
        SmeHttpResponse res = getResponseContent(response);
        Header[] headers = response.getAllHeaders();
        if (null != headers)
        {
            Map<String, String> map = new HashMap<>();
            for (Header h : headers)
            {
                if (map.containsKey(h.getName()))
                {
                    map.put(h.getName(), map.get(h.getName()) + ";" + h.getValue());
                }
                else
                {
                    map.put(h.getName(), h.getValue());
                }
            }
            res.setHeaders(map);
        }
        return res;
    }
    
    /**
     * 返回请求内容
     * @param response
     * @return
     * @throws Exception
     */
    public static SmeHttpResponse getResponseContent(CloseableHttpResponse response) throws IOException
    {
        try
        {
            SmeHttpResponse res = new SmeHttpResponse(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), "utf-8"));
            return res;
        }
        finally
        {
            response.close();
        }
    }
    
    /**
     * 向指定的URL发送请求
     * @param url
     * @param paraMap 参数
     * @return
     * @throws Exception
     */
    public static SmeHttpResponse postParamsToUrl(String url, Map<String, String> paraMap) throws Exception
    {
        return postParamsToUrl(url, paraMap, null, null);
    }
    
    /**
     * 向指定的URL发送请求
     * @param url
     * @param paraMap 参数
     * @return
     * @throws Exception
     */
    public static SmeHttpResponse postParamsToUrl(String url, Map<String, String> paraMap, SmeCookie[] cookies)
            throws Exception
    {
        return postParamsToUrl(url, paraMap, cookies, null);
    }
    
    public static SmeHttpResponse postParamsToUrl(String url, Map<String, String> paraMap, SmeCookie[] cookies,
            SmeHeader[] headers) throws IOException
    {
        CloseableHttpClient httpclient = getHttpClient(url, cookies);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", SOFT_USER_AGENT);
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(DEFAULT_DATA_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .build();
        httpPost.setConfig(requestConfig);
        
        if (null != headers)
        {
            for (SmeHeader header : headers)
            {
                httpPost.setHeader(header.getName(), header.getValue());
            }
        }
        try
        {
            if (null != paraMap && !paraMap.isEmpty())
            {
                List<NameValuePair> nvps = new ArrayList<>(paraMap.size());
                for (Entry<String, String> entry : paraMap.entrySet())
                {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            }
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("can not format by utf-8.", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
        
        CloseableHttpResponse response = httpclient.execute(httpPost);
        
        return getResponseContent(response);
    }
    
    /**
     * 返回header和内容
     * @param url
     * @param paraMap
     * @return
     * @throws Exception
     */
    public static SmeHttpResponse postParamsToUrl1(String url, Map<String, String> paraMap, SmeCookie[] cookies)
            throws Exception
    {
        CloseableHttpClient httpclient = getHttpClient(url, cookies);
        
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", SOFT_USER_AGENT);
        List<NameValuePair> nvps = new ArrayList<>(paraMap.size());
        
        for (Entry<String, String> entry : paraMap.entrySet())
        {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        
        try
        {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("can not format by utf-8.", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
        
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(DEFAULT_DATA_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .build();
        httpPost.setConfig(requestConfig);
        
        CloseableHttpResponse response = httpclient.execute(httpPost);
        
        return getResponseHeaderAndContent(response);
    }
    
    /**
     * 获取httclient
     * @param cookies
     * @return
     */
    private static CloseableHttpClient getHttpClient(SmeCookie[] cookies)
    {
        CloseableHttpClient httpclient;
        if (null == cookies || cookies.length == 0)
        {
            httpclient = HttpClients.createDefault();
        }
        else
        {
            httpclient = HttpClients.custom().setDefaultCookieStore(createCookie(cookies)).build();
        }
        return httpclient;
    }
    
    private static CookieStore createCookie(SmeCookie[] cookies)
    {
        CookieStore cookieStore = new BasicCookieStore();
        for (SmeCookie softCookie : cookies)
        {
            BasicClientCookie cookie = new BasicClientCookie(softCookie.getName(), softCookie.getValue());
            cookie.setDomain(softCookie.getDomain());
            cookie.setPath("/");
            cookieStore.addCookie(cookie);
        }
        return cookieStore;
    }
    
    /**
     * 获取httclient
     * @param cookies
     * @return
     */
    private static CloseableHttpClient getHttpClient(String url, SmeCookie[] cookies)
    {
        if (url.startsWith("https"))
        {
            return getSSLHttpClient(cookies);
        }
        else
        {
            return getHttpClient(cookies);
        }
    }
    
    private static CloseableHttpClient getSSLHttpClient(SmeCookie[] cookies)
    {
        try
        {
            SSLContext context = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
            {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                    return true;
                }
            }).build();
            
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(context);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(DEFAULT_DATA_TIMEOUT)
                    .setSocketTimeout(DEFAULT_CONNECT_TIMEOUT)
                    .build();
            if (null == cookies || cookies.length == 0)
            {
                return HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(requestConfig).build();
            }
            else
            {
                return HttpClients.custom()
                        .setSSLSocketFactory(sslsf)
                        .setDefaultRequestConfig(requestConfig)
                        .setDefaultCookieStore(createCookie(cookies))
                        .build();
            }
        }
        catch (KeyManagementException e)
        {
            LOGGER.error("ssl context key management Exception:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
        catch (NoSuchAlgorithmException e)
        {
            LOGGER.error("ssl context get instance faild, no such method:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
        catch (KeyStoreException e)
        {
            LOGGER.error("ssl context key store Exception:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /**
     * 发送json数据到指定的URL
     * @param url
     * @param json
     * @return
     * @throws Exception
     */
    public static SmeHttpResponse postJsonDataToUrl(String url, String json) throws Exception
    {
        return postJsonDataToUrl(url, json, null);
    }
    
    public static SmeHttpResponse postJsonDataToUrl(String url, String json, SmeCookie[] softCookies) throws Exception
    {
        
        return postJsonDataToUrl(url, json, softCookies, null);
    }
    
    public static SmeHttpResponse postJsonDataToUrl(String url, String json, SmeCookie[] softCookies,
            SmeHeader[] headers) throws Exception
    {
        
        CloseableHttpClient httpclient = getHttpClient(url, softCookies);
        
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", SOFT_USER_AGENT);
        if (null != headers)
        {
            for (SmeHeader header : headers)
            {
                httpPost.setHeader(header.getName(), header.getValue());
            }
        }
        
        StringEntity entity = new StringEntity(json, "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json; charset=utf-8");
        httpPost.setEntity(entity);
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(DEFAULT_DATA_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .build();
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        
        return getResponseContent(response);
    }
    
    /**
     * get HttpURLConnection
     * @param strUrl url��ַ
     * @return HttpURLConnection
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String strUrl) throws IOException
    {
        URL url = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        return httpURLConnection;
    }
    
    /**
     * get HttpsURLConnection
     * @param strUrl url��ַ
     * @return HttpsURLConnection
     * @throws IOException
     */
    public static HttpsURLConnection getHttpsURLConnection(String strUrl) throws IOException
    {
        URL url = new URL(strUrl);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        return httpsURLConnection;
    }
    
    public static String getURL(String strUrl)
    {
        
        if (null != strUrl)
        {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf)
            {
                return strUrl.substring(0, indexOf);
            }
            return strUrl;
        }
        return strUrl;
        
    }
    
    public static String getQueryString(String strUrl)
    {
        
        if (null != strUrl)
        {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf)
            {
                return strUrl.substring(indexOf + 1, strUrl.length());
            }
            return "";
        }
        return strUrl;
    }
    
    public static Map queryString2Map(String queryString)
    {
        if (null == queryString || "".equals(queryString))
        {
            return null;
        }
        Map m = new HashMap();
        String[] strArray = queryString.split("&");
        for (int index = 0; index < strArray.length; index++)
        {
            String pair = strArray[index];
            putMapByPair(pair, m);
        }
        return m;
        
    }
    
    private static void putMapByPair(String pair, Map m)
    {
        
        if (null == pair || "".equals(pair))
        {
            return;
        }
        int indexOf = pair.indexOf("=");
        if (-1 != indexOf)
        {
            String k = pair.substring(0, indexOf);
            String v = pair.substring(indexOf + 1, pair.length());
            if (null != k && !"".equals(k))
            {
                m.put(k, v);
            }
        }
        else
        {
            m.put(pair, "");
        }
    }
    
    /**
     * @param reader
     * @return String
     * @throws IOException
     */
    public static String bufferedReader2String(BufferedReader reader) throws IOException
    {
        StringBuffer buf = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            buf.append(line);
            buf.append("\r\n");
        }
        
        return buf.toString();
    }
    
    /**
     * 用get方法调用一个url
     * 返回json字符串
     */
    public static SmeHttpResponse getJson(String url) throws IOException
    {
        return getJson(url, null);
    }
    
    /**
     * 使用GET方法获取指定URL的内容
     * @param url
     * @param softCookies
     * @return
     * @throws IOException
     */
    public static SmeHttpResponse getJson(String url, SmeCookie[] softCookies) throws IOException
    {
        CloseableHttpClient httpclient = getHttpClient(url, softCookies);
        
        HttpGet httpGet = new HttpGet(new String(url.getBytes(), "UTF-8"));
        httpGet.setHeader("User-Agent", SOFT_USER_AGENT);
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(DEFAULT_DATA_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .build();
        
        httpGet.setConfig(requestConfig);
        
        CloseableHttpResponse response = httpclient.execute(httpGet);
        return getResponseContent(response);
    }
    
    /**
     * 使用GET方法获取指定URL的内容
     * @param url
     * @param softCookies
     * @param proxyHost 代理主机
     * @return
     * @throws IOException
     */
    public static SmeHttpResponse getJson(String url, SmeCookie[] softCookies, String proxyHost) throws IOException
    {
        CloseableHttpClient httpclient = getHttpClient(url, softCookies);
        
        HttpGet httpGet = new HttpGet(new String(url.getBytes(), "UTF-8"));
        httpGet.setHeader("User-Agent", SOFT_USER_AGENT);
        
        org.apache.http.client.config.RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig
                .custom().setSocketTimeout(DEFAULT_DATA_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        HttpHost host = getProxy(proxyHost);
        if (null != host)
        {
            builder.setProxy(host);
        }
        
        org.apache.http.client.config.RequestConfig requestConfig = builder.build();
        httpGet.setConfig(requestConfig);
        
        CloseableHttpResponse response = httpclient.execute(httpGet);
        return getResponseContent(response);
    }
    
    private static HttpHost getProxy(String proxyHost)
    {
        String[] hostPort = proxyHost.split(":");
        if (hostPort.length < LEN_HOST_PORT)
        {
            return null;
        }
        
        HttpHost proxy = new HttpHost(hostPort[0], Integer.parseInt(hostPort[1]));
        return proxy;
    }
}
