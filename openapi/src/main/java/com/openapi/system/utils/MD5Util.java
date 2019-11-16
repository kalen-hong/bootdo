package com.openapi.system.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/* *
 * @Author tomsun28
 * @Description 
 * @Date 20:48 2018/2/27
 */
public class MD5Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(MD5Util.class);

    public static String md5(String content) {
        // 用于加密的字符
        char[] md5String = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            // 使用平台默认的字符集将md5String编码为byte序列,并将结果存储到一个新的byte数组中
            byte[] byteInput = content.getBytes();

            // 信息摘要是安全的单向哈希函数,它接收任意大小的数据,并输出固定长度的哈希值
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            // MessageDigest对象通过使用update方法处理数据,使用指定的byte数组更新摘要
            mdInst.update(byteInput);

            //摘要更新后通过调用digest() 执行哈希计算,获得密文
            byte[] md = mdInst.digest();

            //把密文转换成16进制的字符串形式
            int j = md.length;
            char[] str = new char[j*2];
            int k = 0;
            for (int i=0;i<j;i++) {
                byte byte0 = md[i];
                str[k++] = md5String[byte0 >>> 4 & 0xf];
                str[k++] = md5String[byte0 & 0xf];
            }
            // 返回加密后的字符串
            return new String(str);

        }catch (Exception e) {
            LOGGER.warn(e.getMessage(),e);
            return null;
        }
    }
    
    public static void main(String[] args) {
		String tokenString="eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNokykEKgCAQQNG7zDohnSm128yIghElWFBEd89o-T7_hnnPMIGT4KI2SY1ktSJJXkkvWkVjyYYkxIGgg3pIm3nJha_GXGvjVuLKJX_mHSY9WHR-RDQdxLP8wfc4oHleAAAA__8.3wKNh7otfVmVxvB7JvhC3BwvI4aKmKGfyrPm53IXM8I92fZ2fELxoAPH-Febr8TOOPLiQ4cEFfPM1iTy8cCN4A";
		String timestamp="2019-11-16 14:28:28";
		System.out.println(md5(tokenString+timestamp));

//		String phone = "15013870136";
//        System.out.println(md5(phone));
	}

}
