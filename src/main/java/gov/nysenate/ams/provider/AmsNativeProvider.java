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
public class AmsNativeProvider implements AddressService, LicensingService, LibraryService {
    private static final Logger logger = LoggerFactory.getLogger(AmsNativeProvider.class);
    private final AmsNativeDao amsNativeDao;
    private final Config config;
    private final AmsSettings amsSettings;

    private static boolean LIBRARY_LOADED = false;

    public AmsNativeProvider(Config config, AmsSettings amsSettings) {
        this.amsNativeDao = new AmsNativeDao();
        this.config = config;
        this.amsSettings = amsSettings;
    }

    // LibraryService implementation

    /**
     * Loads the shared AMS Native library wrapper. The library name is indicated by SHARED_LIBRARY_NAME.
     * @see LibraryService, AmsNativeDao
     * @return true if the library was successfully loaded, false otherwise.
     */
    @Override
    public boolean load() {
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
    public boolean setup() {
        try {
            return amsNativeDao.setupAmsLibrary(amsSettings);
        }
        catch (Exception ex) {
            logger.debug("Failed to setup AMS using the supplied configuration settings!", ex);
            return false;
        }
    }

    @Override
    public boolean shutDown() {
        return amsNativeDao.closeAmsLibrary();
    }

    /** AddressService implementation */

    @Override
    public AddressInquiryResult addressInquiry(Address address) {
        return amsNativeDao.addressInquiry(address);
    }

    @Override
    public CityStateResult cityStateLookup(String zip5) {
        return amsNativeDao.cityStateLookup(zip5);
    }

    @Override
    public AddressInquiryResult zip9Inquiry(String zip9) {
        return amsNativeDao.zip9Inquiry(zip9);
    }

    /** LicensingService implementation */

    @Override
    public String getApiVersion() {
        return amsNativeDao.getAmsVersion();
    }

    @Override
    public int getDataExpireDays() {
        return amsNativeDao.getDataExpireDays();
    }

    @Override
    public int getLibraryExpireDays() {
        return amsNativeDao.getLibraryExpireDays();
    }
}