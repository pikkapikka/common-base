/*
 * Copyright (C), 2002-2016, ������ƻ������ݿƼ����޹�˾
 * FileName: ShortMsgUncheckedException.java
 * Author:   qxf
 * Date:     2016��9��7�� ����8:44:51
 * Description: //ģ��Ŀ�ġ���������      
 * History: //�޸ļ�¼
 * <author>      <time>      <version>    <desc>
 * �޸�������             �޸�ʱ��            �汾��                  ����
 */
package com.sme.shortmsg.platform.exception;

/**
 * ��һ�仰���ܼ�����<br> 
 * ��������ϸ������
 *
 * @author qxf
 * @see [�����/����]����ѡ��
 * @since [��Ʒ/ģ��汾] ����ѡ��
 */
public class ShortMsgUncheckedException extends RuntimeException
{
    
    /**
     * ���к�
     */
    private static final long serialVersionUID = -861189309849712261L;
    
    /**
     * ���캯��
     * @param message �쳣��Ϣ
     */
    public ShortMsgUncheckedException(String message)
    {
        super(message);
    }
    
    /**
     * ���캯��
     * @param t �쳣��Ϣ
     */
    public ShortMsgUncheckedException(Throwable t)
    {
        super(t);
    }
}
