<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>注册</title>
    
    <script type="text/javascript" src="http://ajax.microsoft.com/ajax/jquery/jquery-1.9.1.min.js"></script>
<%--    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.11.3.js"></script>--%>
    <script>
    	var verifyMark = false;
    	var Global_Path = "<%=request.getContextPath()%>";
    	$(document).ready(function(){
    		$("#registerBtn").click(function(){
    			var password = $("#password").val();
    			var realName = $("#realName").val();
    			var phone = $("#phone").val();
    			var organizationName = $("#organizationName").val();
    			var registerInvitation = $("#registerInvitation").val();
    			if(verifyMark && password && realName && phone && organizationName && registerInvitation){
    				var options = {
	   					userName: $("#userName").val(),
	   					password: $("#password").val(),
	   					realName: $("#realName").val(),
	   					phone: $("#phone").val(),
	   					organizationName: $("#organizationName").val(),
	   					registerInvitation: $("#registerInvitation").val()
	   	   			};
	    			console.log(JSON.stringify(options));
	   	    		$.ajax({
	   	    			url: Global_Path+"/organizationRegister",
	   	    			type: "POST",
	   	    			dataType: "json",
	   	    			data: JSON.stringify(options),
	   	    			xhrFields: {
	   	                    withCredentials: true
	   	                },
	   	    			headers: {
	   	                    "Content-Type": "application/json;charset=UTF-8"
	   	                },
	   	    			success: function (data) {
	   	    				if(data.body.doctorId){
	   	    					alert("注册成功！");
	   	    				}else{
	   	    					alert("请输入正确的密钥！");
	   	    				}
	   	    			},
	   	    			error: function (XMLHttpRequest) {
	   	    				console.log(XMLHttpRequest);
	   	    			}
	   	    		});
    			}else{
    				alert("请输入正确信息！");
    			}
    		});
    	});
    	
    	function verifyUserName(){
    		var userName = $("#userName").val();
    		var options = {
					userName: userName
	   			};
    		if(userName!=null && userName!=''){
    			$.ajax({
   	    			url: Global_Path+"/verifyUserName",
   	    			type: "POST",
   	    			dataType: "json",
   	    			data: JSON.stringify(options),
   	    			xhrFields: {
   	                    withCredentials: true
   	                },
   	    			headers: {
   	                    "Content-Type": "application/json;charset=UTF-8"
   	                },
   	    			success: function (data) {
   	    				if(data.body.status==1){
   	    					verifyMark = true;
   	    				}else{
   	    					verifyMark = false;
   	    				}
   	    			},
   	    			error: function (XMLHttpRequest) {
   	    				console.log(XMLHttpRequest);
   	    			}
   	    		});
    		}
    	}
    </script>
  </head>
  
  <body>
	  <p>用户名称: <input type="text" name="userName" id="userName" onmouseout="verifyUserName()"/></p>
	  <p>用户密码: <input type="text" name="password" id="password"/></p>
	  <p>真实姓名: <input type="text" name="realName" id="realName"/></p>
	  <p>手机号码: <input type="text" name="phone" id="phone"/></p>
	  <p>医院名称: <input type="text" name="organizationName" id="organizationName"/></p>
	  <p>惠每密钥: <input type="text" name="registerInvitation" id="registerInvitation"/></p>
	  <input type="button" value="提交" id="registerBtn"/>
  </body>
</html>
