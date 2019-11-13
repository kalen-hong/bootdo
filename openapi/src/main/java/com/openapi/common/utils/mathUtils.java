package com.openapi.common.utils;

import java.util.regex.Pattern;

/**
 * @功能名称:
 * @APP_ID: bootdo
 * @包名称: com.openapi.common.utils
 * @Date: 2019-10-29 20:55.
 * @Author: kalen.hong
 * @Copyright（C）: 2014-2019 X-Financial Inc.   All rights reserved.
 * 注意：本内容仅限于小赢科技有限责任公司内部传阅，禁止外泄以及用于其他的商业目的。
 */
public class mathUtils {

    public  static int maxString(String s){
        String regEx = "[^0-9]+";
        Pattern pattern = Pattern.compile(regEx);
        String[] cs = pattern.split(s);
        int max = 0;
        int i = 0;
        for (i = 0; i < cs.length; i++) {

            if (!"".equals(cs[i])  && Integer.valueOf(cs[i]) > max) {
                max = Integer.valueOf(cs[i]);
            }


        }
        return max;
    }
}
