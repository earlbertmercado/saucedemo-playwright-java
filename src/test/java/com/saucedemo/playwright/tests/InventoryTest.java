package com.saucedemo.playwright.tests;

import com.saucedemo.playwright.base.BaseTest;
import com.saucedemo.playwright.constants.AppConstants;
import com.saucedemo.playwright.constants.HeaderLocators;
import com.saucedemo.playwright.pages.InventoryPage;
import com.saucedemo.playwright.pages.ItemDetailPage;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.utils.AppStateUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryTest extends BaseTest {

    @Test
    public void testInventoryPageLoad() {
        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword());

        logger.info("Verifying Inventory page elements presence and visibility.");
        assertThat(inventoryPage.getPage().url())
                .as("Inventory page URL")
                .isEqualTo(AppConstants.INVENTORY_URL);

        softly.assertThat(inventoryPage.getPageTitle())
                .as("Inventory page title")
                .isEqualTo("Products");

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

        softly.assertThat(inventoryPage.areAddToCartButtonsVisible())
                .as("Add to cart buttons visible for each item")
                .isTrue();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testInventorySortingByNameAsc() {
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .sortByNameAsc();

        logger.info("Verifying that item prices are sorted in ascending order.");
        assertThat(inventoryPage.isSortedAlphabeticallyAsc())
                .as("Inventory sorted by name ascending")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testInventorySortingByNameDesc() {
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .sortByNameDesc();

        logger.info("Verifying that item prices are sorted in descending order.");
        assertThat(inventoryPage.isSortedAlphabeticallyDesc())
                .as("Inventory sorted by name descending")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testInventorySortingByPriceAsc() {
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .sortByPriceAsc();

        logger.info("Verifying that item prices are sorted ascending order.");
        assertThat(inventoryPage.isSortedByPriceAsc())
                .as("Inventory sorted by price ascending")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testInventorySortingByPriceDesc() {
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .sortByPriceDesc();

        logger.info("Verifying that item prices are sorted descending order.");
        assertThat(inventoryPage.isSortedByPriceDesc())
                .as("Inventory sorted by price descending")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testEachItemDetails() {
        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword());

        assertThat(inventoryPage.areAllItemsValid())
                .as("All inventory items have valid details")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testShoppingCartBadgeAfterAddingItems() {
        int FIRST_ITEM = 0;
        int SECOND_ITEM = 1;
        int THIRD_ITEM = 2;
        int EXPECTED_CART_BADGE_COUNT = 3;

        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemToCartByIndex(FIRST_ITEM)
                .addItemToCartByIndex(SECOND_ITEM)
                .addItemToCartByIndex(THIRD_ITEM);

        int actualCount = inventoryPage.getCartItemCount();
        logger.info("Verification: Cart badge count is {}", actualCount);

        assertThat(actualCount)
                .as("Shopping cart badge item count after adding items")
                .isEqualTo(EXPECTED_CART_BADGE_COUNT);

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testShoppingCartBadgeAfterRemovingItems() {
        int FIRST_ITEM = 0;
        int SECOND_ITEM = 1;
        int THIRD_ITEM = 2;

        AppStateUtils appStateUtils = new AppStateUtils(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemToCartByIndex(FIRST_ITEM)
                .addItemToCartByIndex(SECOND_ITEM)
                .addItemToCartByIndex(THIRD_ITEM)
                .removeItemFromCartByIndex(FIRST_ITEM)
                .removeItemFromCartByIndex(SECOND_ITEM)
                .removeItemFromCartByIndex(THIRD_ITEM);

        boolean isBadgeVisible = inventoryPage
                .getPage()
                .locator(HeaderLocators.SHOPPING_CART_BADGE)
                .isVisible();
        logger.info("Verification: Cart badge visibility after removing all items is {}", isBadgeVisible);
        assertThat(isBadgeVisible)
                .as("Shopping cart badge is invisible after removing all items")
                .isFalse();

        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testClickingItemNavigatesToItemDetailPage() {
        int TARGET_INDEX = 0;

        AppStateUtils appStateUtils = new AppStateUtils(page);
        InventoryPage inventoryPage = new InventoryPage(page);

        ItemDetailPage itemDetailPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .clickItemNameByIndex(TARGET_INDEX);

        logger.info("Verifying navigation to Detail Page for item index {}", TARGET_INDEX);
        assertThat(itemDetailPage.isRedirectedToItemDetailPage())
                .as("Redirected to item detail page when clicking item name")
                .isTrue();

        itemDetailPage.clickBackToProducts();
        inventoryPage.clickItemImageByIndex(TARGET_INDEX);

        assertThat(itemDetailPage.isRedirectedToItemDetailPage())
                .as("Redirected to item detail page when clicking item image")
                .isTrue();

        appStateUtils.resetStateAndLogout();
    }
}