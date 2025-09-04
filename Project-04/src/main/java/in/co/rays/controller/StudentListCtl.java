package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.StudentBean;
import in.co.rays.model.StudentModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * StudentListCtl Servlet Controller is responsible for controlling the list of
 * Student records. It handles search, pagination, new, delete, reset, and back
 * operations.
 * 
 * @author Akbar Mansuri
 * @version 1.0
 * @since 2025
 */
@WebServlet(name = "/StudentListCtl", urlPatterns = { "/ctl/StudentListCtl" })
public class StudentListCtl extends BaseClt {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(StudentListCtl.class);

    /**
     * Populates the StudentBean with request parameters.
     * 
     * @param request HttpServletRequest object
     * @return populated StudentBean object
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("StudentListCtl populateBean method start");

        StudentBean bean = new StudentBean();

        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setEmail(DataUtility.getString(request.getParameter("email")));

        log.debug("StudentListCtl populateBean method end");
        return bean;
    }

    /**
     * Handles GET requests. Displays the list of students with pagination.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("StudentListCtl doGet method start");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        StudentBean bean = (StudentBean) populateBean(request);
        StudentModel model = new StudentModel();

        try {
            List<StudentBean> list = model.search(bean, pageNo, pageSize);
            List<StudentBean> next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("Record not found", request);
                
            }

            ServletUtility.setBean(bean, request);
            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            request.setAttribute("nextListSize", next.size());

            ServletUtility.forward(getView(), request, response);

        } catch (Exception e) {
            log.error("Error in StudentListCtl doGet", e);
            ServletUtility.handleException(e, request, response);
            return;
        }

        log.debug("StudentListCtl doGet method end");
    }

    /**
     * Handles POST requests. Supports operations like Search, Next, Previous,
     * New, Delete, Reset, and Back with pagination.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("StudentListCtl doPost method start");

        List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        StudentBean bean = (StudentBean) populateBean(request);
        StudentModel model = new StudentModel();

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
               

            } else if (OP_NEW.equalsIgnoreCase(op)) {
                
                ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;

                if (ids != null && ids.length > 0) {
                    StudentBean deleteBean = new StudentBean();

                    for (String id : ids) {
                        deleteBean.setId(DataUtility.getInt(id));
                        model.delete(bean);
                        
                        ServletUtility.setSuccessMessage("Data Delete Success !...", request);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                    
                }

            } else if (OP_RESET.equalsIgnoreCase(op)) {
                
                ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
                return;

            } else if (OP_BACK.equalsIgnoreCase(op)) {
               
                ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
                return;
            }

            list = model.search(bean, pageNo, pageSize);
            next = model.search(bean, pageNo + 1, pageSize);

            if (!OP_DELETE.equalsIgnoreCase(op)) {
                if (list == null || list.size() == 0) {
                    ServletUtility.setErrorMessage("No record found", request);
                    
                }
            }

            ServletUtility.setBean(bean, request);
            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            request.setAttribute("nextListSize", next.size());

            ServletUtility.forward(getView(), request, response);

        } catch (Exception e) {
            log.error("Error in StudentListCtl doPost", e);
            ServletUtility.handleException(e, request, response);
            return;
        }

        log.debug("StudentListCtl doPost method end");
    }

    /**
     * Returns the view page for student list.
     */
    @Override
    protected String getView() {
        return ORSView.STUDENT_LIST_VIEW;
    }
}
