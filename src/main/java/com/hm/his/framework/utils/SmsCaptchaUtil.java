package com.hm.his.framework.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;


public class SmsCaptchaUtil {

	/**
	 * <p>Description:短信随机码<p>
	 * @author ZhouPengyu
	 * @date 2016年3月24日 下午7:21:02
	 */
	public static String smsCaptchaUtil(){
        String captcha = "";
        for (int i = 0; i < 6; i++) {
        	captcha = captcha + (int)(Math.random() * 9);
        }
        return captcha;
    }
	
	/**
	 * <p>Description:短信发送<p>
	 * @author ZhouPengyu
	 * @date 2016年3月24日 下午7:21:02
	 */
	public static Integer sendSms(String phoneNo, String smsDetail){
		Integer result = 0;
		Parser parser = null;
		try{
			HttpURLConnection connection = (HttpURLConnection) new URL("http://si.800617.com:4400/SendSms.aspx?un=bjhmsd-2&pwd=5b247e&mobile="+phoneNo+"&msg="+URLEncoder.encode(smsDetail,"GBK")).openConnection();
			
			parser = new Parser(connection);
			parser.setEncoding("UTF-8");
			NodeList nodes = parser.parse(null);
			for(int i=0, len=nodes.size(); i<len; i++){
				Node node = nodes.elementAt(i);
				String resultStr = node.getText();
				String regex = "(?<=result=)\\S+(?=&)";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(resultStr);
				if (matcher.find()) {
					result =Integer.parseInt(matcher.group());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			parser.reset();
		}
		return result;
    }
	
	public static void main(String[] args) {
		SmsCaptchaUtil.sendSms("15901161012", "【惠每注册】");
	}
}