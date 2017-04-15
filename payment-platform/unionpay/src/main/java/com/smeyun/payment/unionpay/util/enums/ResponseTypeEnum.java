/** 
* @ClassName: ResponseTypeEnum 
* @Description:  
* @author: liuyaoshen@smeyun.com 
* @date 2017年2月3日 下午3:14:53 
* @version V1.0
*/
package com.smeyun.payment.unionpay.util.enums;

/**
 * <p>:<p/>
 * @author liuyaoshen@smeyun.com
 * @date 2017年2月3日 下午3:14:53 
 */
public enum ResponseTypeEnum
{
    /**
     * 同步应答请求
     */
    SYNC("sync"),
    /**
     * 异步应答请求
     */
    ASYNC("async");
    
    private String type;
    
    ResponseTypeEnum(String type)
    {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }
    
}
