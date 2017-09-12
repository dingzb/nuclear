<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- 图片上传 -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<link rel="stylesheet"
	href="../plugin/bootstrap-3.3.4-dist/css/bootstrap.css" />
<script type="text/javascript" src="../plugin/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript"
	src="../plugin/bootstrap-3.3.4-dist/js/bootstrap.js"></script>
<script>
	//apmac=00-03-7F-11-24-13&clientmac=AC-B5-7D-52-25-87
	var account = {
		mac : '${param["apmac"]}',
		clientmac : '${param["clientmac"]}'
	};

	//跳转短信登录界面	
	function goSms() {
		if ($('#agree').is(':checked')) {
			window.location.href = "wifi.jsp?apmac=" + account.mac
					+ "&clientmac=" + account.clientmac;
			$("#errmsg").text('');
		} else {
			$("#errmsg").text('还没勾选同意使用说明');
		}
	}

	//跳转微信登录界面
	function goWechat() {
		if ($('#agree').is(':checked')) {
			window.location.href = "wechat.jsp?apmac=" + account.mac
					+ "&clientmac=" + account.clientmac;
		} else {
			$("#errmsg").text('还没勾选同意使用说明');
		}
	}

	$(function() {
		var id;
		var query = location.search.substring(1);
		var values = query.split("?");
		for (var i = 0; i < values.length; i++) {
			var pos = values[i].indexOf('=');
			if (pos == -1)
				continue;
			var ps =  values[i].split('&');
			for(var k=0;k<ps.length;k++){
				var key = ps[k].substring(0,ps[k].indexOf('='));
				var val = ps[k].substring(ps[k].indexOf('=')+1,ps[k].length);
				if(key=='id'){
					id=val;
				}
				if(key=='imgsrc'){
					$('#adv').attr('src','../'+val);
				}
			}
			
		}
		$.ajax({
			type : "POST",
			url : "/wireless/app/authroization/portalMana/getPortalPreview",
			dataType : "json",
			data : {
				id : id,
			},
			success : function(data) {
				if (data.success) {
					info = data.data;
					$("#advUrl").attr("href", info.advURL);
					$("#logoUrl").attr("href", info.logoURL);
					$("#clause")
							.text(info.clause == 'null' ? '无' : info.clause);
					$("#logoDes").text(
							info.logoDes == 'null' ? '无' : info.logoDes
									.substring(0, 14));
					$("#adv").attr("src", "../" + info.advPath);
					$("#logo").attr("src", "../" + info.logoPath);
				} else {
					$("#errmsg").text(data.message);
				}
			}
		});
	});

	function showClause() {
		var show=document.getElementById("read");  
		if(show){  
		   if(show.style.display=='block'){  
		  	 show.style.display='none';  
		   }else{  
		  	 show.style.display='block';  
		   }  
		}  
	}
</script>
<title>protal定制</title>
</head>
<body style="background-color: #EEEEEE">
	<div id="pcView" style="clear:both; margin: 40px 70px 50px 70px;height: 540px;width: 1230px;background-color: #FFFFFF">
		<div style="float:left">
			<img id="adv" style="width: 700px;height:540px;padding: 10px;" src="../resources/image/pc.jpg"> 
		</div>
		<div style="float:left;margin-top: 40px;margin-left: 20px">
			<!-- wifi 标志 -->
			<div style="float:left;margin-top: 120px;margin-bottom:100px;margin-left: 140px">
				<img alt="" src="../resources/image/wifi.png">
			</div>
			<!-- 验证码 -->
			<div style="margin-top: 100px;">
				<div style="float: left; width: 140px;height: 32px;">
					<div id="errImgCode" style="padding-top:5px;width:100%;height:100%;display: none; color: red;">请输入正确格式验证码</div>
				</div>
				<div style="float: left;">
					<input type="text"  disabled class="form-control" placeholder="图文验证码" style="height: 32px;width: 150px;" id="imgCode">
				</div>
				<div style="float: left;margin-left: 4px">
					<img style="cursor: pointer; width: 122px; height: 32px;" alt="看不清" src="../app/wifi/auth/sms/verifyImage">
				</div>
			</div>
			<!-- 电话号码 -->
			<div style="">
				<div style="float: left; margin-top:10px;width: 140px;height: 32px">
					<div id="errPhone" style="padding-left:3px;padding-top:5px;width:100%;height:100%;display: none; color: red;">请输入正确手机号</div>
				</div>
				<div style="float: left;margin-top:10px;">
					<input style="width: 150px; height: 32px;" type="text" disabled class="form-control" id="phoneNumber" placeholder="电话号码">
				</div>
				<div style="float: left;margin-left: 4px;margin-top:10px;">
					<button id="getSmsCodeId" disabled class="btn btn-default">获取短信验证码</button>
				</div>
			</div>
			<!-- 登录 -->
			<div style="">
				<div style="float: left; margin-top:10px;width: 140px;height: 32px">
					<div id="errCodeFormat" style="padding-left:3px;padding-top:5px;width:100%;height:100%;display: none; color: red;">请输入短信验证码</div>
					<div id="errCode" style="padding-top:0px;display: none; color: red;"></div>
				</div>
				<div style="float: left;margin-top:10px;">
					<input type="text" id="smsCode" class="form-control" style="width: 150px; height: 32px;" disabled placeholder="短信验证码">
				</div>
				<div style="float: left;margin-left: 4px;margin-top:10px;margin-bottom: 10px;">
					<button id="sendSmsCodeId"  disabled style="width: 125px; height: 32px;" class="btn btn-default">登录</button>
				</div>
			</div><br/>
			<!-- 认证消息 -->
			<div class="row">
				<div id="wait" style="display: none;margin-left: 220px">
					<img style="width: 17px;margin-bottom:5px;" alt="" src="../resources/image/wait.gif">&nbsp;正在认证，请等待。
				</div>&nbsp;
				<a style="visibility: hidden;margin-top:5px;margin-left: 180px" id="go" href="https://www.baidu.com/">注册成功，开始访问网络！</a>
			</div>
		</div>
	</div>
</body>
</html>