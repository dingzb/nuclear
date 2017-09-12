<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- 短信验证 -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<link rel="stylesheet"
	href="../plugin/bootstrap-3.3.4-dist/css/bootstrap.css" />
<script type="text/javascript" src="../plugin/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript"
	src="../plugin/bootstrap-3.3.4-dist/js/bootstrap.js"></script>
<script type="text/javascript">
	/*登陆时传递的参数
	 *apmac=00-03-7F-11-24-13&clientmac=AC-B5-7D-52-25-87
	 */
	var account = {
		mac : '${param["apmac"]}',
		clientmac : '${param["clientmac"]}',
		oneKey : '${param["oneKey"]}'
	};

	/**
	 * 获取短信验证码
	 **/
	function getSmsCode() {
		if (!(/^[a-z,A-Z,0-9]{4}$/.test($('#imgCode').val()))) {
			changeErr('errImgCode', true);
			return;
		}
		if (!(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/
				.test($('#phoneNumber').val()))) {
			changeErr('errPhone', true);
			return;
		}
		$.ajax({
			type : "POST",
			url : "/management/app/wifi/auth/sms/getcode",
			dataType : "json",
			data : {
				phoneNum : $('#phoneNumber').val(),
				imgCode : $('#imgCode').val()
			},
			success : function(data) {
				if (data.success) {
					var time = data.data.effectiveTime;
					var getText = $('#getSmsCodeId').text();
					$('#getSmsCodeId').text(getText + "(" + time + ")");
					$('#getSmsCodeId').addClass('disabled');
					var interval = setInterval(
							function() {
								time -= 1;
								if (time <= 0) {
									$('#getSmsCodeId').text(getText);
									$('#getSmsCodeId').removeClass('disabled')
									clearInterval(interval);
								} else {
									$('#getSmsCodeId').text(
											getText + "(" + time + ")");
								}
							}, 1000);
				} else {
					var msgs = data.message.split(',');
					if (msgs[0] == 0) {
						changeErr('errImgCode', true, msgs[1]);
					} else {
						changeErr('errPhone', true, msgs[1]);
					}
				}
			}
		});
	}

	/**
	 * 验证短信验证码
	 **/
	function verifSmsCode() {
		if (!(/^\d{6}$/.test($('#smsCode').val()))) {
			changeErr('errCodeFormat', true);
		} else {
			$.extend(account, {
				code : $('#smsCode').val()
			});
			$('#wait').css('display', 'block');
			$.ajax({
				type : "POST",
				url : "/management/app/wifi/auth/sms/verif",
				dataType : "json",
				data : account,
				success : function(data) {
					if (data.success) {
						changeErr('errCode', false);
						//$('#go').css('visibility', 'visible');
						window.location.href="https://www.baidu.com";
					} else {
						changeErr('errCode', true, data.message);
					}
					$('#wait').css('display', 'none');
				}
			});
		}
	}
	/**
	 * 显示或关闭错误提示
	 **/
	function changeErr(id, show, msg) {
		if (show) {
			if (msg) {
				$('#' + id).text(msg);
			}
			$('#' + id).css('display', 'block');
		} else {
			$('#' + id).css('display', 'none');
		}
	}
	/**
	 *显示短信认证
	 **/
	function changeVisibility() {
		if ($('#agree').is(':checked')) {
			$('#senSms').addClass('in');
		} else {
			$('#senSms').removeClass('in');
		}
	}
	$(function() {
		if(account.oneKey != "oneKey"){
			document.getElementById("phoneNumber").removeAttribute("readonly");
			document.getElementById("oneKey").style.display = 'none';
			document.getElementById("senSms").style.display = 'block';
			document.getElementById("novalidate").style.display="none";
		}
	});
	//认证上网
	function validateAuthorization(){
		
		if (!(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/
				.test($('#phoneNumber1').val()))) {
			document.getElementById("errPhone1").style.visibility='visible';
			return;
		}
		document.getElementById("wait").style.display = 'block';
		$.ajax({
			type : "POST",
			url : "/management/app/wifi/auth/validateAuthorization",
			dataType : "json",
			data : {
				apmac : account.mac,
				clientmac : account.clientmac,
				phoneNumber : $('#phoneNumber1').val(),
			},
			success : function(data) {
				if (data.success) {
					info = data.data;
					document.getElementById("wait").style.display = 'none';
					if(info.status=='yes'){
						window.location.href="https://www.baidu.com";
					}else{
						document.getElementById('phoneNumber').value=info.phoneNumber;
						document.getElementById("senSms").style.display = 'block';
						document.getElementById("oneKey").style.display = 'none';
					}
				} else {
					changeErr('errCode', true, data.message);
				}
			}
		});
	}
	function hiddenError(){
		document.getElementById("errPhone1").style.visibility='hidden';
	}
