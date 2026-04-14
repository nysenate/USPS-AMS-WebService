package gov.nysenate.ams.service;

/**
 * This interface defines the functions pertaining to the license of the address management system.
 */
public interface LicensingService {
    /** Get the Version of the API code. */
    String getApiVersion();

    /** Get the number of days after which the data expires. */
    int getDataExpireDays();
}
