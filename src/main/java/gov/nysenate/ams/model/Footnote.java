package gov.nysenate.ams.model;

/**
 * Various footnote flags are used to denote a response condition.
 */
public enum Footnote
{
    A ("ZIP Code Corrected",
        "The address was found to have a different 5-digit ZIP Code than given in the submitted list. The correct " +
        "ZIP Code is shown in the output address."),

    B ("City/State Corrected",
        "The spelling of the city name and/or state abbreviation in the submitted address was found to be different " +
        "than the standard spelling. The standard spelling of the city name and state abbreviation are shown in the " +
        "output address."),

    C ("Invalid City/State/ZIP",
        "The ZIP Code in the submitted address could not be found because neither a valid city, state, nor valid 5- " +
        "digit ZIP Code was present. It is also recommended that the requestor check the submitted address for " +
        "accuracy."),

    D ("No ZIP+4 Code Assigned",
        "This is a record listed by the United States Postal Service on the national ZIP+4 file as a non-deliverable " +
        "location. It is recommended that the requestor verify the accuracy of the submitted address."),

    E ("ZIP Code Assigned with a Multiple Response",
        "Multiple records were returned, but each shares the same 5-digit ZIP Code."),

    F ("Address Not Found",
        "The address, exactly as submitted, could not be found in the city, state, or ZIP Code provided."),

    G ("All or Part of the Firm Line Used For Address Line",
        "Information in the firm line was determined to be a part of the address. It was moved out of the firm line " +
        "and incorporated into the address line."),

    H ("Missing Secondary Number",
        "ZIP+4 information indicates this address is a building. The address as submitted does not contain an " +
        "apartment/suite number."),

    I ("Insufficient/Incorrect Data",
        "More than one ZIP+4 Code was found to satisfy the address as submitted. The submitted address did not " +
        "contain sufficiently complete or correct data to determine a single ZIP+4 Code."),

    J ("PO Box Dual Address",
        "The input contained two addresses. For example: 123 MAIN ST PO BOX 99."),

    K ("Non-PO Box Dual Address",
        "CASS rule does not allow a match when the cardinal point of a directional changes more than 90%."),

    L ("Address Component Changed",
        "An address component was added, changed, or deleted in order to achieve a " +
        "match. "),

    M ("Street Name Changed",
        "The spelling of the street name was changed in order to achieve a match. "),

    N ("Address Standardized",
        "The delivery address was standardized. For example, if STREET was in the delivery address, the system " +
        "will return ST as its standard spelling. "),

    O ("Multiple response can be broken using the lowest +4",
        "More than one ZIP+4 Code was found to satisfy the address as submitted. The lowest ZIP +4 addon may " +
        "be used to break the tie between the records. "),

    P ("Better Address Exists",
        "The delivery address is matchable, but is known by another (preferred) name. For example, in New York, " +
        "NY, AVENUE OF THE AMERICAS is also known as 6TH AVE. An inquiry using a delivery address of " +
        "55 AVE OF THE AMERICAS would be flagged with a Footnote Flag P. "),

    Q ("Unique ZIP Code Match",
        "Match to an address with a unique ZIP Code. "),

    R ("No Match due to EWS",
        "The delivery address is matchable, but the EWS file indicates that an exact match will be available soon. "),

    S ("Incorrect Secondary Number",
        "The secondary information (i.e., floor, suite, apartment, or box number) does not match that on the national " +
        "ZIP+4 file. This secondary information, although present on the input address, was not valid in the range " +
        "found on the national ZIP+4 file. "),

    T ("Multiple response due to Magnet Street Syndrome",
        "The search resulted in a single response; however, the record matched was flagged as having magnet street " +
        "syndrome. "),

    U ("Unofficial Post Office Name",
        "The city or post office name in the submitted address is not recognized by the United States Postal Service " +
        "as an official last line name (preferred city name), and is not acceptable as an alternate name. This does " +
        "denote an error and the preferred city name will be provided as output. "),

    V ("Unverifiable City/State",
        "The city and state in the submitted address could not be verified as corresponding to the given 5-digit ZIP " +
        "Code. This comment does not necessarily denote an error; however, it is recommended that the requestor " +
        "check the city and state in the submitted address for accuracy. "),

    W ("Small Town Default",
        "The input address record contains a delivery address other than a PO BOX, General Delivery, or " +
        "Postmaster with a 5-digit ZIP Code that is identified as a “small town default.” The United States Postal " +
        "Service does not provide street delivery for this ZIP Code "),

    X ("Unique ZIP Code Default", 
        "Default match inside a unique ZIP Code. "),
    
    Y ("Military Match", 
        "Match made to a record with a military ZIP Code. "),
    
    Z ("ZIP Move Match", 
        "The ZIPMOVE product shows which ZIP + 4 records have moved from one ZIP Code to another. If an " +
        "input address matches to a ZIP + 4 record which the ZIPMOVE product indicates as having moved, the " +
        "search is performed again in the new ZIP Code. ");

    private String shortDesc;
    private String longDesc;
    
    Footnote (String shortDesc, String longDesc) {
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }
}