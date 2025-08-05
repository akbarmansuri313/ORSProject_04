package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.TimetableBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.CourseModel;
import in.co.rays.model.SubjectModel;
import in.co.rays.model.TimetableModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

@WebServlet(name = "/TimeTableListCtl", urlPatterns = { "/TimeTableListCtl" })
public class TimeTableListCtl extends BaseClt {

	@Override
	protected void preload(HttpServletRequest request) {

		CourseModel couresModel = new CourseModel();

		SubjectModel subjectModel = new SubjectModel();

		try {
			List courseList = couresModel.list();

			request.setAttribute("courseList", courseList);

			List subjectList = subjectModel.list();

			request.setAttribute("subjectList", subjectList);

		} catch (ApplicationException e) {

			e.printStackTrace();
		}

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		TimetableBean bean = new TimetableBean();

		bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));

		bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));

		bean.setExamDate(DataUtility.getDate(request.getParameter("examDate")));

		return bean;

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int pageNo = 1;
		
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		TimetableBean bean = (TimetableBean) populateBean(request);

		TimetableModel model = new TimetableModel();

		try {
			List<TimetableBean> list = model.search(bean, pageNo, pageSize);

			List<TimetableBean> next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {

				ServletUtility.setErrorMessage("No record found", request);
			}

			ServletUtility.setList(list, request);

			ServletUtility.setPageNo(pageNo, request);

			ServletUtility.setPageSize(pageSize, request);

			ServletUtility.setBean(bean, request);

			request.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {

			e.printStackTrace();

			ServletUtility.handleException(e, request, response);

			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List list = null;
		
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));

		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		TimetableBean bean = (TimetableBean) populateBean(request);
		
		TimetableModel model = new TimetableModel();

		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {

					pageNo = 1;

				} else if (OP_NEXT.equalsIgnoreCase(op)) {

					pageNo++;

				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {

					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.TIME_TABLE_CTL, request, response);

				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					TimetableBean deletebean = new TimetableBean();

					for (String id : ids) {

						deletebean.setId(DataUtility.getInt(id));

						model.delete(deletebean);

						ServletUtility.setSuccessMessage("Data is deleted successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}

			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.TIME_TABLE_LIST_CTL, request, response);

				return;

			} else if (OP_BACK.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.TIME_TABLE_LIST_CTL, request, response);

				return;
			}

			list = model.search(bean, pageNo, pageSize);

			next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {

				ServletUtility.setErrorMessage("No record found ", request);
			}

			ServletUtility.setList(list, request);

			ServletUtility.setPageNo(pageNo, request);

			ServletUtility.setPageSize(pageSize, request);

			ServletUtility.setBean(bean, request);

			request.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {

			e.printStackTrace();

			ServletUtility.handleException(e, request, response);

			return;
		}
	}

	@Override
	protected String getView() {

		return ORSView.TIME_TABLE_LIST_VIEW;
	}

}
