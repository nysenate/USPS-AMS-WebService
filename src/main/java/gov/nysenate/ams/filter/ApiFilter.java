package gov.nysenate.ams.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.*;
import java.io.IOException;

public class ApiFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ApiFilter.class);

    private static final String RESPONSE_OBJECT_KEY = "responseObject";
    private static final String FORMATTED_RESPONSE_KEY = "formattedResponse";

    /** Serializers */
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();

    /** Available format types */
    public enum FormatType { JSON, XML, JSONP }

    @Override
    public void init(FilterConfig filterConfig) {
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
        formatResponse(request, response);
        sendResponse(request, response);
    }

    /** Obtains the response object and serializes it using the format specified in the request parameters.
     *  The default output format is JSON. The default format will be used in the following cases:
     *  - No format specified in the parameters.
     *  - Invalid format specified in the parameters.
     * @param request   ServletRequest
     */
    private void formatResponse(ServletRequest request, ServletResponse response) {
        String formatStr = request.getParameter("format");
        FormatType format;
        if (formatStr == null) {
            format = FormatType.JSON;
        }
        else {
            format = FormatType.valueOf(formatStr.toUpperCase());
        }
        logger.trace("Serializing response as {}", format);
        Object responseObj = request.getAttribute(RESPONSE_OBJECT_KEY);

        try {
            if (format == FormatType.XML) {
                String xml = xmlMapper.writeValueAsString(responseObj);
                request.setAttribute(FORMATTED_RESPONSE_KEY, xml);
                response.setContentType("application/xml");
                response.setContentLength(xml.length());
            }
            else if (format == FormatType.JSONP) {
                String callback = request.getParameter("callback");
                String json = jsonMapper.writeValueAsString(responseObj);
                String jsonp = String.format("%s(%s);", callback, json);
                request.setAttribute(FORMATTED_RESPONSE_KEY, jsonp);
                response.setContentType("application/javascript");
                response.setContentLength(jsonp.length());
            }
            else {
                String json = jsonMapper.writeValueAsString(responseObj);
                request.setAttribute(FORMATTED_RESPONSE_KEY, json);
                response.setContentType("application/json");
                response.setContentLength(json.length());
            }
            logger.trace("Completed serialization");
        }
        catch (JsonProcessingException ex) {
            logger.error("Failed to serialize response!", ex);
        }
    }

    /**
     * Writes the formatted response to the output stream.
     * @param request   ServletRequest
     * @param response  ServletResponse
     */
    private void sendResponse(ServletRequest request, ServletResponse response) {
        Object formattedResponse = request.getAttribute(FORMATTED_RESPONSE_KEY);
        try {
            if (formattedResponse != null) {
                response.getWriter().write(formattedResponse.toString());
            }
            else {
                logger.error("No formatted response set!");
            }
        }
        catch (IOException ex){
            logger.error("Failed to write to output stream!", ex);
        }
    }

    /**
     * Simply sets the given response object as an attribute within the request. This is used
     * for passing response data to the formatting methods for output processing.
     * @param response  Object containing response data
     * @param request   ServletRequest
     */
    public static void setApiResponse(Object response, ServletRequest request) {
        request.setAttribute(RESPONSE_OBJECT_KEY, response);
    }
}
