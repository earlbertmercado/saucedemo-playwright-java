package com.earlbertmercado.playwright.saucedemo.tests;

import com.earlbertmercado.playwright.saucedemo.base.BaseTest;
import com.earlbertmercado.playwright.saucedemo.constants.AppConstants;
import com.earlbertmercado.playwright.saucedemo.pages.InventoryPage;
import com.earlbertmercado.playwright.saucedemo.pages.LoginPage;
import com.earlbertmercado.playwright.saucedemo.utils.AppStateUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest extends BaseTest {

    @Test
    public void testLoginPageLoad() {
        SoftAssertions softly = new SoftAssertions();

        logger.debug("Navigating to Login Page...");
        LoginPage loginPage = new LoginPage(page).navigate();


        logger.info("Verifying login page URL and UI elements visibility.");
        assertThat(loginPage.getPage().url())
                .as("Login page URL")
                .contains(AppConstants.BASE_URL);

        softly.assertThat(loginPage.isUsernameInputVisible())
                .as("Username input visible")
                .isTrue();

        softly.assertThat(loginPage.isPasswordInputVisible())
                .as("Password input visible")
                .isTrue();

        softly.assertThat(loginPage.isLoginButtonVisible())
                .as("Login button visible")
                .isTrue();

        softly.assertAll();
    }

    @Test
    public void testValidLogin() {
        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        logger.debug("Performing login sequence...");
        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .enterUsername(user.getUsername())
                .enterPassword(user.getPassword())
                .clickLoginButton();

        logger.info("Verifying inventory page components after login.");
        assertThat(inventoryPage.getPage().url())
                .as("Inventory page URL")
                .isEqualTo(AppConstants.INVENTORY_URL);

        softly.assertThat(inventoryPage.isBurgerMenuVisible())
                .as("Burger menu visible")
                .isTrue();

        softly.assertThat(inventoryPage.isShoppingCartVisible())
                .as("Shopping cart visible")
                .isTrue();

        softly.assertThat(inventoryPage.isSortDropdownVisible())
                .as("Sort dropdown visible")
                .isTrue();

        softly.assertThat(inventoryPage.areItemsVisible())
                .as("Inventory items visible")
                .isTrue();

        softly.assertAll();

        logger.debug("Resetting application state and logging out.");
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testInvalidLogin() {
        SoftAssertions softly = new SoftAssertions();

        logger.debug("Attempting login with invalid credentials...");
        LoginPage loginPage = new LoginPage(page).navigate();

        loginPage.enterUsername("invalid_username")
                .enterPassword("invalid_password")
                .clickLoginButton();

        logger.info("Verifying error message display for invalid credentials.");
        softly.assertThat(loginPage.isErrorMessageDisplayed())
                .as("Error message displayed")
                .isTrue();

        softly.assertThat(loginPage.hasErrorMessage("Username and password do not match"))
                .as("Error message contains correct text")
                .isTrue();

        loginPage.clearAllFields();
        softly.assertAll();
    }
}