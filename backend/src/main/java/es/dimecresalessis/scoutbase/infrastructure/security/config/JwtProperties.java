package es.dimecresalessis.scoutbase.infrastructure.security.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="security.jwt")
public record JwtProperties(String secret, long expirationMs) {}