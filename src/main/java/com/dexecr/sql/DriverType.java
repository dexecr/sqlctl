package com.dexecr.sql;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Driver;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

@RequiredArgsConstructor
public enum DriverType {

    postgresql("org.postgresql.Driver",
            PropertyDescriptor.builder()
                    .username("user")
                    .password("password")
                    .build()
    ),
    mysql("com.mysql.cj.jdbc.Driver",
            PropertyDescriptor.builder()
                    .username("user")
                    .password("password")
                    .build()
    ),
    h2("org.h2.Driver",
            PropertyDescriptor.builder()
                    .username("USER")
                    .password("PASSWORD")
                    .build()
    );


    private static final String URL_FORMAT = "jdbc:<driver>://<host>:<port>/<database>?<properties>";

    private final String driverClassName;
    private final PropertyDescriptor propertyDescriptor;
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

    public static DriverType fromUrl(String url) {
        Objects.requireNonNull(url, "Url is null");
        if (!url.startsWith("jdbc:")) {
            throw new IllegalArgumentException("Url is not in jdbc format: " + URL_FORMAT);
        }
        if (url.indexOf(":", "jdbc:".length()) == -1) {
            throw new IllegalArgumentException("Driver missed: " + URL_FORMAT);
        }
        String driver = url.substring("jdbc:".length(), url.indexOf(":", "jdbc:".length()));
        return Arrays.stream(DriverType.values()).filter(d -> d.name().equals(driver)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Driver is not supported: " + driver));
    }


    public String buildUrl(String host, int port, String database) {
        return "jdbc:" + name() + "://" + host + ":" + port + "/" + database;
    }

    public Properties credentialsInfo(Properties properties, CredentialsInfo credentials) {
        properties.put(propertyDescriptor.username, credentials.getUsername());
        properties.put(propertyDescriptor.password, new String(credentials.getPassword()));
        return properties;
    }

    public Properties credentialsInfo(CredentialsInfo credentials) {
        return credentialsInfo(new Properties(), credentials);
    }

    @Builder
    private static class PropertyDescriptor {
        private String username;
        private String password;
    }
}
