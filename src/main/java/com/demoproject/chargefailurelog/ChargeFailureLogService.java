package com.demoproject.chargefailurelog;

public interface ChargeFailureLogService {
    void addLogEntry(ChargeFailureLog log) throws Exception;
    void clearAllLogs() throws Exception;
    void commitBatchedChanges(boolean shallDisposeResources);
}
