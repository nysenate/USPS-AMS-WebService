package gov.nysenate.ams.provider;

import gov.nysenate.ams.dao.AmsNativeDao;
import gov.nysenate.ams.model.*;
import gov.nysenate.ams.service.AddressService;
import gov.nysenate.ams.service.LibraryService;
import gov.nysenate.ams.service.LicensingService;
import gov.nysenate.util.Config;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Serves as a wrapper to the AmsNativeDao class and holds references to
 * the configuration dependencies.
 */
public class AmsNativeProvider implements AddressService, LicensingService, LibraryService
{
    private static Logger logger = LoggerFactory.getLogger(AmsNativeDao.class);
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
        if (address != null && !address.isEmpty()) {
            AddressInquiryResult result = this.amsNativeDao.addressInquiry(address);
            result.getUspsAddress().getValidatedAddress().setId(address.getId());
            return result;
        }
        else return new AddressInquiryResult(-1, null, StatusCode.INSUFFICIENT_ADDRESS.getCode(), null, null);
    }

    @Override
    public CityStateResult cityStateLookup(String zip5)
    {
        if (zip5 != null && !zip5.isEmpty()) {
            return this.amsNativeDao.cityStateLookup(zip5);
        }
        else return new CityStateResult(-1, null);

    }

    @Override
    public AddressInquiryResult zip9Inquiry(String zip9)
    {
        if (zip9 != null && !zip9.isEmpty()) {
            return this.amsNativeDao.zip9Inquiry(zip9);
        }
        else return new AddressInquiryResult(-1, null, StatusCode.INSUFFICIENT_ZIP9.getCode(), null, null);
    }

    /** LicensingService implementation
     * -------------------------------*/

    @Override
    public String getApiVersion()
    {
        return amsNativeDao.getAmsVersion();
    }

    @Override
    public int getDataExpireDays()
    {
        return amsNativeDao.getDataExpireDays();
    }

    @Override
    public int getLibraryExpireDays()
    {
        return amsNativeDao.getLibraryExpireDays();
    }
}