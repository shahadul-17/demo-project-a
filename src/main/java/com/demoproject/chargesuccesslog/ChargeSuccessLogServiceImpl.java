package com.demoproject.chargesuccesslog;

public class ChargeSuccessLogServiceImpl implements ChargeSuccessLogService {

    private final ChargeSuccessLogRepository repository = new ChargeSuccessLogRepository();

    private ChargeSuccessLogServiceImpl() { }

    @Override
    public void addLogEntry(ChargeSuccessLog log) throws Exception {
        repository.insert(log);
    }

    @Override
    public void clearAllLogs() throws Exception {
        // deletes all logs from the table...
        repository.deleteAll();
        // and also resets the auto increment counter...
        repository.resetAutoIncrement();
    }

    @Override
    public void commitBatchedChanges(boolean shallDisposeResources) {
        repository.commitBatchedCommands(shallDisposeResources);
    }
}
