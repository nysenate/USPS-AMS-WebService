package gov.nysenate.ams.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nysenate.ams.client.response.BatchResponse;
import gov.nysenate.ams.filter.ApiFilter;
import gov.nysenate.ams.provider.AmsNativeProvider;
import gov.nysenate.ams.util.Application;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseApiController<InputType> extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(BaseApiController.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    protected AmsNativeProvider amsNativeProvider;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);

        List<InputType> inputs = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(json);
            for (JsonNode child : root) {
                inputs.add(getInputFromJson(child));
            }
        }
        catch (Exception ex) {
            logger.error("Invalid JSON payload in {}", getClass(), ex);
        }

        List<Object> responses = inputs.stream().map(this::getResponse).toList();
        var batchResponse = new BatchResponse<>(responses);
        ApiFilter.setApiResponse(batchResponse, request);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        InputType input = getInputFromParams(request);
        ApiFilter.setApiResponse(getResponse(input), request);
    }

    public void init(ServletConfig config) {
        this.amsNativeProvider = Application.getAmsNativeProvider();
    }

    protected abstract InputType getInputFromParams(HttpServletRequest request);

    protected abstract InputType getInputFromJson(JsonNode node);

    protected abstract Object getResponse(InputType input);

    /**
     * Retrieve zip5 value from query parameter.
     * @param request HttpServletRequest.
     * @return zip5 string or empty string if param doesn't exist.
     */
    protected static String getZip5FromParams(HttpServletRequest request) {
        return StringUtils.defaultIfEmpty(request.getParameter("zip5"), "");
    }
}
