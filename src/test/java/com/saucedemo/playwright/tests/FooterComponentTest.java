package com.saucedemo.playwright.tests;

import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BaseTest;
import com.saucedemo.playwright.pages.InventoryPage;
import com.saucedemo.playwright.pages.LoginPage;
import com.saucedemo.playwright.pages.components.FooterComponent;
import com.saucedemo.playwright.utils.AppStateUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FooterComponentTest extends BaseTest {

    @Test
    public void testFooterLinksVisibility() {
        SoftAssertions softly = new SoftAssertions();
        AppStateUtils appStateUtils = new AppStateUtils(page);
        FooterComponent footer = new FooterComponent(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword());

        logger.info("Verifying all social media links and copyright visibility.");

        softly.assertThat(footer.isTwitterLinkVisible())
                .as("Twitter link visibility")
                .isTrue();

        softly.assertThat(footer.isFacebookLinkVisible())
                .as("Facebook link visibility")
                .isTrue();

        softly.assertThat(footer.isLinkedinLinkVisible())
                .as("LinkedIn link visibility")
                .isTrue();

        softly.assertThat(footer.getFooterCopyrightText())
                .as("Footer copyright text")
                .isNotBlank()
                .isNotNull()
                .isNotEmpty();

        softly.assertAll();
        appStateUtils.logout();
    }

    @Test
    public void testFooterLinksNavigation() {
        String EXPECTED_TWITTER_URL = "https://x.com/saucelabs";
        String EXPECTED_FACEBOOK_URL = "https://www.facebook.com/saucelabs";
        String EXPECTED_LINKEDIN_URL = "https://www.linkedin.com/company/sauce-labs/";

        AppStateUtils appStateUtils = new AppStateUtils(page);
        FooterComponent footer = new FooterComponent(page);

        InventoryPage inventoryPage = new LoginPage(page)
                .navigate()
                .login(user.getUsername(), user.getPassword());

        logger.info("Verifying all social media links");

        // Validate Twitter link
        Page twitterTab = page.waitForPopup(footer::clickTwitterLink);
        twitterTab.waitForLoadState();
        assertThat(twitterTab.url())
                .as("Twitter link URL")
                .isEqualTo(EXPECTED_TWITTER_URL);
        twitterTab.close();

        // Validate Facebook link
        Page facebookTab = page.waitForPopup(footer::clickFacebookLink);
        facebookTab.waitForLoadState();
        assertThat(facebookTab.url())
                .as("Facebook link URL")
                .isEqualTo(EXPECTED_FACEBOOK_URL);
        facebookTab.close();

        // Validate LinkedIn link
        Page linkedinTab = page.waitForPopup(footer::clickLinkedinLink);
        linkedinTab.waitForLoadState();
        assertThat(linkedinTab.url())
                .as("LinkedIn link URL")
                .isEqualTo(EXPECTED_LINKEDIN_URL);
        linkedinTab.close();

        appStateUtils.logout();
    }
}
