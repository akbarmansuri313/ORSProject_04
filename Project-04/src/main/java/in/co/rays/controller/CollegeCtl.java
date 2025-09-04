package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CollegeModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * CollegeCtl is a Controller to handle College operations such as
 * Add, Update, Reset and Cancel.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Validate college form data</li>
 *   <li>Populate CollegeBean from request parameters</li>
 *   <li>Interact with CollegeModel for DB operations</li>
 *   <li>Forward or redirect to respective views</li>
 * </ul>
 * </p>
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
@WebServlet(name = "CollegeCtl", urlPatterns = {"/ctl/CollegeCtl"})
public class CollegeCtl extends BaseClt {

    /** Logger for debugging and error logging */
    private static Logger log = Logger.getLogger(CollegeCtl.class);

    /**
     * Validates input data from College form.
     * <ul>
     * <li>Checks required fields (Name, Address, State, City, Phone No)</li>
     * <li>Ensures Name is valid</li>
     * <li>Validates Phone number length and format</li>
     * </ul>
     * 
     * @param request HTTP request containing form data
     * @return true if all validations pass, false otherwise
     */
    @Override
    public boolean validate(HttpServletRequest request) {
        log.debug("CollegeCtl validate started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
            log.error("Validation failed: Name is required");
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Name");
            pass = false;
            log.error("Validation failed: Invalid Name format");
        }

        if (DataValidator.isNull(request.getParameter("address"))) {
            request.setAttribute("address", PropertyReader.getValue("error.require", "Address"));
            pass = false;
            log.error("Validation failed: Address is required");
        }

        if (DataValidator.isNull(request.getParameter("state"))) {
            request.setAttribute("state", PropertyReader.getValue("error.require", "State"));
            pass = false;
            log.error("Validation failed: State is required");
        }

        if (DataValidator.isNull(request.getParameter("city"))) {
            request.setAttribute("city", PropertyReader.getValue("error.require", "City"));
            pass = false;
            log.error("Validation failed: City is required");
        }

        if (DataValidator.isNull(request.getParameter("phoneNo"))) {
            request.setAttribute("phoneNo", PropertyReader.getValue("error.require", "Phone No"));
            pass = false;
            log.error("Validation failed: Phone No is required");
        } else if (!DataValidator.isPhoneLength(request.getParameter("phoneNo"))) {
            request.setAttribute("phoneNo", "Phone No must have 10 digits");
            pass = false;
            log.error("Validation failed: Phone number length incorrect");
        } else if (!DataValidator.isPhoneNo(request.getParameter("phoneNo"))) {
            request.setAttribute("phoneNo", "Invalid Phone No");
            pass = false;
            log.error("Validation failed: Invalid phone number format");
        }

        log.debug("CollegeCtl validate ended = " + pass);
        return pass;
    }

    /**
     * Populates CollegeBean with request parameters.
     * 
     * @param request HTTP request containing College details
     * @return CollegeBean object with populated data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("CollegeCtl populateBean started");

        CollegeBean bean = new CollegeBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setAddress(DataUtility.getString(request.getParameter("address")));
        bean.setState(DataUtility.getString(request.getParameter("state")));
        bean.setCity(DataUtility.getString(request.getParameter("city")));
        bean.setPhoneNo(DataUtility.getString(request.getParameter("phoneNo")));

        populateDTO(bean, request);

        log.debug("CollegeCtl populateBean ended");
        return bean;
    }

    /**
     * Handles HTTP GET request to fetch College details by ID (for edit purpose).
     * If ID is present, fetches data from CollegeModel and sets it in request.
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("CollegeCtl doGet started");

        String op = DataUtility.getString(request.getParameter("operation"));
        log.debug("Operation received: " + op);

        CollegeModel model = new CollegeModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (id > 0) {
            try {
                CollegeBean bean = model.findByPK(id);
                ServletUtility.setBean(bean, request);
                log.info("College details fetched for ID: " + id);
            } catch (ApplicationException e) {
                log.error("ApplicationException in doGet", e);
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("CollegeCtl doGet ended");
    }

    /**
     * Handles HTTP POST request for College operations like SAVE, UPDATE, RESET,
     * and CANCEL.
     * <ul>
     * <li>SAVE → Adds new College record</li>
     * <li>UPDATE → Updates existing College record</li>
     * <li>CANCEL → Redirects to College list</li>
     * <li>RESET → Clears form and reloads page</li>
     * </ul>
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("CollegeCtl doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));
        log.debug("Operation received: " + op);

        CollegeModel model = new CollegeModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op)) {
            CollegeBean bean = (CollegeBean) populateBean(request);
            try {
                long pk = model.add(bean);
                log.info("College saved successfully with ID: " + pk);

                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Data is successfully saved", request);
            } catch (ApplicationException | DuplicateRecordException e) {
                log.error("Error while saving College", e);

                ServletUtility.setSuccessMessage("College Name already exist", request);
                ServletUtility.forward(getView(), request, response);

                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            CollegeBean bean = (CollegeBean) populateBean(request);
            try {
                if (id > 0) {
                    model.update(bean);
                    log.info("College updated successfully with ID: " + id);
                }

                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Data is successfully update", request);
            } catch (ApplicationException e) {
                log.error("ApplicationException while updating College", e);
                e.printStackTrace();
            } catch (DuplicateRecordException e) {
                log.error("DuplicateRecordException: College name already exists", e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("College Name Already exist", request);
                e.printStackTrace();
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            log.debug("Operation: CANCEL");
            ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            log.debug("Operation: RESET");
            ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("CollegeCtl doPost ended");
    }

    /**
     * Returns the view page for College form.
     * 
     * @return String constant of College view page
     */
    @Override
    protected String getView() {
        return ORSView.COLLEGE_VIEW;
    }
}
