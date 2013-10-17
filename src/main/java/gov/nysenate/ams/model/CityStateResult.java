package gov.nysenate.ams.model;

/**
 * This class holds the city state cityRecord, from which you can tell if the cityRecord was successful or not.
 */
public class CityStateResult
{
    protected CityRecord cityRecord; // Holds the cityRecord from CityState look up.
    protected int responseCode;      // Returns true if the look up was a success.

    public CityStateResult(int responseCode, CityRecord cityRecord)
    {
       this.responseCode = responseCode;
       this.cityRecord = cityRecord;
    }

    /**
     * This method tell if the lookup was a success. (0=success 1=Failure 2=Couldn't connect)
     * @return boolean is true if successful, is false if otherwise.
     */
    public boolean isSuccess() {
        return (responseCode == 0);
    }

    public CityRecord getCityRecord() {
        return cityRecord;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
