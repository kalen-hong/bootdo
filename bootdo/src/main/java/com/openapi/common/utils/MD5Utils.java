package com.openapi.common.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.Random;

public class MD5Utils {
	private static final String SALT = "1qazxsw2";

	private static final String ALGORITH_NAME = "md5";

	private static final int HASH_ITERATIONS = 2;

	public static String encrypt(String pswd) {
		String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(SALT), HASH_ITERATIONS).toHex();
		return newPassword;
	}

	public static String encrypt(String username, String pswd) {
		String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(username + SALT),
				HASH_ITERATIONS).toHex();
		return newPassword;
	}
	public static void main(String[] args) {
		
		//System.out.println(MD5Utils.encrypt("admin", "1"));
		System.out.println(generatePassword(10));
	}

	//随机生成密码
	public static String generatePassword (int length) {
		// 最终生成的密码
		String password = "";
		Random random = new Random();
		for (int i = 0; i < length; i ++) {
			// 随机生成0或1，用来确定是当前使用数字还是字母 (0则输出数字，1则输出字母)
			int charOrNum = random.nextInt(2);
			if (charOrNum == 1) {
				// 随机生成0或1，用来判断是大写字母还是小写字母 (0则输出小写字母，1则输出大写字母)
				int temp = random.nextInt(2) == 1 ? 65 : 97;
				password += (char) (random.nextInt(26) + temp);
			} else {
				// 生成随机数字
				password += random.nextInt(10);
			}
		}
		return password;
	}
}
