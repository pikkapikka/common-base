/**
 * 
 */
package com.sme.shortmsg.platform.filter;

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
