
package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.RoleModel;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * LoginCtl is a Controller class that handles Login, Logout, and SignUp
 * operations.
 * <p>
 * It validates user credentials, manages session handling, and redirects users
 * to appropriate views based on their role or actions performed.
 * </p>
 *
 * @author Akbar Mansuri
 * @version 1.0
 * @since 2023
 */
@WebServlet(name = "LoginCtl", urlPatterns = { "/LoginCtl" })
public class LoginCtl extends BaseClt {

	/** Logger instance for debugging and error logging */
	private static Logger log = Logger.getLogger(LoginCtl.class);

	/** Operation constant for SignIn. */
	public static final String OP_SIGN_IN = "SignIn";

	/** Operation constant for SignUp. */
	public static final String OP_SIGN_UP = "SignUp";

	/** Operation constant for LogOut. */
	public static final String OP_LOG_OUT = "LogOut";

	/**
	 * Validates the login form inputs.
	 * <p>
	 * Ensures that login ID and password are entered correctly. Allows skipping
	 * validation for SignUp and LogOut operations.
	 * </p>
	 *
	 * @param request the {@link HttpServletRequest} object containing client inputs
	 * @return true if input is valid, false otherwise
	 */
	@Override
	public boolean validate(HttpServletRequest request) {

		log.debug("LoginCtl validate started");

		boolean isValid = true;

		String op = request.getParameter("operation");

		if (OP_SIGN_UP.equals(op) || (OP_LOG_OUT.equals(op))) {
			return isValid;
		}

		if (DataValidator.isNull(request.getParameter("login"))) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "login Id"));
			log.error("Validation Error: Login ID is required");
			isValid = false;

		} else if (!DataValidator.isEmail(request.getParameter("login"))) {
			request.setAttribute("login", "Invalid Login Id");
			log.error("Validation Error: Invalid Login ID format");
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("password"))) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "password"));
			log.error("Validation Error: Password is required");
			isValid = false;
		}

		log.debug("LoginCtl validate Ended with status: " + isValid);
		return isValid;
	}

	/**
	 * Populates a {@link UserBean} with request parameters.
	 *
	 * @param request the {@link HttpServletRequest} containing form data
	 * @return populated {@link UserBean}
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("LoginCtl populateBean started");

		UserBean bean = new UserBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));

		log.debug("LoginCtl populateBean ended");
		return bean;
	}

	/**
	 * Handles GET requests for login and logout operations.
	 * <p>
	 * If operation is LogOut, the session is invalidated and the user is redirected
	 * back to the login view with a success message.
	 * </p>
	 *
	 * @param request  the {@link HttpServletRequest} object
	 * @param response the {@link HttpServletResponse} object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("LoginCtl doGet Started");

		HttpSession session = request.getSession();
		String op = DataUtility.getString(request.getParameter("operation"));

		if (OP_LOG_OUT.equals(op)) {
			session.invalidate();
			ServletUtility.setSuccessMessage("Logout Successful..!", request);
			ServletUtility.forward(getView(), request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("LoginCtl doGet Ended");
	}

	/**
	 * Handles POST requests for Login and SignUp operations.
	 * <p>
	 * - On SignIn: Authenticates user credentials, sets session attributes, and
	 * redirects to Welcome view if successful. - On failure: Returns error message
	 * and redisplays login form. - On SignUp: Redirects to User Registration
	 * Controller.
	 * </p>
	 *
	 * @param request  the {@link HttpServletRequest} object
	 * @param response the {@link HttpServletResponse} object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("LoginCtl doPost Started");

		HttpSession session = request.getSession();
		String op = DataUtility.getString(request.getParameter("operation"));

		UserModel model = new UserModel();
		RoleModel role = new RoleModel();

		if (OP_SIGN_IN.equalsIgnoreCase(op)) {

			log.debug("Operation: SignIn");

			UserBean bean = (UserBean) populateBean(request);

			String str = request.getParameter("uri");

			try {
				bean = model.authenticate(bean.getLogin(), bean.getPassword());

				if (bean != null) {
					session.setAttribute("user", bean);

					RoleBean rolebean = role.findByPK(bean.getRoleId());
					if (rolebean != null) {
						session.setAttribute("role", rolebean.getName());
					}
					if ("null".equalsIgnoreCase(str)) {
						ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
						return; 
					} else {
						ServletUtility.redirect(str, request, response);
						return;
					}

				} else {
					bean = (UserBean) populateBean(request);
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Invalid LoginId And Password", request);
				}
			} catch (ApplicationException e) {
				log.error("ApplicationException in LoginCtl", e);
				ServletUtility.handleException(e, request, response);
				return;

			} catch (Exception e) {
				log.error("Unexpected Exception in LoginCtl", e);
			}

		} else if (OP_SIGN_UP.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("LoginCtl doPost Ended");
	}

	/**
	 * Returns the view page for login.
	 *
	 * @return path of the login view
	 */
	@Override
	protected String getView() {
		return ORSView.LOGIN_VIEW;
	}
}
