package com.dexecr.sql;

import java.util.Map;
import java.util.Properties;

public interface InfoProvider {

    default Properties build(CredentialsInfo credentials) {
        return build(credentials, Map.of());
    }

    default Properties build(CredentialsInfo credentials, Map<String, Object> properties) {
        Properties info = new Properties();
        info.put("user", credentials.getUsername());
        info.put("password", new String(credentials.getPassword()));
        properties.forEach(this::validate);
        info.putAll(properties);
        return info;
    }

    void validate(String key, Object value) throws IllegalArgumentException;

}
