package com.earlbertmercado.playwright.saucedemo.tests;

import com.earlbertmercado.playwright.saucedemo.base.BaseTest;
import com.earlbertmercado.playwright.saucedemo.constants.AppConstants;
import com.earlbertmercado.playwright.saucedemo.pages.CartPage;
import com.earlbertmercado.playwright.saucedemo.pages.components.HeaderComponent;
import com.earlbertmercado.playwright.saucedemo.pages.InventoryPage;
import com.earlbertmercado.playwright.saucedemo.pages.ItemDetailPage;
import com.earlbertmercado.playwright.saucedemo.pages.LoginPage;
import com.earlbertmercado.playwright.saucedemo.utils.AppStateUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HeaderComponentTest extends BaseTest {

    @Test
    public void testBurgerMenuAllItemsLink() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);
        HeaderComponent header = new HeaderComponent(page);

        ItemDetailPage itemDetailPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .clickItemNameByIndex(FIRST_ITEM);

        header.clickAllItemsLink();

        assertThat(page.url())
                .as("Inventory page URL after clicking All Items link")
                .isEqualTo(AppConstants.INVENTORY_URL);

        softly.assertThat(new InventoryPage(page).getPageTitle())
                .as("Inventory page title after clicking All Items link")
                .isEqualTo("Products");

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testBurgerMenuAboutLink() {
        AppStateUtils appStateUtils = new AppStateUtils(page);
        HeaderComponent header = new HeaderComponent(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword());

        header.clickAboutLink();

        assertThat(page.url())
                .as("Sauce Labs About page URL after clicking About link")
                .isEqualTo(AppConstants.SAUCE_LABS_URL);

        inventoryPage.getPage().goBack();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testBurgerMenuLogoutLink() {
        HeaderComponent header = new HeaderComponent(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword());

        logger.info("Executing logout via Sidebar menu.");
        LoginPage loginPage = header.clickLogoutLink();

        logger.info("Verifying redirection to login page and UI reset.");
        assertThat(page.url())
                .as("Login page URL after clicking Logout link")
                .isEqualTo(AppConstants.BASE_URL + "/");

        assertThat(loginPage.isLoginButtonVisible())
                .as("Login button visible after logout")
                .isTrue();
    }

    @Test
    public void testResetAppStateLink() {
        int FIRST_ITEM = 0;

        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);
        HeaderComponent header = new HeaderComponent(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM);

        logger.info("Initial State: Item added. Cart badge visible: {}", header.isShoppingCartBadgeVisible());

        softly.assertThat(header.isShoppingCartBadgeVisible())
                .as("Shopping cart badge visible before resetting app state")
                .isTrue();

        logger.info("Triggering Reset App State action.");
        header.clickResetAppStateLink();

        logger.info("Verifying that application state has been wiped.");
        softly.assertThat(header.isShoppingCartBadgeVisible())
                .as("Shopping cart badge not visible after resetting app state")
                .isFalse();

        boolean allButtonsReset = inventoryPage.getAddToCartButtons().allTextContents()
                .stream().allMatch(text -> text.equals("Add to cart"));

        logger.debug("Verified all item buttons reverted to 'Add to cart': {}", allButtonsReset);
        softly.assertThat(allButtonsReset)
                .as("All buttons show 'Add to cart' after resetting app state")
                .isTrue();

        softly.assertAll();
        appStateUtils.resetStateAndLogout();
    }

    @Test
    public void testCartBadgePersistence() {
        int FIRST_ITEM = 0;
        int SECOND_ITEM = 1;
        int THIRD_ITEM = 2;

        AppStateUtils appStateUtils = new AppStateUtils(page);
        HeaderComponent header = new HeaderComponent(page);

        logger.info("Adding 3 items to cart to establish baseline count.");
        CartPage cartPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword())
                .addItemsToCart(FIRST_ITEM, SECOND_ITEM, THIRD_ITEM)
                .clickShoppingCart();

        String badgeCount = header.getShoppingCartBadgeText();
        logger.info("Initial badge count: {}. Performing page reload to test persistence.", badgeCount);
        cartPage.getPage().reload();

        String badgeAfterReload = header.getShoppingCartBadgeText();
        logger.info("Badge count after reload: {}", badgeAfterReload);

        Assertions.assertThat(badgeAfterReload)
                .as("Badge persistence check")
                .isEqualTo("3");

        appStateUtils.resetStateAndLogout();
    }
}