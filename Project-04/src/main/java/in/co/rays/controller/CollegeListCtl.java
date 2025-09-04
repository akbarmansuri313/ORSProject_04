package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.CollegeModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * CollegeListCtl is a Controller to handle list operations for College entities.
 * <p>
 * It provides search, pagination, delete, and navigation functionality for
 * colleges. It interacts with {@link CollegeModel} to perform database operations.
 * </p>
 * 
 * <h2>Operations Supported:</h2>
 * <ul>
 *   <li>SEARCH → Filter list by name/city</li>
 *   <li>NEXT/PREVIOUS → Pagination navigation</li>
 *   <li>NEW → Add new College entry</li>
 *   <li>DELETE → Remove selected College(s)</li>
 *   <li>RESET → Reset the search criteria</li>
 *   <li>BACK → Navigate back to list view</li>
 * </ul>
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
@WebServlet(name = "/CollegeListCtl", urlPatterns = { "/ctl/CollegeListCtl" })
public class CollegeListCtl extends BaseClt {

    /** The Constant log for logging messages. */
    private static final Logger log = Logger.getLogger(CollegeListCtl.class);

    /**
     * Preloads College list into request scope. 
     * <p>This method is called before view rendering to prepare data (dropdowns, etc.).</p>
     * 
     * @param request HTTP request object
     */
    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("CollegeListCtl preload started");

        CollegeModel collegeModel = new CollegeModel();

        try {
            List collegeList = collegeModel.list();
            request.setAttribute("collegeList", collegeList);
            log.info("College list preloaded successfully");

        } catch (ApplicationException e) {
            log.error("Error in preload CollegeListCtl", e);
            e.printStackTrace();
            return;
        }

        log.debug("CollegeListCtl preload ended");
    }

    /**
     * Populates a {@link CollegeBean} object with request parameters.
     * 
     * @param request HTTP request containing filter/search parameters
     * @return CollegeBean populated with values from request
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("CollegeListCtl populateBean started");

        CollegeBean bean = new CollegeBean();
        
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setCity(DataUtility.getString(request.getParameter("city")));
        bean.setId(DataUtility.getLong(request.getParameter("collegeId")));
        System.out.println(request.getParameter("collegeId"));

        log.debug("CollegeListCtl populateBean ended");
        return bean;
    }

    /**
     * Handles HTTP GET request for displaying list of colleges with pagination support.
     * <p>
     * If no records are found, sets error message in request.
     * </p>
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("CollegeListCtl doGet started");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        CollegeBean bean = (CollegeBean) populateBean(request);
        CollegeModel model = new CollegeModel();

        try {
            List<CollegeBean> list = model.search(bean, pageNo, pageSize);
            List<CollegeBean> next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("Record Not found", request);
                log.info("No College records found for given search criteria");
            } else {
                log.info("College list fetched, size = " + list.size());
            }

            ServletUtility.setBean(bean, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setList(list, request);
            request.setAttribute("nextListSize", next != null ? next.size() : 0);

            ServletUtility.forward(getView(), request, response);

        } catch (ApplicationException e) {
            log.error("ApplicationException in CollegeListCtl doGet", e);
            ServletUtility.handleException(e, request, response);
            e.printStackTrace();
            return;
        }

        log.debug("CollegeListCtl doGet ended");
    }

    /**
     * Handles HTTP POST request for College list operations such as:
     * <ul>
     * <li>SEARCH → Filters list by criteria</li>
     * <li>NEXT/PREVIOUS → Pagination control</li>
     * <li>NEW → Redirects to CollegeCtl for adding new College</li>
     * <li>DELETE → Deletes selected College records</li>
     * <li>RESET → Resets form and reloads list</li>
     * <li>BACK → Redirects to College list</li>
     * </ul>
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("CollegeListCtl doPost started");

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

        log.info("Operation received in doPost: " + op);

        if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

            if (OP_SEARCH.equalsIgnoreCase(op)) {
                pageNo = 1;
                log.debug("Search operation triggered");
            } else if (OP_NEXT.equalsIgnoreCase(op)) {
                pageNo++;
                log.debug("Next page requested");
            } else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
                pageNo--;
                log.debug("Previous page requested");
            }

        } else if (OP_NEW.equalsIgnoreCase(op)) {
            log.info("Redirecting to CollegeCtl for new entry");
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
                        log.info("College record deleted, ID=" + id);
                    } catch (ApplicationException e) {
                        log.error("Error deleting College record, ID=" + id, e);
                        e.printStackTrace();
                    }
                }
                ServletUtility.setSuccessMessage("Data Deleted Successfully", request);

            } else {
                ServletUtility.setSuccessMessage("Select at least one record", request);
                log.warn("Delete operation triggered without selecting records");
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            log.debug("Reset operation triggered");
            ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
            return;

        } else if (OP_BACK.equalsIgnoreCase(op)) {
            log.debug("Back operation triggered");
            ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
            return;
        }

        try {
            list = model.search(bean, pageNo, pageSize);
            next = model.search(bean, pageNo + 1, pageSize);

            if (!OP_DELETE.equalsIgnoreCase(op)) {
                if (list == null || list.size() == 0) {
                    ServletUtility.setErrorMessage("Record not Found", request);
                    log.info("No records found in search result");
                }
            }

            ServletUtility.setList(list, request);
            ServletUtility.setBean(bean, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            request.setAttribute("nextListSize", next != null ? next.size() : 0);

            ServletUtility.forward(getView(), request, response);

        } catch (ApplicationException e) {
            log.error("ApplicationException in CollegeListCtl doPost", e);
            ServletUtility.handleException(e, request, response);
            e.printStackTrace();
            return;
        }

        log.debug("CollegeListCtl doPost ended");
    }

    /**
     * Returns the view for College list page.
     * 
     * @return String constant of College list view
     */
    @Override
    protected String getView() {
        return ORSView.COLLEGE_LIST_VIEW;
    }
}
