<%@page import="in.co.rays.controller.LoginCtl"%>
<%@page import="in.co.rays.util.DataUtility"%>
<%@page import="in.co.rays.util.ServletUtility"%>
<%@page import="in.co.rays.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<form action="<%=ORSView.LOGIN_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.bean.UserBean"
			scope="request"></jsp:useBean>


		<div align="center">

			<h1 align="center" style="margin-bottom: -15; color: navy;">User
				Login</h1>

			<div style="height: 15px; margin-bottom: 12px">
				<H3 align="center">


					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%></font>

				</H3>

				<H3 align="center">
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
					</font>
				</H3>

				<table>

					<tr>
					<tr>
						<th align="left">Login Id<span style="color: red">*</span></th>
						<td><input type="text" name="login"
							placeholder="Enter Email id"
							value="<%=DataUtility.getStringData(bean.getLogin())%>"></td>
						<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("login", request)%></font></td>
					</tr>

					<tr>
						<th align="left">Password<span style="color: red">*</span></th>
						<td><input type="password" name="password"
							placeholder="Enter Password"
							value="<%=DataUtility.getStringData(bean.getPassword())%>"></td>
						<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("password", request)%></font></td>
					</tr>



					<th></th>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value=<%=LoginCtl.OP_SIGN_IN%>>&nbsp; <input
						type="submit" name="operation" value="<%=LoginCtl.OP_SIGN_UP%>">
						&nbsp;</td>
				</table>
	</form>



</body>
</html>