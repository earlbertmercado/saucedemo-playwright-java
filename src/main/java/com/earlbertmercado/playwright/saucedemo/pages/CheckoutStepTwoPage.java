package com.earlbertmercado.playwright.saucedemo.pages;

import com.earlbertmercado.playwright.saucedemo.base.BasePage;
import com.earlbertmercado.playwright.saucedemo.constants.CheckoutStepTwoPageLocators;
import com.earlbertmercado.playwright.saucedemo.constants.HeaderLocators;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutStepTwoPage extends BasePage {

    private final Locator pageTitle;
    private final Locator finishButton;
    private final Locator cancelButton;
    private final Locator cartItems;
    private final Locator itemNames;
    private final Locator itemPrices;
    private final Locator itemDescriptions;
    private final Locator itemQuantities;
    private final Locator itemTotal;
    private final Locator tax;
    private final Locator total;

    public CheckoutStepTwoPage(Page page) {
        super(page);
        pageTitle        = locator(HeaderLocators.PAGE_TITLE);
        finishButton     = locator(CheckoutStepTwoPageLocators.FINISH_BUTTON);
        cancelButton     = locator(CheckoutStepTwoPageLocators.CANCEL_BUTTON);
        cartItems        = locator(CheckoutStepTwoPageLocators.CART_ITEMS);
        itemNames        = locator(CheckoutStepTwoPageLocators.ITEM_NAMES);
        itemPrices       = locator(CheckoutStepTwoPageLocators.ITEM_PRICES);
        itemDescriptions = locator(CheckoutStepTwoPageLocators.ITEM_DESCRIPTIONS);
        itemQuantities   = locator(CheckoutStepTwoPageLocators.ITEM_QUANTITIES);
        itemTotal        = locator(CheckoutStepTwoPageLocators.ITEM_TOTAL);
        tax              = locator(CheckoutStepTwoPageLocators.TAX);
        total            = locator(CheckoutStepTwoPageLocators.TOTAL);
    }

    // ------------------ Getter Methods ------------------
    public String getPageTitle() {
        return pageTitle.innerText();
    }

    public int getNumberOfCartItems() {
        return cartItems.count();
    }

    public String getItemName(int index) {
        return itemNames.nth(index).innerText();
    }

    public Double getItemPrice(int index) {
        String priceText = itemPrices.nth(index).innerText().replace("$", "");
        return Double.parseDouble(priceText);
    }

    public String getItemDescription(int index) {
        return itemDescriptions.nth(index).innerText();
    }

    public String getItemQuantity(int index) {
        return itemQuantities.nth(index).innerText();
    }

    public Double getTotalItemPrices() {
        double total = 0.0;
        int count = getNumberOfCartItems();
        for (int i = 0; i < count; i++) {
            total += getItemPrice(i);
        }
        return total;
    }

    public Double getTotalBeforeTax() {
        String amountText = itemTotal.innerText().replace("Item total: $", "");
        Double amount = Double.parseDouble(amountText);
        logger.info("Checkout Summary - Item Total: ${}", amount);
        return amount;
    }

    public Double getTax() {
        String amountText = tax.innerText().replace("Tax: $", "");
        Double amount = Double.parseDouble(amountText);
        logger.info("Checkout Summary - Tax: ${}", amount);
        return amount;
    }

    public Double getTotalAfterTax() {
        String amountText = total.innerText().replace("Total: $", "");
        Double amount = Double.parseDouble(amountText);
        logger.info("Checkout Summary - Grand Total: ${}", amount);
        return amount;
    }

    // ------------------ Visibility Methods ------------------
    public boolean isFinishButtonVisible() {
        return finishButton.isVisible();
    }

    public boolean isCancelButtonVisible() {
        return cancelButton.isVisible();
    }

    // ------------------ Validation Methods ------------------
    public boolean areItemDetailsValid(int index) {
        logger.debug("Validating item details at index: {}", index);
        boolean isValid = !getItemName(index).isEmpty() &&
                !getItemDescription(index).isEmpty() &&
                getItemPrice(index) != null;

        if (!isValid) {
            logger.warn("Item details validation failed at index: {}", index);
        }
        return isValid;
    }

    // ------------------ Action Methods ------------------
    public CheckoutCompletePage clickFinishButton() {
        logger.info("Finalizing order. Clicking 'Finish' button.");
        finishButton.click();
        return new CheckoutCompletePage(page);
    }

    public InventoryPage clickCancelButton() {
        logger.info("Cancelling checkout. Returning to Inventory.");
        cancelButton.click();
        return new InventoryPage(page);
    }
}
