package com.openapi.system.vo;

import java.io.Serializable;

/**
 * @功能名称:
 * @APP_ID: bootdo
 * @包名称: com.openapi.system.vo
 * @Date: 2019-10-24 20:07.
 * @Author: kalen.hong
 * @Copyright（C）: 2014-2019 X-Financial Inc.   All rights reserved.
 * 注意：本内容仅限于小赢科技有限责任公司内部传阅，禁止外泄以及用于其他的商业目的。
 */
public class RequestVo <T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accessToken;

    private String sign;

    private String timestamp;

    private T data;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
