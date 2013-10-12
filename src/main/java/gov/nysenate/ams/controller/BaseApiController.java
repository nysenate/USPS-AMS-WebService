package gov.nysenate.ams.controller;

import gov.nysenate.ams.model.Address;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseApiController extends HttpServlet
{
    private static Logger logger = Logger.getLogger(BaseApiController.class);
    public abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
    public abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
    public abstract void init(ServletConfig config) throws ServletException;

    /**
     * Constructs a new Address object using the query parameters of the supplied HttpServletRequest.
     * This method exists to provide consistency among the different controllers when retrieving an
     * address from the query string.
     * @param request HttpServletRequest object
     * @return new Address instance if request was valid
     *         null if request was null
     */
    public static Address getAddressFromParams(HttpServletRequest request)
    {
        Address address = null;
        if (request != null){
            if (request.getParameter("addr") != null) {
                address = new Address(request.getParameter("addr"));
            }
            else {
                address = new Address(request.getParameter("firm"),  request.getParameter("addr1"),
                                      request.getParameter("addr2"), request.getParameter("city"),
                                      request.getParameter("state"), request.getParameter("zip5"),
                                      request.getParameter("zip4"));
            }
        }
        return address;
    }
}
