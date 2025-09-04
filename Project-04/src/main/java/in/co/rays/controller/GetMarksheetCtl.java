package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.MarksheetBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.MarksheetModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * GetMarksheetCtl is a Controller that is responsible for retrieving a
 * Marksheet based on the Roll Number entered by the user.
 * <p>
 * It validates the input, fetches the Marksheet details using
 * {@link MarksheetModel}, and forwards the data to the corresponding view.
 * </p>
 * 
 * @author
 * @version 1.0
 * @since 2025
 */
@WebServlet(name = "/GetMarksheetCtl", urlPatterns = { "/ctl/GetMarksheetCtl" })
public class GetMarksheetCtl extends BaseClt {

	private static final Logger log = Logger.getLogger(GetMarksheetCtl.class);

	/**
	 * Validates the input Roll Number entered by the user.
	 *
	 * @param request the HttpServletRequest containing client input
	 * @return true if validation passes, false otherwise
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("GetMarksheetCtl validate method started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("rollNo"))) {
			request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll Number"));
			log.warn("Validation failed: Roll Number is missing");
			pass = false;
		}

		log.debug("GetMarksheetCtl validate method ended ");
		return pass;
	}

	/**
	 * Populates the MarksheetBean with request parameters.
	 *
	 * @param request the HttpServletRequest containing form data
	 * @return a populated {@link MarksheetBean} instance
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("GetMarksheetCtl populateBean method started");

		MarksheetBean bean = new MarksheetBean();
		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));

		log.debug("GetMarksheetCtl populateBean method Ended");
		return bean;
	}

	/**
	 * Handles HTTP GET requests and forwards to the marksheet view.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info(" GetMarksheetCtl doGet Start");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Handles HTTP POST requests, retrieves marksheet based on Roll Number, and
	 * forwards the result to the view.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("GetMarksheetCtl POST Start");

		String op = DataUtility.getString(request.getParameter("operation"));
		MarksheetModel model = new MarksheetModel();
		MarksheetBean bean = (MarksheetBean) populateBean(request);

		if (OP_GO.equalsIgnoreCase(op)) {
			try {

				bean = model.findByRollNo(bean.getRollNo());

				if (bean != null) {
					ServletUtility.setBean(bean, request);

				} else {
					ServletUtility.setErrorMessage("RollNo does not exist", request);

				}
			} catch (ApplicationException e) {
				
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("GetMarksheetCtl Post Method end");
	}

	/**
	 * Returns the view page for displaying the marksheet.
	 *
	 * @return the path of the GetMarksheet view
	 */
	@Override
	protected String getView() {
		return ORSView.GET_MARKSHEET_VIEW;
	}
}
