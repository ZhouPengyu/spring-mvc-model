package com.hm.his.module.weixin.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class HttpsRequestUtil {
	
	private static Logger logger = Logger.getLogger(HttpsRequestUtil.class.getName());
	
	public static JSONObject handleHttpsRequest(String requestUrl, String requestMethod, String outputStream){
		JSONObject jsonObject = null;
		try {
			TrustManager[] tm = { new HmX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			URL url = new URL(requestUrl);
			HttpsURLConnection connection  = (HttpsURLConnection) url.openConnection();
			connection.setSSLSocketFactory(ssf);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			
			//当outputStream不为null时向输出流写数据
			if(null != outputStream){
				OutputStream stream = connection.getOutputStream();
				
				stream.write(outputStream.getBytes("UTF-8"));
				stream.close();
			}
			
			//从输入流读取返回内容
			InputStream inputStream = connection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			
			//释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			connection.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException e) {
			logger.error("连接超时：", e);
		} catch (Exception e) {
			logger.error("https请求异常：", e);
		}
		return jsonObject;
	}

}
