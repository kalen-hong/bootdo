package com.bootdo.system.utils;

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
		String tokenString="eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNosytEKgzAMQNF_ybMBa2Lb-De2plAH2zDdEMb-XQUf7-H-YG0VJlhiGWgOGUmyQ04cUUQy9lykDEtUZYIO7JPOuam1M6rZFa-HPtF0--p24dxgcmPomSSQ70D39w3shfz_AAAA__8.zPaABx5D_K8FlrDVAyV4FIX1PH0DgLjX_3-CkTJBlGL05qQ8bl8JYOwYbJwKoBq32CghTXWSWVIuH8mY4Asmag";
		String timestamp="2019-10-07 10:17:12";
		System.out.println(md5(tokenString+timestamp));
	}

}
