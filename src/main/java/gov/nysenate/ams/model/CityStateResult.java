package gov.nysenate.ams.model;

/**
 * This class holds the city state cityRecord, from which you can tell if the cityRecord was successful or not.
 */
public record CityStateResult(int responseCode, CityRecord cityRecord) {}
