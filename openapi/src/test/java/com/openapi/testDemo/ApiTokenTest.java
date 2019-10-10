package com.openapi.testDemo;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpUtils;

import org.junit.Test;

import com.alibaba.druid.util.HttpClientUtils;
import com.alibaba.fastjson.JSON;

public class ApiTokenTest {
	@Test
	public void getToken() {
		try {
			String clientId="yinsheng";
			String clientSecret="ys123!@#";
			Map<String, String> params=new HashMap<String, String>();
			params.put("clientId", clientId);
			params.put("clientSecret", clientSecret);
			String serverUrl="http://localhost:8888/api/token/getToken";
//			HttpClientUtils.post(serverUrl, JSON.toJSONString(params), 5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
