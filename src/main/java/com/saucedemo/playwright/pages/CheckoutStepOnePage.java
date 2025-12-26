package com.saucedemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;
import com.saucedemo.playwright.constants.CheckoutStepOnePageLocators;
import com.saucedemo.playwright.constants.HeaderLocators;

public class CheckoutStepOnePage extends BasePage {

    private final Locator pageTitle;
    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator postalCodeInput;
    private final Locator continueButton;
    private final Locator cancelButton;

    public CheckoutStepOnePage(Page page) {
        super(page);
        pageTitle       = locator(HeaderLocators.PAGE_TITLE);
        firstNameInput  = locator(CheckoutStepOnePageLocators.FIRST_NAME_INPUT);
        lastNameInput   = locator(CheckoutStepOnePageLocators.LAST_NAME_INPUT);
        postalCodeInput = locator(CheckoutStepOnePageLocators.POSTAL_CODE_INPUT);
        continueButton  = locator(CheckoutStepOnePageLocators.CONTINUE_BUTTON);
        cancelButton    = locator(CheckoutStepOnePageLocators.CANCEL_BUTTON);
    }

    // ------------------ Getter Methods ------------------
    public String getPageTitle() {
        return pageTitle.innerText();
    }

    // ------------------ Visibility Methods ------------------
    public boolean isFirstNameInputVisible() {
        return firstNameInput.isVisible();
    }

    public boolean isLastNameInputVisible() {
        return lastNameInput.isVisible();
    }

    public boolean isPostalCodeInputVisible() {
        return postalCodeInput.isVisible();
    }

    public boolean isContinueButtonVisible() {
        return continueButton.isVisible();
    }

    public boolean isCancelButtonVisible() {
        return cancelButton.isVisible();
    }

    // ------------------ Action / Input Methods ------------------
    public CheckoutStepOnePage enterFirstName(String firstName) {
        logger.debug("Entering first name: {}", firstName);
        firstNameInput.fill(firstName);
        return this;
    }

    public CheckoutStepOnePage enterLastName(String lastName) {
        logger.debug("Entering last name: {}", lastName);
        lastNameInput.fill(lastName);
        return this;
    }

    public CheckoutStepOnePage enterPostalCode(String postalCode) {
        logger.debug("Entering postal code: {}", postalCode);
        postalCodeInput.fill(postalCode);
        return this;
    }

    //TODO: use this method to fill all customer information at once in test
//    public CheckoutStepOnePage fillCustomerInfo(String fName,
//                                                       String lName,
//                                                       String zip) {
//        logger.info("Filling customer information: {} {}, {}", fName, lName, zip);
//        return enterFirstName(fName)
//                .enterLastName(lName)
//                .enterPostalCode(zip);
//    }

    public CheckoutStepTwoPage clickContinueButton() {
        logger.info("Clicking 'Continue' button to proceed to Step Two.");
        continueButton.click();
        return new CheckoutStepTwoPage(page);
    }

    public CartPage clickCancelButton() {
        logger.info("Clicking 'Cancel' button. Returning to Cart.");
        cancelButton.click();
        return new CartPage(page);
    }
}
