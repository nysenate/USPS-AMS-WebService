package gov.nysenate.ams.dao;

import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.AmsSettings;
import gov.nysenate.ams.model.CityStateResult;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * JNI wrapper to the C implementation of the USPS AMS Service.
 *
 * The goal of this class is to serve as the bridge between the native C API for the USPS AMS
 * service. Since the AMS library is closed source, a C wrapper was written to interface with it.
 * The C wrapper has methods to perform configuration/address inquiries and they are delegated
 * to by the native methods of this class.
 *
 * The C wrapper code will be found under the c section of the src directory.
 *
 * View the AMS library documentation at: https://ribbs.usps.gov/index.cfm?page=amsapi
 */
public class AmsNativeDao
{
    Marker fatal = MarkerFactory.getMarker("FATAL");
    private static Logger logger = LoggerFactory.getLogger(AmsNativeDao.class);

    /**
     * Loads the AMS wrapper library. In order for this to work the java.library.path environment
     * variable must be set to the directory where the shared library is located. If this class is
     * being utilized via the command line, you can specify via -Djava.library.path=PATH_TO_LIBRARY_DIR.
     * If running on a servlet engine, set the variable appropriately in the startup script for the server.
     * This path variable is read only once by the JVM so it's not possible to dynamically set it.

     * @param libraryName If the shared library is called 'libamswrapper.so', libraryName will be 'amswrapper'.
     * @return true if library loaded successfully, false otherwise.
     */
    public boolean loadAmsLibrary(String libraryName)
    {
        try {
            System.loadLibrary(libraryName);
            logger.info("Loaded AMS Native Library successfully.");
            return true;
        }
        catch (UnsatisfiedLinkError ex) {
            logger.error(fatal, "Failed to load the AMS Native Library!", ex);
            return false;
        }
    }

    /**
     * Calls the AMS z4opencfg() method which retrieves configuration details from the
     * passed in AmsSettings object.
     *
     * @param amsSettings AmsSettings object that has been constructed using the application Config.
     * @return true if library setup was successful, false otherwise.
     */
    public native boolean setupAmsLibrary(AmsSettings amsSettings);

    /**
     * Calls the AMS z4close() method which closes the system.
     * @return true if closed, false if system was not already running.
     */
    public native boolean closeAmsLibrary();

    /**
     * Wrapper to the AMS z4adrinq() method using {address} as input.
     *
     * @param address Input Address
     * @return AddressInquiryResult
     */
    public synchronized native AddressInquiryResult addressInquiry(Address address);

    /**
     * Wrapper to the AMS z4ctyget() method using the {zip5} as the search key.
     *
     * @param zip Zip5 or Zip9 code. String should just contain numbers.
     * @return CityStateResult
     */
    public synchronized native CityStateResult cityStateLookup(String zip);

    /**
     * Wrapper to the AMS z4xrfinq() method using the supplied {zip9} as the search key.
     *
     * @param zip9 9 digit zip5 code.
     * @return AddressInquiryResult
     */
    public synchronized native AddressInquiryResult zip9Inquiry(String zip9);

    /**
     * Wrapper to the AMS z4ver() method.
     *
     * @return String
     */
    public synchronized native String getAmsVersion();

    /**
     * Wrapper to the AMS z4GetDataExpireDays() method.
     *
     * @return int
     */
    public synchronized native int getDataExpireDays();

    /**
     * Wrapper to the AMS z4GetCodeExpireDays() method.
     *
     * @return int
     */
    public synchronized native int getLibraryExpireDays();
}