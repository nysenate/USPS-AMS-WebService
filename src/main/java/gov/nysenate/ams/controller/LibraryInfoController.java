package gov.nysenate.ams.controller;

import gov.nysenate.ams.client.response.LibraryInfoResponse;
import gov.nysenate.ams.filter.ApiFilter;
import gov.nysenate.ams.provider.AmsNativeProvider;
import gov.nysenate.ams.util.Application;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to handle library info requests.
 */
public class LibraryInfoController extends BaseApiController
{
    private Logger logger = Logger.getLogger(LibraryInfoController.class);
    private AmsNativeProvider amsNativeProvider;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        this.amsNativeProvider = Application.getAmsNativeProvider();
        logger.debug("Initialized LibraryInfoController.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Object responseObj;

        String apiVersion = amsNativeProvider.getApiVersion();
        int dataExpireDays = amsNativeProvider.getDataExpireDays();
        int libraryExpireDays = amsNativeProvider.getLibraryExpireDays();

        responseObj = new LibraryInfoResponse(apiVersion, dataExpireDays, libraryExpireDays);

        ApiFilter.setApiResponse(responseObj, request);
    }
}
