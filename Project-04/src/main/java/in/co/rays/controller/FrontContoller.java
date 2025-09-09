package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.rays.util.ServletUtility;

/**
 * FrontContoller is a servlet filter that acts as a Front Controller
 * for all incoming requests to the application.
 * 
 * It intercepts requests to specific URL patterns ("/doc/*", "/ctl/*"),
 * checks for a valid user session, and ensures that only authenticated
 * users can access secured resources.
 * 
 * If the session has expired or the user is not logged in, the request
 * is forwarded to the login view with an appropriate error message.
 * 
 * @author Akbar
 * @version 1.0
 * @since 2023
 */
@WebFilter(filterName = "FrontContoller", urlPatterns = { "/ctl/*", "/doc/*" })
public class FrontContoller implements Filter {

    private static final Logger log = Logger.getLogger(FrontContoller.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("FrontController init Start");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);

        log.debug("FrontController started ");
        
        String uri = request.getRequestURI(); 
        request.setAttribute("uri", uri);

        try {
            if ( session.getAttribute("user") == null) {
                request.setAttribute("error", "Your session has expired. Please login again.");
                ServletUtility.forward(ORSView.LOGIN_VIEW, request, response);
                return;
            } else {
                
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
          
            throw new ServletException("An unexpected error occurred in FrontController", e);
        }

        log.debug("FrontController finished");
    }

    @Override
    public void destroy() {
        log.debug("FrontController Filter destroyed");
    }
}
