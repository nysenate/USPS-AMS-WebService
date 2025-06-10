package gov.nysenate.ams.client.response;

import java.util.List;

/**
 * Represents the structure of batch responses.
 * @param <T> T is the response class.
 */
public record BatchResponse<T>(List<T> results) {
    // Used by SAGE.
    @SuppressWarnings("unused")
    public int total() {
        return results.size();
    }
}
