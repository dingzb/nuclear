<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统设置</title>
<link rel="stylesheet"
	href="${root}/plugin/bootstrap-3.3.4-dist/css/bootstrap.css" />
<style type="text/css">
label {
	margin: 3px;
}
</style>
</head>
<body>
	<div class="panel panel-default">
		<div class="panel-heading">系统初始化</div>
		<div class="panel-body">
			<div class="row" style="display: none;">
				<div class="col-sm-6 col-sm-offset-3">
					<form action="${root}/app/init/import/stateaction"
						enctype="multipart/form-data" accept-charset="UTF-8" method="post">
						<table class="table table-bordered">
							<thead>
								<tr>
									<td colspan="3">初始化目录</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td><input name="file" type="file" /></td>
									<td><input type="submit" value="初始化"></td>
									<td><label>${param.stateActionMsg}</label>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6 col-sm-offset-3">
					<form action="${root}/app/init/import/admin"
						enctype="multipart/form-data" accept-charset="UTF-8" method="post">
						<table class="table table-bordered">
							<thead>
								<tr>
									<td colspan="3">初始化管理员角色</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td><input name="file" type="file" /></td>
									<td><input type="submit" value="初始化"></td>
									<td><label>${param.adminMsg}</label>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>