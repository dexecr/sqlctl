package com.dexecr.sql;

public interface DatabaseInfo extends CredentialsInfo {
    String getHost();

    int getPort();

    String getDatabase();

    DriverType getDriver();
}