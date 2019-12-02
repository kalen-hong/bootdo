package com.openapi.common.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @功能名称:
 * @APP_ID: bootdo
 * @包名称: com.openapi.common.utils
 * @Date: 2019-10-29 20:55.
 * @Author: kalen.hong @Copyright（C）: 2014-2019 X-Financial Inc. All rights
 *          reserved. 注意：本内容仅限于小赢科技有限责任公司内部传阅，禁止外泄以及用于其他的商业目的。
 */
public class MathUtils {

	public static int maxString(String s) {
		String regEx = "[^0-9]+";
		Pattern pattern = Pattern.compile(regEx);
		String[] cs = pattern.split(s);
		int max = 0;
		int i = 0;
		for (i = 0; i < cs.length; i++) {

			if (!"".equals(cs[i]) && Integer.valueOf(cs[i]) > max) {
				max = Integer.valueOf(cs[i]);
			}
		}
		return max;
	}

	public static String getExceedAmount(String messageContent) {
		String regEx = "[^0-9]+";
		Pattern pattern = Pattern.compile(regEx);
		String[] cs = pattern.split(messageContent);
		int i = 0;
		for (i = 0; i < cs.length; i++) {
			if (StringUtils.isEmpty(cs[i])) {
				continue;
			}
			String exceedAmountFlag = "RP." + cs[i];
			if (messageContent.contains(exceedAmountFlag)) {
				return String.valueOf(cs[i]);
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String msg="[LendingAdda] The verification code is 234161. The code is valid within 5 minutes. Don't share it to other people.";

		System.out.println(getExceedAmount(msg));
	}
}
