package gov.nysenate.ams.provider;

import gov.nysenate.ams.dao.AmsNativeDao;
import gov.nysenate.ams.model.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Serves as a wrapper to the AmsNativeDao class and holds references to
 * the configuration dependencies.
 */
public class AmsNativeProvider {
    private static final Logger logger = LoggerFactory.getLogger(AmsNativeProvider.class);
    private final AmsNativeDao amsNativeDao = new AmsNativeDao();
    private final String libraryName;
    private final AmsSettings amsSettings;

    private static boolean LIBRARY_LOADED = false;

    public AmsNativeProvider(String libraryName, AmsSettings amsSettings) {
        this.libraryName = libraryName;
        this.amsSettings = amsSettings;
    }

    /**
     * Loads the shared AMS Native library wrapper, and configures the library.
     * @return true if all necessary dependencies were loaded, false otherwise.
     */
    public boolean load() {
        if (!LIBRARY_LOADED) {
            LIBRARY_LOADED = amsNativeDao.loadAmsLibrary(libraryName);
        }
        try {
            amsNativeDao.setupAmsLibrary(amsSettings);
        }
        catch (Exception ex) {
            logger.debug("Failed to setup AMS using the supplied configuration settings!", ex);
            return false;
        }
        return LIBRARY_LOADED;
    }

    /**
     * Unloads the library and perform any necessary cleanup.
     * @return true if shutdown completed successfully, false otherwise.
     */
    public boolean shutDown() {
        return amsNativeDao.closeAmsLibrary();
    }

    /**
     * Performs a standardized address inquiry using an address and city/state/zip5 information.
     * @param address Address to addressInquiry.
     * @return AddressInquiryResult.
     */
    public AddressInquiryResult addressInquiry(Address address) {
        return amsNativeDao.addressInquiry(address);
    }

    /**
     * Performs city/state lookup using a zip5 code as the search key.
     * @param zip5 5 digit zip code string.
     * @return CityStateResult.
     */
    public CityStateResult cityStateLookup(String zip5) {
        return amsNativeDao.cityStateLookup(zip5);
    }

    /**
     * Performs a standardized address inquiry using a 9 digit zip5.
     * @param zip9 9 digit zip5 code string. Any hyphens will be removed.
     * @return AddressInquiryResult.
     */
    public AddressInquiryResult zip9Inquiry(String zip9) {
        return amsNativeDao.zip9Inquiry(zip9);
    }

    /** Get the Version of the API code. */
    public String getApiVersion() {
        return amsNativeDao.getAmsVersion();
    }

    /** Get the number of days after which the data expires. */
    public int getDataExpireDays() {
        return amsNativeDao.getDataExpireDays();
    }
}