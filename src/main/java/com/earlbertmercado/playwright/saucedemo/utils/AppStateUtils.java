package com.earlbertmercado.playwright.saucedemo.utils;

import com.earlbertmercado.playwright.saucedemo.constants.HeaderLocators;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppStateUtils {

    private final Page page;
    private final Logger logger = LogManager.getLogger(AppStateUtils.class);

    private final Locator burgerMenuButton;
    private final Locator resetAppStateLink;
    private final Locator logoutLink;

    public AppStateUtils(Page page) {
        this.page = page;
        burgerMenuButton    = locator(HeaderLocators.BURGER_MENU_BUTTON);
        resetAppStateLink   = locator(HeaderLocators.RESET_APP_STATE_LINK);
        logoutLink          = locator(HeaderLocators.LOGOUT_LINK);
    }

    private Locator locator(String selector) {
        return page.locator(selector);
    }

    public void clickBurgerMenu() {
        burgerMenuButton.click();
        logger.info("Burger menu clicked.");
    }

    public void resetAppState() {
        clickBurgerMenu();
        resetAppStateLink.click();
        page.reload();
        logger.info("Application state has been reset.");
    }

    public void logout() {
        clickBurgerMenu();
        logoutLink.click();
        logger.info("User has been logged out.");
    }

    public void resetStateAndLogout() {
        resetAppState();
        logout();
    }

//    public LoginPage logoutAndReturnLoginPage() {
//        logout();
//        return new LoginPage(page);
//    }
}