</script>
<title>登陆免费Wifi</title>
</head>
<body>
	<div class="panel panel-primary"
		style="height: 100%; margin-bottom: 0px; border: 0px;">
		<div class="panel-heading">
			<h3 class="panel-title">欢迎访问免费WiFi</h3>
		</div>
	</div>

	<div class="container" style="padding-top: 20px;">
		<div class="row">
			<div align="center" style="text-align: center;">
				<img alt="" src="../resources/image/wifi.jpg"
					style="height: 80px; margin-bottom: 15px;">
			</div>
			<!--<div class="col-sm-4"
				style="text-align: left; margin-bottom: 15px; vertical-align: middle;">
				<p>短信验证登陆免费WiFi，畅想网络</p>
				<input id="agree" type="checkbox" onchange="changeVisibility()">我同意：<a
					href="#"><span>《WiFi使用协议》</span></a>
			</div> -->
		</div>
		<div id="senSms" class="in" style="display: none">
			<div class="row">	
				<div class="col-sm-2 col-sm-offset-2"
					style="padding-top: 8px; text-align: center;">
					<span id="novalidate" style="color: blue;">该手机号未认证或认证已过期，请重新认证</span>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2 col-sm-offset-2"
					style="padding-top: 8px; text-align: center;">
					<span id="errImgCode" style="display: none; color: red;">请输入正确格式验证码</span>
				</div>
				<div class="col-sm-2">
					<div class="form-group">
						<label class="sr-only" for="phoneNumber">图文验证码</label> <input
							type="text" class="form-control" id="imgCode" placeholder="图文验证码"
							onfocus="changeErr('errImgCode',false)">
					</div>
				</div>
				<div class="col-sm-2">
					<div style="padding-bottom: 15px;">
						<img style="cursor: pointer; width: 122px; height: 32px;"
							alt="看不清" src="../app/wifi/auth/sms/verifyImage"
							onclick="this.src='../app/wifi/auth/sms/verifyImage?t='+new Date()">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2 col-sm-offset-2"
					style="padding-top: 8px; text-align: right;">
					<span id="errPhone" style="display: none; color: red;">请输入正确手机号</span>
				</div>
				<div class="col-sm-2 ">
					<div class="form-group">
						<label class="sr-only" for="phoneNumber">电话号码</label> <input
							type="text" readonly class="form-control" id="phoneNumber"
							placeholder="电话号码" onfocus="changeErr('errPhone',false)">
					</div>
				</div>
				<div class="col-sm-2">
					<div style="padding-bottom: 15px;">
						<a href="#" id="getSmsCodeId" class="btn btn-default"
							onclick="getSmsCode()" role="button">获取短信验证码</a>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4" style="padding-top: 8px; text-align: right;">
					<span id="errCodeFormat" style="display: none; color: red;">请输入短信验证码</span>
					<span id="errCode" style="display: none; color: red;"></span>
				</div>
				<div class="col-sm-2 ">
					<div class="form-group">
						<label class="sr-only" for="smsCode">短信验证码</label> <input
							type="text" class="form-control" id="smsCode"
							onfocus="changeErr('errCodeFormat',false)" placeholder="短信验证码">
					</div>
				</div>
				<div class="col-sm-2">
					<div style="padding-bottom: 15px;">
						<a href="#" id="sendSmsCodeId" class="btn btn-default"
							onclick="verifSmsCode()" role="button">发送短信验证码</a>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4 col-sm-offset-4" style="text-align: center;">
					<span id="wait" style="display: none;"><img
						style="width: 17px;" alt="" src="../resources/image/wait.gif">&nbsp;正在认证，请等待。</span>&nbsp;
						<!-- <a style="visibility: hidden;" id="go" href="https://www.baidu.com/">注册成功，开始访问网络！</a> -->
				</div>
			</div>
		</div>
		<!-- 认证上网 -->
		<div id="oneKey" style="display: block;">
			<div class="row">
				<div class="col-sm-2 col-sm-offset-2" style="padding-top: 8px; text-align: right;">
					<span id="errPhone1" style="visibility: hidden; color: red;">请输入正确手机号</span>
				</div>
				<div class="col-sm-2 ">
					<div class="form-group">
						<label class="sr-only" for="phoneNumber">电话号码</label> 
						<input type="text" class="form-control" id="phoneNumber1" placeholder="电话号码" onfocus="hiddenError()">
					</div>
				</div>
				<div class="col-sm-2">
					<div style="padding-bottom: 15px;">
						<a href="#" class="btn btn-default" onclick="validateAuthorization()" role="button">开始上网</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>