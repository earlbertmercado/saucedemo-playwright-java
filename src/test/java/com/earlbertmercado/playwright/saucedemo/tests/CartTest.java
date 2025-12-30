package com.earlbertmercado.playwright.saucedemo.tests;

import com.earlbertmercado.playwright.saucedemo.base.BaseTest;
import com.earlbertmercado.playwright.saucedemo.constants.AppConstants;
import com.earlbertmercado.playwright.saucedemo.pages.CartPage;
import com.earlbertmercado.playwright.saucedemo.pages.components.HeaderComponent;
import com.earlbertmercado.playwright.saucedemo.pages.InventoryPage;
import com.earlbertmercado.playwright.saucedemo.pages.LoginPage;
import com.earlbertmercado.playwright.saucedemo.utils.AppStateUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CartTest extends BaseTest {

    @Test
    public void testCartPageLoad() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        CartPage cartPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart();

        logger.info("Verifying Cart Page URL and primary UI components.");
        assertThat(page.url())
                .as("Cart page URL")
                .isEqualTo(AppConstants.CART_URL);

        softly.assertThat(cartPage.getPageTitle())
                .as("Cart page title")
                .isEqualTo("Your Cart");

        softly.assertThat(cartPage.getCartItemCount())
                .as("Number of items in cart")
                .isEqualTo(1);

        softly.assertThat(cartPage.isCheckoutButtonVisible())
                .as("Checkout button visible")
                .isTrue();

        softly.assertThat(cartPage.isContinueShoppingButtonVisible())
                .as("Continue Shopping button visible")
                .isTrue();

        softly.assertThat(cartPage.isValidItem(FIRST_ITEM))
                .as("Item details are valid")
                .isTrue();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testAddOneItemToCart() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        CartPage cartPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart();

        softly.assertThat(cartPage.getCartItemCount())
                .as("Number of items in cart")
                .isEqualTo(1);

        softly.assertThat(cartPage.getTotalQuantity())
                .as("Total quantity of items in cart")
                .isEqualTo(1);

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testAddMultipleItemsToCart() {
        int FIRST_ITEM = 0;
        int SECOND_ITEM = 1;
        int THIRD_ITEM = 2;
        int EXPECTED_TOTAL_QUANTITY = 3;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        CartPage cartPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM, SECOND_ITEM, THIRD_ITEM)
                .clickShoppingCart();

        logger.info("Validating cart item count and total quantity.");
        softly.assertThat(cartPage.getCartItemCount())
                .as("Number of items in cart")
                .isEqualTo(EXPECTED_TOTAL_QUANTITY);

        softly.assertThat(cartPage.getTotalQuantity())
                .as("Total quantity of items in cart")
                .isEqualTo(EXPECTED_TOTAL_QUANTITY);

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testRemoveItemFromCart() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        CartPage cartPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart();

        softly.assertThat(cartPage.getCartItemCount())
                .as("Number of items in cart before removal")
                .isEqualTo(1);

        logger.debug("Current cart count: {}. Removing item...", cartPage.getCartItemCount());
        cartPage.removeItemByIndex(FIRST_ITEM);

        logger.info("Verifying cart is empty after removal.");
        softly.assertThat(cartPage.getCartItemCount())
                .as("Number of items in cart after removal")
                .isEqualTo(0);

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testContinueShoppingButton() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM)
                .clickShoppingCart()
                .clickContinueShopping();

        assertThat(page.url())
                .as("Returned to inventory page URL")
                .contains("/inventory.html");

        softly.assertThat(inventoryPage.areItemsVisible())
                .as("Inventory items are visible after continuing shopping")
                .isTrue();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testEmptyCart() {
        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);
        HeaderComponent header = new HeaderComponent(page);

        CartPage cartPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .clickShoppingCart();

        logger.info("Verifying behavior of an empty cart and header badge.");
        softly.assertThat(cartPage.getCartItemCount())
                .as("Number of items in empty cart")
                .isEqualTo(0);

        softly.assertThat(cartPage.getTotalQuantity())
                .as("Total quantity of items in empty cart")
                .isEqualTo(0);

        softly.assertThat(header.isShoppingCartVisible())
                .as("Shopping cart icon is visible")
                .isTrue();

        softly.assertThat(header.isShoppingCartBadgeVisible())
                .as("Shopping cart badge is not visible for empty cart")
                .isFalse();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }
}