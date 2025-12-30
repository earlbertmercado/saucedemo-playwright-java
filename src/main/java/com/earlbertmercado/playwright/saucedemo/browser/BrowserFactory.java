package com.earlbertmercado.playwright.saucedemo.browser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Factory class to create browser-specific context creators.
 * Returns an IBrowserCreator implementation based on the requested browser type.
 * Logs an error and throws an exception if the browser type is unsupported.
 */
public class BrowserFactory {

    private static final Logger logger = LogManager.getLogger(BrowserFactory.class);

    // Create a browser context based on the given browser type
    public static IBrowserCreator createContext(BrowserTypes browserType) {
        return switch (browserType) {
            case CHROMIUM -> new ChromiumBrowser();
            case FIREFOX -> new FirefoxBrowser();
            case WEBKIT -> new WebkitBrowser();
            case CHROME -> new ChromeBrowser();
            case EDGE -> new EdgeBrowser();
            default -> {
                logger.error("Unsupported browser type: {}", browserType);
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
            }
        };
    }
}
