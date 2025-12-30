package com.earlbertmercado.playwright.saucedemo.browser;

/**
 * Enum representing supported browser types.
 * Provides a utility method to convert a string to the corresponding BrowserTypes value.
 * Throws an exception if the input string is null, empty, or does not match a supported browser.
 */
public enum BrowserTypes {
    CHROMIUM,
    FIREFOX,
    WEBKIT,
    CHROME,
    EDGE;

    // Convert string to BrowserTypes enum, validating input
    public static BrowserTypes fromString(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Browser name cannot be null or empty");
        }

        try {
            return BrowserTypes.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported browser: " + name, e);
        }
    }
}
