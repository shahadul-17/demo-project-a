package com.demoproject.appconfig;

public class AppConfigServiceImpl implements AppConfigService {

    private static final int DEFAULT_APP_CONFIG_ID = 1;
    private final AppConfigRepository repository = new AppConfigRepository();

    private AppConfigServiceImpl() { }

    private void validateStatus(AppConfigStatus status) throws Exception {
        if (status != AppConfigStatus.NONE) { return; }

        throw new Exception("Status can either be STOPPED or IN_PROGRESS.");
    }

    @Override
    public AppConfig getAppConfig() throws Exception {
        AppConfig appConfig = repository.findById(DEFAULT_APP_CONFIG_ID);

        // if app config is null, we shall throw exception...
        if (appConfig == null) {
            throw new Exception("Application configuration was not found in the database.");
        }

        return appConfig;
    }

    @Override
    public void setStatus(AppConfigStatus status) throws Exception {
        validateStatus(status);

        repository.updateStatusById(DEFAULT_APP_CONFIG_ID, status.getValue());
    }
}
