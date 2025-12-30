package com.earlbertmercado.playwright.saucedemo.pages;

import com.earlbertmercado.playwright.saucedemo.base.BasePage;
import com.earlbertmercado.playwright.saucedemo.constants.AppConstants;
import com.earlbertmercado.playwright.saucedemo.constants.LoginPageLocators;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        super(page);
        usernameInput   = locator(LoginPageLocators.USERNAME_INPUT);
        passwordInput   = locator(LoginPageLocators.PASSWORD_INPUT);
        loginButton     = locator(LoginPageLocators.LOGIN_BUTTON);
        errorMessage    = locator(LoginPageLocators.ERROR_MESSAGE);
    }

    public LoginPage navigate() {
        logger.info("Navigating to Login Page: {}", AppConstants.BASE_URL);
        page.navigate(AppConstants.BASE_URL);
        return this;
    }

    // ------------------ Action Methods ------------------
    public LoginPage enterUsername(String username) {
        logger.debug("Entering username: {}", username);
        usernameInput.fill(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        logger.debug("Entering password.");
        passwordInput.fill(password);
        return this;
    }

    public InventoryPage clickLoginButton() {
        logger.debug("Clicking login button.");
        loginButton.click();
        return new InventoryPage(page);
    }

    public InventoryPage login(String username, String password) {
        logger.info("Attempting login for user: {}", username);
        return enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
    }

    public void clearUsername() {
        usernameInput.clear();
    }

    public void clearPassword() {
        passwordInput.clear();
    }

    public void clearAllFields() {
        logger.debug("Clearing login form fields.");
        clearUsername();
        clearPassword();
    }

    // ------------------ Verification Methods ------------------
    public boolean isUsernameInputVisible() {
        return usernameInput.isVisible();
    }

    public boolean isPasswordInputVisible() {
        return passwordInput.isVisible();
    }

    public boolean isLoginButtonVisible() {
        return loginButton.isVisible();
    }

    public boolean isErrorMessageDisplayed() {
        boolean isVisible = errorMessage.isVisible();
        if (isVisible) {
            logger.warn("Error message is visible on the Login Page.");
        }
        return isVisible;
    }

    public String getErrorMessage() {
        String msg = errorMessage.textContent().trim();
        logger.debug("Captured error message: {}", msg);
        return msg;
    }

    public boolean hasErrorMessage(String expectedError) {
        return isErrorMessageDisplayed()
                && getErrorMessage().contains(expectedError);
    }
}
