package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.RoleModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * RoleCtl Servlet acts as Controller for Role functionality.
 * It handles role creation, updating, validation, and view forwarding.
 * 
 * @author 
 * @version 1.0
 * @since 2023
 */
@WebServlet(name = "RoleCtl", urlPatterns = {"/ctl/RoleCtl"})
public class RoleCtl extends BaseClt {

    /** The log. */
    private static Logger log = Logger.getLogger(RoleCtl.class);

    /**
     * Validates input parameters from request.
     * 
     * @param request HttpServletRequest object
     * @return boolean true if validation passes, false otherwise
     */
    @Override
    public boolean validate(HttpServletRequest request) {
        log.debug("RoleCtl validate started");

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

        if (DataValidator.isNull(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
            log.error("Validation failed: Description is required");
        }

        log.debug("RoleCtl validate End");
        return pass;
    }

    /**
     * Populates RoleBean from request parameters.
     * 
     * @param request HttpServletRequest object
     * @return BaseBean containing form data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("RoleCtl populateBean started");

        RoleBean bean = new RoleBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));

        populateDTO(bean, request);

        log.debug("RoleCtl populateBean End");
        return bean;
    }

    /**
     * Handles HTTP GET request.
     * Loads role data for editing if ID is provided.
     * 
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("RoleCtl doGet Started");

        String op = DataUtility.getString(request.getParameter("operation"));

        RoleModel model = new RoleModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (id > 0 || op != null) {
            RoleBean bean;
            try {
                bean = model.findByPK(id);
                ServletUtility.setBean(bean, request);
                log.info("Role fetched successfully with ID: " + id);
            } catch (Exception e) {
                log.error("Error in RoleCtl doGet: ", e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("RoleCtl doGet End");
    }

    /**
     * Handles HTTP POST request.
     * Performs Create, Update, Reset, and Cancel operations on Role.
     * 
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("RoleCtl doPost Started");

        String op = DataUtility.getString(request.getParameter("operation"));
        RoleModel model = new RoleModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op)) {
            RoleBean bean = (RoleBean) populateBean(request);

            try {
                long pk = model.add(bean);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Data is successfully saved", request);
                log.info("Role saved successfully with ID: " + pk);

            } catch (ApplicationException e) {
                log.error("ApplicationException in doPost Save: ", e);
                ServletUtility.handleException(e, request, response);
                return;

            } catch (DuplicateRecordException e) {
                log.error("DuplicateRecordException in doPost Save: Role already exists", e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Role already exists", request);
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            RoleBean bean = (RoleBean) populateBean(request);

            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Data is successfully updated", request);
               
            } catch (ApplicationException e) {
                log.error("ApplicationException in doPost Update: ", e);
                ServletUtility.handleException(e, request, response);
                return;

            } catch (DuplicateRecordException e) {
                log.error("DuplicateRecordException in doPost Update: Role already exists", e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Role already exists", request);
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            
            ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {
           
            ServletUtility.redirect(ORSView.ROLE_CTL, request, response);
            return;
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("RoleCtl doPost End");
    }

    /**
     * Returns the view page of Role.
     * 
     * @return String path of Role view JSP
     */
    @Override
    protected String getView() {
        return ORSView.ROLE_VIEW;
    }
}
