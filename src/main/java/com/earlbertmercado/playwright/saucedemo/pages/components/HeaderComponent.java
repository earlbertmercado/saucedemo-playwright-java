package com.earlbertmercado.playwright.saucedemo.pages.components;

import com.earlbertmercado.playwright.saucedemo.base.BasePage;
import com.earlbertmercado.playwright.saucedemo.constants.HeaderLocators;
import com.earlbertmercado.playwright.saucedemo.pages.LoginPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HeaderComponent extends BasePage {

    private final Locator shoppingCart;
    private final Locator shoppingCartBadge;
    private final Locator burgerMenuButton;
    private final Locator allItemsLink;
    private final Locator aboutLink;
    private final Locator logoutLink;
    private final Locator resetAppStateLink;

    public HeaderComponent(Page page) {
        super(page);
        shoppingCart        = locator(HeaderLocators.SHOPPING_CART);
        shoppingCartBadge   = locator(HeaderLocators.SHOPPING_CART_BADGE);
        burgerMenuButton    = locator(HeaderLocators.BURGER_MENU_BUTTON);
        allItemsLink        = locator(HeaderLocators.ALL_ITEMS_LINK);
        aboutLink           = locator(HeaderLocators.ABOUT_LINK);
        logoutLink          = locator(HeaderLocators.LOGOUT_LINK);
        resetAppStateLink   = locator(HeaderLocators.RESET_APP_STATE_LINK);
    }

    // ------------------ Visibility Methods ------------------
    public boolean isShoppingCartVisible() {
        return shoppingCart.isVisible();
    }

    public boolean isShoppingCartBadgeVisible() {
        boolean isVisible = shoppingCartBadge.isVisible();
        logger.debug("Header: Cart badge visibility = {}", isVisible);
        return isVisible;
    }

    // ------------------ Getter Methods ------------------
    public String getShoppingCartBadgeText() {
        String text = shoppingCartBadge.innerText();
        logger.debug("Header: Shopping cart badge count is: {}", text);
        return text;
    }

    // ------------------ Action Methods ------------------
    public void clickBurgerMenu() {
        logger.debug("Opening Sidebar Menu via burger button.");
        burgerMenuButton.click();
    }

    public void clickAllItemsLink() {
        clickBurgerMenu();
        allItemsLink.click();
    }

    public void clickAboutLink() {
        clickBurgerMenu();
        aboutLink.click();
    }

    public LoginPage clickLogoutLink() {
        logger.info("Header: Initiating logout sequence.");
        clickBurgerMenu();
        logger.debug("Clicking Logout link in sidebar.");
        logoutLink.click();
        return new LoginPage(page);
    }

    public void clickResetAppStateLink() {
        logger.info("Header: Resetting Application State.");
        clickBurgerMenu();
        resetAppStateLink.click();
        logger.debug("Reloading page to finalize state reset.");
        page.reload();
    }
}
