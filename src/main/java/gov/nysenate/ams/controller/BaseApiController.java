package gov.nysenate.ams.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nysenate.ams.model.Address;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

    /**
     * Constructs a collection of Address objects using the JSON payload data in the body of the
     * HttpServletRequest. The root JSON element must be an array containing a collection of
     * address component objects e.g
     * <code>
     *  [{"addr1":"", "addr2":"", "city":"", "state":"","zip5":"", "zip4":""} .. ]
     * </code>
     * @param json Json payload
     * @return ArrayList<Address>
     */
    public static ArrayList<Address> getAddressesFromJsonBody(String json)
    {
        ArrayList<Address> addresses = new ArrayList<>();
        try {
            logger.trace("Batch address json body " + json);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            for (int i = 0; i < root.size(); i++) {
                JsonNode addressNode = root.get(i);
                String firm = (addressNode.has("firm")) ? addressNode.get("firm").asText() : "";
                String addr1 = (addressNode.has("addr1")) ? addressNode.get("addr1").asText() : "";
                String addr2 = (addressNode.has("addr2")) ? addressNode.get("addr2").asText() : "";
                String city = (addressNode.has("city")) ? addressNode.get("city").asText() : "";
                String state = (addressNode.has("state")) ? addressNode.get("state").asText() : "";
                String zip5 = (addressNode.has("zip5")) ? addressNode.get("zip5").asText() : "";
                String zip4 = (addressNode.has("zip4")) ? addressNode.get("zip4").asText() : "";
                addresses.add(new Address(firm, addr1, addr2, city, state, zip5, zip4));
            }
        }
        catch(Exception ex) {
            logger.debug("Invalid batch address payload detected.");
            logger.trace(ex);
        }
        return addresses;
    }


}
