/**
* Copyright © 2016 SME Corporation. All rights reserved
* @Title: UnionPayBase.java
* @Description: TODO
* @Author liuyaoshen
* @Date 2016年12月20日
* @Version v1.0
*/

package com.smeyun.payment.unionpay.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
* JDK version used:      <JDK1.8> 
* @ClassName: UnionPayBase
* @Description: TODO(What does this class do?)
* @author liuyaoshen
* @date 2016年12月20日
*/
public class UnionPayBase
{
  //默认配置的是UTF-8
    public static String ENCODING_UTF8 = "UTF-8";
    
    public static String ENCODING_GBK = "GBK";
    //全渠道固定值
    public static String VERSION = "5.0.0";
    public static String SIGN_RSA = "01";
    

    // 商户发送交易时间 格式:YYYYMMDDhhmmss
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
    
    // AN8..40 商户订单号，不能含"-"或"_"
    public static String getOrderId() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
    /**
     * 组装请求，返回报文字符串用于显示
     * @param data
     * @return
     */
    public static String genHtmlResult(Map<String, String> data){

        TreeMap<String, String> tree = new TreeMap<String, String>();
        Iterator<Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            tree.put(en.getKey(), en.getValue());
        }
        it = tree.entrySet().iterator();
        StringBuffer sf = new StringBuffer();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            String key = en.getKey(); 
            String value =  en.getValue();
            if("respCode".equals(key)){
                sf.append("<b>"+key + SDKConstants.EQUAL + value+"</br></b>");
            }else
                sf.append(key + SDKConstants.EQUAL + value+"</br>");
        }
        return sf.toString();
    }
    /**
     * 功能：解析全渠道商户对账文件中的ZM文件并以List<Map>方式返回
     * 适用交易：对账文件下载后对文件的查看
     * @param filePath ZM文件全路径
     * @return 包含每一笔交易中 序列号 和 值 的map序列
     */
    public static List<Map<Integer, String>> parseZMFile(String filePath){
        int lengthArray[] = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,131};
        return parseFile(filePath,lengthArray);
    }
    
    /**
     * 功能：解析全渠道商户对账文件中的ZME文件并以List<Map>方式返回
     * 适用交易：对账文件下载后对文件的查看
     * @param filePath ZME文件全路径
     * @return 包含每一笔交易中 序列号 和 值 的map序列
     */
    public static List<Map<Integer, String>> parseZMEFile(String filePath){
        int lengthArray[] = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,131};
        return parseFile(filePath,lengthArray);
    }
    
    /**
     * 功能：解析全渠道商户 ZM,ZME对账文件
     * @param filePath
     * @param lengthArray 参照《全渠道平台接入接口规范 第3部分 文件接口》 全渠道商户对账文件 6.1 ZM文件和6.2 ZME 文件 格式的类型长度组成int型数组
     * @return
     */
     private static List<Map<Integer, String>> parseFile(String filePath,int lengthArray[]){
        List<Map<Integer, String>> ZmDataList = new ArrayList<Map<Integer, String>>();
        try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    //解析的结果MAP，key为对账文件列序号，value为解析的值
                    Map<Integer,String> ZmDataMap = new LinkedHashMap<Integer,String>();
                    //左侧游标
                    int leftIndex = 0;
                    //右侧游标
                    int rightIndex = 0;
                    for(int i=0;i<lengthArray.length;i++){
                        rightIndex = leftIndex + lengthArray[i];
                        String filed = lineTxt.substring(leftIndex,rightIndex);
                        leftIndex = rightIndex+1;
                        ZmDataMap.put(i, filed);
                    }
                    ZmDataList.add(ZmDataMap);
                }
                read.close();
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        for(int i=0;i<ZmDataList.size();i++){
            System.out.println("行数: "+ (i+1));
            Map<Integer,String> ZmDataMapTmp = ZmDataList.get(i);
            
            for(Iterator<Integer> it = ZmDataMapTmp.keySet().iterator();it.hasNext();){
                Integer key = it.next();
                String value = ZmDataMapTmp.get(key);
                System.out.println("序号："+ key + " 值: '"+ value +"'");
            }
        }
        return ZmDataList;  
    }
}
