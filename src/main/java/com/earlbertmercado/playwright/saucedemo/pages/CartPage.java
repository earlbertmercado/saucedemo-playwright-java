package com.earlbertmercado.playwright.saucedemo.pages;

import com.earlbertmercado.playwright.saucedemo.base.BasePage;
import com.earlbertmercado.playwright.saucedemo.constants.CartPageLocators;
import com.earlbertmercado.playwright.saucedemo.constants.HeaderLocators;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CartPage extends BasePage {

    private final Locator pageTitle;
    private final Locator checkoutButton;
    private final Locator continueShoppingButton;
    private final Locator cartItems;
    private final Locator itemNames;
    private final Locator itemPrices;
    private final Locator itemDescriptions;
    private final Locator itemQuantities;
    private final Locator removeButtons;

    public CartPage(Page page) {
        super(page);
        pageTitle              = locator(HeaderLocators.PAGE_TITLE);
        checkoutButton         = locator(CartPageLocators.CHECKOUT_BUTTON);
        continueShoppingButton = locator(CartPageLocators.CONTINUE_SHOPPING_BUTTON);
        cartItems              = locator(CartPageLocators.CART_ITEMS);
        itemNames              = locator(CartPageLocators.ITEM_NAMES);
        itemPrices             = locator(CartPageLocators.ITEM_PRICES);
        itemDescriptions       = locator(CartPageLocators.ITEM_DESCRIPTIONS);
        itemQuantities         = locator(CartPageLocators.ITEM_QUANTITIES);
        removeButtons          = locator(CartPageLocators.REMOVE_BUTTONS);
    }

    // ------------------ Getter Methods ------------------
    public String getPageTitle() {
        return pageTitle.innerText();
    }

    public int getCartItemCount() {
        int count = cartItems.count();
        logger.debug("Cart item count retrieved: {}", count);
        return count;
    }

    public int getTotalQuantity() {
        logger.debug("Calculating total quantity for all items in cart...");
        int totalQuantity = 0;
        for (int i = 0; i < itemQuantities.count(); i++) {
            String quantityText = itemQuantities.nth(i).innerText();
            totalQuantity += Integer.parseInt(quantityText);
        }
        logger.info("Total quantity in cart: {}", totalQuantity);
        return totalQuantity;
    }

    // ------------------ Visibility Methods ------------------
    public boolean isCheckoutButtonVisible() {
        return checkoutButton.isVisible();
    }

    public boolean isContinueShoppingButtonVisible() {
        return continueShoppingButton.isVisible();
    }

    // ------------------ Validation Methods ------------------
    public boolean areAllItemDetailsValid() {
        logger.info("Validating details for all items in the cart.");
        for (int i = 0; i < cartItems.count(); i++) {
            logger.error("Validation failed for item at index: {}", i);
            if (!isValidItem(i)) return false;
        }
        return true;
    }

    public boolean isValidItem(int index) {
        String name = itemNames.nth(index).innerText();
        String priceText = itemPrices.nth(index).innerText().replace("$", "");
        String description = itemDescriptions.nth(index).innerText();

        logger.debug("Checking item at index {}: [Name: {}, Price: {}]", index, name, priceText);

        if (name.isEmpty() || description.isEmpty()) {
            logger.warn("Item at index {} has an empty name or description.", index);
            return false;
        }

        boolean isPriceValid = priceText.matches("\\d+(\\.\\d+)?");
        if (!isPriceValid) {
            logger.warn("Item '{}' has an invalid price format: {}", name, priceText);
        }

        return isPriceValid;
    }

    // ------------------ Action Methods ------------------
    public void removeItemByIndex(int index) {
        logger.info("Removing item at index {} from cart.", index);
        removeButtons.nth(index).click();
    }

    public InventoryPage clickContinueShopping() {
        logger.info("Clicking 'Continue Shopping' button.");
        continueShoppingButton.click();
        return new InventoryPage(page);
    }

    public CheckoutStepOnePage clickCheckout() {
        logger.info("Clicking 'Checkout' button.");
        checkoutButton.click();
        return new CheckoutStepOnePage(page);
    }
}
