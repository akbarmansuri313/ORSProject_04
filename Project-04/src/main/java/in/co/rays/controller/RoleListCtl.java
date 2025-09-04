package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.RoleModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * RoleListCtl Servlet handles the Role list operations such as searching,
 * displaying, pagination, and deleting role records.
 * <p>
 * It extends {@link BaseClt} and interacts with {@link RoleModel}.
 * </p>
 * 
 * @author Akbar
 * @version 1.0
 * @since 2023
 */
@WebServlet(name = "/RoleListCtl", urlPatterns = { "/ctl/RoleListCtl" })
public class RoleListCtl extends BaseClt {

	/** The log object for logging messages. */
	private static Logger log = Logger.getLogger(RoleListCtl.class);

	/**
	 * Preloads role data to be used in the Role List view.
	 * 
	 * @param request the HttpServletRequest object
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("RoleListCtl preload started");
		RoleModel roleModel = new RoleModel();
		try {
			List roleList = roleModel.list();
			request.setAttribute("roleList", roleList);

		} catch (ApplicationException e) {
			log.error("ApplicationException in preload: ", e);
			e.printStackTrace();
		}
		log.debug("RoleListCtl preload ended");
	}

	/**
	 * Populates the RoleBean object from request parameters.
	 * 
	 * @param request the HttpServletRequest object
	 * @return populated RoleBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("RoleListCtl populateBean started");
		RoleBean bean = new RoleBean();

		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setId(DataUtility.getLong(request.getParameter("roleId")));
		log.debug("RoleListCtl populateBean ended");
		return bean;
	}

	/**
	 * Handles HTTP GET requests. Displays the list of roles with pagination.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("RoleListCtl doGet started");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		RoleBean bean = (RoleBean) populateBean(request);
		RoleModel model = new RoleModel();

		try {
			List<RoleBean> list = model.search(bean, pageNo, pageSize);
			List<RoleBean> next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {
				log.warn("No role records found");
				ServletUtility.setErrorMessage("No record found", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setBean(bean, request);
			request.setAttribute("nextListSize", next.size());
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error("ApplicationException in doGet: ", e);
			ServletUtility.handleException(e, request, response);
			e.printStackTrace();
			return;
		}
		log.debug("RoleListCtl doGet ended");
	}

	/**
	 * Handles HTTP POST requests. Supports operations such as Search, Next,
	 * Previous, New, Delete, Reset, and Back.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("RoleListCtl doPost started");

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
					log.info("Search operation executed");
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
					log.info("Next page operation executed");
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
					log.info("Previous page operation executed");
				}
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.ROLE_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					for (String id : ids) {
						RoleBean deletebean = new RoleBean();
						deletebean.setId(DataUtility.getInt(id));
						model.delete(bean);
						log.info("Role deleted successfully with id = " + id);
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
			log.error("ApplicationException in doPost: ", e);
			ServletUtility.handleException(e, request, response);
			e.printStackTrace();
			return;
		}

		log.debug("RoleListCtl doPost ended");
	}

	/**
	 * Returns the view page for Role List.
	 */
	@Override
	protected String getView() {
		return ORSView.ROLE_LIST_VIEW;
	}
}
