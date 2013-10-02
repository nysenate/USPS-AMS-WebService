package gov.nysenate.ams.service;

/**
 * This interface defines the functions pertaining to the license of the address management system.
 */
public interface LicensingService
{
    /** Get the Version of the API code. */
    public String getApiVersion();

    /** Get the number of days after which the data expires. */
    public int getDataExpireDays();

    /** Get the number of days after which the library expires. */
    public int getLibraryExpireDays();
}