package org.dani.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends BasePage {

    private final By homePageHeaderLink = By.cssSelector("#nav-link-home");
    private final By profilePageHeaderLink = By.cssSelector("#nav-link-profile");
    private final By newPostHeaderLink = By.cssSelector("#nav-link-new-post");

    /**
     * Creates a new LoginPage and initializes the base page.
     *
     * @param driver the WebDriver instance
     */
    public HeaderPage(WebDriver driver) {
        super(driver); // Calls the BasePage constructor
    }

    public void clickOnHeaderNavProfile() {
        click(profilePageHeaderLink);
    }

    public void clickOnHeaderNavNewPost() {
        click(newPostHeaderLink);
    }
}