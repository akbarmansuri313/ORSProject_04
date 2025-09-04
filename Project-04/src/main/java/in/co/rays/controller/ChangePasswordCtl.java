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
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * Controller for handling change password functionality.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Validate user input for old, new and confirm password</li>
 *   <li>Ensure new password follows security constraints</li>
 *   <li>Update user password in database if validation passes</li>
 *   <li>Redirect to profile page if requested</li>
 * </ul>
 * </p>
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
@WebServlet(name = "ChangePasswordCtl", urlPatterns = { "/ctl/ChangePasswordCtl" })
public class ChangePasswordCtl extends BaseClt {

    /** Logger instance for debugging and error logging */
    private static Logger log = Logger.getLogger(ChangePasswordCtl.class);

    /** Constant for change my profile operation */
    public static final String OP_CHANGE_MY_PROFILE = "Change My Profile";

    /**
     * Validates user inputs for change password form.
     * 
     * @param request HTTP request containing form inputs
     * @return true if validation succeeds, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("ChangePasswordCtl validate started");

        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op)) {
            log.debug("Validation skipped for Change My Profile operation");
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("oldPassword"))) {
            request.setAttribute("oldPassword", PropertyReader.getValue("error.require", "Old Password"));
            pass = false;
            log.error("Validation failed: Old password is required");
        } else if (request.getParameter("oldPassword").equals(request.getParameter("newPassword"))) {
            request.setAttribute("newPassword", "Old and New passwords should be different");
            pass = false;
            log.error("Validation failed: Old and New passwords are same");
        }

        if (DataValidator.isNull(request.getParameter("newPassword"))) {
            request.setAttribute("newPassword", PropertyReader.getValue("error.require", "New Password"));
            pass = false;
            log.error("Validation failed: New password is required");
        } else if (!DataValidator.isPasswordLength(request.getParameter("newPassword"))) {
            request.setAttribute("newPassword", "Password should be 8 to 12 characters");
            pass = false;
            log.error("Validation failed: Invalid password length");
        } else if (!DataValidator.isPassword(request.getParameter("newPassword"))) {
            request.setAttribute("newPassword", "Must contain uppercase, lowercase, digit & special character");
            pass = false;
            log.error("Validation failed: Password does not meet complexity requirements");
        }

        if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
            log.error("Validation failed: Confirm password is required");
        }

        if (!request.getParameter("newPassword").equals(request.getParameter("confirmPassword"))
                && !"".equals(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", "New and confirm passwords not matched");
            pass = false;
            log.error("Validation failed: New and Confirm password mismatch");
        }

        log.debug("ChangePasswordCtl validate ended with pass = " + pass);
        return pass;
    }

    /**
     * Populates UserBean from request parameters.
     * 
     * @param request HTTP request
     * @return populated UserBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("ChangePasswordCtl populateBean started");

        UserBean bean = new UserBean();
        bean.setPassword(DataUtility.getString(request.getParameter("oldPassword")));
        bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));
        populateDTO(bean, request);

        log.debug("ChangePasswordCtl populateBean ended");
        return bean;
    }

    /**
     * Handles HTTP GET request. Forwards to change password view.
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("ChangePasswordCtl doGet started");
        ServletUtility.forward(getView(), request, response);
        log.debug("ChangePasswordCtl doGet ended");
    }

    /**
     * Handles HTTP POST request for password change operations.
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("ChangePasswordCtl doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));
        String newPassword = (String) request.getParameter("newPassword");

        UserBean bean = (UserBean) populateBean(request);
        UserModel model = new UserModel();

        HttpSession session = request.getSession(true);
        UserBean user = (UserBean) session.getAttribute("user");
        long id = user.getId();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            log.debug("Operation: SAVE");
            try {
                boolean flag = model.changePassword(id, bean.getPassword(), newPassword);
                if (flag == true) {
                    log.info("Password changed successfully for User ID: " + id);
                    bean = model.findByLogin(user.getLogin());
                    session.setAttribute("user", bean);
                    ServletUtility.setBean(bean, request);
                    ServletUtility.setSuccessMessage("Password has been changed Successfully", request);
                }
            } catch (RecordNotFoundException e) {
                log.error("Invalid old password for User ID: " + id, e);
                ServletUtility.setErrorMessage("Old Password is Invalid", request);
            } catch (ApplicationException e) {
                log.error("ApplicationException in doPost", e);
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        } else if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op)) {
            log.debug("Operation: CHANGE_MY_PROFILE");
            ServletUtility.redirect(ORSView.MY_PROFILE_CTL, request, response);
            return;
        }

        ServletUtility.forward(ORSView.CHANGE_PASSWORD_VIEW, request, response);
        log.debug("ChangePasswordCtl doPost ended");
    }

    /**
     * Returns the view for Change Password screen.
     * 
     * @return view page for change password
     */
    @Override
    protected String getView() {
        return ORSView.CHANGE_PASSWORD_VIEW;
    }
}
