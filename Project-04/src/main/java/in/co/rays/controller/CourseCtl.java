package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CourseBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CourseModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * CourseCtl Controller is used to handle Course operations such as 
 * Add, Update, Delete, and Search. 
 * 
 * It communicates with the {@link CourseModel} to perform CRUD operations 
 * and forwards the result to the respective views.
 * 
 * @author Akbar Mansuri
 * @version 1.0
 * @since 2025
 */
@WebServlet(name = "/CourseCtl", urlPatterns = { "/ctl/CourseCtl" })
public class CourseCtl extends BaseClt {

    /** The log object for logging debug, info, and error messages. */
    private static Logger log = Logger.getLogger(CourseCtl.class);

    /**
     * Validates the Course input fields.
     * 
     * @param request the HTTP request containing form data
     * @return true if validation passes, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("CourseCtl validate started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
            log.error("Validation failed: Name is required");
            
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Name");
            pass = false;
            log.error("Validation failed: Invalid Name format");
        }

        if (DataValidator.isNull(request.getParameter("duration"))) {
            request.setAttribute("duration", PropertyReader.getValue("error.require", "Duration"));
            pass = false;
            log.error("Validation failed: Duration is required");
        }

        if (DataValidator.isNull(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
            log.error("Validation failed: Description is required");
        }

        log.debug("CourseCtl validate Ended with status: " + pass);
        return pass;
    }

    /**
     * Populates a {@link CourseBean} with request parameters.
     * 
     * @param request the HTTP request containing form data
     * @return populated {@link CourseBean}
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("CourseCtl populateBean started");

        CourseBean bean = new CourseBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));
        bean.setDuration(DataUtility.getString(request.getParameter("duration")));

        populateDTO(bean, request);

        log.debug("CourseCtl populateBean Ended: " + bean);
        return bean;
    }

    /**
     * Handles GET requests to display Course details for editing.
     * 
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("CourseCtl doGet Started");

        long id = DataUtility.getLong(request.getParameter("id"));

        CourseModel model = new CourseModel();

        if (id > 0) {
            try {
                CourseBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, request);
                log.info("Course found with ID: " + id);

            } catch (ApplicationException e) {
                log.error("ApplicationException in doGet", e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("CourseCtl doGet Ended");
    }

    /**
     * Handles POST requests for operations like SAVE, UPDATE, CANCEL, and RESET.
     * 
     * @param request  the HTTP request containing form data
     * @param response the HTTP response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("CourseCtl doPost Started");

        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));

        CourseModel model = new CourseModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            CourseBean bean = (CourseBean) populateBean(request);

            try {
                long pk = model.add(bean);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Data Added Successfully", request);
                log.info("Course added successfully with PK: " + pk);

            } catch (ApplicationException e) {
                log.error("ApplicationException in Save operation", e);
                ServletUtility.handleException(e, request, response);
                return;

            } catch (DuplicateRecordException e) {
                log.error("DuplicateRecordException in Save operation: Course already exists", e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Course Already Exist", request);
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            CourseBean bean = (CourseBean) populateBean(request);

            try {
                if (id > 0) {
                    model.Update(bean);
                    log.info("Course updated successfully with ID: " + id);
                }

                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Course Updated Successfully", request);

            } catch (ApplicationException e) {
                log.error("ApplicationException in Update operation", e);
                ServletUtility.handleException(e, request, response);
                return;

            } catch (DuplicateRecordException e) {
                log.error("DuplicateRecordException in Update operation", e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Duplicate Record found", request);
                return;
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            log.info("CourseCtl operation cancelled, redirecting to Course List");
            ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            log.info("CourseCtl reset operation, redirecting to Course Form");
            ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("CourseCtl doPost Ended");
    }

    /**
     * Returns the view page for Course.
     * 
     * @return the Course view path
     */
    @Override
    protected String getView() {
        return ORSView.COURSE_VIEW;
    }

}
