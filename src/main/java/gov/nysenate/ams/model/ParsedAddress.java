package gov.nysenate.ams.model;

/**
 * Represents an address by its parsed components.
 * The C constructor is also the default constructor.
 */
public record ParsedAddress(String primaryNum, String secondaryNum, String ruralRouteNum, String secondaryUnit,
                            String firstPreDir, String secondPreDir, String firstSuffix, String secondSuffix,
                            String firstPostDir, String secondPostDir, String primaryName) {}
