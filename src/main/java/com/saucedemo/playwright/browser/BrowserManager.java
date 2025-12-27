package com.saucedemo.playwright.browser;

import com.saucedemo.playwright.constants.AppConstants;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

/**
 * Manages the Playwright browser lifecycle for each thread.
 * Provides methods to initialize a browser, create a page, and clean up resources.
 * Uses ThreadLocal to support parallel test execution without conflicts.
 * Supports configurable browser type, headless mode, and browser dimensions.
 */
public class BrowserManager {

    private static final Logger logger = LogManager.getLogger(BrowserManager.class);

    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();
    private static final ThreadLocal<BrowserManager> tlFactory = new ThreadLocal<>();

    // Get the current thread's BrowserManager instance
    public static BrowserManager getFactoryInstance() {
        return tlFactory.get();
    }

    // Get the current thread's Page
    public Page getPage() {
        return tlPage.get();
    }

    // Initialize Playwright browser, context, and page based on properties
    public Page initializeBrowser(Properties props) {

        // Get browser and headless settings:
        // 1. Check if overridden via Maven -D parameters (e.g., -Dbrowser=EDGE -DisHeadless=true)
        // 2. If not provided, fallback to config.properties values
        // 3. If still not set, use defaults: CHROMIUM for browser, false for headless
        String browserName = getProperty(props, "browser", "CHROMIUM");
        boolean headless = Boolean.parseBoolean(getProperty(props, "isHeadless", "false"));

        Integer width = parseDimension(props.getProperty("browserWidth"), "width");
        Integer height = parseDimension(props.getProperty("browserHeight"), "height");

        BrowserTypes browserType = BrowserTypes.fromString(browserName);
        logger.info("Starting {} browser with {} mode", browserType, headless ? "headless" : "headed");

        // Create Playwright instance for this thread
        tlPlaywright.set(Playwright.create());
        IBrowserCreator creator = BrowserFactory.createContext(browserType);

        // Create browser context with given dimensions and headless option
        tlBrowserContext.set(creator.createContext(tlPlaywright.get(), headless, width, height));

        // Create a new page and navigate to base URL
        Page page = tlBrowserContext.get().newPage();
        page.navigate(AppConstants.BASE_URL);

        tlPage.set(page);
        tlFactory.set(this);

        logger.info("Started {} browser", browserType);
        return page;
    }

    // Close browser, context, and Playwright instance, with cleanup
    public void closeBrowser() {
        logger.info("Closing browser.");

        try {
            if (tlBrowserContext.get() != null) {
                Browser browser = tlBrowserContext.get().browser();
                tlBrowserContext.get().close();
                if (browser != null) browser.close();
            }
            if (tlPlaywright.get() != null) tlPlaywright.get().close();
        } catch (Exception e) {
            logger.error("Close failed: {}", e.getMessage(), e);
        } finally {
            // Remove thread-local references to prevent memory leaks
            tlPage.remove();
            tlBrowserContext.remove();
            tlPlaywright.remove();
            tlFactory.remove();
            logger.info("Cleanup done.");
        }
    }

    // Parse dimension string to integer with logging and fallback
    private Integer parseDimension(String value, String type) {
        if (value != null && !value.trim().isEmpty()) {
            try {
//                int dim = Integer.parseInt(value.trim());
//                logger.info("{}: {}", type, dim);
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                logger.warn("Invalid {} '{}'", type, value);
            }
        }
        return null;
    }

    // Helper to get a property with system override and default fallback
    private String getProperty(Properties props, String key, String defaultValue) {
        return System.getProperty(key, props.getProperty(key, defaultValue));
    }
}
