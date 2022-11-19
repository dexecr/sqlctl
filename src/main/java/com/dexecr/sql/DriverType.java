package com.dexecr.sql;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Driver;

@RequiredArgsConstructor
public enum DriverType {

    postgresql("org.postgresql.Driver", new DefaultInfoProvider()),
    mysql("com.mysql.cj.jdbc.Driver", new DefaultInfoProvider()),
    h2("org.h2.Driver", new DefaultInfoProvider());

    public final String driverClassName;
    public final InfoProvider info;
    private volatile Driver driverInstance;

    @SneakyThrows
    public Driver getDriverInstance() {
        if (driverInstance == null) {
            synchronized (this) {
                if (driverInstance == null) {
                    driverInstance = (Driver) Class.forName(driverClassName).getDeclaredConstructors()[0].newInstance();
                }
            }
        }
        return driverInstance;
    }

    public String buildUrl(String host, int port, String database) {
        return "jdbc:" + name() + "://" + host + ":" + port + "/" + database;
    }
}
