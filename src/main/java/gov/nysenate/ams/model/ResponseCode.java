package gov.nysenate.ams.model;

/**
 * Represents the return codes returned by the AmsNativeProvider API.
 */
public enum ResponseCode {
    ENGINE_DISABLED (0, "Engine Disabled", "The engine is disabled. Check to see if DPV or LACS has been locked."),

    INVALID_DUAL_ADDRESS (10, "Dual Address",
        "Information presented could not be processed in current format. Corrective action is needed. Be sure that " +
        "the address line components are correct. For example, the input address line may contain more than one " +
        "delivery address."),

    INVALID_CITY_ST_ZIP (11, "Invalid Address",
        "The ZIP Code in the submitted address could not be found because neither a valid city, state, nor valid 5- " +
        "digit ZIP Code was present. Corrective action is needed. It is also recommended that the requestor check " +
        "the submitted address for accuracy."),

    INVALID_STATE (12, "Invalid State",
        "The state in the submitted address is invalid. Corrective action is needed. It is also recommended that the " +
        "requestor check the submitted address for accuracy."),

    INVALID_CITY (13, "Invalid City",
        "The city in the submitted address is invalid. Corrective action is needed. It is also recommended that the " +
        "requestor check the submitted address for accuracy."),

    NOT_FOUND (21, "No Match",
        "The address, exactly as submitted, could not be found in the national ZIP+4 file. It is recommended that the " +
        "requestor check the submitted address for accuracy. For example, the street address line may be abbreviated " +
        "excessively and may not be fully recognizable."),

    MULTI_RESPONSE (22, "Multiple Matches",
        "The submitted address did not contain sufficiently complete or correct data to determine a single ZIP+4 Code. " +
        "It is recommended that the requestor check the address for completeness."),

    EXACT_MATCH (31, "Exact Match",
        "Single response based on input information."),

    DEFAULT_MATCH (32, "Default Match",
        "A match was made to a default record in the national ZIP+4 file. A more specific match may be available if " +
        "a secondary number (i.e., apartment, suite, etc.) exists.");

    /** Numerical return code. */
    private final int code;

    /** Description of return code and possible corrective actions. */
    private final String shortDesc;
    private final String longDesc;

    ResponseCode(int code, String shortDesc, String longDesc) {
        this.code = code;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    /**
     * Returns the StatusCode using the numerical code.
     * @param code int
     * @return StatusCode if code matches, null otherwise.
     */
    public static ResponseCode getByCode(int code) {
        for (ResponseCode responseCode : values()) {
            if (responseCode.code == code) {
                return responseCode;
            }
        }
        throw new IllegalArgumentException("No StatusCode with code " + code + " found.");
    }
}
