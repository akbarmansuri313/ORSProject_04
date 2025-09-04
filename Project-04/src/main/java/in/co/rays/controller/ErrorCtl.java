package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.util.ServletUtility;

/**
 * ErrorCtl is a controller to handle and forward the error requests
 * to the predefined error view. It extends {@link BaseClt}.
 * 
 * @author Akbar Mansuri
 * @version 1.0
 * @since 2025
 */
@WebServlet(name = "/ErrorCtl", urlPatterns = { "/ErrorCtl" })
public class ErrorCtl extends BaseClt {

    private static final Logger log = Logger.getLogger(ErrorCtl.class);

    /**
     * Handles HTTP GET requests and forwards them to the error view.
     *
     * @param request  the HttpServletRequest object that contains the request
     *                 the client made of the servlet
     * @param response the HttpServletResponse object that contains the response
     *                 the servlet returns to the client
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("ErrorCtl doGet started");
        try {
            ServletUtility.forward(getView(), request, response);
            log.info("Forwarded to error view successfully in doGet");
        } catch (Exception e) {
            log.error("Exception in ErrorCtl doGet", e);
            throw e;
        }
        log.debug("ErrorCtl doGet ended");
    }

    /**
     * Handles HTTP POST requests and forwards them to the error view.
     *
     * @param request  the HttpServletRequest object that contains the request
     *                 the client made of the servlet
     * @param response the HttpServletResponse object that contains the response
     *                 the servlet returns to the client
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("ErrorCtl doPost started");
        try {
            ServletUtility.forward(getView(), request, response);
            log.info("Forwarded to error view successfully in doPost");
        } catch (Exception e) {
            log.error("Exception in ErrorCtl doPost", e);
            throw e;
        }
        log.debug("ErrorCtl doPost ended");
    }

    /**
     * Returns the view page for error handling.
     *
     * @return the path of the error view defined in {@link ORSView}
     */
    @Override
    protected String getView() {
        log.debug("Returning Error View Path");
        return ORSView.ERROR_VIEW;
    }

}
