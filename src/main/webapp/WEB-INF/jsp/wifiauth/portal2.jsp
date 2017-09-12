<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- 图片上传 -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<link rel="stylesheet"
	href="${root}/plugin/bootstrap-3.3.4-dist/css/bootstrap.css" />
<script type="text/javascript"
	src="${root}/plugin/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript"
	src="${root}/plugin/bootstrap-3.3.4-dist/js/bootstrap.js"></script>
<script>
	//apmac=00-03-7F-11-24-13&clientmac=AC-B5-7D-52-25-87
	var account = {
		mac : '${param["apmac"]}',
		clientmac : '${param["clientmac"]}'
	};

	//跳转短信登录界面	
	function goSms() {
		if ($('#agree').is(':checked')) {
			window.location.href = "${root}/wifiauth/wifi.jsp?apmac="
			+ account.mac + "&clientmac=" + account.clientmac+ "&oneKey=" + info.oneKey;
		} else {
			$("#errmsg").text('还没勾选同意使用说明');
		}
	}

	//跳转微信登录界面
	function goWechat() {
		if ($('#agree').is(':checked')) {
			window.location.href = "${root}/wifiauth/wechat.jsp?apmac=" + account.mac
			+ "&clientmac=" + account.clientmac;
		} else {
			$("#errmsg").text('还没勾选同意使用说明');
		}
	}
	$(function() {
		$.ajax({
			type : "POST",
			url : '${root}/app/authroization/portalMana/getPortalConf',
			dataType : "json",
			data : {
				apmac : '${param["apmac"]}',
				clientmac : '${param["clientmac"]}'
			},
			success : function(data) {
				if (data.success) {
					info = data.data;
					$("#logoUrl").attr("href", info.logoURL);
					$("#clause")
							.text(info.clause == 'null' ? '无' : info.clause);
					$("#logoDes").text(
							info.logoDes == 'null' ? '无' : info.logoDes
									.substring(0, 14));
					$("#adv").attr("src", "${root}/" + info.advPath);
					$("#advTmp").attr("src", "${root}/" + info.advTmpPath);
					$("#logo").attr("src", "${root}/" + info.logoPath);
					if (info.authStrategyType == 'all') {
						$('#sms').css('display', 'block');
						$('#weixin').css('display', 'block');
					}
					if (info.authStrategyType == 'weixin') {
						$('#weixin').css('display', 'block');
					}
					if (info.authStrategyType == 'sms') {
						$('#sms').css('display', 'block');
					}
					terminalType(info.oneKey);
				} else {
					$("#errmsg").text(data.message);
				}
			}
		});
	});
	//显示认证条款
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
	//隐藏广告
	function hiden(){
		document.getElementById("advWin").style.display='none';
		document.getElementById("porWin").style.display='block';
	}
	//倒计时
	function closed(){
		var count = 3;
        var countdown = setInterval(CountDown, 1000);
        function CountDown() {
        	$("#btn1").val("跳过  "+count);
            if (count == 0) {
                $("#btn").val("close").removeAttr("disabled");
                clearInterval(countdown);
            }
            count--;
		}
	}
	
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
			url : "${root}/app/wifi/auth/sms/getcode",
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
				url : "${root}/app/wifi/auth/sms/verif",
				dataType : "json",
				data : account,
				success : function(data) {
					if (data.success) {
						changeErr('errCode', false);
						$('#go').css('visibility', 'visible');
						window.location.href="http://www.baidu.com";
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
	
	//校验终端类型  pc || phone
	function terminalType(ss){
		currentLang = navigator.language;   //判断除IE外其他浏览器使用语言
		if(!currentLang){//判断IE浏览器使用语言
		    currentLang = navigator.browserLanguage;
		}
		//判断访问终端
		var browser={
		    versions:function(){
		        var u = navigator.userAgent, app = navigator.appVersion;
		        return {
		            trident: u.indexOf('Trident') > -1, //IE内核
		            presto: u.indexOf('Presto') > -1, //opera内核
		            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
		            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1,//火狐内核
		            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
		            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
		            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
		            iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
		            iPad: u.indexOf('iPad') > -1, //是否iPad
		            webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
		        };
		    }(),
		    language:(navigator.browserLanguage || navigator.language).toLowerCase()
		}
		if(browser.versions.mobile||browser.versions.android||browser.versions.iPhone){
			document.getElementById("advWin").style.display = 'block';
			closed();
			setTimeout(hiden,4000);
		}else{
			if(info.oneKey != "oneKey"){
				document.getElementById("oneKey").style.display = 'none';
				document.getElementById("errorPhone").style.display = 'none';
				document.getElementById("authorization").style.display = 'block';
				document.getElementById("phoneNumber").removeAttribute("readonly");
			}
			document.getElementById("pcView").style.display = 'block';
		}
	}
	
	//一键认证
	function oneKey(type){
		if(type=='mobile'){
			if ($('#agree').is(':checked')) {
			} else {
				$("#errmsg").text('还没勾选同意使用说明');
				return;
			}
		}
		document.getElementById("wait").style.display = 'block';
		$.ajax({
			type : "POST",
			url : "${root}/app/wifi/auth/oneKey",
			dataType : "json",
			data : {
				apmac : account.mac,
				clientmac : account.clientmac
			},
			success : function(data) {
				if (data.success) {
					changeErr('errCode', false);
					$('#go').css('visibility', 'visible');
					document.getElementById("wait").style.display = 'none';
					window.location.href="http://www.baidu.com";
				} else {
					changeErr('errCode', true, data.message);
				}
			}
		});
	}
	
	
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
			url : "${root}/app/wifi/auth/validateAuthorization",
			dataType : "json",
			data : {
				apmac : account.mac,
				clientmac : account.clientmac,
				phoneNumber : $('#phoneNumber1').val()
			},
			success : function(data) {
				if (data.success) {
					info = data.data;
					document.getElementById("wait").style.display = 'none';
					if(info.status=='yes'){
						window.location.href="https://www.baidu.com";
					}else{
						document.getElementById('phoneNumber').value=info.phoneNumber;
						document.getElementById("authorization").style.display = 'block';
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
<style type="text/css">
	.ws-defaultBtn1{
		-moz-border-radius: 5px; /* Gecko browsers */
		-webkit-border-radius: 5px; /* Webkit browsers */
		border-radius: 5px; /* W3C syntax */
		background: rgba(252,252,252, 0.5);
		border: none;/*1px solid #272822;*/
	}
</style>
<title>portal定制</title>
</head>
<body style="overflow-y:hidden;background-color: #EEEEEE">
	<div id="porWin" class="panel panel-primary" style="margin-bottom: 0px; overflow-x: hidden;display:none">
		<div class="panel-heading" style="">
			<span style="font-size: medium;">欢迎访问免费wifi</span> 
			<span class="pull-right" style="font-size: medium;"></span>
		</div>
		<div class="panel-body" style="padding: 0px;">
			<div style="clear: both;">
				<div class="row" style="display: inline-block;position: relative;z-index:99">
					<div class="img-responsive" style="">
						<img id="adv" style="height: 475px; width: 100%;" src=""> 
					</div>
				</div>
				<div class="container" style="position:absolute;z-index: 100;
					left:60px;top:200px;background-color:#EDEDED;display: inline-block; padding-top: 20px;
					width: 240px;height: 200px; ">
					<div class="row">
						<div class="col-sm-4 col-sm-offset-2 col-md-6 col-md-offset-3"
							style="text-align: left; vertical-align: middle;">
							<p>登陆免费WiFi，畅想网络。</p>
							<input id="agree" type="checkbox" checked
								style="margin-top: 10px"> <label for="agree" class="">我同意：</label>
							<a style="cursor:pointer;" onclick="showClause()">
								<span>《WiFi使用协议》</span>
							</a></br> 
							<span id="errmsg" style="font-size: 12px; color: red; text-align: left;"></span>
							<div id="read" style="display: none;">
								<p id="clause" style="text-align: left; text-indent: 2em;"></p>
							</div>	
						</div>
					</div>
					<div class="row" style="margin-left:20px;margin-bottom: 15px;">
						<div id="mobileAuthorization" style="display: block;">
							<div style="text-align: center; vertical-align: middle;">
								<div id="sms" style="float: left; display: none;">
									<img style="width: 20px; margin-top: 15px;cursor: pointer;" onclick="goSms()" alt="短信认证" src="${root}/resources/image/sms.gif"> 
									<span style="margin-top:10px;display: block;">短信认证</span>
								</div>
								<div id="weixin"  style="float: left; display: none;">
									<img style="width: 20px; margin-left: 20px; margin-top: 15px;cursor: pointer;" onclick="goWechat()" alt="微信认证" src="${root}/resources/image/wechat.png"> 
									<span style="margin-top:10px;margin-left: 20px; display: block;font-size: small;">微信认证</span>
								</div>
							</div>
						</div>
						<div id="mobileOneKey" class="col-sm-4 col-md-4" style="text-align: center;display: none;">
							<div style="float: left;margin-top: 25px;">
								<img style="width: 50px; margin-left: 20px;" onclick="oneKey('mobile')" alt="一键认证" src="${root}/resources/image/oneKey.png"> 
								<span style="margin-left: 15px; display: block;">一键认证</span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-footer" style="padding: 0 15px;">
			<div class="row" style="text-align: center;">
				<a id="logoUrl" href="#"> 
				<img id="logo" alt="" style="width: 50px;" src="">
				</a> <span id="logoDes"></span>
			</div>
		</div>
	</div>
	<div id="advWin" class="panel panel-primary" style="margin-bottom: 0px; overflow-x: hidden;display:none">
		<div class="panel-body" style="padding: 0px;">
			<div style="clear: both;">
				<div class="row" style="display: inline-block;position: relative;z-index:99">
					<div  style="">
						<img id="advTmp" style="height: 560px; width: 100%;" src="">
					</div>
				</div>
				<div class="row">
					<input type="button" id="btn1" class="ws-defaultBtn1" style="position:absolute;z-index: 100; right:5px;top:20px;"  onclick="hiden()" value="跳过  4"/>
				</div>
			</div>
		</div>	
	</div>
	<div id="pcView" style="clear:both; margin: 40px 70px 50px 70px;height: 540px;width: 1230px;display:none;background-color: #FFFFFF">
		<div style="float:left">
			<img id="adv" style="width: 700px;height:540px;padding: 10px;" src="${root}/resources/image/pc.jpg"> 
		</div>
		<div style="margin-top: 40px;margin-left: 20px">
			<!-- wifi 标志 -->
			<div style="float:left;margin-top: 120px;margin-left: 140px">
				<img alt="" src="${root}/resources/image/wifi.png">
			</div>
			<div id="authorization" style="display:none;">
				<!-- 电话号码 -->
				<div style="float:left;">
					<div style="float: left;">	
						<div style="margin-top:20px;margin-left:150px; color: blue;">该手机号未认证或认证已过期，请重新认证</div>
					</div>
				</div>	
				<div style="float:left;">
					<div style="float: left; margin-top:20px;width: 140px;height: 32px">
						<div id="errPhone" style="padding-left:3px;padding-top:5px;width:100%;height:100%;display: none; color: red;">请输入正确手机号</div>
					</div>
					<div style="float: left;margin-top:20px;">
						<input readonly="readonly" style="width: 280px; height: 32px;" type="text" class="form-control" id="phoneNumber" placeholder="电话号码" onfocus="changeErr('errPhone',false)">
					</div>
				</div>
				<!-- 验证码 -->
				<div style="margin-top: 10px;float:left;">
					<div style="float: left; width: 140px;height: 32px;">
						<div id="errImgCode" style="padding-top:5px;width:100%;height:100%;display: none; color: red;">请输入正确格式验证码</div>
					</div>
					<div style="float: left;margin-left: 4px">
						<img style="cursor: pointer; width: 70px; height: 32px;" alt="看不清" src="${root}/app/wifi/auth/sms/verifyImage" onclick="this.src='${root}/app/wifi/auth/sms/verifyImage?t='+new Date()">
					</div>
					<div style="float: left;">
						<input type="text"  class="form-control" placeholder="图文验证码" style="margin-left:5px;height: 32px;width: 100px;" id="imgCode" onfocus="changeErr('errImgCode',false)">
					</div>
					<div style="float: left;margin-left: 5px;margin-top:0px;width: 80px;">
						<a href="#" id="getSmsCodeId" class="btn btn-default" onclick="getSmsCode()" role="button">获取验证码</a>
					</div>
				</div>
				<!-- 登录 -->
				<div style="">
					<div style="float: left; margin-top:10px;width: 140px;height: 32px">
						<div id="errCodeFormat" style="padding-left:3px;padding-top:5px;width:100%;height:100%;display: none; color: red;">请输入短信验证码</div>
						<div id="errCode" style="padding-top:0px;display: none; color: red;"></div>
					</div>
					<div style="float: left;margin-top:10px;">
						<input type="text" id="smsCode" class="form-control" style="width: 150px; height: 32px;" onfocus="changeErr('errCodeFormat',false)" placeholder="短信验证码">
					</div>
					<div style="float: left;margin-left: 4px;margin-top:10px;margin-bottom: 10px;">
						<a href="#" id="sendSmsCodeId" style="width: 125px; height: 32px;" class="btn btn-default" onclick="verifSmsCode()" role="button">校验短信验证码</a>
					</div>
				</div><br/>
			</div>
			<!-- 认证上网 -->
			<div id="oneKey" style="display:block;">
				<div style="float: left;margin-top:50px;">
					<div id="errPhone1" style="padding-left:3px;padding-top:5px;width:115px;height:32px;visibility:hidden; color: red;">请输入正确手机号</div>
				</div>
				<div style="float: left;margin-top:50px;margin-left: 20px;">
					<input style="width: 170px; height: 32px;" type="text" class="form-control" id="phoneNumber1" placeholder="电话号码" onfocus="hiddenError()">
				</div>
				<div style="float: left;margin-top:50px;margin-left: 10px;">
					<a href="#"  style="display:block;width: 100px; height: 32px;" class="btn btn-default" onclick="validateAuthorization()" role="button">开始上网</a>
				</div>
			</div>
			<!-- 认证消息 -->
			<div style="float:left;">
				<div id="wait" style="display: none;margin-left: 220px">
					<img style="width: 17px;margin-bottom:5px;" alt="" src="${root}/resources/image/wait.gif">&nbsp;正在认证，请等待。
				</div>&nbsp;
				<a style="visibility: hidden;margin-top:5px;margin-left: 180px" id="go" href="https://www.baidu.com/">注册成功，开始访问网络！</a>
			</div>
		</div>
	</div>
</body>
</html>