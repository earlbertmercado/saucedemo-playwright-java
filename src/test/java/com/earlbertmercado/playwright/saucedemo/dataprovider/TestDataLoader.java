package com.earlbertmercado.playwright.saucedemo.dataprovider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * Loads user-related test data from a JSON file located in the project's resources.
 * Data is loaded lazily (only on first request) and cached in memory for subsequent calls.
 * The loader is thread-safe to ensure consistent behavior in parallel executions.
 */
public class TestDataLoader {

    private static final String USERS_RESOURCE = "/testdata/users.json";
    private static Map<String, TestDataUsers> users;

    // Ensures that the test data is loaded exactly once.
    private static synchronized void ensureLoaded() {
        // Already loaded → do nothing
        if (users != null) return;

        ObjectMapper mapper = new ObjectMapper();

        // Load the JSON file as a resource stream
        try (InputStream is = TestDataLoader.class.getResourceAsStream(USERS_RESOURCE)) {
            if (is == null) {
                users = Collections.emptyMap();
            } else {
                // Convert JSON into a strongly-typed Map<String, TestDataUsers>
                users = mapper.readValue(is, new TypeReference<Map<String, TestDataUsers>>() {});
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data: " + USERS_RESOURCE, e);
        }
    }

    // Returns a single user object from the loaded test data.
    public static TestDataUsers getUser(String key) {
        ensureLoaded();
        return users.get(key);
    }

    // Returns an unmodifiable view of all user test data.
    public static Map<String, TestDataUsers> getAllUsers() {
        ensureLoaded();
        return Collections.unmodifiableMap(users);
    }
}
