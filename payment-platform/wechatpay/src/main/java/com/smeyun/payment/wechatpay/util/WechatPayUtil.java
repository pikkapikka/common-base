/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: TenpayUtil.java
 * Author:   qxf
 * Date:     2016年11月23日 上午9:28:32
 */
package com.smeyun.payment.wechatpay.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.platform.util.common.constant.PlatformConstant;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * 工具类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class WechatPayUtil
{
    // 日志打印对象
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatPayUtil.class);
    
    /**
     * 获取编码字符集
     * 
     * @param request
     * @param response
     * @return String
     */
    
    public static String getCharacterEncoding(HttpServletRequest request, HttpServletResponse response)
    {
        
        if (null == request || null == response)
        {
            return PlatformConstant.CHARSET_UTF8;
        }
        
        String enc = request.getCharacterEncoding();
        if (null == enc || "".equals(enc))
        {
            enc = response.getCharacterEncoding();
        }
        
        if (null == enc || "".equals(enc))
        {
            enc = PlatformConstant.CHARSET_UTF8;
        }
        
        return enc;
    }
    
    /**
     * 解析xml字符串
     * 
     * @param strxml xml字符串
     * @return  Map<String,String>
     * @exception/throws [异常类型] [异常说明]
     *
     * @author qxf
     * @see   [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static Map<String, String> doXMLParse(String strxml)
    {
        Map<String, String> m = new HashMap<String, String>();
        if (null == strxml || "".equals(strxml))
        {
            LOGGER.error("the parameter strxml is null.");
            return m;
        }
        
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        Document doc = null;
        try
        {
            doc = DocumentHelper.parseText(strxml);
        }
        catch (DocumentException e)
        {
            LOGGER.error("parse xml failed, xml=" + strxml, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
        
        Element root = doc.getRootElement();
        List<?> list = root.elements();
        Iterator<?> it = list.iterator();
        while (it.hasNext())
        {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List<?> children = e.elements();
            if (CollectionUtils.isEmpty(children))
            {
                v = e.getText();
            }
            else
            {
                v = getChildrenText(children);
            }
            
            m.put(k, v);
        }
        
        return m;
    }
    
    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
    private static String getChildrenText(List<?> children)
    {
        StringBuffer sb = new StringBuffer();
        if (children.isEmpty())
        {
            return sb.toString();
        }
        
        Iterator<?> it = children.iterator();
        while (it.hasNext())
        {
            Element e = (Element) it.next();
            String name = e.getName();
            String value = e.getText();
            List<?> list = e.elements();
            sb.append("<" + name + ">");
            if (!list.isEmpty())
            {
                sb.append(getChildrenText(list));
            }
            sb.append(value);
            sb.append("</" + name + ">");
        }
        
        return sb.toString();
    }
    
}
