package com.demoproject.appconfig;

import com.demoproject.utils.JsonSerializer;

public class AppConfig {

    private int appConfigId;                                          // id
    private int numberOfRows;                                         // number_of_row
    private AppConfigStatus status = AppConfigStatus.NONE;            // status

    public AppConfig() { }

    public AppConfig(int appConfigId, int numberOfRows, AppConfigStatus status) {
        setAppConfigId(appConfigId);
        setNumberOfRows(numberOfRows);
        setStatus(status);
    }

    public int getAppConfigId() {
        return appConfigId;
    }

    public void setAppConfigId(int appConfigId) {
        this.appConfigId = appConfigId;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public AppConfigStatus getStatus() {
        return status;
    }

    public void setStatus(AppConfigStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}
