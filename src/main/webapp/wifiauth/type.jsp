<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- 类型选择 -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<link rel="stylesheet"	href="../plugin/bootstrap-3.3.4-dist/css/bootstrap.css" />
<script type="text/javascript" src="../plugin/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="../plugin/bootstrap-3.3.4-dist/js/bootstrap.js"></script>
<script type="text/javascript">
	//apmac=00-03-7F-11-24-13&clientmac=AC-B5-7D-52-25-87
	var account = {
		mac : '${param["apmac"]}',
		clientmac : '${param["clientmac"]}'
	};

	//跳转短信登录界面	
	function goSms(){
		if($('#agree').is(':checked')){
			window.location.href="wifi.jsp?apmac="+account.mac+"&clientmac="+account.clientmac;
		}else{
			alert('还没勾选同意使用说明');
		}
	}
	
	//跳转微信登录界面
	function goWechat(){
		if($('#agree').is(':checked')){
			window.location.href="wechat.jsp?apmac="+account.mac+"&clientmac="+account.clientmac;
		}else{
			alert('还没勾选同意使用说明');
		}
	}
		
	$(function() {
		console.info(account);
	});
</script>
<title>登陆免费Wifi</title>
</head>
<body>
	<div class="panel panel-primary"
		style="height: 100%; margin-bottom: 0px; border: 0px;">
		<div class="panel-heading">
			<h3 class="panel-title">欢迎使用免费WiFi</h3>
		</div>
	</div>

	<div class="container" style="padding-top: 20px;">
		<div class="row">
			<div class="col-sm-2 col-sm-offset-4" style="text-align: center;">
				<img alt="" src="../resources/image/wifi.jpg"
					style="height: 80px; margin-bottom: 15px;">
			</div>
			<div class="col-sm-4" style="text-align: left; margin-bottom: 15px; vertical-align: middle;">
				<p>登陆免费WiFi，畅想网络。</p>
				<input id="agree" type="checkbox" checked style="margin-top: 10px"> 
				<label for="agree" class="">我同意：</label>
				<a href="#"><span>《WiFi使用协议》</span></a>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-4 col-sm-offset-4" style="text-align: center;">
				<img style="width: 50px;" onclick="goSms()" alt="短信认证" src="../resources/image/sms.gif">
				<img style="width: 50px;" onclick="goWechat()" alt="微信认证" src="../resources/image/wechat.png">
			</div>
		</div>
	</div>
</body>
</html>