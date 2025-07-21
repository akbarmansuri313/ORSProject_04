package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

@WebServlet("/LoginCtl")
public class LoginCtl extends BaseClt {

	public static final String OP_SIGN_IN = "SignIn";

//	public static final String OP_REGISTER = "Register";

	public static final String OP_SIGN_UP = "SignUp";

	public static final String OP_LOG_OUT = "LogOut";

	@Override
	public boolean validate(HttpServletRequest request) {

		boolean isValid = true;

		String op = request.getParameter("operation");

		if (OP_SIGN_UP.equals(op) || (OP_LOG_OUT.equals(op))) {

			return isValid;

		}

		if (DataValidator.isNull(request.getParameter("login"))) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "login Id"));
			isValid = false;
		} else if (!DataValidator.isEmail(request.getParameter("login"))) {
			request.setAttribute("login", "Invalid Login Id");
			isValid = false;
		}
		if (DataValidator.isNull(request.getParameter("password"))) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "password"));
			isValid = false;
		}
		return isValid;

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		UserBean bean = new UserBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		String op = DataUtility.getString(request.getParameter("operation"));

		if (OP_LOG_OUT.equals(op)) {
			
			session.invalidate();
			
			ServletUtility.setSuccessMessage("Logout Successful..!", request);
			
			ServletUtility.forward(getView(), request, response);
			
			return;
		}
		ServletUtility.forward(getView(), request, response);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		String op = DataUtility.getString(request.getParameter("operation"));

		UserModel model = new UserModel();
		RoleModel role = new RoleModel();

		if (OP_SIGN_IN.equalsIgnoreCase(op)) {

			UserBean bean = (UserBean) populateBean(request);

			try {
				bean = model.authenticate(bean.getLogin(), bean.getPassword());

				if (bean != null) {

					session.setAttribute("user", bean);

					RoleBean rolebean = role.findByPK(bean.getRoleId());

					if (rolebean != null) {
						session.setAttribute("role", rolebean.getName());
					}
					ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);

					
					return;

				} else {

					bean = (UserBean) populateBean(request);
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Invalid LoginId And Password", request);

				}
			} catch (ApplicationException e) {

				e.printStackTrace();

				return;
			} catch (Exception e) {

				e.printStackTrace();
			}
		} else if (OP_SIGN_UP.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);

			return;
		}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {

		return ORSView.LOGIN_VIEW;
	}

}
