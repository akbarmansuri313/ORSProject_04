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
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CollegeModel;
import in.co.rays.model.StudentModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * StudentCtl Controller is responsible for handling requests related 
 * to Student operations such as add, update, and validation.
 * <p>
 * It extends {@link BaseClt} and provides implementations for 
 * {@code preload}, {@code validate}, {@code populateBean}, {@code doGet}, 
 * {@code doPost}, and {@code getView} methods.
 * </p>
 * 
 * @author Akbar Mansuri
 * @version 1.0
 * @since 2025
 */
@WebServlet(name = "/StudentCtl", urlPatterns = { "/ctl/StudentCtl" })
public class StudentCtl extends BaseClt {

    private static Logger log = Logger.getLogger(StudentCtl.class);

    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("StudentCtl preload started");

        CollegeModel model = new CollegeModel();

        try {
            List collegeList = model.list();
            request.setAttribute("collegeList", collegeList);
            
        } catch (ApplicationException e) {
            log.error("Error in preload", e);
            e.printStackTrace();
            return;
        }

        log.debug("StudentCtl preload ended");
    }

    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("StudentCtl validate started");
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("firstName"))) {
            request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
            log.error("First Name is required");
        } else if (!DataValidator.isName(request.getParameter("firstName"))) {
            request.setAttribute("firstName", "Invalid First Name");
            pass = false;
            log.error("Invalid First Name entered");
        }

        if (DataValidator.isNull(request.getParameter("lastName"))) {
            request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
            log.error("Last Name is required");
        } else if (!DataValidator.isName(request.getParameter("lastName"))) {
            request.setAttribute("lastName", "Invalid Last Name");
            pass = false;
            log.error("Invalid Last Name entered");
        }

        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
            log.error("Mobile No is required");
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Mobile No must have 10 digits");
            pass = false;
            log.error("Invalid Mobile No length");
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Invalid Mobile No");
            pass = false;
            log.error("Invalid Mobile No entered");
        }

        if (DataValidator.isNull(request.getParameter("gender"))) {
            request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
            pass = false;
            log.error("Gender is required");
        }

        if (DataValidator.isNull(request.getParameter("email"))) {
            request.setAttribute("email", PropertyReader.getValue("error.require", "Email "));
            pass = false;
            log.error("Email is required");
        } else if (!DataValidator.isEmail(request.getParameter("email"))) {
            request.setAttribute("email", PropertyReader.getValue("error.email", "Email "));
            pass = false;
            log.error("Invalid Email entered");
        }

        if (DataValidator.isNull(request.getParameter("collegeId"))) {
            request.setAttribute("collegeId", PropertyReader.getValue("error.require", "College Name"));
            pass = false;
            log.error("College Name is required");
        }

        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date of Birth"));
            pass = false;
            log.error("Date of Birth is required");
        } else if (!DataValidator.isDate(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
            pass = false;
            log.error("Invalid Date of Birth entered");
        }

        log.debug("StudentCtl validate ended with status: " + pass);
        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("StudentCtl populateBean started");

        StudentBean bean = new StudentBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        bean.setEmail(DataUtility.getString(request.getParameter("email")));
        bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));

        populateDTO(bean, request);

        log.debug("StudentCtl populateBean ended ");
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("StudentCtl doGet started");

        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));

        StudentModel model = new StudentModel();

        if (id > 0) {
            try {
                StudentBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, request);
               
            } catch (ApplicationException e) {
                log.error("ApplicationException in doGet", e);
                ServletUtility.handleException(e, request, response);
                e.printStackTrace();
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("StudentCtl doGet ended");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("StudentCtl doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));
        StudentModel model = new StudentModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            StudentBean bean = (StudentBean) populateBean(request);
            try {
                long pk = model.add(bean);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Student Successfully save", request);
               
            } catch (ApplicationException e) {
                log.error("ApplicationException in doPost while saving student", e);
                ServletUtility.handleException(e, request, response);
                e.printStackTrace();
                return;
            } catch (DuplicateRecordException e) {
                log.error("Duplicate student entry found", e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Login Id already exist", request);
                e.printStackTrace();
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            StudentBean bean = (StudentBean) populateBean(request);
            try {
                if (bean.getId() > 0) {
                    model.update(bean);
                    
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Student Update Successfully", request);
            } catch (ApplicationException e) {
                log.error("ApplicationException in doPost while updating student", e);
                ServletUtility.handleException(e, request, response);
                e.printStackTrace();
                return;
            } catch (DuplicateRecordException e) {
                log.error("Duplicate student entry found during update", e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Login Id already exists", request);
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
           
            ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {
           
            ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
            return;
        }
        ServletUtility.forward(getView(), request, response);
        
    }

    @Override
    protected String getView() {
        return ORSView.STUDENT_VIEW;
    }

}
