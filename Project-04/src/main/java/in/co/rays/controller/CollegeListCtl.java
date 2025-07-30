package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.model.CollegeModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

@WebServlet("/CollegeListCtl")
public class CollegeListCtl extends BaseClt {

	@Override
	protected void preload(HttpServletRequest request) {

		CollegeModel collegeModel = new CollegeModel();

		List collegeList;
		try {
			collegeList = collegeModel.list();

		request.setAttribute("collegeList", collegeList);

		} catch (ApplicationException e) {

			e.printStackTrace();
		}

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		CollegeBean bean = new CollegeBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setName(DataUtility.getString(request.getParameter("name")));

		bean.setCity(DataUtility.getString(request.getParameter("city")));

		return bean;

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int pageNo = 1;

		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		CollegeBean bean = (CollegeBean) populateBean(request);

		CollegeModel model = new CollegeModel();

		try {
			List<CollegeBean> list = model.search(bean, pageNo, pageSize);
				
				List<CollegeBean> next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {

				ServletUtility.setErrorMessage("Record Not found", request);

			}

			ServletUtility.setBean(bean, request);

			ServletUtility.setPageNo(pageNo, request);

			ServletUtility.setPageSize(pageSize, request);

			ServletUtility.setList(list, request);

			request.setAttribute("nextListSize", request);

			ServletUtility.forward(getView(), request, response);

			} catch (ApplicationException e) {
				
				e.printStackTrace();
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

		CollegeBean bean = (CollegeBean) populateBean(request);

		CollegeModel model = new CollegeModel();

		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

			if (OP_SEARCH.equalsIgnoreCase(op)) {

				pageNo = 1;

			} else if (OP_NEXT.equalsIgnoreCase(op)) {

				pageNo++;

			} else if (OP_PREVIOUS.equalsIgnoreCase(op)) {

				pageNo--;

			}

		} else if (OP_NEW.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);

			return;

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			pageNo = 1;

			if (ids != null && ids.length > 0) {

				CollegeBean deleteBean = new CollegeBean();

				for (String id : ids) {

					deleteBean.setId(DataUtility.getInt(id));

					try {
						model.delete(deleteBean);
					} catch (ApplicationException e) {

						e.printStackTrace();
					}

					ServletUtility.setSuccessMessage("Data Delete Succesfully", request);

				}

			} else {
				ServletUtility.setSuccessMessage("Select At least one record", request);
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);

			return;
		} else if (OP_BACK.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);

			return;

		}

		try {			
				list = model.search(bean, pageNo, pageSize);
				
				next = model.search(bean, pageNo + 1, pageSize);

			if (!OP_DELETE.equalsIgnoreCase(op)) {

				if (list == null || list.size() > 0) {

					ServletUtility.setErrorMessage("Record not Found", request);
				}
			}

			ServletUtility.setList(list, request);

			ServletUtility.setBean(bean, request);

			ServletUtility.setPageNo(pageNo, request);

			ServletUtility.setPageSize(pageSize, request);

			request.setAttribute("nextListSize", request);

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}

	}

	@Override
	protected String getView() {

		return ORSView.COLLEGE_LIST_VIEW;
	}

}
