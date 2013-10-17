package gov.nysenate.ams.controller;

import gov.nysenate.ams.client.response.BaseCityStateResponse;
import gov.nysenate.ams.client.response.DetailCityStateResponse;import gov.nysenate.ams.dao.AmsNativeDao;
import gov.nysenate.ams.filter.ApiFilter;
import gov.nysenate.ams.model.CityStateResult;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 10/17/13
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class CityStateController extends BaseApiController
{
    @Override
    public void init(ServletConfig config) throws ServletException {}

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Object responseObj = null;

        boolean batch = Boolean.parseBoolean(request.getParameter("batch"));



        doGet(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object responseObj = null;

        /** Retrieve input address from query parameters. */
        String zip5 = request.getParameter("zip5");

        /** Query Parameter - detail : If true, output all info returned by AMS. */
        boolean detail = Boolean.parseBoolean(request.getParameter("detail"));

        /** Perform address validation if input address is valid. */
        if (zip5 != null && !zip5.isEmpty()) {
            AmsNativeDao amsNativeDao = new AmsNativeDao();
            CityStateResult result = amsNativeDao.cityStateLookup(zip5);

            /** Create the response object based on the detail level. */
            if (!detail) {
                responseObj = new BaseCityStateResponse(result);
            }
            else {
                responseObj = new DetailCityStateResponse(result);
            }
        }

        /** Set the response. */
        ApiFilter.setApiResponse(responseObj, request);
    }

}
