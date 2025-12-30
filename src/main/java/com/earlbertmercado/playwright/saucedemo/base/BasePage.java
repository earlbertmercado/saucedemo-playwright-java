package com.earlbertmercado.playwright.saucedemo.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasePage {

    protected Page page;
    protected final Logger logger;

    public BasePage(Page page) {
        this.page = page;
        this.logger = LogManager.getLogger(this.getClass());
    }

    public Page getPage() {
        return page;
    }

    protected Locator locator(String selector) {
        return page.locator(selector);
    }
}