<%@ page language="java" contentType="text/html; charset=UTF-8"
	isELIgnored="false"%>
<%
	String root = request.getServletContext().getContextPath();
	pageContext.setAttribute("root", root);
%>

<%=root%>