package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.FacultyBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.FacultyModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * FacultyListCtl is a Controller that handles operations for displaying, searching, 
 * paginating, and deleting the list of Faculty records. 
 * <p>
 * It interacts with {@link FacultyModel} to fetch faculty data from the database, 
 * handles user operations like Search, Next, Previous, Delete, Reset, and Back, 
 * and forwards the results to the Faculty List view.
 * </p>
 * 
 * @author Akbar Mansuri 
 * @version 1.0
 * @since 2025
 */
@WebServlet(name = "/FacultyListCtl", urlPatterns = { "/ctl/FacultyListCtl" })
public class FacultyListCtl extends BaseClt {

    private static Logger log = Logger.getLogger(FacultyListCtl.class);

    /**
     * Populates a FacultyBean object from the request parameters.
     *
     * @param request the HttpServletRequest containing input data
     * @return FacultyBean populated with request data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        FacultyBean bean = new FacultyBean();

        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setEmail(DataUtility.getString(request.getParameter("email")));

        log.debug("FacultyListCtl populateBean ");
        return bean;
    }

    /**
     * Handles HTTP GET requests. 
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("FacultyListCtl doGet");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        FacultyBean bean = (FacultyBean) populateBean(request);
        FacultyModel model = new FacultyModel();

        try {
            List<FacultyBean> list = model.search(bean, pageNo, pageSize);
            List<FacultyBean> next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
                log.warn("No faculty records found in GET");
                ServletUtility.setErrorMessage("No record found", request);
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);
            request.setAttribute("nextListSize", next.size());

            log.debug("FacultyListCtl.doGet() forwarding to view");
            ServletUtility.forward(getView(), request, response);

        } catch (ApplicationException e) {
            log.error("ApplicationException in FacultyListCtl.doGet()", e);
            ServletUtility.handleException(e, request, response);
            return;
        }
        log.debug(" Faculty Do post End");
    }

    /**
     * Handles HTTP POST requests.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("FacultyListCtl doPost started");

        List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        FacultyBean bean = (FacultyBean) populateBean(request);
        FacultyModel model = new FacultyModel();

        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");

        try {
            log.debug("Operation in POST = " + op);

            if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                    pageNo--;
                }

            } else if (OP_NEW.equalsIgnoreCase(op)) {
                log.debug("Redirecting to FacultyCtl (New record)");
                ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    FacultyBean deletebean = new FacultyBean();
                    for (String id : ids) {
                        deletebean.setId(DataUtility.getInt(id));
                        model.delete(bean);
                        log.info("Faculty deleted successfully, ID=" + id);
                        ServletUtility.setSuccessMessage("Faculty is deleted successfully", request);
                    }
                } else {
                    log.warn("Delete attempted with no records selected");
                    ServletUtility.setErrorMessage("Select at least one record", request);
                }

            } else if (OP_RESET.equalsIgnoreCase(op)) {
                log.debug("Reset operation - redirecting to FacultyListCtl");
                ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
                return;

            } else if (OP_BACK.equalsIgnoreCase(op)) {
                log.debug("Back operation - redirecting to FacultyListCtl");
                ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
                return;
            }

            list = model.search(bean, pageNo, pageSize);
            next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.size() == 0) {
                log.warn("No faculty records found in POST");
                ServletUtility.setErrorMessage("No record found ", request);
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);
            request.setAttribute("nextListSize", next.size());

            log.debug("FacultyListCtl.doPost() forwarding to view");
            ServletUtility.forward(getView(), request, response);

        } catch (ApplicationException e) {
            log.error("ApplicationException in FacultyListCtl.doPost()", e);
            ServletUtility.handleException(e, request, response);
            return;
        }
        log.debug("Faculty List Do Post End");
    }

    /**
     * Returns the Faculty List view page constant.
     */
    @Override
    protected String getView() {
        return ORSView.FACULTY_LIST_VIEW;
    }
}
