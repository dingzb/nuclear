<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- 微信验证 -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<link rel="stylesheet"	href="../plugin/bootstrap-3.3.4-dist/css/bootstrap.css" />
<script type="text/javascript" src="../plugin/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="../plugin/bootstrap-3.3.4-dist/js/bootstrap.js"></script>
<script type="text/javascript" src="../plugin/md5.js"></script>
<!-- <script type="text/javascript" src="https://wifi.weixin.qq.com/resources/js/wechatticket/wechatutil.js" ></script> -->
<script type="text/javascript">
	/*登陆时传递的参数
	 *apmac=00-03-7F-11-24-13&clientmac=AC-B5-7D-52-25-87
	 */
	var account = {
		mac : '${param["apmac"]}',
		clientmac : '${param["clientmac"]}'
	};

	var config=null;

	/**
	 * 微信连Wi-Fi协议3.1供运营商portal呼起微信浏览器使用
	 */
	 var loadIframe = null;
	 var noResponse = null;
	 var callUpTimestamp = 0;
	 function createIframe(){
		 var iframe = document.createElement("iframe");
	     iframe.style.cssText = "display:none;width:0px;height:0px;";
	     document.body.appendChild(iframe);
	     loadIframe = iframe;
	 }
	//注册回调函数
	function jsonpCallback(result){  
		if(result && result.success){
			var ua=navigator.userAgent;              
			if (ua.indexOf("iPhone") != -1 ||ua.indexOf("iPod")!=-1||ua.indexOf("iPad") != -1) {   //iPhone             
				document.location = result.data;
			}else if(ua.indexOf("SAMSUNG") != -1){
				document.location = result.data;
			}else{
				if ('false' == 'true') {
                alert('[强制]该浏览器不支持自动跳转微信请手动打开微信\n如果已跳转请忽略此提示');
                return;
            }
            createIframe();
            callUpTimestamp = new Date().getTime();
            loadIframe.src = result.data;
            noResponse = setTimeout(function() {
                errorJump();
            }, 3000);
			}
		}else if(result && !result.success){
			alert(result.data);
		}
	}

	function Wechat_GotoRedirect(appId, extend, timestamp, sign, shopId, authUrl, mac, ssid, bssid){
		//将回调函数名称带到服务器端
		var url = "https://wifi.weixin.qq.com/operator/callWechatBrowser.xhtml?appId=" + appId + "&extend=" + extend + "&timestamp=" + timestamp + "&sign=" + sign;	
		
		//如果sign后面的参数有值，则是新3.1发起的流程
		if(authUrl && shopId){	
			url = "https://wifi.weixin.qq.com/operator/callWechat.xhtml?appId=" + appId 
						+ "&extend=" + extend + "&timestamp=" + timestamp + "&sign=" + sign
						+ "&shopId=" + shopId+ "&authUrl=" + encodeURIComponent(authUrl)
						+ "&mac=" + mac + "&ssid=" + ssid+ "&bssid=" + bssid;
		}
		//通过dom操作创建script节点实现异步请求  
		var script = document.createElement('script');  
		script.setAttribute('src', url);  
		document.getElementsByTagName('head')[0].appendChild(script);
	}
	
	
	//发送请求到微信验证
	function contect(){
		//发送开发参数到微信并且启动微信
		$("#auth").addClass("disabled");
		$("#auth").text("正在连接，请稍等..");
		var apmac = account.mac.replace(/-/g, ":");
		var mac = account.clientmac.replace(/-/g, ":");

		var appId= config.appID;//开发者凭证里面的固定的 
		var extend =account.clientmac+"_"+account.mac; //回调回来参数 //mac_apmac
		var timestamp=new Date().getTime();//时间戳
		var shopId= config.shopId;
		var authUrl="http://182.92.85.89:8080/wireless/app/wifi/auth/wechat/backfun"; //回调url
		var mac = mac;//手机mac
		var ssid=config.ssid; //ssid
		var bssid=apmac; //apmac
		var secretKey= config.secretKey; //针对ap的密钥 --开发者凭证
		
		// console.info(appId,extend, timestamp,sign,shopId, authUrl, mac, ssid,bssid); 
		// return;

		var sign= hex_md5(appId + extend + timestamp + shopId + authUrl + mac + ssid + bssid + secretKey);
		
		$.ajax({
			type : "POST",
			url : "/wireless/app/wifi/auth/wechat/sendTempPermit",
			dataType : "json",
			data : {
				apMac:account.mac,
				clientMac:account.clientmac
			},
			success : function(data) {
				if (data.success) {
					setTimeout(function(){
						Wechat_GotoRedirect(appId,extend,timestamp,sign,shopId,authUrl,mac,ssid,bssid); 
					},2000);
					//调用呼起微信
				} else {
					$("#errmsg").text(data.message);
				}
			},
			error:function(){
				$("#errmsg").text("上网认证失败");
			}
		});
	}

	function errorJump(){
		var now = new Date().getTime();
	    if ((now - callUpTimestamp) > 4 * 1000) {
	        return;
	    }
	    alert('该浏览器不支持自动跳转微信请手动打开微信\n如果已跳转请忽略此提示');
	}

	$(function() {
		var ua = navigator.userAgent;
		if (ua.indexOf("iPhone") != -1 || ua.indexOf("iPod") != -1 || ua.indexOf("iPad") != -1||ua.indexOf("Linux") != -1 || ua.indexOf("Android") != -1) { 
			//iPhone or android
		} else if(ua.indexOf("Windows") != -1||ua.indexOf("Macintosh") != -1){
			alert("微信连接方式暂时不支持电脑连接！");
			return;
		}else{
			alert("微信连接方式暂时不支持电脑连接！");
			return;
		}

		var mac=account.mac;
		$.ajax({
			type : "POST",
			url : "/wireless/app/wifi/auth/wechat/getWechatConf",
			dataType : "json",
			data : {
				apmac:mac
			},
			success : function(data) {
				//console.info(data);
				if (data.success) {
					config=data.data;
					if(config.ssid==null){
						$("#auth").text("获取信息失败");
						return;
					}
					$("#ssidNm").text(config.ssid);
					$("#auth").removeClass("disabled");
					$("#auth").text("立即连接");
				} else {
					$("#errmsg").text(data.message);
				}
			}
		});
	});
