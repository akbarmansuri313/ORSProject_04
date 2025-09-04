package in.co.rays.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.controller.BaseClt;
import in.co.rays.controller.ORSView;

/**
 * ServletUtility is a utility class that provides helper methods for
 * servlet operations such as forwarding, redirecting, setting/getting
 * request attributes, and handling exceptions.
 * <p>
 * This class simplifies common tasks in servlet-based applications.
 * </p>
 * 
 * Example usage:
 * <pre>
 * ServletUtility.setErrorMessage("Error occurred", request);
 * ServletUtility.forward("/welcome.jsp", request, response);
 * </pre>
 * 
 * @author Akbar
 * @version 1.0
 */
public class ServletUtility {

    /**
     * Forwards the request to the specified page.
     *
     * @param page     the target page to forward to
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws IOException
     * @throws ServletException
     */
    public static void forward(String page, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(page);
        rd.forward(request, response);
    }

    /**
     * Redirects the response to the specified page.
     *
     * @param page     the target page to redirect to
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws IOException
     * @throws ServletException
     */
    public static void redirect(String page, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.sendRedirect(page);
    }

    /**
     * Retrieves an error message from the request attributes.
     *
     * @param property the attribute name
     * @param request  the HttpServletRequest object
     * @return the error message, or empty string if not found
     */
    public static String getErrorMessage(String property, HttpServletRequest request) {
        String val = (String) request.getAttribute(property);
        return val != null ? val : "";
    }

    /**
     * Retrieves a generic message from the request attributes.
     *
     * @param property the attribute name
     * @param request  the HttpServletRequest object
     * @return the message, or empty string if not found
     */
    public static String getMessage(String property, HttpServletRequest request) {
        String val = (String) request.getAttribute(property);
        return val != null ? val : "";
    }

    /**
     * Sets an error message in the request attributes.
     *
     * @param msg     the error message
     * @param request the HttpServletRequest object
     */
    public static void setErrorMessage(String msg, HttpServletRequest request) {
        request.setAttribute(BaseClt.MSG_ERROR, msg);
    }

    /**
     * Retrieves the error message from the request attributes.
     *
     * @param request the HttpServletRequest object
     * @return the error message, or empty string if not found
     */
    public static String getErrorMessage(HttpServletRequest request) {
        String val = (String) request.getAttribute(BaseClt.MSG_ERROR);
        return val != null ? val : "";
    }

    /**
     * Sets a success message in the request attributes.
     *
     * @param msg     the success message
     * @param request the HttpServletRequest object
     */
    public static void setSuccessMessage(String msg, HttpServletRequest request) {
        request.setAttribute(BaseClt.MSG_SUCCESS, msg);
    }

    /**
     * Retrieves the success message from the request attributes.
     *
     * @param request the HttpServletRequest object
     * @return the success message, or empty string if not found
     */
    public static String getSuccessMessage(HttpServletRequest request) {
        String val = (String) request.getAttribute(BaseClt.MSG_SUCCESS);
        return val != null ? val : "";
    }

    /**
     * Sets a BaseBean object in the request attributes.
     *
     * @param bean    the BaseBean object
     * @param request the HttpServletRequest object
     */
    public static void setBean(BaseBean bean, HttpServletRequest request) {
        request.setAttribute("bean", bean);
    }

    /**
     * Retrieves a BaseBean object from the request attributes.
     *
     * @param request the HttpServletRequest object
     * @return the BaseBean object, or null if not found
     */
    public static BaseBean getBean(HttpServletRequest request) {
        return (BaseBean) request.getAttribute("bean");
    }

    /**
     * Retrieves a parameter value from the request.
     *
     * @param property the parameter name
     * @param request  the HttpServletRequest object
     * @return the parameter value, or empty string if not found
     */
    public static String getParameter(String property, HttpServletRequest request) {
        String val = (String) request.getParameter(property);
        return val != null ? val : "";
    }

    /**
     * Sets a list of objects in the request attributes.
     *
     * @param list    the list of objects
     * @param request the HttpServletRequest object
     */
    public static void setList(List list, HttpServletRequest request) {
        request.setAttribute("list", list);
    }

    /**
     * Retrieves a list of objects from the request attributes.
     *
     * @param request the HttpServletRequest object
     * @return the list of objects, or null if not found
     */
    public static List getList(HttpServletRequest request) {
        return (List) request.getAttribute("list");
    }

    /**
     * Sets the current page number in the request attributes.
     *
     * @param pageNo  the page number
     * @param request the HttpServletRequest object
     */
    public static void setPageNo(int pageNo, HttpServletRequest request) {
        request.setAttribute("pageNo", pageNo);
    }

    /**
     * Retrieves the current page number from the request attributes.
     *
     * @param request the HttpServletRequest object
     * @return the page number
     */
    public static int getPageNo(HttpServletRequest request) {
        return (Integer) request.getAttribute("pageNo");
    }

    /**
     * Sets the page size in the request attributes.
     *
     * @param pageSize the page size
     * @param request  the HttpServletRequest object
     */
    public static void setPageSize(int pageSize, HttpServletRequest request) {
        request.setAttribute("pageSize", pageSize);
    }

    /**
     * Retrieves the page size from the request attributes.
     *
     * @param request the HttpServletRequest object
     * @return the page size
     */
    public static int getPageSize(HttpServletRequest request) {
        return (Integer) request.getAttribute("pageSize");
    }

    /**
     * Handles exceptions by setting the exception object in the request
     * attributes and redirecting to the error page.
     *
     * @param e       the exception object
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws IOException
     * @throws ServletException
     */
    public static void handleException(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setAttribute("exception", e);
        response.sendRedirect(ORSView.ERROR_CTL);
    }
}
