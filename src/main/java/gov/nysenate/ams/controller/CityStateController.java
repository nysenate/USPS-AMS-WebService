package gov.nysenate.ams.controller;

import gov.nysenate.ams.client.response.BaseCityStateResponse;
import gov.nysenate.ams.client.response.BatchResponse;
import gov.nysenate.ams.client.response.DetailCityStateResponse;
import gov.nysenate.ams.filter.ApiFilter;
import gov.nysenate.ams.model.CityStateResult;
import gov.nysenate.ams.provider.AmsNativeProvider;
import gov.nysenate.ams.util.Application;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CityStateController extends BaseApiController
{
    /* TODO - Implement init caps functionality for city/state */

    private Logger logger = LoggerFactory.getLogger(CityStateController.class);
    private AmsNativeProvider amsNativeProvider;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        this.amsNativeProvider = Application.getAmsNativeProvider();
        logger.debug("Initialized CityStateController.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Object responseObj;

        boolean batch = isBatch(request);
        boolean detail = isDetail(request);
        boolean initCaps = isInitCaps(request);

        if (batch) {
            String json = IOUtils.toString(request.getInputStream(), "UTF-8");
            List<String> inputZip5List = getZip5ListFromJson(json);
            List<CityStateResult> results = new ArrayList<>();
            if (inputZip5List != null && inputZip5List.size() > 0) {
                results = amsNativeProvider.cityStateLookup(inputZip5List);
            }
            List<BaseCityStateResponse> baseResponses = new ArrayList<>();
            List<DetailCityStateResponse> detailResponses = new ArrayList<>();
            for (CityStateResult result : results) {
                if (!detail) {
                    baseResponses.add(new BaseCityStateResponse(result, initCaps));
                }
                else {
                    detailResponses.add(new DetailCityStateResponse(result, initCaps));
                }
            }
            if (!detail) {
                responseObj = new BatchResponse<>(baseResponses);
            }
            else {
                responseObj = new BatchResponse<>(detailResponses);
            }

            ApiFilter.setApiResponse(responseObj, request);
        }
        else {
            doGet(request, response);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Object responseObj;

        String zip5 = getZip5FromParams(request);
        boolean detail = isDetail(request);
        boolean initCaps = isInitCaps(request);

        CityStateResult result = amsNativeProvider.cityStateLookup(zip5);

        if (!detail) {
            responseObj = new BaseCityStateResponse(result, initCaps);
        }
        else {
            responseObj = new DetailCityStateResponse(result, initCaps);
        }

        ApiFilter.setApiResponse(responseObj, request);
    }
}