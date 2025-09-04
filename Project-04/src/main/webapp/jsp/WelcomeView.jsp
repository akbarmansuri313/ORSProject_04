<%@page import="in.co.rays.bean.RoleBean"%>
<%@page import="in.co.rays.bean.UserBean"%>
<%@page import="in.co.rays.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Welcome To ORS</title>
</head>
<body>

	<form action="<%=ORSView.WELCOME_CTL%>">
		<%@ include file="Header.jsp"%>

		<br> <br> <br>

		<h1 align="center">
			<font size="10px" color="navy">Welcome To ORS</font>

		</h1>

		<%
			UserBean Ubean = (UserBean) session.getAttribute("user");

			if (Ubean != null) {
				if (Ubean.getRoleId() == RoleBean.STUDENT) {	
		%>
		<h2 align="Center">
			<a style="color: maroon" href="#">Click here to see your MarkSheet </a>
		</h2>

		<%
			}
			}
		%>

	</form>
	
	<%@ include file="Footer.jsp" %>

</body>
</html>