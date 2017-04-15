/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: SmeyunUncheckedException.java
 * Author:   qxf
 * Date:     2016年11月16日 上午11:19:44
 */
package com.smeyun.platform.util.base.filter;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * �Զ������
 * @author Administrator
 *
 */
public class CustomPrintWriter extends PrintWriter
{
    public CustomPrintWriter(Writer outputStream)
    {
        super(outputStream);
    }
}
