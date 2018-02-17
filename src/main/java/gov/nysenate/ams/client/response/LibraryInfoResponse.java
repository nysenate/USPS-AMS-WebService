package gov.nysenate.ams.client.response;

public class LibraryInfoResponse
{
	protected String apiVersion;
	protected int dataExpireDays;
	protected int libraryExpireDays;

	public LibraryInfoResponse(String apiVersion, int dataExpireDays, int libraryExpireDays)
	{
		this.apiVersion = apiVersion;
		this.dataExpireDays = dataExpireDays;
		this.libraryExpireDays = libraryExpireDays;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public int getDataExpireDays() {
		return dataExpireDays;
	}

	public int getLibraryExpireDays() {
		return libraryExpireDays;
	}
}
