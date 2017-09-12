<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="${root}/plugin/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript">
	function init() {
		$('#init').attr('disabled','disabled');
		$.ajax({
			type : "POST",
			url : '${root}/app/init/init',
			dataType : "json",
			success : function(data) {
				$('#msg').text(data.message);
				$('#init').removeAttr('disabled');
			}
		});
		getProcess();
	}
	function logout() {
		$.ajax({
			type : "POST",
			url : '${root}/app/logout',
			dataType : "json",
			success : function(data) {
				if (data.success) {
					window.location = '${root}';
				} else {
					$('#msg').text(data.message);
				}
			}
		});
	}
	
	function getProcess(){
		$.ajax({
			type : "POST",
			url : '${root}/app/init/process',
			dataType : "json",
			success : function(data) {
				if (data.success) {
					console.info(data);
					if(data.data){
						if (data.data['value'] < 100 && data.data['value'] > -1 ) {
							$('#msg').text(data.data['name']?data.data['name']:'');
							window.setTimeout(getProcess,100);
						}
					}
				} else {
					$('#msg').text(data.message);
				}
			}
		});
	}
</script>
<title>初始化系统</title>
</head>
<body>
	<button id="init" onclick="init()">初始化系统</button>
	<label id="msg"></label>
	<br />
	<button onclick="logout()">退出系统</button>
	<br />
</body>
</html>