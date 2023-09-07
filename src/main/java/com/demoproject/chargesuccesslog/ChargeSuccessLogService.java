package com.demoproject.chargesuccesslog;

public interface ChargeSuccessLogService {
    void addLogEntry(ChargeSuccessLog log) throws Exception;
    void clearAllLogs() throws Exception;
    void commitBatchedChanges(boolean shallDisposeResources);
}
