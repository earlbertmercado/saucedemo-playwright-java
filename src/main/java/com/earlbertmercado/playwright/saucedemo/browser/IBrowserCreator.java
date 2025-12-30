package com.earlbertmercado.playwright.saucedemo.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Playwright;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Interface for creating Playwright browser contexts.
 * Implementations provide browser-specific context creation.
 * Includes a default method to set browser viewport size,
 * using either provided dimensions or the screen size if not specified.
 */
public interface IBrowserCreator {

    // Create a browser context with given Playwright instance, headless flag, and optional dimensions
    BrowserContext createContext(Playwright playwright,
                                 boolean isHeadless,
                                 Integer width,
                                 Integer height);

    // Set the browser viewport size; defaults to screen size if width/height not provided
    default Browser.NewContextOptions setBrowserSize(Integer width, Integer height) {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();

        if (width != null && height != null) {
            contextOptions.setViewportSize(width, height);
        } else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;
            contextOptions.setViewportSize(screenWidth, screenHeight);
        }

        return contextOptions;
    }
}
