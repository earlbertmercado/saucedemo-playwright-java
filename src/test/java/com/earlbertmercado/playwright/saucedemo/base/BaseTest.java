package com.earlbertmercado.playwright.saucedemo.base;

import com.earlbertmercado.playwright.saucedemo.browser.BrowserManager;
import com.earlbertmercado.playwright.saucedemo.dataprovider.TestDataLoader;
import com.earlbertmercado.playwright.saucedemo.dataprovider.TestDataUsers;
import com.earlbertmercado.playwright.saucedemo.utils.InitializeProperties;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.Properties;

public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected BrowserManager browserManager;
    protected Page page;
    protected Properties properties;
    protected TestDataUsers user;

    //  Initializes BrowserManager, loads properties, and launches the browser before any tests.
    @BeforeClass
    public void setupBrowserAndPages() {
        browserManager = new BrowserManager();
        properties = InitializeProperties.loadProperties();

        page = browserManager.initializeBrowser(properties);

        if (page == null) {
            throw new RuntimeException("Setup failed: Browser not launched.");
        }

        logger.info("Browser launched successfully.");
    }

    // Loads the user test data before each test method
    @BeforeMethod
    public void loadTestUser() {
        String userKey = System.getProperty("user", "standard_user");
        user = TestDataLoader.getUser(userKey);
        if (user == null) {
            throw new RuntimeException("Test user not found: " + userKey);
        }
        logger.info("Loaded user test data: {}", userKey);
    }

    //  Closes the browser and cleans up resources after all tests in the class have executed.
    @AfterClass
    public void tearDownBrowser() {
        if (browserManager != null) {
            browserManager.closeBrowser();
            logger.info("Browser closed successfully.");
        }
    }
}
