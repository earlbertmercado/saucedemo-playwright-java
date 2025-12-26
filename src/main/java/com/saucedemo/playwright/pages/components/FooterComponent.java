package com.saucedemo.playwright.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;
import com.saucedemo.playwright.constants.FooterLocators;

/**
 * Represents the footer section of the page.
 * Provides access to social media links and footer copyright text.
 */
public class FooterComponent extends BasePage {

    private final Locator twitterLink;
    private final Locator facebookLink;
    private final Locator linkedinLink;
    private final Locator footerCopyright;

    public FooterComponent(Page page) {
        super(page);
        twitterLink     = locator(FooterLocators.TWITTER_LINK);
        facebookLink    = locator(FooterLocators.FACEBOOK_LINK);
        linkedinLink    = locator(FooterLocators.LINKEDIN_LINK);
        footerCopyright = locator(FooterLocators.FOOTER_COPYRIGHT);
    }

    // ------------------ Visibility Methods ------------------
    public boolean isTwitterLinkVisible() {
        boolean isVisible = twitterLink.isVisible();
        logger.debug("Footer: Twitter link visibility = {}", isVisible);
        return isVisible;
    }

    public boolean isFacebookLinkVisible() {
        boolean isVisible = facebookLink.isVisible();
        logger.debug("Footer: Facebook link visibility = {}", isVisible);
        return isVisible;
    }

    public boolean isLinkedinLinkVisible() {
        boolean isVisible = linkedinLink.isVisible();
        logger.debug("Footer: LinkedIn link visibility = {}", isVisible);
        return isVisible;
    }

    // ------------------ Getter Methods ------------------
    public String getFooterCopyrightText() {
        String text = footerCopyright.innerText();
        logger.debug("Footer: Retrieved copyright text: '{}'", text);
        return text;
    }

    // ------------------ Action Methods ------------------
    public void clickTwitterLink() {
        logger.info("Footer: Clicking Twitter social media link.");
        twitterLink.click();
    }

    public void clickFacebookLink() {
        logger.info("Footer: Clicking Facebook social media link.");
        facebookLink.click();
    }

    public void clickLinkedinLink() {
        logger.info("Footer: Clicking LinkedIn social media link.");
        linkedinLink.click();
    }
}
