package gov.nysenate.ams.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nysenate.ams.model.Address;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseApiController extends HttpServlet
{
    private static Logger logger = LoggerFactory.getLogger(BaseApiController.class);
    private static ObjectMapper mapper = new ObjectMapper();
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
    public static ArrayList<Address> getAddressesFromJson(String json)
    {
        ArrayList<Address> addresses = new ArrayList<>();
        try {
            logger.trace("Batch address json body " + json);
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
                Integer id = (addressNode.has("id")) ? addressNode.get("id").asInt() : null;
                addresses.add(new Address(firm, addr1, addr2, city, state, zip5, zip4, id));
            }
        }
        catch(Exception ex) {
            logger.debug("Invalid batch address payload detected.", ex);
        }
        return addresses;
    }

    /**
     * Retrieve zip5 value from query parameter.
     * @param request HttpServletRequest.
     * @return zip5 string or empty string if param doesn't exist.
     */
    public static String getZip5FromParams(HttpServletRequest request)
    {
        String zip5 = request.getParameter("zip5");
        return (zip5 != null) ? zip5 : "";
    }

    /**
     * Retrieve zip5 value from query parameter.
     * @param json Json string containing array of zip5 strings.
     * @return ArrayList<String>
     */
    public static List<String> getZip5ListFromJson(String json)
    {
        List<String> zip5List = new ArrayList<>();
        try {
            logger.trace("Batch zip5 json body " + json);
            JsonNode root = mapper.readTree(json);
            for (int i = 0; i < root.size(); i++) {
                zip5List.add(root.get(i).asText());
            }
        }
        catch (Exception ex) {
            logger.debug("Invalid zip5 json payload.", ex);
        }
        return zip5List;
    }

    /**
     * Retrieve zip4 value from query parameter.
     * @param request HttpServletRequest.
     * @return zip4 string or empty string if param doesn't exist.
     */
    public static String getZip4FromParams(HttpServletRequest request)
    {
        String zip4 = request.getParameter("zip4");
        return (zip4 != null) ? zip4 : "";
    }

    /**
     * Retrieve zip9 value from query parameters zip5 and zip4.
     * @param request HttpServletRequest.
     * @return zip9 string or empty string.
     */
    public static String getZip9FromParams(HttpServletRequest request)
    {
        return getZip5FromParams(request) + getZip4FromParams(request);
    }

    /**
     * Retrieve zip9 value from query parameters zip5 and zip4.
     * @param json Json string containing array of objects with zip5 and zip4 fields.
     * @return zip9 string or empty string.
     */
    public static List<String> getZip9ListFromJson(String json)
    {
        List<String> zip9List = new ArrayList<>();
        try {
            logger.trace("Batch zip9 json body " + json);
            JsonNode root = mapper.readTree(json);
            for (int i = 0; i < root.size(); i++) {
                JsonNode node = root.get(i);
                String zip5 = (node.hasNonNull("zip5")) ? node.get("zip5").asText() : "";
                String zip4 = (node.hasNonNull("zip4")) ? node.get("zip4").asText() : "";
                zip9List.add(zip5 + zip4);
            }
        }
        catch (Exception ex) {
            logger.debug("Invalid zip9 json payload.", ex);
        }
        return zip9List;
    }

    /**
     * Indicate if batch = true in the query parameters.
     * @param request HttpServletRequest object.
     * @return true if batch=true, false otherwise.
     */
    public static boolean isBatch(HttpServletRequest request)
    {
        return Boolean.parseBoolean(request.getParameter("batch"));
    }

    /**
     * Indicate if detail = true in the query parameters.
     * @param request HttpServletRequest object.
     * @return true if detail = true, false otherwise.
     */
    public static boolean isDetail(HttpServletRequest request)
    {
        return Boolean.parseBoolean(request.getParameter("detail"));
    }

    /**
     * Indicate if initCaps = true in the query parameters.
     * @param request HttpServletRequest object.
     * @return true if initCaps = true, false otherwise.
     */
    public static boolean isInitCaps(HttpServletRequest request)
    {
        return Boolean.parseBoolean(request.getParameter("initCaps")) ||
               Boolean.parseBoolean(request.getParameter("initcaps"));
    }
}
