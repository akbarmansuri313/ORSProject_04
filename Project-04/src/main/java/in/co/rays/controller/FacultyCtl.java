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
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CollegeModel;
import in.co.rays.model.CourseModel;
import in.co.rays.model.FacultyModel;
import in.co.rays.model.SubjectModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * FacultyCtl Controller is responsible for handling all Faculty-related operations 
 * such as Add, Update, Reset, Cancel, and Preloading required data (College, Course, Subject).
 * <p>
 * This controller manages:
 * <ul>
 *   <li>Validation of input data</li>
 *   <li>Populating FacultyBean</li>
 *   <li>Delegating business logic to FacultyModel</li>
 *   <li>Forwarding or redirecting requests to appropriate views</li>
 * </ul>
 *
 * @author Akbar
 * @version 1.0
 * @since 2025
 */
@WebServlet(name = "/FacultyCtl", urlPatterns = { "/ctl/FacultyCtl" })
public class FacultyCtl extends BaseClt {

    private static final Logger log = Logger.getLogger(FacultyCtl.class);

    /**
     * Preloads the list of colleges, courses, and subjects 
     * to make them available in request scope for dropdowns.
     *
     * @param request the HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("FacultyCtl preload started");
        CollegeModel collegeModel = new CollegeModel();
        SubjectModel subjectModel = new SubjectModel();
        CourseModel courseModel = new CourseModel();

        try {
            List collegeList = collegeModel.list();
            request.setAttribute("collegeList", collegeList);
            log.info("College list loaded, size: ");

            List subjectList = subjectModel.list();
            request.setAttribute("subjectList", subjectList);
            log.info("Subject list loaded, size: " );

            List courseList = courseModel.list();
            request.setAttribute("courseList", courseList);
            log.info("Course list loaded, size: ");

        } catch (ApplicationException e) {
            log.error("Error in preload method", e);
            e.printStackTrace();
            return;
        }
        log.debug("FacultyCtl preload ended");
    }

    /**
     * Validates the Faculty form inputs such as first name, last name,
     * gender, date of birth, email, mobile number, college, course, and subject.
     *
     * @param request the HttpServletRequest object containing form data
     * @return true if validation is successful, otherwise false
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("FacultyCtl validate started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("firstName"))) {
            request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
            log.warn("First Name validation failed");
        } else if (!DataValidator.isName(request.getParameter("firstName"))) {
            request.setAttribute("firstName", "Invalid First Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("lastName"))) {
            request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
            log.warn("Last Name validation failed");
        } else if (!DataValidator.isName(request.getParameter("lastName"))) {
            request.setAttribute("lastName", "Invalid Last Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("gender"))) {
            request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date of Birth"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("email"))) {
            request.setAttribute("email", PropertyReader.getValue("error.require", "Email "));
            pass = false;
        } else if (!DataValidator.isEmail(request.getParameter("email"))) {
            request.setAttribute("email", PropertyReader.getValue("error.email", "Email "));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Mobile No must have 10 digits");
            pass = false;
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Invalid Mobile No");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("collegeId"))) {
            request.setAttribute("collegeId", PropertyReader.getValue("error.require", "College Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("courseId"))) {
            request.setAttribute("courseId", PropertyReader.getValue("error.require", "Course Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("subjectId"))) {
            request.setAttribute("subjectId", PropertyReader.getValue("error.require", "Subject Name"));
            pass = false;
        }

        log.debug("FacultyCtl validate ended with result: ");
        return pass;
    }

    /**
     * Populates FacultyBean from HTTP request parameters.
     *
     * @param request the HttpServletRequest object containing form data
     * @return FacultyBean populated with request parameters
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("FacultyCtl populateBean started");
        FacultyBean bean = new FacultyBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        bean.setEmail(DataUtility.getString(request.getParameter("email")));
        bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));
        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));

        populateDTO(bean, request);

        
        log.debug("FacultyCtl populateBean ended");
        return bean;
    }

    /**
     * Handles GET requests. If an ID is provided, it loads the faculty details
     * from the model and sets it into request scope.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("FacultyCtl doGet started");

        long id = DataUtility.getLong(request.getParameter("id"));
        FacultyModel model = new FacultyModel();

        if (id > 0) {
            try {
                FacultyBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, request);
                log.info("Faculty details loaded for ID: " + id);
            } catch (ApplicationException e) {
                log.error("Error in doGet while fetching Faculty ID: " + id, e);
                ServletUtility.handleException(e, request, response);
                e.printStackTrace();
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("FacultyCtl doGet ended");
    }

    /**
     * Handles POST requests for Faculty operations such as Save, Update, Cancel, and Reset.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("FacultyCtl doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));
        FacultyModel model = new FacultyModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        log.info("Operation: " + op + ", Faculty ID: " + id);

        if (OP_SAVE.equalsIgnoreCase(op)) {
            FacultyBean bean = (FacultyBean) populateBean(request);
            try {
                long pk = model.add(bean);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Faculty added successfully", request);
                log.info("Faculty added successfully with PK: " + pk);
            } catch (ApplicationException e) {
                log.error("ApplicationException in doPost Save", e);
                ServletUtility.handleException(e, request, response);
                e.printStackTrace();
                return;
            } catch (DuplicateRecordException e) {
                log.warn("Duplicate email found in Save: " + bean.getEmail());
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Email already exists", request);
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            FacultyBean bean = (FacultyBean) populateBean(request);
            try {
                if (id > 0) {
                    model.update(bean);
                    log.info("Faculty updated successfully for ID: " + id);
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Faculty updated successfully", request);
            } catch (ApplicationException e) {
                log.error("ApplicationException in doPost Update", e);
                ServletUtility.handleException(e, request, response);
                e.printStackTrace();
                return;
            } catch (DuplicateRecordException e) {
                log.warn("Duplicate email found in Update: " + bean.getEmail());
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Email already exists", request);
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            log.info("Cancel operation triggered, redirecting to Faculty List");
            ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            log.info("Reset operation triggered, redirecting to FacultyCtl");
            ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("FacultyCtl doPost ended");
    }

    /**
     * Returns the Faculty view page constant.
     */
    @Override
    protected String getView() {
        log.debug("Returning Faculty View Path: ");
        return ORSView.FACULTY_VIEW;
    }
}
