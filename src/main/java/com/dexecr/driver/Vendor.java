package com.dexecr.driver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Driver;

@RequiredArgsConstructor
@Getter
public enum Vendor {

    postgresql("org.postgresql.Driver"),
    mysql("com.mysql.cj.jdbc.Driver"),
    h2("org.h2.Driver");
    private final String driverClassName;
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
}
