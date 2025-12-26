package com.saucedemo.playwright.tests;

import com.saucedemo.playwright.base.BaseTest;
import com.saucedemo.playwright.constants.AppConstants;
import com.saucedemo.playwright.constants.HeaderLocators;
import com.saucedemo.playwright.constants.InventoryPageLocators;
import com.saucedemo.playwright.constants.ItemDetailPageLocators;
import com.saucedemo.playwright.pages.InventoryPage;
import com.saucedemo.playwright.pages.ItemDetailPage;
import com.saucedemo.playwright.pages.LoginPage;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemDetailTest extends BaseTest {

    @Test
    public void testItemDetailPageLoad() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();

        ItemDetailPage itemDetailPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .clickItemNameByIndex(FIRST_ITEM);

        logger.info("Verifying item detail page loads with all UI components.");
        assertThat(itemDetailPage.getPage().url())
                .as("Item Detail page URL")
                .contains(AppConstants.ITEM_DETAIL_URL);

        softly.assertThat(itemDetailPage.isItemNameVisibleAndNotBlank())
                .as("Item name visible and not blank")
                .isTrue();

        softly.assertThat(itemDetailPage.isItemDescriptionVisibleAndNotBlank())
                .as("Item description visible and not blank")
                .isTrue();

        softly.assertThat(itemDetailPage.isItemPriceVisibleAndNotBlank())
                .as("Item price visible and not blank")
                .isTrue();

        softly.assertThat(itemDetailPage.isItemImageVisible())
                .as("Item image visible")
                .isTrue();

        softly.assertThat(itemDetailPage.isBackToProductsButtonVisible())
                .as("Back to products button visible")
                .isTrue();

        softly.assertAll();
    }

    @Test
    public void testAddAndRemoveItemToCart() {
        int FIRST_ITEM = 0;
        String EXPECTED_CART_BADGE_DISPLAYED_NUMBER = "1";

        SoftAssertions softly = new SoftAssertions();

        ItemDetailPage itemDetailPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .clickItemNameByIndex(FIRST_ITEM);

        logger.info("Adding item to cart from detail page.");
        itemDetailPage.clickAddToCartButton();

        String badgeCount = page.locator(HeaderLocators.SHOPPING_CART_BADGE).innerText();
        logger.info("Verified cart badge updated to: {}", badgeCount);
        softly.assertThat(badgeCount)
                .as("Shopping cart badge shows 1 item")
                .isEqualTo(EXPECTED_CART_BADGE_DISPLAYED_NUMBER);

        softly.assertThat(page.locator(ItemDetailPageLocators.REMOVE_BUTTON).isVisible())
                .as("Remove button is visible after adding to cart")
                .isTrue();

        logger.info("Removing item from cart from detail page.");
        itemDetailPage.clickRemoveButton();

        boolean isBadgeVisible = page.locator(HeaderLocators.SHOPPING_CART_BADGE).isVisible();
        logger.debug("Cart badge visibility after removal: {}", isBadgeVisible);

        softly.assertThat(isBadgeVisible)
                .as("Shopping cart badge is not visible after removing item")
                .isFalse();

        softly.assertThat(page.locator(ItemDetailPageLocators.ADD_TO_CART_BUTTON).isVisible())
                .as("Add to cart button is visible after removing item")
                .isTrue();

        softly.assertAll();
    }

    @Test
    public void testBackToProductsButton() {
        int FIRST_ITEM = 0;

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .clickItemNameByIndex(FIRST_ITEM)
                .clickBackToProducts();

        assertThat(page.url())
                .as("URL after clicking Back to Products button")
                .endsWith(AppConstants.INVENTORY_URL);
    }

    @Test
    public void testSpecificItemDetailPage() {
        int FLEE_JACKET_ID = 5;
        double EXPECTED_PRICE = 49.99;

        SoftAssertions softly = new SoftAssertions();

        ItemDetailPage itemDetailPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .clickSpecificItem(InventoryPageLocators.FLEE_JACKET);

        String actualName = itemDetailPage.getItemName();
        Double actualPrice = itemDetailPage.getItemPrice();

        logger.info("Verifying specific product details: " +
                "[Expected Name: Sauce Labs Fleece Jacket, Actual: {}]", actualName);
        logger.info("Verifying product price: [Expected: ${}, Actual: ${}]", EXPECTED_PRICE, actualPrice);

        assertThat(page.url())
                .as("Item Detail page URL for Sauce Labs Fleece Jacket")
                .contains(AppConstants.ITEM_DETAIL_URL + FLEE_JACKET_ID);

        softly.assertThat(actualName)
                .as("Item name for Sauce Labs Fleece Jacket")
                .isEqualTo("Sauce Labs Fleece Jacket");

        softly.assertThat(actualPrice)
                .as("Item price for Sauce Labs Fleece Jacket")
                .isEqualTo(EXPECTED_PRICE);

        softly.assertThat(itemDetailPage.getItemDescription())
                .as("Item description for Sauce Labs Fleece Jacket")
                .isEqualTo(
                        "It's not every day that you come across a midweight quarter-zip " +
                                "fleece jacket capable of handling everything from a relaxing day " +
                                "outdoors to a busy day at the office."
                );

        softly.assertAll();
    }

    @Test
    public void testItemPriceNotZero() {
        int FIRST_ITEM = 0;

        ItemDetailPage itemDetailPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .clickItemNameByIndex(FIRST_ITEM);

        assertThat(itemDetailPage.getItemPrice())
                .as("Item price should be greater than 0")
                .isGreaterThan(0.0);
    }
}
