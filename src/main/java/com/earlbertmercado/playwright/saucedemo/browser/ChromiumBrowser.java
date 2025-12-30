package com.earlbertmercado.playwright.saucedemo.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

/**
 * IBrowserCreator implementation for Chromium browser.
 * Launches Chromium with optional headless mode and custom viewport size.
 */
public class ChromiumBrowser implements IBrowserCreator {

    /**
     * Creates a new Chromium browser context using the given Playwright instance.
     * Sets viewport size using the default method from IBrowserCreator.
     */
    @Override
    public BrowserContext createContext(Playwright playwright,
                                        boolean isHeadless,
                                        Integer width,
                                        Integer height) {

        Browser browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions()
                        .setHeadless(isHeadless));

        return browser.newContext(setBrowserSize(width, height));
    }
}
