package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CourseBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.CourseModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * Course List Controller. Performs operations like searching, listing,
 * pagination, deletion of Course records.
 * 
 * @author Akbar Mansuri
 * @version 1.0
 * @since 2025
 */
@WebServlet(name = "/CourseListCtl", urlPatterns = { "/ctl/CourseListCtl" })
public class CourseListCtl extends BaseClt {

    private static final Logger log = Logger.getLogger(CourseListCtl.class);

    /**
     * Preloads the course list data and sets it into request scope for UI dropdowns.
     * 
     * @param request HttpServletRequest object containing client request
     */
    @Override
    protected void preload(HttpServletRequest request) {

        log.debug("CourseListCtl preload started");
        CourseModel courseModel = new CourseModel();

        try {
            List courseList = courseModel.list();
            request.setAttribute("courseList", courseList);
            
        } catch (ApplicationException e) {
           
            e.printStackTrace();
        }
        log.debug("CourseListCtl preload ended");
    }

    /**
     * Populates a CourseBean object with request parameters.
     * 
     * @param request HttpServletRequest object containing client input
     * @return bean populated CourseBean instance
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("CourseListCtl populateBean started");
        CourseBean bean = new CourseBean();

        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setId(DataUtility.getLong(request.getParameter("courseId")));

       
        log.debug("CourseListCtl populateBean ended");
        return bean;
    }

    /**
     * Handles GET requests for listing and searching course records.
     * Supports pagination.
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("CourseListCtl doGet started");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        CourseBean bean = (CourseBean) populateBean(request);
        CourseModel model = new CourseModel();

        try {
            List<CourseBean> list = model.search(bean, pageNo, pageSize);
            List<CourseBean> next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
               
                ServletUtility.setErrorMessage("No Record Found", request);
            } else {
            

            ServletUtility.setBean(bean, request);
            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);

            request.setAttribute("nextListSize", next.size());

            ServletUtility.forward(getView(), request, response);
            }
        } catch (ApplicationException e) {
            log.error("ApplicationException in doGet", e);
            ServletUtility.handleException(e, request, response);
            e.printStackTrace();
            return;
        }

        log.debug("CourseListCtl doGet ended");
    }

    /**
     * Handles POST requests for searching, pagination, adding new records,
     * deleting, and resetting the form.
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("CourseListCtl doPost started");

        List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        CourseBean bean = (CourseBean) populateBean(request);
        CourseModel model = new CourseModel();

        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");

      
        try {
            if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;

                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                    pageNo--;
                }

            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;

                if (ids != null && ids.length > 0) {
                    CourseBean deletebean = new CourseBean();

                    for (String id : ids) {
                        deletebean.setId(DataUtility.getInt(id));
                        model.delete(deletebean);
                        ServletUtility.setSuccessMessage("Course deleted successfully", request);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                }

            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
                return;

            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
                return;
            }

            list = model.search(bean, pageNo, pageSize);
            next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.size() == 0) {
                ServletUtility.setErrorMessage("No record found ", request);
            } else {
                log.info("Records fetched in doPost: " + list.size());
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);
            request.setAttribute("nextListSize", next.size());

            ServletUtility.forward(getView(), request, response);

        } catch (ApplicationException e) {
            log.error("ApplicationException in doPost", e);
            ServletUtility.handleException(e, request, response);
            e.printStackTrace();
            return;
        }

        log.debug("CourseListCtl doPost ended");
    }

    /**
     * Returns the view page for this controller.
     * 
     * @return String constant for Course List View
     */
    @Override
    protected String getView() {
        return ORSView.COURSE_LIST_VIEW;
    }
}
