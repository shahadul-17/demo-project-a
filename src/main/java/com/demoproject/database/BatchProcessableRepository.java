package com.demoproject.database;

public interface BatchProcessableRepository extends Repository {

    /**
     * Submits a batch of commands to the database for execution.
     * @param shallDisposeResources If set to true, the underlying connection
     * to the database is closed.
     */
    void commitBatchedCommands(boolean shallDisposeResources);
}
