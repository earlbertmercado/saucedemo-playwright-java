package com.earlbertmercado.playwright.saucedemo.constants;

public final class AppConstants {

    private AppConstants() {

    }

    public static final String BASE_URL                 = "https://www.saucedemo.com";
    public static final String SAUCE_LABS_URL           = "https://saucelabs.com/";
    public static final String CART_URL                 = BASE_URL + "/cart.html";
    public static final String CHECKOUT_COMPLETE_URL    = BASE_URL + "/checkout-complete.html";
    public static final String CHECKOUT_STEP_ONE_URL    = BASE_URL + "/checkout-step-one.html";
    public static final String CHECKOUT_STEP_TWO_URL    = BASE_URL + "/checkout-step-two.html";
    public static final String INVENTORY_URL            = BASE_URL + "/inventory.html";
    public static final String ITEM_DETAIL_URL          = BASE_URL + "/inventory-item.html?id=";
}
