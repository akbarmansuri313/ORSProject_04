package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.RoleModel;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * UserListCtl Servlet Controller. 
 * Handles operations for displaying, searching, deleting, 
 * and paginating user records.
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
@WebServlet(name = "UserListCtl", urlPatterns = {"/ctl/UserListCtl"})
public class UserListCtl extends BaseClt {

    private static Logger log = Logger.getLogger(UserListCtl.class);

    /**
     * Preloads role list data into the request scope 
     * to populate dropdowns in the view.
     *
     * @param request the HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("UserListCtl preload started");
        RoleModel roleModel = new RoleModel();

        try {
            List roleList = roleModel.list();
            request.setAttribute("roleList", roleList);
            log.info("Role list preloaded successfully, size: " + roleList.size());
        } catch (ApplicationException e) {
            log.error("Error in preload of UserListCtl", e);
            e.printStackTrace();
            return;
        }
        log.debug("UserListCtl preload ended");
    }

    /**
     * Populates a UserBean from request parameters.
     *
     * @param request the HttpServletRequest object
     * @return populated UserBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("UserListCtl populateBean started");
        UserBean bean = new UserBean();

        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLogin(DataUtility.getString(request.getParameter("login")));
        bean.setRoleId(DataUtility.getLong(request.getParameter("roleId")));

        log.debug("UserListCtl populateBean ended");
        return bean;
    }

    /**
     * Handles HTTP GET requests. 
     * Loads user list with pagination and forwards to the view.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("UserListCtl doGet started");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        UserBean bean = (UserBean) populateBean(request);
        UserModel model = new UserModel();

        try {
            List<UserBean> list = model.search(bean, pageNo, pageSize);
            List<UserBean> next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("No record found", request);
                log.warn("No user records found in doGet");
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);
            request.setAttribute("nextListSize", next.size());

            ServletUtility.forward(getView(), request, response);
            log.info("User list loaded successfully in doGet, size: " + list.size());

        } catch (ApplicationException e) {
            log.error("ApplicationException in doGet of UserListCtl", e);
            ServletUtility.handleException(e, request, response);
            e.printStackTrace();
            return;
        }
        log.debug("UserListCtl doGet ended");
    }

    /**
     * Handles HTTP POST requests. 
     * Supports operations such as Search, Next, Previous, New, Delete, Reset, and Back.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("UserListCtl doPost started");

        List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        UserBean bean = (UserBean) populateBean(request);
        UserModel model = new UserModel();

        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");

        try {
            if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
                    pageNo--;
                }
                log.info("Operation performed: " + op + ", Page No: " + pageNo);

            } else if (OP_NEW.equalsIgnoreCase(op)) {
                log.info("Redirecting to UserCtl for new record creation");
                ServletUtility.redirect(ORSView.USER_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;

                if (ids != null && ids.length > 0) {
                    UserBean deleteBean = new UserBean();

                    for (String id : ids) {
                        deleteBean.setId(DataUtility.getInt(id));
                        model.delete(deleteBean);
                        log.info("User deleted with ID: " + id);
                        ServletUtility.setSuccessMessage("Data delete Successfully", request);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                    log.warn("Delete operation requested but no IDs selected");
                }

            } else if (OP_RESET.equalsIgnoreCase(op)) {
                log.info("Reset operation performed, redirecting to User List");
                ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
                return;

            } else if (OP_BACK.equalsIgnoreCase(op)) {
                log.info("Back operation performed, redirecting to User List");
                ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
                return;
            }

            list = model.search(bean, pageNo, pageSize);
            next = model.search(bean, pageNo + 1, pageSize);

            if (!OP_DELETE.equalsIgnoreCase(op)) {
                if (list == null || list.size() == 0) {
                    ServletUtility.setErrorMessage("No record found ", request);
                    log.warn("No user records found after operation: " + op);
                }
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);
            request.setAttribute("nextListSize", next.size());

            ServletUtility.forward(getView(), request, response);
            log.info("User list loaded successfully in doPost, size: " + list.size());

        } catch (Exception e) {
            log.error("Exception in doPost of UserListCtl", e);
            ServletUtility.handleException(e, request, response);
            e.printStackTrace();
            return;
        }
        log.debug("UserListCtl doPost ended");
    }

    /**
     * Returns the view page for User List.
     */
    @Override
    protected String getView() {
        return ORSView.USER_LIST_VIEW;
    }

}
