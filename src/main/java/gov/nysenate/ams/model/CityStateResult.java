package gov.nysenate.ams.model;

/**
 * This class holds the city state cityRecord, from which you can tell if the cityRecord was successful or not.
 * The default construct is also the C constructor.
 */
public record CityStateResult(int responseCode, CityRecord cityRecord) {
    /**
     * This method tell if the lookup was a success. (0=success 1=Failure 2=Couldn't connect)
     * @return boolean is true if successful, is false if otherwise.
     */
    public boolean isSuccess() {
        return responseCode == 0;
    }
}
