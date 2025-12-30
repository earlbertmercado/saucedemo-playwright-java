package com.earlbertmercado.playwright.saucedemo.pages;

import com.earlbertmercado.playwright.saucedemo.base.BasePage;
import com.earlbertmercado.playwright.saucedemo.constants.CheckoutCompletePageLocators;
import com.earlbertmercado.playwright.saucedemo.constants.HeaderLocators;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutCompletePage extends BasePage {

    private final Locator pageTitle;
    private final Locator thankYouHeader;
    private final Locator thankYouMessage;
    private final Locator backHomeButton;

    public CheckoutCompletePage(Page page) {
        super(page);
        pageTitle       = locator(HeaderLocators.PAGE_TITLE);
        thankYouHeader  = locator(CheckoutCompletePageLocators.THANK_YOU_HEADER);
        thankYouMessage = locator(CheckoutCompletePageLocators.THANK_YOU_MESSAGE);
        backHomeButton  = locator(CheckoutCompletePageLocators.BACK_HOME_BUTTON);
    }

    // ------------------ Getter Methods ------------------
    public String getPageTitle() {
        return pageTitle.innerText();
    }

    // ------------------ Visibility Methods ------------------
    public boolean isThankYouHeaderVisible() {
        boolean isVisible = thankYouHeader.isVisible();
        if (isVisible) {
            logger.info("Order completion confirmed: 'Thank You' header is visible.");
        } else {
            logger.warn("Checkout might not be complete: 'Thank You' header is NOT visible.");
        }
        return isVisible;
    }

    public boolean isThankYouMessageVisible() {
        return thankYouMessage.isVisible();
    }

    public boolean isBackHomeButtonVisible() {
        return backHomeButton.isVisible();
    }

    // ------------------ Action Methods ------------------
    public InventoryPage clickBackHomeButton() {
        logger.info("Clicking 'Back Home' button to return to products.");
        backHomeButton.click();
        return new InventoryPage(page);
    }
}
