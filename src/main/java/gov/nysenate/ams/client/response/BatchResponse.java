package gov.nysenate.ams.client.response;

import java.util.List;

/**
 * Represents the structure of batch responses.
 * @param <T> T is the response class.
 */
public class BatchResponse<T>
{
    protected List<T> results;

    public BatchResponse(List<T> responses) {
        this.results = responses;
    }

    public List<T> getResults() {
        return results;
    }

    public int getTotal() {
        return (this.results != null) ? this.results.size() : 0;
    }
}
