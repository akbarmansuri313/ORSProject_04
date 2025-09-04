package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CourseModel;
import in.co.rays.model.SubjectModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * Controller class for handling Subject operations such as add, update, delete,
 * and view.
 * 
 * @author 
 * @version 1.0
 * @since 2025
 */
@WebServlet(name = "/SubjectCtl", urlPatterns = { "/ctl/SubjectCtl" })
public class SubjectCtl extends BaseClt {

    private static Logger log = Logger.getLogger(SubjectCtl.class);

    /**
     * Loads the list of courses to be displayed before rendering Subject form.
     * 
     * @param request HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("SubjectCtl preload started");
        CourseModel model = new CourseModel();
        try {
            List courseList = model.list();
            request.setAttribute("courseList", courseList);
        } catch (ApplicationException e) {
            log.error("Error in preload: ", e);
            e.printStackTrace();
        }
        log.debug("SubjectCtl preload ended");
    }

    /**
     * Validates the Subject form input fields.
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("SubjectCtl validate started");
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Subject Name"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("courseId"))) {
            request.setAttribute("courseId", PropertyReader.getValue("error.require", "Course Name"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
        }
        log.debug("SubjectCtl validate ended with status: " + pass);
        return pass;
    }

    /**
     * Populates a SubjectBean object with request parameters.
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("SubjectCtl populateBean started");
        SubjectBean bean = new SubjectBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        bean.setCourseName(DataUtility.getString(request.getParameter("courseName")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));

        populateDTO(bean, request);
        log.debug("SubjectCtl populateBean ended");
        return bean;
    }

    /**
     * Handles GET requests.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("SubjectCtl doGet started");

        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));

        SubjectModel model = new SubjectModel();

        if (id > 0 || op != null) {
            try {
                SubjectBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                log.error("Error in doGet: ", e);
                ServletUtility.handleException(e, request, response);
                e.printStackTrace();
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("SubjectCtl doGet ended");
    }

    /**
     * Handles POST requests.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("SubjectCtl doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));
        SubjectModel model = new SubjectModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            SubjectBean bean = (SubjectBean) populateBean(request);
            try {
                long pk = model.add(bean);
                
                ServletUtility.setSuccessMessage("Subject Saved Successfully", request);
            } catch (ApplicationException e) {
                log.error("ApplicationException in doPost (Save): ", e);
                ServletUtility.handleException(e, request, response);
                e.printStackTrace();
                return;
            } catch (DuplicateRecordException e) {
                log.error("DuplicateRecordException in doPost (Save): ", e);
                ServletUtility.setErrorMessage("Subject Id Already Exist", request);
                ServletUtility.forward(getView(), request, response);
                e.printStackTrace();
                return;
            }
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            SubjectBean bean = (SubjectBean) populateBean(request);
            try {
                if (bean.getId() > 0) {
                    model.update(bean);
                    
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Subject Updated Successfully", request);
            } catch (ApplicationException e) {
                log.error("ApplicationException in doPost (Update): ", e);
                ServletUtility.handleException(e, request, response);
                e.printStackTrace();
                return;
            } catch (DuplicateRecordException e) {
                log.error("DuplicateRecordException in doPost (Update): ", e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Subject Already Exist", request);
                e.printStackTrace();
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
         ServletUtility.redirect(ORSView.SUBJECT_LIST_CTL, request, response);
            return;
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            
            ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("SubjectCtl doPost ended");
    }

    /**
     * Returns the view page for Subject operations.
     */
    @Override
    protected String getView() {
        return ORSView.SUBJECT_VIEW;
    }

}
