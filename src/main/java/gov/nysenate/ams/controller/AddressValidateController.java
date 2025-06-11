package gov.nysenate.ams.controller;

import com.fasterxml.jackson.databind.JsonNode;
import gov.nysenate.ams.client.response.AddressInquiryResponse;
import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Servlet to handle address validation requests.
 */
public class AddressValidateController extends BaseApiController<Address> {
    /**
     * Constructs a new Address object using the query parameters of the supplied HttpServletRequest.
     * This method exists to provide consistency among the different controllers when retrieving an
     * address from the query string.
     * @param request HttpServletRequest object
     * @return new Address instance if request was valid
     *         null if request was null
     */
    @Override
    protected Address getInputFromParams(HttpServletRequest request) {
        return new Address(request.getParameter("addr1"),
                request.getParameter("addr2"), request.getParameter("city"),
                request.getParameter("state"), request.getParameter("zip5"),
                request.getParameter("zip4"));
    }

    /**
     * Constructs an Address object using the given JSON, which must be an array containing a collection of
     * address component objects, e.g.
     * <code>
     *  [{"addr1":"", "addr2":"", "city":"", "state":"","zip5":"", "zip4":""} .. ]
     * </code>
     * @param node JSON payload
     * @return Address
     */
    @Override
    protected Address getInputFromJson(JsonNode node) {
        String addr1 = getOrEmpty(node, "addr1");
        String addr2 = getOrEmpty(node, "addr2");
        String city = getOrEmpty(node, "city");
        String state = getOrEmpty(node, "state");
        String zip5 = getOrEmpty(node, "zip5");
        String zip4 = getOrEmpty(node, "zip4");
        return new Address(addr1, addr2, city, state, zip5, zip4);
    }

    @Override
    protected Object getResponse(Address input) {
        AddressInquiryResult result = amsNativeProvider.addressInquiry(input);
        return AddressInquiryResponse.getResponse(result);
    }

    private static String getOrEmpty(JsonNode baseNode, String fieldName) {
        return baseNode.has(fieldName) ? baseNode.get(fieldName).asText() : "";
    }
}
