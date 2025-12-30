package com.earlbertmercado.playwright.saucedemo.tests;

import com.earlbertmercado.playwright.saucedemo.base.BaseTest;
import com.earlbertmercado.playwright.saucedemo.constants.AppConstants;
import com.earlbertmercado.playwright.saucedemo.pages.CheckoutStepTwoPage;
import com.earlbertmercado.playwright.saucedemo.pages.LoginPage;
import com.earlbertmercado.playwright.saucedemo.utils.AppStateUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutStepTwoTest extends BaseTest {

    @Test
    public void testCheckoutStepTwoPageLoad() {
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

        logger.info("Verifying Step Two Page URL and UI elements.");
        assertThat(page.url())
                .as("Checkout Step Two page URL")
                .isEqualTo(AppConstants.CHECKOUT_STEP_TWO_URL);

        softly.assertThat(checkoutStepTwoPage.getPageTitle())
                .as("Checkout Step Two page title")
                .isEqualTo("Checkout: Overview");

        softly.assertThat(checkoutStepTwoPage.isFinishButtonVisible())
                .as("Finish button visible")
                .isTrue();

        softly.assertThat(checkoutStepTwoPage.isCancelButtonVisible())
                .as("Cancel button visible")
                .isTrue();

        softly.assertThat(checkoutStepTwoPage.areItemDetailsValid(FIRST_ITEM))
                .as("Item details are valid")
                .isTrue();

        softly.assertThat(checkoutStepTwoPage.getTotalBeforeTax())
                .as("Total before tax is not null")
                .isNotNull();

        softly.assertThat(checkoutStepTwoPage.getTax())
                .as("Tax is not null")
                .isNotNull();

        softly.assertThat(checkoutStepTwoPage.getTotalAfterTax())
                .as("Total after tax is not null")
                .isNotNull();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepMultipleItemTotals() {
        int FIRST_ITEM = 0;
        int SECOND_ITEM = 1;
        int THIRD_ITEM = 2;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        logger.debug("Navigating through checkout with multiple items...");
        CheckoutStepTwoPage checkoutStepTwoPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM, SECOND_ITEM, THIRD_ITEM)
                .clickShoppingCart()
                .clickCheckout()
                .enterFirstName(user.getFirstName())
                .enterLastName(user.getLastName())
                .enterPostalCode(user.getPostalCode())
                .clickContinueButton();

        logger.info("Beginning math verification for Checkout Totals.");
        Double expectedTotalBeforeTax = checkoutStepTwoPage.getTotalItemPrices();
        Double actualTotalBeforeTax = checkoutStepTwoPage.getTotalBeforeTax();
        logger.info("Expected Subtotal: ${}, Actual: ${}", expectedTotalBeforeTax, actualTotalBeforeTax);

        softly.assertThat(actualTotalBeforeTax)
                .as("Total before tax matches sum of item prices")
                .isEqualTo(expectedTotalBeforeTax);

        Double tax = checkoutStepTwoPage.getTax();
        Double expectedTotalAfterTax = expectedTotalBeforeTax + tax;
        Double actualTotalAfterTax = checkoutStepTwoPage.getTotalAfterTax();
        logger.info("Tax: ${}", tax);
        logger.info("Expected Final: ${}, Actual Final: ${}", expectedTotalAfterTax, actualTotalAfterTax);

        softly.assertThat(actualTotalAfterTax)
                .as("Total after tax matches total before tax plus tax")
                .isEqualTo(expectedTotalAfterTax);

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCheckoutStepTwoCancelButton() {
        int FIRST_ITEM = 0;

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

        checkoutStepTwoPage.clickCancelButton();

        assertThat(page.url())
                .as("URL after clicking Cancel button")
                .isEqualTo(AppConstants.INVENTORY_URL);

        appStateUtils.resetStateAndLogout();
    }
}
