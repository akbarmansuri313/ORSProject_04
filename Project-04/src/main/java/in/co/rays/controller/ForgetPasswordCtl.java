package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * ForgetPasswordCtl handles the functionality for the "Forget Password" use case.
 * <p>
 * This controller validates the user's email (login), checks if it exists in
 * the system, and sends the password to the registered email address.
 * </p>
 * 
 * @author Akbar
 * @version 1.0
 * @since 2023
 */
@WebServlet(name = "ForgetPasswordCtl", urlPatterns = { "/ForgetPasswordCtl" })
public class ForgetPasswordCtl extends BaseClt {

    private static final Logger log = Logger.getLogger(ForgetPasswordCtl.class);

    /**
     * Validates the input fields from the request.
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("ForgetPasswordCtl validate started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.require", "Email Id"));
            pass = false;
            log.error("Validation failed: Email Id is required");
        } else if (!DataValidator.isEmail(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.email", "Login "));
            pass = false;
            log.error("Validation failed: Invalid Email format");
        }

        log.debug("ForgetPasswordCtl validate ended");
        return pass;
    }

    /**
     * Populates a UserBean object with request parameters.
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("ForgetPasswordCtl populateBean started");

        UserBean bean = new UserBean();
        bean.setLogin(DataUtility.getString(request.getParameter("login")));

        log.debug("ForgetPasswordCtl populateBean ended ");
        return bean;
    }

    /**
     * Handles GET requests. Forwards the request to the Forget Password view.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("ForgetPasswordCtl doGet started");
        ServletUtility.forward(getView(), request, response);
        log.debug("ForgetPasswordCtl doGet ended");
    }

    /**
     * Handles POST requests. Processes the forget password operation.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("ForgetPasswordCtl doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));
        UserBean bean = (UserBean) populateBean(request);

        UserModel model = new UserModel();

        if (OP_GO.equalsIgnoreCase(op)) {
            try {
                log.info("Attempting to send password to email: " + bean.getLogin());

                boolean flag = model.forgetPassword(bean.getLogin());

                if (flag) {
                    ServletUtility.setSuccessMessage("Password has been sent to your email id", request);
                    log.info("Password sent successfully to " + bean.getLogin());
                }
            } catch (RecordNotFoundException e) {
                log.error("RecordNotFoundException: " + e.getMessage(), e);
                ServletUtility.setErrorMessage(e.getMessage(), request);
            } catch (ApplicationException e) {
                log.error("ApplicationException: " + e.getMessage(), e);
                ServletUtility.setErrorMessage("Please check your internet connection...!!!", request);
            }
            ServletUtility.forward(getView(), request, response);
        }

        log.debug("ForgetPasswordCtl doPost ended");
    }

    /**
     * Returns the view page for Forget Password functionality.
     */
    @Override
    protected String getView() {
        return ORSView.FORGET_PASSWORD_VIEW;
    }
}
