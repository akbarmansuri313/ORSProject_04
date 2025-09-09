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
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.RoleModel;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * UserCtl Controller is responsible for handling user-related requests.
 * It manages the creation, validation, update, and saving of user information.
 * 
 * @author Akbar
 * @version 1.0
 */
@WebServlet(name = "UserCtl", urlPatterns = {"/ctl/UserCtl" })
public class UserCtl extends BaseClt {

    private static Logger log = Logger.getLogger(UserCtl.class);

    /**
     * Preloads required data (like role list) into the request scope.
     */
    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("UserCtl preload started");
        RoleModel model = new RoleModel();
        try {
            List roleList = model.list();
            request.setAttribute("roleList", roleList);
           
        } catch (ApplicationException e) {
            log.error("Error in preload: " + e.getMessage(), e);
        }
        log.debug("UserCtl preload ended");
    }

    /**
     * Validates user input fields
     */
    @Override
    public boolean validate(HttpServletRequest request) {
        log.debug("UserCtl validate started");
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("firstName"))) {
            request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
            log.error("Validation failed: First Name is required");
        } else if (!DataValidator.isName(request.getParameter("firstName"))) {
            request.setAttribute("firstName", "Invalid First Name");
            pass = false;
            log.error("Validation failed: Invalid First Name format");
        }

        if (DataValidator.isNull(request.getParameter("lastName"))) {
            request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
            log.error("Validation failed: Last Name is required");
        } else if (!DataValidator.isName(request.getParameter("lastName"))) {
            request.setAttribute("lastName", "Invalid Last Name");
            pass = false;
            log.error("Validation failed: Invalid Last Name format");
        }

        if (DataValidator.isNull(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
            pass = false;
            log.error("Validation failed: Login Id is required");
        } else if (!DataValidator.isEmail(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.email", "Login "));
            pass = false;
            log.error("Validation failed: Invalid Email format");
        }

        if (DataValidator.isNull(request.getParameter("password"))) {
            request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
            pass = false;
            log.error("Validation failed: Password is required");
        } else if (!DataValidator.isPasswordLength(request.getParameter("password"))) {
            request.setAttribute("password", "Password should be 8 to 12 characters");
            pass = false;
            log.error("Validation failed: Password length issue");
        } else if (!DataValidator.isPassword(request.getParameter("password"))) {
            request.setAttribute("password", "Must contain uppercase, lowercase, digit & special character");
            pass = false;
            log.error("Validation failed: Weak password format");
        }

        if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
            log.error("Validation failed: Confirm Password is required");
        }

        if (DataValidator.isNull(request.getParameter("gender"))) {
            request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
            pass = false;
            log.error("Validation failed: Gender is required");
        }

        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date of Birth"));
            pass = false;
            log.error("Validation failed: Date of Birth is required");
        } else if (!DataValidator.isDate(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
            pass = false;
            log.error("Validation failed: Invalid Date of Birth format");
        }

        if (DataValidator.isNull(request.getParameter("roleId"))) {
            request.setAttribute("roleId", PropertyReader.getValue("error.require", "Role"));
            pass = false;
            log.error("Validation failed: Role is required");
        }

        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "MobileNo"));
            pass = false;
            log.error("Validation failed: Mobile No is required");
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Mobile No must have 10 digits");
            pass = false;
            log.error("Validation failed: Invalid Mobile No length");
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Invalid Mobile No");
            pass = false;
            log.error("Validation failed: Invalid Mobile No format");
        }

        if (!request.getParameter("password").equals(request.getParameter("confirmPassword"))
                && !"".equals(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", "Password and Confirm Password must be Same!");
            pass = false;
            log.error("Validation failed: Password and Confirm Password mismatch");
        }

        log.debug("UserCtl validate ended with result: " + pass);
        return pass;
    }

    /**
     * Populates UserBean with data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("UserCtl populateBean started");
        UserBean bean = new UserBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setRoleId(DataUtility.getLong(request.getParameter("roleId")));
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setLogin(DataUtility.getString(request.getParameter("login")));
        bean.setPassword(DataUtility.getString(request.getParameter("password")));
        bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));

        populateDTO(bean, request);
        log.debug("UserCtl populateBean ended");
        return bean;
    }

    /**
     * Handles GET requests
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("UserCtl doGet started");

        String op = DataUtility.getString(request.getParameter("operation"));
        UserModel model = new UserModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (id > 0) {
            try {
                UserBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, request);
              
            } catch (ApplicationException e) {
                log.error("Error in doGet: " + e.getMessage(), e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("UserCtl doGet ended");
    }

    /**
     * Handles POST requests
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("UserCtl doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));
        UserModel model = new UserModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            UserBean bean = (UserBean) populateBean(request);
            try {
                long pk = model.add(bean);
                ServletUtility.setSuccessMessage("User Successfully saved", request);
               
                
            } catch (ApplicationException e) {
                log.error("ApplicationException in Save: " + e.getMessage(), e);
                ServletUtility.handleException(e, request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setErrorMessage("Login Id already exist", request);
                ServletUtility.forward(getView(), request, response);
                log.error("Duplicate login found while saving user", e);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            UserBean bean = (UserBean) populateBean(request);
            try {
                if (bean.getId() > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Data Updated Successfully", request);
              
            } catch (ApplicationException e) {
                log.error("ApplicationException in Update: " + e.getMessage(), e);
                ServletUtility.handleException(e, request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Login Id already exists", request);
                log.error("Duplicate login found while updating user", e);
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
           
            ServletUtility.redirect(ORSView.USER_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {
           
            ServletUtility.redirect(ORSView.USER_CTL, request, response);
            return;
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("UserCtl doPost ended");
    }

    /**
     * Returns the view page for UserCtl
     */
    @Override
    protected String getView() {
        return ORSView.USER_VIEW;
    }
}
