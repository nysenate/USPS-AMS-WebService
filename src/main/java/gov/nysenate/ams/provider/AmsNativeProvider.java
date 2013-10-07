package gov.nysenate.ams.provider;

import gov.nysenate.ams.dao.AmsNativeDao;
import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.AmsSettings;
import gov.nysenate.ams.model.CityStateResult;
import gov.nysenate.ams.service.AddressService;
import gov.nysenate.ams.service.LibraryService;
import gov.nysenate.ams.service.LicensingService;
import gov.nysenate.ams.util.Application;

public class AmsNativeProvider implements AddressService, LicensingService, LibraryService
{
    private static String SHARED_LIBRARY_NAME = "amsnative";

    private final AmsNativeDao amsNativeDao;

    public AmsNativeProvider()
    {
        amsNativeDao = new AmsNativeDao();
    }

    /** LibraryService implementation
     * -------------------------------*/

    /**
     * Loads the shared AMS Native library wrapper. The library name is indicated by SHARED_LIBRARY_NAME.
     * @see LibraryService, AmsNativeDao
     * @return true if the library was successfully loaded, false otherwise.
     */
    @Override
    public boolean load()
    {
        return amsNativeDao.loadAmsLibrary(SHARED_LIBRARY_NAME);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean setup()
    {
        AmsSettings amsSettings = new AmsSettings(Application.getConfig());
        return amsNativeDao.setupAmsLibrary(amsSettings);
    }

    @Override
    public boolean shutDown()
    {
        return false;
    }

    /** AddressService implementation
     * -------------------------------*/

    @Override
    public AddressInquiryResult addressInquiry(Address address) {
        return null;
    }

    @Override
    public CityStateResult cityStateLookup(String zip) {
        return null;
    }

    @Override
    public AddressInquiryResult zip9Inquiry(String zip9) {
        return null;
    }

    /** LicensingService implementation
     * -------------------------------*/

    @Override
    public String getApiVersion()
    {
        return null;
    }

    @Override
    public int getDataExpireDays()
    {
        return 0;
    }

    @Override
    public int getLibraryExpireDays()
    {
        return 0;
    }
}