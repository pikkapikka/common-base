/*
 * Copyright (C), 2002-2016, ������ƻ��ǿƼ����޹�˾
 * FileName: IShortMsgSendService.java
 * Author:   qxf
 * Date:     2016��9��6�� ����4:36:43
 * Description: //ģ��Ŀ�ġ���������      
 * History: //�޸ļ�¼
 * <author>      <time>      <version>    <desc>
 * �޸�������             �޸�ʱ��            �汾��                  ����
 */
package com.sme.shortmsg.platform.service;

/**
 * ���ŷ��͵Ľӿ�
 *
 * @author qxf
 * @see [�����/����]����ѡ��
 * @since [��Ʒ/ģ��汾] ����ѡ��
 */
public interface IShortMsgSendService
{
    /**
     * ������Ϣ
     * 
     * @param chargeNum ���Ѻ���
     * @param content ����
     * @param receivers ��Ϣ������
     * @author qxf
     * @see        [�����/����]����ѡ��
     * @since      [��Ʒ/ģ��汾] ����ѡ��
     */
    void send(String chargeNum, String content, String[] receivers);
}
