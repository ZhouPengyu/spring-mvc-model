/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hm.his;

import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class BaseControllerTest {
	public static final String userGuid = "2360";
	public static final String serialNumber = "3345";

	private static final String PROJECT_URL_DEV = "http://his.huimei.com/api/his/";
	private static final String PROJECT_URL_PREONLINE_TEST = "http://10.117.64.153:8080/his";
	private static final String PROJECT_URL_LOCALHOST = "http://localhost:8080/his/";
	private static final String PROJECT_URL_TANGWW = "http://localhost:8080/his/";

	// private static final String
	// PROJECT_URL_TANGWW="http://localhost:8080/huimeitimes_cdss_remind/";
	// private static final String
	// PROJECT_URL="http://localhost:8080/huimeitimes_cdss_remind/";
	private static final String PROJECT_URL = "http://api.huimeionline.com:8080/cdss2/";
	protected static String TICKET = "SDFGHJKLQWERTYUIOXCVBNMPOIUYTR";

	public static void addHeader(HttpRequestBase method, Map<String, String> paramterStr) {
		for (String key : paramterStr.keySet()) {
			System.out.println(paramterStr.get(key) + "");
			method.addHeader(key, paramterStr.get(key) + "");
		}
	}

	public static void login( HttpClient httpClient) {
		Map<String, String> params = Maps.newHashMap();
//		params.put("doctorName", "惠每");
//		params.put("password", "413f98f448a05365873eae33e54efc58");
//		params.put("doctorName", "gyp");
//		params.put("password", "96e79218965eb72c92a549dd5a330112");
//		params.put("doctorName", "csx");
//		params.put("password", "96e79218965eb72c92a549dd5a330112");
		params.put("doctorName", "supplier1");
		params.put("password", "96e79218965eb72c92a549dd5a330112");

//		params.put("doctorName", "tangwwk");
//		params.put("password", "a16e4191b22e1d5975a83358d5400618");

		params.put("loginStatus", "1");
		HttpPost loginPost = new HttpPost(PROJECT_URL_LOCALHOST + "/login/login");
		loginPost.setHeader("Content-Type", "application/json; charset=UTF-8");
		loginPost.setEntity(new StringEntity(JSON.toJSONString(params), Charset.forName("UTF-8")));
		try {
			HttpResponse loginResponse = httpClient.execute(loginPost);// 登录
			String loginRes = EntityUtils.toString(loginResponse.getEntity(), Charset.forName("UTF-8"));
			System.out.println("登录 res  =====================" + loginRes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void testRole(String string, String data) throws IOException {
		long beginTime = System.currentTimeMillis();// 1、开始时间
		String url = PROJECT_URL_LOCALHOST + string;
		System.out.println("request url:" + url);
		System.out.println("request data:" + data);

		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
		// 先登录
		login(httpClient);

//		HttpClient httpClient = new DefaultHttpClient();
//		httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
		HttpPost post = new HttpPost(url);
		try {
			String token = new BASE64Encoder().encode(("HMBasic" + " :" + TICKET).getBytes());
			Map<String, String> paramterStr = new HashMap<>();
			paramterStr.put("Content-Type", "application/json;charset=UTF-8");
			paramterStr.put("Authorization", token);
			addHeader(post, paramterStr);
			post.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
			HttpResponse response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			System.out.println("response data:" + EntityUtils.toString(entity));
			long endTime = System.currentTimeMillis();// 2、结束时间
			long consumeTime = endTime - beginTime;// 3、消耗的时间
			System.out.println(String.format("%s consume %d millis", string, consumeTime));
		} catch (IOException e) {
			throw e;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

	}

	public static void testRole(String string, Object obj) {
		long beginTime = System.currentTimeMillis();// 1、开始时间
		String data = JSON.toJSONString(obj);
		String url = PROJECT_URL_LOCALHOST + string;
		System.out.println("request url:" + url);
		System.out.println("request data:" + data);

		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
		// 先登录
//		login( httpClient);

//		httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
		HttpPost post = new HttpPost(url);
		try {
			String token = new BASE64Encoder().encode(("HMBasic" + " :" + TICKET).getBytes());
			Map<String, String> paramterStr = new HashMap<>();
			paramterStr.put("Content-Type", "application/json;charset=UTF-8");
			paramterStr.put("Authorization", token);
			addHeader(post, paramterStr);
			post.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
			HttpResponse response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			System.out.println("response data:" + EntityUtils.toString(entity));
			long endTime = System.currentTimeMillis();// 2、结束时间
			long consumeTime = endTime - beginTime;// 3、消耗的时间
			System.out.println(String.format("%s consume %d millis", string, consumeTime));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

	}
}
