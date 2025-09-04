package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.MarksheetBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.MarksheetModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * Controller to handle requests for displaying the Marksheet Merit List.
 * 
 * It retrieves a list of top students based on merit and forwards the 
 * data to the corresponding view for display.
 * 
 * Supported operations:
 * <ul>
 *   <li>Display merit list on GET</li>
 *   <li>Redirect to Welcome page on BACK operation (POST)</li>
 * </ul>
 * 
 * @author 
 * @version 1.0
 */
@WebServlet(name = "/MarksheetMeritListCtl", urlPatterns = { "/ctl/MarksheetMeritListCtl" })
public class MarksheetMeritListCtl extends BaseClt {

    /** The log instance for this class */
    private static Logger log = Logger.getLogger(MarksheetMeritListCtl.class);

    /**
     * Handles GET requests to fetch and display the merit list of marksheets.
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("MarksheetMeritListCtl doGet started");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        MarksheetModel model = new MarksheetModel();

        try {
            List<MarksheetBean> list = model.getMeritList(pageNo, pageSize);

            if (list == null || list.isEmpty()) {
               
                ServletUtility.setErrorMessage("No record found", request);
            } else {
                log.debug("Merit list retrieved successfully, size: " + list.size());
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);

            ServletUtility.forward(getView(), request, response);
            

        } catch (ApplicationException e) {
            log.error("Error in MarksheetMeritListCtl doGet", e);
            ServletUtility.handleException(e, request, response);
            return;
        }
        log.debug("MarksheetMeritListCtl doGet End");
    }

    /**
     * Handles POST requests for operations like BACK navigation.
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("MarksheetMeritListCtl doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));

        if (OP_BACK.equalsIgnoreCase(op)) {
            log.info("Back operation invoked, redirecting to WelcomeCtl");
            ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
            return;
        }

        log.debug("MarksheetMeritListCtl doPost End");
    }

    /**
     * Returns the view page for the Marksheet Merit List.
     * 
     * @return path of the merit list view page
     */
    @Override
    protected String getView() {
        return ORSView.MARKSHEET_MERIT_LIST_VIEW;
    }
}
