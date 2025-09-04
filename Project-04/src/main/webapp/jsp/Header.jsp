<%@page import="in.co.rays.bean.RoleBean"%>
<%@page import="in.co.rays.controller.ORSView"%>
<%@page import="in.co.rays.controller.LoginCtl"%>
<%@page import="in.co.rays.bean.UserBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<!-- Include jQuery -->
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<!-- Include jQuery UI -->
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
<!-- Include jQuery UI CSS -->
<link rel="stylesheet"
	href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<script src="/Project-04/js/checkbox.js"></script>
<script src="/Project-04/js/datepicker.js"></script>
</head>
<body>
		<%
		UserBean userBean = (UserBean) session.getAttribute("user");
		boolean userLoggedIn = userBean != null;
		String welcomeMsg = "Hi, ";
		if (userLoggedIn) {
			String role = (String) session.getAttribute("role");
			welcomeMsg += userBean.getFirstName() + " (" + role + ")";
		} else {
			welcomeMsg += "Guest";
		}
	%>

	<table>
		<tr>
			<th></th>
			<td><a href="<%=ORSView.WELCOME_CTL%>">Welcome</b></a> |
				<%
				if (userLoggedIn) {
			%> <a
				href=" <%=ORSView.LOGIN_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>">Logout</b></a>

				<%
					} else {
				%> <a href="<%=ORSView.LOGIN_CTL%>">Login</b></a> <%
 	}
 %></td>
			<td rowspan="2">
				<h1 align="right">
					<img src="<%=ORSView.APP_CONTEXT%>/img/customLogo.jpg" width="155"
						height="50">
				</h1>
			</td>
		</tr>

		<tr>
			<th></th>
			<td>
				<h3><%=welcomeMsg%></h3>
			</td>
		</tr>


		<%
			if (userLoggedIn) {
		%>

		<tr>
			<td colspan="2">
				<%
					if (userBean.getRoleId() == RoleBean.ADMIN) {
				%> <a href="<%=ORSView.MY_PROFILE_CTL%>">My Profile</a> | <a
				href="<%=ORSView.CHANGE_PASSWORD_CTL%>">Change Password</a> |<a
				href="<%=ORSView.MARKSHEET_CTL%>">Add Marksheet</b></a> | <a
				href="<%=ORSView.MARKSHEET_LIST_CTL%>">Marksheet List</b></a> | <a
				href="<%=ORSView.USER_CTL%>">Add User</b></a> | <a
				href="<%=ORSView.USER_LIST_CTL%>">User List</b></a> | <a
				href="<%=ORSView.COLLEGE_CTL%>">Add College</b></a> | <a
				href="<%=ORSView.COLLEGE_LIST_CTL%>">College List</b></a> | <a
				href="<%=ORSView.ROLE_CTL%>">Add Role</b></a> | <a
				href="<%=ORSView.ROLE_LIST_CTL%>">Role List</b></a> | <a
				href="<%=ORSView.STUDENT_CTL%>">Add Student</b></a> | <a
				href="<%=ORSView.STUDENT_LIST_CTL%>">Student List</b></a> | <a
				href="<%=ORSView.COURSE_CTL%>">Add Course</b></a> | <a
				href="<%=ORSView.COURSE_LIST_CTL%>">Course List</b></a> | <a
				href="<%=ORSView.SUBJECT_CTL%>">Add Subject</b></a> | <a
				href="<%=ORSView.SUBJECT_LIST_CTL%>">Subject List</b></a> | <a
				href="<%=ORSView.FACULTY_CTL%>">Add Faculty</b></a> | <a
				href="<%=ORSView.FACULTY_LIST_CTL%>">Faculty List</b></a> | <a
				href="<%=ORSView.TIME_TABLE_CTL%>">Add TimeTable</b></a> | <a
				href="<%=ORSView.TIME_TABLE_LIST_CTL%>">TimeTable List</b></a> | <a
				href="<%=ORSView.GET_MARKSHEET_CTL%>">GetMarksheet List</a> | <a
				href="<%=ORSView.MARKSHHET_MERIT_LIST_CTL%>">Marksheet MeritList</a>
				| <a href="<%=ORSView.JAVA_DOC%>">Javadoc</a> <%
 	}

 		if (userBean.getRoleId() == RoleBean.STUDENT) {
 %> <a href="<%=ORSView.COLLEGE_LIST_CTL%>">College List</a> | <a
				href="<%=ORSView.STUDENT_LIST_CTL%>">Student List</a> | <a
				href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a> | <a
				href="<%=ORSView.SUBJECT_LIST_CTL%>">Subject List</a> | <a
				href="<%=ORSView.FACULTY_LIST_CTL%>">Faculty List</a> | <a
				href="<%=ORSView.TIME_TABLE_LIST_CTL%>">TimeTable List</a> | <a
				href="<%=ORSView.MARKSHEET_LIST_CTL%>">Marksheet List</a> | <a
				href="<%=ORSView.GET_MARKSHEET_CTL%>">GetMarksheet List</a> <%
 	}
 		if (userBean.getRoleId() == RoleBean.COLLEGE) {
 %> <a href="<%=ORSView.MARKSHEET_CTL%>">Add Marksheet</a> | <a
				href="<%=ORSView.MARKSHEET_LIST_CTL%>">Marksheet List</a> | <a
				href="<%=ORSView.STUDENT_CTL%>">Add Student</a> | <a
				href="<%=ORSView.STUDENT_LIST_CTL%>">Student List</a> | <a
				href="<%=ORSView.FACULTY_LIST_CTL%>">Faculty List</a> | <a
				href="<%=ORSView.TIME_TABLE_LIST_CTL%>">TimeTable List</a> | <a
				href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a> | <a
				href="<%=ORSView.GET_MARKSHEET_CTL%>">GetMarksheet List</a> | <a
				href="<%=ORSView.MARKSHHET_MERIT_LIST_CTL%>">Marksheet MeritList</a>
				<%
					}
						if (userBean.getRoleId() == RoleBean.FACULTY) {
							// System.out.println("======>><><>"+userBean.getRoleId());
				%> <a href="<%=ORSView.MARKSHEET_CTL%>">Add Marksheet</a> | <a
				href="<%=ORSView.MARKSHEET_LIST_CTL%>">Marksheet List</a> | <a
				href="<%=ORSView.COLLEGE_LIST_CTL%>">College List</a> | <a
				href="<%=ORSView.STUDENT_CTL%>">Add Student</a> | <a
				href="<%=ORSView.STUDENT_LIST_CTL%>">Student List</a> | <a
				href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a> | <a
				href="<%=ORSView.SUBJECT_CTL%>">Add Subject</a> | <a
				href="<%=ORSView.SUBJECT_LIST_CTL%>">Subject List</a> | <a
				href="<%=ORSView.TIME_TABLE_CTL%>">Add TimeTable</a> | <a
				href="<%=ORSView.TIME_TABLE_LIST_CTL%>">TimeTable List</a> | <a
				href="<%=ORSView.GET_MARKSHEET_CTL%>">GetMarksheet List</a> | <a
				href="<%=ORSView.MARKSHEET_MERIT_LIST_VIEW%>">Marksheet MeritList</a>
				<%
					}
						if (userBean.getRoleId() == RoleBean.KIOSK) {
				%> <a href="<%=ORSView.COLLEGE_LIST_CTL%>">College List</a> | <a
				href="<%=ORSView.TIME_TABLE_LIST_CTL%>">TimeTable List</a> | <a
				href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a>| <%
 	}
 %>

			</td>

		</tr>
		<%
			}
		%>

	</table>
	<hr>
</body>
</html>