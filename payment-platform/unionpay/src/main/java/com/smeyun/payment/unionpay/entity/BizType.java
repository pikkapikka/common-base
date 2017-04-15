/**
* Copyright © 2016 SME Corporation. All rights reserved
* @Title: BizType.java
* @Description: TODO
* @Author liuyaoshen
* @Date 2017年1月22日
* @Version v1.0
*/

package com.smeyun.payment.unionpay.entity;

/**
* JDK version used:      <JDK1.7> 
* @ClassName: BizType
* @Description: TODO(What does this class do?)
* @author liuyaoshen
* @date 2017年1月22日
*/
public enum BizType
{
    B2C(201),B2B(202);
    
    private int type;
    
    private BizType(int type)
    {
        this.type = type;
    }
    /**
     * 获取类型
     * 
     * @return  int
     */
    public int getType()
    {
        return this.type;
    }
    
    /**
     * 检验某个值是否有效
     * 
     * @param type 类型
     * @return  boolean
     */
    public static boolean judge(int type)
    {
        return type == B2C.getType() || type == B2B.type;
    }
}
