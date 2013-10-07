package gov.nysenate.ams.service;

public interface LibraryService
{
    /**
     * Loads the necessary libraries (shared/web-apis) before any requests can be made.
     * @return true if all necessary dependencies were loaded, false otherwise.
     */
    public boolean load();

    /**
     * Once the dependencies are loaded, the setup method should be invoked to configure
     * the library.
     * @return true if the configuration step was successful, false otherwise.
     */
    public boolean setup();

    /**
     * Unloads the library and perform any necessary cleanup.
     * @return true if shutdown completed successfully, false otherwise.
     */
    public boolean shutDown();
}
