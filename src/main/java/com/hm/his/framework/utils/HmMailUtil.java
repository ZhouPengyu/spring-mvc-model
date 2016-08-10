package com.hm.his.framework.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;

public class HmMailUtil {

	public static void sendMail(Doctor doctor, Hospital hospital){
		try{
			// 配置发送邮件的环境属性
			final Properties props = new Properties();
			/*
			 * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
			 * mail.user / mail.from
			 */
			// 表示SMTP发送邮件，需要进行身份验证
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", "smtp.exmail.qq.com");
			// 发件人的账号
			props.put("mail.user", "zhoupengyu@huimeitimes.com");
			// 访问SMTP服务时需要提供的密码
			try {
				props.put("mail.password", HmDesUtils.decrypt("q3CNYZR/We5PDjFBDfEaBQ=="));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 构建授权信息，用于进行SMTP进行身份验证
			Authenticator authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// 用户名、密码
					String userName = props.getProperty("mail.user");
					String password = props.getProperty("mail.password");
					return new PasswordAuthentication(userName, password);
				}
			};
			// 使用环境属性和授权信息，创建邮件会话
			Session mailSession = Session.getInstance(props, authenticator);
			// 创建邮件消息
			MimeMessage message = new MimeMessage(mailSession);
			// 设置发件人
			InternetAddress form = new InternetAddress(
					props.getProperty("mail.user"));
			message.setFrom(form);

			// 设置收件人
			InternetAddress to = new InternetAddress("yinhongli@huimeitimes.com");
			message.setRecipient(RecipientType.TO, to);

			// 设置邮件标题
			message.setSubject("用户注册");

			// 设置邮件的内容体
			String[] license = hospital.getOrganizationLicense().split("#HM#");
			String imgUrl = "";
			for (String url : license) {
				if(imgUrl.equals(""))
					imgUrl = "<a href='http://huimei.image.alimmdn.com/idPhoto/"+url+"'>" + url + "</a>";
				else
					imgUrl += "，<a href='http://huimei.image.alimmdn.com/idPhoto/"+url+"'>" + url + "</a>";
			}
			message.setContent("注册账号：" + doctor.getDoctorName() + "<br>账号ID：" + doctor.getDoctorId() + 
					"<br>真实姓名："+doctor.getRealName() + "<br>联系方式："+doctor.getPhone() + 
					"<br>诊所名称：" + hospital.getHospitalName() + "<br>证件信息：" + imgUrl,
					"text/html;charset=UTF-8");

			// 发送邮件
			Transport.send(message);
		}catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
