package com.openapi.testDemo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class ApiTokenTest {
//	@Test
//	public void getToken() {
//		try {
//			String clientId="yinsheng";
//			String clientSecret="ys123!@#";
//			Map<String, String> params=new HashMap<String, String>();
//			params.put("clientId", clientId);
//			params.put("clientSecret", clientSecret);
//			String serverUrl="http://localhost:8888/api/token/getToken";
////			HttpClientUtils.post(serverUrl, JSON.toJSONString(params), 5000);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args) {
		Map<String, Object> dataMap=new HashMap<String, Object>();
		dataMap.put("iphone", java.util.Arrays.asList("15013870136"));
		System.out.println(JSON.toJSONString(dataMap));
	}
}