</script>
<title>登陆免费Wifi</title>
</head>
<body>
	<div class="panel panel-primary" style="height: 100%; margin-bottom: 0px; border: 0px;">
		<div class="panel-heading" style="background-color:#40BB02;border-color:#3FBC00;">
			<h3 class="panel-title">微信登录页面</h3>
		</div>
	</div>
	<div class="container" style="padding-top: 20px;">
		<div class="row">
			<div class="col-sm-10 col-sm-offset-1 col-xs-10 col-xs-offset-1" style="text-align: center;">
				<img alt="" src="../resources/image/wifi.jpg"
					style="height: 80px; margin-bottom: 15px;">
			</div>
		</div>
		<div class="row">
			<div class="col-sm-10 col-sm-offset-1 col-xs-10 col-xs-offset-1"
				style="text-align: center; margin-bottom: 15px; vertical-align: middle;">
				<h4>微信连WI-FI</h4>
				<span style="color:#8D8D8F">WI-FI名称：</span><span id="ssidNm"></span>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-10 col-sm-offset-1 col-xs-10 col-xs-offset-1">
				<button id="auth" type="button" class="btn disabled" style="width:100%;background-color:#40BB02;border-color: #3FBC00;color: #fff;" onclick="contect()">
				  正在加载..
				</button>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-10 col-sm-offset-1 col-xs-10 col-xs-offset-1" style="margin-top: 5px;text-align: center;">
				<span style="font-size: 10px;color:#8D8D8F">(温馨提示：微信6.2.5及以上版本支持此连接方式)</span><br/>
				<span id="errmsg" style="font-size: 10px;color:#8D8D8F"></span>
			</div>
		</div>
	</div>
</body>
</html>