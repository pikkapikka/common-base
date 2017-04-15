/** 
* @ClassName: RespCodeEnum 
* @Description:  
* @author: liuyaoshen@smeyun.com 
* @date 2017年2月3日 下午2:37:07 
* @version V1.0
*/
package com.smeyun.payment.unionpay.util.enums;

/**
 * <p>银联支付应答码<p/>
 * @author liuyaoshen@smeyun.com
 * @date 2017年2月3日 下午2:37:07 
 */
public enum RespCodeEnum
{
    SUCESS("00");
    
    private String  code;
    
    RespCodeEnum(String code)
    {
        this.code = code;
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

}
