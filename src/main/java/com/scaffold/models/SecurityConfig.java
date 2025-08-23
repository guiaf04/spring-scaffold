package com.scaffold.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class SecurityConfig {
    private String packageName;
    private String jwtSecret;
    private long jwtExpiration;
    private String userEntity;
    private String userPackage;
    private String userRepositoryPackage;
    private boolean enableCors;
    private String controllerPackage;

    public String getJwtExpirationHours() {
        return String.valueOf(jwtExpiration / 3600000);
    }

    public String getJwtExpirationDays() {
        return String.valueOf(jwtExpiration / 86400000);
    }
}
