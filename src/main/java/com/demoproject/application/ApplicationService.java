package com.demoproject.application;

public interface ApplicationService {

    /**
     * Performs any reset operation (if needed).
     * e.g. Resetting the entire database schema to default state.
     * @throws Exception
     */
    void reset() throws Exception;

    /**
     * Executes service to perform necessary actions.
     * @throws Exception
     */
    void execute() throws Exception;

    /**
     * Releases resources.
     */
    void dispose();
}
