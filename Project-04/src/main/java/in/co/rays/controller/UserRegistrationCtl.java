package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * UserRegistrationCtl Servlet handles user registration process.
 * Validates input, populates UserBean, and registers new user in the system.
 * 
 * Supports operations: Sign Up, Reset
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
@WebServlet(name = "UserRegistrationCtl", urlPatterns = { "/UserRegistrationCtl" })
public class UserRegistrationCtl extends BaseClt {

    private static final Logger log = Logger.getLogger(UserRegistrationCtl.class);

    /** Operation constant for Sign Up */
    public static final String OP_SIGN_UP = "Sign Up";

    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("UserRegistrationCtl validate started");
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("firstName"))) {
            request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
            log.error("Validation Error: First Name is required");
        } else if (!DataValidator.isName(request.getParameter("firstName"))) {
            request.setAttribute("firstName", "Invalid First Name");
            pass = false;
            log.error("Validation Error: Invalid First Name");
        }

        if (DataValidator.isNull(request.getParameter("lastName"))) {
            request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
            log.error("Validation Error: Last Name is required");
        } else if (!DataValidator.isName(request.getParameter("lastName"))) {
            request.setAttribute("lastName", "Invalid Last Name");
            pass = false;
            log.error("Validation Error: Invalid Last Name");
        }

        if (DataValidator.isNull(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
            pass = false;
            log.error("Validation Error: Login Id is required");
        } else if (!DataValidator.isEmail(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.email", "Login"));
            pass = false;
            log.error("Validation Error: Invalid Email Format");
        }

        if (DataValidator.isNull(request.getParameter("password"))) {
            request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
            pass = false;
            log.error("Validation Error: Password is required");
        } else if (!DataValidator.isPasswordLength(request.getParameter("password"))) {
            request.setAttribute("password", "Password should be 8 to 12 characters");
            pass = false;
            log.error("Validation Error: Password length invalid");
        } else if (!DataValidator.isPassword(request.getParameter("password"))) {
            request.setAttribute("password", "Must contain uppercase, lowercase, digit & special character");
            pass = false;
            log.error("Validation Error: Weak Password Format");
        }

        if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
            log.error("Validation Error: Confirm Password is required");
        }

        if (DataValidator.isNull(request.getParameter("gender"))) {
            request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
            pass = false;
            log.error("Validation Error: Gender is required");
        }

        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date of Birth"));
            pass = false;
            log.error("Validation Error: Date of Birth is required");
        } else if (!DataValidator.isDate(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
            pass = false;
            log.error("Validation Error: Invalid Date of Birth");
        }

        if (!request.getParameter("password").equals(request.getParameter("confirmPassword"))
                && !"".equals(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", "Password and Confirm Password must be Same!");
            pass = false;
            log.error("Validation Error: Password and Confirm Password mismatch");
        }

        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
            log.error("Validation Error: Mobile No is required");
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Mobile No must have 10 digits");
            pass = false;
            log.error("Validation Error: Mobile No must be 10 digits");
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Invalid Mobile No");
            pass = false;
            log.error("Validation Error: Invalid Mobile No");
        }

        log.debug("UserRegistrationCtl validate ended with pass = " + pass);
        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("UserRegistrationCtl populateBean started");
        UserBean bean = new UserBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setLogin(DataUtility.getString(request.getParameter("login")));
        bean.setPassword(DataUtility.getString(request.getParameter("password")));
        bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        bean.setRoleId(RoleBean.STUDENT);

        populateDTO(bean, request);

        log.debug("UserRegistrationCtl populateBean ended");
        return bean;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("UserRegistrationCtl doGet started");
        ServletUtility.forward(getView(), request, response);
        log.debug("UserRegistrationCtl doGet ended");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("UserRegistrationCtl doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));
        UserModel model = new UserModel();

        if (OP_SIGN_UP.equalsIgnoreCase(op)) {
            log.info("Processing Sign Up operation");

            UserBean bean = (UserBean) populateBean(request);

            try {
                long pk = model.registerUser(bean);
                log.info("User registered successfully with ID = " + pk);

                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Registration successful!", request);

            } catch (DuplicateRecordException e) {
                log.error("Duplicate Login Id found: " + bean.getLogin(), e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Login id already exists", request);

            } catch (ApplicationException e) {
                log.error("ApplicationException in User Registration", e);
                ServletUtility.handleException(e, request, response);
                return;
            }

            ServletUtility.forward(getView(), request, response);

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            log.info("Reset operation requested");
            ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
            return;
        }

        log.debug("UserRegistrationCtl doPost ended");
    }

    @Override
    protected String getView() {
        return ORSView.USER_REGISTRATION_VIEW;
    }
}
