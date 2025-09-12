package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.util.ServletUtility;

/**
 * WelcomeCtl Servlet handles requests for the Welcome page of the application.
 * It forwards all GET requests to the welcome view.
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
@WebServlet(name = "WelcomeCtl", urlPatterns = { "/WelcomeCtl" })
public class WelcomeCtl extends BaseClt {

    /** The log object for this class */
    private static final Logger log = Logger.getLogger(WelcomeCtl.class);

    /**
     * Handles HTTP GET request.
     * Forwards the request to the welcome view page.
     *
     * @param request  HttpServletRequest object containing client request
     * @param response HttpServletResponse object containing servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("WelcomeCtl doGet Started");
        try {
            ServletUtility.forward(getView(), request, response);
            
        } catch (Exception e) {
            log.error("Error in WelcomeCtl doGet", e);
            throw new ServletException("Exception in WelcomeCtl", e);
        }
        log.debug("WelcomeCtl doGet Ended");
    }

    /**
     * Returns the view page for the Welcome page.
     *
     * @return the path of the welcome view page
     */
    @Override
    protected String getView() {
        return ORSView.WELCOME_VIEW;
    }
}
