package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * MyProfileCtl Controller handles user profile management such as 
 * viewing and updating personal details and redirecting to change password.
 * 
 * @author Akbar Mansuri
 * @version 1.0
 * @since 2025
 */
@WebServlet(name = "MyProfileCtl", urlPatterns = { "/ctl/MyProfileCtl" })
public class MyProfileCtl extends BaseClt {

    private static final Logger log = Logger.getLogger(MyProfileCtl.class);

    /** Operation constant for change password request */
    public static final String OP_CHANGE_MY_PASSWORD = "Change Password";

    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("MyProfileCtl validate started");

        boolean pass = true;
        String op = DataUtility.getString(request.getParameter("operation"));

        if (OP_CHANGE_MY_PASSWORD.equalsIgnoreCase(op) || op == null) {
            return pass;
        }
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
        if (DataValidator.isNull(request.getParameter("gender"))) {
            request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
            pass = false;
            log.error("Gender is required");
        }
        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "MobileNo"));
            pass = false;
            log.error("Mobile number is required");
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Mobile No must have 10 digits");
            pass = false;
            log.error("Mobile number must be 10 digits");
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Invalid Mobile No");
            pass = false;
            log.error("Invalid mobile number format");
        }
        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
            log.error("Date of Birth is required");
        }
        log.debug("MyProfileCtl validate ended");
        return pass;
    }
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("MyProfileCtl populateBean started");

        UserBean bean = new UserBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setLogin(DataUtility.getString(request.getParameter("login")));
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));

        populateDTO(bean, request);

        log.debug("MyProfileCtl populateBean ended");
        return bean;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("MyProfileCtl doGet started");

        HttpSession session = request.getSession(true);
        UserBean user = (UserBean) session.getAttribute("user");
        long id = user.getId();

        UserModel model = new UserModel();

        if (id > 0) {
            try {
                UserBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, request);
               
            } catch (ApplicationException e) {
                log.error("ApplicationException in doGet: ", e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("MyProfileCtl doGet ended");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("MyProfileCtl doPost started");

        HttpSession session = request.getSession(true);
        UserBean user = (UserBean) session.getAttribute("user");
        long id = user.getId();

        String op = DataUtility.getString(request.getParameter("operation"));
        UserModel model = new UserModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            UserBean bean = (UserBean) populateBean(request);

            try {
                if (id > 0) {
                    user.setFirstName(bean.getFirstName());
                    user.setLastName(bean.getLastName());
                    user.setGender(bean.getGender());
                    user.setMobileNo(bean.getMobileNo());
                    user.setDob(bean.getDob());

                    model.update(user);   
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Profile has been updated Successfully. ", request);

            } catch (DuplicateRecordException e) {
                log.error("DuplicateRecordException: Login ID already exists", e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Login id already exists", request);
            } catch (ApplicationException e) {
                log.error("ApplicationException in doPost: ", e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        } else if (OP_CHANGE_MY_PASSWORD.equalsIgnoreCase(op)) {
          
            ServletUtility.redirect(ORSView.CHANGE_PASSWORD_CTL, request, response);
            return;
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("MyProfileCtl doPost ended");
    }
    @Override
    protected String getView() {
        return ORSView.MY_PROFILE_VIEW;
    }
}
