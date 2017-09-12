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
	function goSms(){
		if($('#agree').is(':checked')){
			window.location.href="wifi.jsp?apmac="+account.mac+"&clientmac="+account.clientmac;
		}else{
			$("#errmsg").text('还没勾选同意使用说明');
		}
	}
	
	//跳转微信登录界面
	function goWechat(){
		if($('#agree').is(':checked')){
			window.location.href="wechat.jsp?apmac="+account.mac+"&clientmac="+account.clientmac;
		}else{
			$("#errmsg").text('还没勾选同意使用说明');
		}
	}

	/*apmac=00-03-7F-11-24-13*/
	$(function() {
		$.ajax({
			type : "POST",
			url : "/wireless/app/authroization/portalMana/getPortalConf",
			dataType : "json",
			data : {
				apmac:'${param["apmac"]}',
			},
			success : function(data) {
				if (data.success) {
					info =data.data;
					$("#logoUrl").attr("href",info.logoURL);
					$("#clause").text(info.clause=='null'?'无':info.clause);
					$("#logoDes").text(info.logoDes=='null'?'无':info.logoDes.substring(0,14));
					$("#adv").attr("src","../"+info.advPath);
					$("#advTmp").attr("src","../"+info.advTmpPath);
					$("#logo").attr("src","../"+info.logoPath);
					if(info.authStrategyType=='all'){
						$('#sms').css('display', 'block');
						$('#weixin').css('display', 'block');
					}
					if(info.authStrategyType=='weixin'){
						$('#weixin').css('display', 'block');
					}
					if(info.authStrategyType=='sms'){
						$('#sms').css('display', 'block');
					}
				} else {
					$("#errmsg").text(data.message);
				}
			}
		});
	});
	
</script>
<title>protal定制</title>
</head>
<body>
    <div class="panel panel-primary" style="margin-bottom: 0px; overflow-x:hidden">
        <div class="panel-heading">
            <span style="font-size: medium;">欢迎访问免费wifi</span>
            <span class="pull-right" style="font-size: medium;"></span>
        </div>
        <div class="panel-body" style="padding:0px">
            <div class="row">
                <div class="img-responsive" style="text-align: center;">
                    <a id="advUrl" href="#">
                        <img id="adv"  style="height:200px; width:100%;" src="">
                        <img id="advTmp"  style="height:200px; width:100%;" src="">
                    </a>
                </div>
            </div>
            <div class="container" style="padding-top: 20px;margin-left: auto;margin-right: auto;width: 1000px">
                <div class="row">
                    <div class="col-sm-4 col-sm-offset-2 col-md-6 col-md-offset-3" style="text-align: left; margin:0 0 15px 0px; vertical-align: middle;">
                        <p>登陆免费WiFi，畅想网络。</p>
                        <input id="agree" type="checkbox" checked style="margin-top: 10px">
                        <label for="agree" class="">我同意：</label>
                        <a href="#"><span>《WiFi使用协议》</span></a><br/>
                        <span id="errmsg" style="font-size: 12px;color:red;text-align:left;"></span>
                        <p id="clause" style="text-align:left;text-indent:2em;"></p>
                    </div>
                </div>
                <div class="row" style="margin-bottom: 15px;">
                    <div class="col-sm-4 col-md-4" style="text-align: center;vertical-align: middle;">
                        <div id="sms" class="col-sm-4 col-md-4" style="float:left;display: none;">
                        	<img style="width: 50px;"  onclick="goSms()" alt="短信认证" src="../resources/image/sms.gif">
                        	<span style="margin-left: 5px;display: block;" >短信认证</span>
                        </div>
                        <div id="weixin" class="col-sm-4 col-md-4" style="float:left;display: none;">
	                        <img style="width: 50px;margin-left: 20px;" onclick="goWechat()" alt="微信认证" src="../resources/image/wechat.png">
	                        <span style="margin-left: 15px;display: block;">微信认证</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="panel-footer" style="padding: 0 15px;">
            <div class="row" style="text-align: center;">
                <a id="logoUrl" href="#">
                    <img id="logo" alt="" style="width: 50px;" src="">
                </a>
                <span id="logoDes"></span>
            </div>
        </div>
    </div>
</body>
</html>