package com.earlbertmercado.playwright.saucedemo.tests;

import com.earlbertmercado.playwright.saucedemo.base.BaseTest;
import com.earlbertmercado.playwright.saucedemo.constants.AppConstants;
import com.earlbertmercado.playwright.saucedemo.constants.CheckoutStepOnePageLocators;
import com.earlbertmercado.playwright.saucedemo.pages.CheckoutStepOnePage;
import com.earlbertmercado.playwright.saucedemo.pages.CheckoutStepTwoPage;
import com.earlbertmercado.playwright.saucedemo.pages.LoginPage;
import com.earlbertmercado.playwright.saucedemo.utils.AppStateUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutStepOneTest extends BaseTest {

    @Test
    public void testCheckoutStepOnePageLoad() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        CheckoutStepOnePage checkoutStepOnePage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout();

        logger.info("Verifying checkout form visibility and URL.");
        assertThat(page.url())
                .as("Checkout Step One page URL")
                .isEqualTo(AppConstants.CHECKOUT_STEP_ONE_URL);

        softly.assertThat(checkoutStepOnePage.getPageTitle())
                .as("Checkout Step One page title")
                .isEqualTo("Checkout: Your Information");

        softly.assertThat(checkoutStepOnePage.isFirstNameInputVisible())
                .as("First Name input visible")
                .isTrue();

        softly.assertThat(checkoutStepOnePage.isLastNameInputVisible())
                .as("Last Name input visible")
                .isTrue();

        softly.assertThat(checkoutStepOnePage.isPostalCodeInputVisible())
                .as("Postal Code input visible")
                .isTrue();

        softly.assertThat(checkoutStepOnePage.isContinueButtonVisible())
                .as("Continue button visible")
                .isTrue();

        softly.assertThat(checkoutStepOnePage.isCancelButtonVisible())
                .as("Cancel button visible")
                .isTrue();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepOneFormSubmission() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        CheckoutStepTwoPage checkoutStepTwoPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(user.getFirstName())
                .enterLastName(user.getLastName())
                .enterPostalCode(user.getPostalCode())
                .clickContinueButton();

        logger.info("Verifying navigation to Checkout Step Two page after form submission.");
        assertThat(page.url())
                .as("Checkout Step Two page URL")
                .isEqualTo(AppConstants.CHECKOUT_STEP_TWO_URL);

        softly.assertThat(checkoutStepTwoPage.areItemDetailsValid(FIRST_ITEM))
                .as("Item details valid on Checkout Step Two page")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepOneWithEmptyFirstName() {
        int FIRST_ITEM = 0;
        String EXPECTED_ERROR_MESSAGE = "Error: First Name is required";

        AppStateUtils appStateUtils = new AppStateUtils(page);

        logger.debug("Submitting checkout form with missing First Name...");
        new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterLastName(user.getLastName())
                .enterPostalCode(user.getPostalCode())
                .clickContinueButton();

        String actualErrorMessage = page
                .locator(CheckoutStepOnePageLocators.ERROR_MESSAGE)
                .textContent()
                .trim();
        logger.info("Captured error when missing first name: '{}'", actualErrorMessage);

        assertThat(actualErrorMessage)
                .as("Error message for empty First Name")
                .isEqualTo(EXPECTED_ERROR_MESSAGE);

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepOneWithEmptyLastName() {
        int FIRST_ITEM = 0;
        String EXPECTED_ERROR_MESSAGE = "Error: Last Name is required";

        AppStateUtils appStateUtils = new AppStateUtils(page);

        logger.debug("Submitting checkout form with missing Last Name...");
        new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(user.getFirstName())
                .enterPostalCode(user.getPostalCode())
                .clickContinueButton();

        String actualErrorMessage = page
                .locator(CheckoutStepOnePageLocators.ERROR_MESSAGE)
                .textContent()
                .trim();
        logger.info("Captured error when missing last name: '{}'", actualErrorMessage);

        assertThat(actualErrorMessage)
                .as("Error message for empty Last Name")
                .isEqualTo(EXPECTED_ERROR_MESSAGE);

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepOneWithEmptyPostalCode() {
        int FIRST_ITEM = 0;
        String EXPECTED_ERROR_MESSAGE = "Error: Postal Code is required";

        AppStateUtils appStateUtils = new AppStateUtils(page);

        logger.debug("Submitting checkout form with missing postal code...");
        new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(user.getFirstName())
                .enterLastName(user.getLastName())
                .clickContinueButton();

        String actualErrorMessage = page
                .locator(CheckoutStepOnePageLocators.ERROR_MESSAGE)
                .textContent()
                .trim();

        logger.info("Captured error when missing postal code: '{}'", actualErrorMessage);
        assertThat(actualErrorMessage)
                .as("Error message for empty Postal Code")
                .isEqualTo(EXPECTED_ERROR_MESSAGE);

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCancelButtonNavigatesToCartPage() {
        int FIRST_ITEM = 0;

        AppStateUtils appStateUtils = new AppStateUtils(page);

        CheckoutStepOnePage checkoutStepOnePage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart()
                .clickCheckout();

        assertThat(page.url())
                .as("Checkout Step One page URL before clicking Cancel")
                .isEqualTo(AppConstants.CHECKOUT_STEP_ONE_URL);

        checkoutStepOnePage.clickCancelButton();

        assertThat(page.url())
                .as("Cart page URL after clicking Cancel")
                .isEqualTo(AppConstants.CART_URL);

        appStateUtils.resetStateAndLogout();
    }
}
