package com.earlbertmercado.playwright.saucedemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class InitializeProperties {

    private InitializeProperties() {
        // Prevent instantiation
    }

    public static Properties loadProperties() {
        Properties props = new Properties();

        try (InputStream input = InitializeProperties.class
                .getResourceAsStream("/config/config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }

            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }

        return props;
    }
}
