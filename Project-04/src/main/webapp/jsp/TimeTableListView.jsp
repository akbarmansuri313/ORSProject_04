<%@page import="java.text.SimpleDateFormat"%>
<%@page import="in.co.rays.util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.rays.bean.TimetableBean"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.util.DataUtility"%>
<%@page import="in.co.rays.util.ServletUtility"%>
<%@page import="in.co.rays.controller.TimeTableListCtl"%>
<%@page import="in.co.rays.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>

<title>Timetable List</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />

</head>
<body>

<%@include file="Header.jsp"%>
	<div align="center">
		<h1 align="center" style="margin-bottom: -15; color: navy;">Timetable
			List</h1>

		<div style="height: 15px; margin-bottom: 12px">
			<h3>
				<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
			</h3>
			<h3>
				<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
			</h3>
		</div>
		<jsp:useBean id="bean" class="in.co.rays.bean.TimetableBean"
			scope="request"></jsp:useBean>

		<form action="<%=ORSView.TIME_TABLE_LIST_CTL%>" method="post">
			<%
				int pageNo = ServletUtility.getPageNo(request);
			
				int pageSize = ServletUtility.getPageSize(request);
				
				int index = ((pageNo - 1) * pageSize) + 1;
				
				int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

				List<TimetableBean> courseList = (List<TimetableBean>) request.getAttribute("courseList");
				List<TimetableBean> subjectList = (List<TimetableBean>) request.getAttribute("subjectList");

				List<TimetableBean> list = (List<TimetableBean>) ServletUtility.getList(request);
				Iterator<TimetableBean> it = list.iterator();

				if (list.size() != 0) {
			%>
			<input type="hidden" name="pageNo" value="<%=pageNo%>"><input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<table style="width: 100%">
				<tr>
					<td align="right"><label><b>Course Name :</b></label> <%=HTMLUtility.getList("courseId", String.valueOf(bean.getCourseId()), courseList)%>&emsp;
						<label><b>Subject Name :</b></label> <%=HTMLUtility.getList("subjectId", String.valueOf(bean.getSubjectId()), subjectList)%>&emsp;
						<label><b>Exam Date :</b></label></td>
					<td align="left"><label class="input-group"> <span
							class="input-group-addon"> <span
								class="glyphicon glyphicon-calendar"></span>
						</span> <span data-datepicker date-format="MM/dd/yyyy" date-typer="true">
								<input type="text" name="examDate" id="udate"
								placeholder="Select Date of Exam"
								value="<%=DataUtility.getDateString(bean.getExamDate())%>">
					</span>
				</label>&emsp; <input type="submit" name="operation"
					value="<%=TimeTableListCtl.OP_SEARCH%>">&nbsp; <input
						type="submit" name="operation"
						value="<%=TimeTableListCtl.OP_RESET%>"></td>
				</tr>
			</table>
			<br>

			<table border="1" style="width: 100%; border: groove;">
				<tr style="background-color: #e1e6f1e3;">
					<th style="width: 5%;"><input type="checkbox" id="selectall" /></th>
					<th style="width: 4%;">S.No</th>
					<th style="width: 15%;">Course Name</th>
					<th style="width: 19%;">Subject Name</th>
					<th style="width: 14%;">Semester</th>
					<th style="width: 10%;">Exam Date</th>
					<th style="width: 10%;">Exam Time</th>
					<th style="width: 19%;">Description</th>
					<th style="width: 9%;">Edit</th>
				</tr>

				<%
					while (it.hasNext()) {
							bean = it.next();
				%>
				<tr>
					<td style="text-align: center;"><input type="checkbox"
						class="case" name="ids" value="<%=bean.getId()%>"></td>
					<td style="text-align: center;"><%=index++%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getCourseName()%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getSubjectName()%></td>
					<td style="text-align: center;"><%=bean.getSemester()%></td>
					<%
						SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
								String date = sdf.format(bean.getExamDate());
					%>
					<td style="text-align: center;"><%=date%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getExamTime()%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getDescription()%></td>
					<td style="text-align: center;"><a
						href="TimeTableCtl?id=<%=bean.getId()%>">Edit</a></td>
				</tr>
				<%
					}
				%>
			</table>
			<table style="width: 100%">
				<tr>

					<td style="width: 25%"><input type="submit" name="operation"
						value="<%=TimeTableListCtl.OP_PREVIOUS%>"
						<%=pageNo == 1 ? "disabled" : "" %>></td>
					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=TimeTableListCtl.OP_NEW%>"></td>
					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=TimeTableListCtl.OP_DELETE%>"></td>
					<td style="width: 25%" align="right"><input type="submit"
						name="operation" value="<%=TimeTableListCtl.OP_NEXT%>"
						<%=(nextPageSize != 0) ? "" : "disabled"%>></td>

				</tr>

			</table>
			<%
				}
				if (list.size() == 0) {
			%>
			<table>
				<tr>
					<td align="right"><input type="submit" name="operation"
						value="<%=TimeTableListCtl.OP_BACK%>"></td>
				</tr>
			</table>
			<%
				}
			%>

		</form>
		 </br>
        </br>
        </br>
	</div>

<%@ include file="Footer.jsp" %>
</body>
</html>