//package com.hm.his.outpatient;
//
//import com.taobao.api.ApiException;
//import com.taobao.api.DefaultTaobaoClient;
//import com.taobao.api.TaobaoClient;
//import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
//import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
//
//public class AliTest {
//
//	public static void main(String[] args) {
//		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23331134", "0bc7cb5427c228374626e666aec924fa");
//		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
//		req.setSmsType("normal");
//		req.setSmsFreeSignName("注册验证");
//		req.setSmsParamString("{\"code\":\"1234\",\"product\":\"alidayu\"}");
//		req.setRecNum("15901161012");
//		req.setSmsTemplateCode("SMS_6405608");
//		AlibabaAliqinFcSmsNumSendResponse rsp = new AlibabaAliqinFcSmsNumSendResponse();
//		try {
//			rsp = client.execute(req, "");
//		} catch (ApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(rsp.getBody());
//	}
//	
//}
