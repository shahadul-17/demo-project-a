package com.demoproject.chargefailurelog;

public class ChargeFailureLogServiceImpl implements ChargeFailureLogService {

    private final ChargeFailureLogRepository repository = new ChargeFailureLogRepository();

    private ChargeFailureLogServiceImpl() { }

    @Override
    public void addLogEntry(ChargeFailureLog log) throws Exception {
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
