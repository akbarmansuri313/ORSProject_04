package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.RoleModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

@WebServlet(name = "/RoleListCtl", urlPatterns = { "/RoleListCtl" })
public class RoleListCtl extends BaseClt {

	@Override
	protected void preload(HttpServletRequest request) {

		RoleModel roleModel = new RoleModel();

		try {
			List roleList = roleModel.list();

			request.setAttribute("roleList", roleList);

		} catch (ApplicationException e) {

			e.printStackTrace();

			return;
		}
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		RoleBean bean = new RoleBean();

		bean.setName(DataUtility.getString(request.getParameter("name")));

		bean.setId(DataUtility.getLong(request.getParameter("roleId")));

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int pageNo = 1;

		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		RoleBean bean = (RoleBean) populateBean(request);

		RoleModel model = new RoleModel();

		try {
			List<RoleBean> list = model.search(bean, pageNo, pageSize);

			List<RoleBean> next = model.search(bean, pageNo + 1, pageSize);

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

			ServletUtility.handleException(e, request, response);

			e.printStackTrace();

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

		RoleBean bean = (RoleBean) populateBean(request);

		RoleModel model = new RoleModel();

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

				ServletUtility.redirect(ORSView.ROLE_CTL, request, response);

				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					RoleBean deletebean = new RoleBean();

					for (String id : ids) {

						deletebean.setId(DataUtility.getInt(id));

						model.delete(bean);

						ServletUtility.setSuccessMessage("Data is deleted successfully", request);

					}
				} else {

					ServletUtility.setErrorMessage("Select at least one record", request);
				}

			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);

				return;

			} else if (OP_BACK.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);

				return;
			}

			list = model.search(bean, pageNo, pageSize);

			next = model.search(bean, pageNo + 1, pageSize);

			if (!OP_DELETE.equalsIgnoreCase(op)) {

				if (list == null || list.size() == 0) {

					ServletUtility.setErrorMessage("No record found ", request);
				}
			}

			ServletUtility.setList(list, request);

			ServletUtility.setPageNo(pageNo, request);

			ServletUtility.setPageSize(pageSize, request);

			ServletUtility.setBean(bean, request);

			request.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {

			ServletUtility.handleException(e, request, response);

			e.printStackTrace();

			return;
		}
	}

	@Override
	protected String getView() {

		return ORSView.ROLE_LIST_VIEW;
	}

}
