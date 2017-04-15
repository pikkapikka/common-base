/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: QRCodeUtil.java
 * Author:   qxf
 * Date:     2016年11月23日 上午9:28:32
 */
package com.smeyun.payment.wechatpay.util;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.smeyun.platform.util.common.constant.PlatformConstant;

/**
 * 二维码生成工具类
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class QRCodeUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeUtil.class);
    
    private static final String CHARSET = PlatformConstant.CHARSET_UTF8;
    
    private static final String FORMAT_NAME = "gif";
    
    // 二维码尺寸  
    private static final int QRCODE_SIZE = 300;//300;//1417;//300;
    
    // LOGO宽度  
    private static final int WIDTH = 400;//60;//280;//60;  
    
    // LOGO高度 
    private static final int HEIGHT = 400;//60;//280;//60;  
    
    private static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception
    {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE,
                QRCODE_SIZE,
                QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath))
        {
            return image;
        }
        
        // 插入图片  
        QRCodeUtil.insertImage(image, imgPath, needCompress);
        return image;
    }
    
    /** 
     * 插入LOGO 
     *  
     * @param source 二维码图片 
     * @param imgPath LOGO图片地址 
     * @param needCompress 是否压缩 
     * @throws Exception 
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception
    {
        File file = new File(imgPath);
        if (!file.exists())
        {
            LOGGER.error("file ({}) is not exist.", imgPath);
            return;
        }
        
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress)
        { // 压缩LOGO  
            if (width > WIDTH)
            {
                width = WIDTH;
            }
            if (height > HEIGHT)
            {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
            g.dispose();
            src = image;
        }
        // 插入LOGO  
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }
    
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 内容 
     * @param logoImgPath LOGO地址 
     * @param destPath 存放目录
     * @param needCompress 是否压缩LOGO 
     * @throws Exception 
     */
    public static void buildQrCodeWithLogo(String content, String logoImgPath, String destPath, boolean needCompress)
            throws Exception
    {
        BufferedImage image = createImage(content, logoImgPath, needCompress);
        mkdirs(destPath);
        String file = new Random().nextInt(99999999) + ".gif";
        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
    }
    
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 内容 
     * @param logoImgPath LOGO地址 
     * @param destPath 存放目录
     * @param needCompress 是否压缩LOGO 
     * @throws Exception 
     */
    public static void buildQrCodeWithLogo(String content, String logoImgPath, String destPath, String fileName,
            boolean needCompress) throws Exception
    {
        BufferedImage image = createImage(content, logoImgPath, needCompress);
        mkdirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath + File.separator + fileName));
    }
    
    /** 
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常) 
     * @author lanyuan 
     * Email: mmm333zzz520@163.com 
     * @date 2013-12-11 上午10:16:36 
     * @param destPath 存放目录 
     */
    public static void mkdirs(String destPath)
    {
        File file = new File(destPath);
        //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)  
        if (!file.exists() && !file.isDirectory())
        {
            file.mkdirs();
        }
    }
    
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 
     *            内容 
     * @param imgPath 
     *            LOGO地址 
     * @param destPath 
     *            存储地址 
     * @throws Exception 
     */
    public static void encode(String content, String imgPath, String destPath) throws Exception
    {
        buildQrCodeWithLogo(content, imgPath, destPath, false);
    }
    
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param destPath 
     *            存储地址 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */
    public static void encode(String content, String destPath, boolean needCompress) throws Exception
    {
        buildQrCodeWithLogo(content, null, destPath, needCompress);
    }
    
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param destPath 
     *            存储地址 
     * @throws Exception 
     */
    public static void encode(String content, String destPath) throws Exception
    {
        buildQrCodeWithLogo(content, null, destPath, false);
    }
    
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 
     *            内容 
     * @param imgPath 
     *            LOGO地址 
     * @param output 
     *            输出流 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */
    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
            throws Exception
    {
        BufferedImage image = createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }
    
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param output 
     *            输出流 
     * @throws Exception 
     */
    public static void encode(String content, OutputStream output) throws Exception
    {
        encode(content, null, output, false);
    }
    
    /** 
     * 解析二维码 
     *  
     * @param file 二维码图片 
     * @return 
     * @throws Exception 
     */
    public static String decode(File file) throws Exception
    {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null)
        {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }
    
    /** 
     * 解析二维码 
     *  
     * @param path 二维码图片地址 
     * @return 
     * @throws Exception 
     */
    public static String decode(String path) throws Exception
    {
        return decode(new File(path));
    }
    
    public static void main(String[] args) throws Exception
    {
        
        /*
         * 将url打上logo标示
         */
        //int money = (int)0.01 * 100;
        String text = "weixin://wxpay/bizpayurl?pr=bkiTaMT";
        //String text = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx421885c56f8e0a75&redirect_uri=https://test.haomeng.com/pay/test.html&response_type=code&scope=snsapi_base&state=" + money + "#wechat_redirect";  
        QRCodeUtil.buildQrCodeWithLogo(text, "", "D:\\", "test.jpg", true);
        
        //    	/*
        //    	 * ￥3600元的学费
        //    	 */
        //    	int money = 3600 * 100;
        //    	String text = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx421885c56f8e0a75&redirect_uri=https://test.haomeng.com/pay/wxPayStart.html&response_type=code&scope=snsapi_base&state=" + money + "#wechat_redirect";  
        //        QRCodeUtil.buildQrCodeWithLogo(text, "D:\\hm-logo-new.jpg", "D:\\", "hm-3600.jpg", true);
        /* 
        * ￥4180元的学费
        */
        //int money = 4180 * 100;
        //    	String money = "C1";
        //    	String text = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx421885c56f8e0a75&redirect_uri=https://test.haomeng.com/pay/wxPayStart.html&response_type=code&scope=snsapi_base&state=" + money + "#wechat_redirect";  
        //        QRCodeUtil.buildQrCodeWithLogo(text, "D:\\hm-logo-pay.jpg", "D:\\", "hm-c1-4180.jpg", true);
        //        
        //    	/* 
        //         * ￥5180元的学费
        //     	 	*/
        //      	//money = 5180 * 100;
        //        money = "C2";
        //      	text = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx421885c56f8e0a75&redirect_uri=https://test.haomeng.com/pay/wxPayStart.html&response_type=code&scope=snsapi_base&state=" + money + "#wechat_redirect";  
        //        QRCodeUtil.buildQrCodeWithLogo(text, "D:\\hm-logo-pay.jpg", "D:\\", "hm-c2-5180.jpg", true);
        //        
        //        /*
        //         * ￥0.01元的学费
        //         */
        //        money = (int) (0.01 * 100);
        //    	text = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx421885c56f8e0a75&redirect_uri=http://test.haomeng.com/pay/wxPayStart.html&response_type=code&scope=snsapi_base&state=" + money + "#wechat_redirect";  
        //        QRCodeUtil.buildQrCodeWithLogo(text, "D:\\hm-logo-new.jpg", "D:\\", "hm-0.01.jpg", true);
        
    }
    
}