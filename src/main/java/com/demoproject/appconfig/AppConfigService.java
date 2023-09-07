package com.demoproject.appconfig;

public interface AppConfigService {
    AppConfig getAppConfig() throws Exception;
    void setStatus(AppConfigStatus status) throws Exception;
}
