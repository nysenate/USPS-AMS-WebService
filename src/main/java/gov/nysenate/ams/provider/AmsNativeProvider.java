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
import gov.nysenate.util.Config;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Serves as a wrapper to the AmsNativeDao class and holds references to
 * the configuration dependencies.
 */
public class AmsNativeProvider implements AddressService, LicensingService, LibraryService
{
    private static Logger logger = Logger.getLogger(AmsNativeDao.class);
    private final AmsNativeDao amsNativeDao;
    private final Config config;
    private final AmsSettings amsSettings;

    private static boolean LIBRARY_LOADED = false;

    public AmsNativeProvider(Config config, AmsSettings amsSettings)
    {
        this.amsNativeDao = new AmsNativeDao();
        this.config = config;
        this.amsSettings = amsSettings;
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
        String libraryName = config.getValue("shared.library.name", "amsnative");
        if (!LIBRARY_LOADED) {
            LIBRARY_LOADED = amsNativeDao.loadAmsLibrary(libraryName);
        }
        return LIBRARY_LOADED;
    }

    /**
     * Sets up AMS using the configuration settings.
     * @return true if success, false otherwise. Note: returns false if AMS is already configured.
     */
    @Override
    public boolean setup()
    {
        boolean success = false;
        try {
            success = this.amsNativeDao.setupAmsLibrary(this.amsSettings);
        }
        catch (Exception ex) {
            logger.debug("Failed to setup AMS using the supplied configuration settings!", ex);
        }
        return success;
    }

    @Override
    public boolean shutDown()
    {
        return this.amsNativeDao.closeAmsLibrary();
    }

    /** AddressService implementation
     * -------------------------------*/

    @Override
    public AddressInquiryResult addressInquiry(Address address)
    {
        return this.amsNativeDao.addressInquiry(address);
    }

    @Override
    public CityStateResult cityStateLookup(String zip5)
    {
        return this.amsNativeDao.cityStateLookup(zip5);
    }

    @Override
    public AddressInquiryResult zip9Inquiry(String zip9)
    {
        return this.amsNativeDao.zip9Inquiry(zip9);
    }

    @Override
    public List<AddressInquiryResult> addressInquiry(List<Address> addresses)
    {
        List<AddressInquiryResult> addressInquiryResults = new ArrayList<>();
        for (Address address : addresses) {
            addressInquiryResults.add(addressInquiry(address));
        }
        return addressInquiryResults;
    }

    @Override
    public List<CityStateResult> cityStateLookup(List<String> zip5List)
    {
        List<CityStateResult> cityStateResults = new ArrayList<>();
        for (String zip5 : zip5List) {
            cityStateResults.add(cityStateLookup(zip5));
        }
        return cityStateResults;
    }

    @Override
    public List<AddressInquiryResult> zip9Inquiry(List<String> zip9List)
    {
        List<AddressInquiryResult> addressInquiryResults = new ArrayList<>();
        for (String zip9 : zip9List) {
            addressInquiryResults.add(zip9Inquiry(zip9));
        }
        return addressInquiryResults;
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