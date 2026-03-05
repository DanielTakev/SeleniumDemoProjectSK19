package org.dani.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Home page using the <b>Page Object Model (POM)</b> pattern.
 *
 * @see org.dani.pagefactory.HomePageFactory for the Page Factory equivalent
 */
public class HomePage extends BasePage {

    // WebElements below
    private final By postElements = By.cssSelector(".post-feed-container");

    private final String homePageUrl = "/posts/all";

    /**
     * Creates a new HomePage and initializes the base page.
     *
     * @param driver the WebDriver instance
     */
    public HomePage(WebDriver driver) {
        super(driver); // Calls the BasePage constructor
    }

    /**
     * Returns the expected full URL of the home page.
     *
     * @return expected home page URL
     */
    public String getPageExpectedUrl() {
        return super.getExpectedUrl(homePageUrl);
    }

    /**
     * Navigates the browser to the home page.
     */
    public void navigateToPage() {
        navigateTo(homePageUrl);
    }

    /**
     * Waits until the home page URL is fully loaded.
     */
    public void verifyPageLoaded() {
        waitForUrl(getExpectedUrl(homePageUrl));
    }

    /**
     * Checks whether the browser is currently on the home page.
     *
     * @return {@code true} if the current URL matches the expected home page URL
     */
    public boolean isUrlLoaded() {
        verifyPageLoaded();
        return getActualCurrentUrl().equals(getPageExpectedUrl());
    }

    /**
     * Returns the number of post elements visible on the home page.
     *
     * @return post count
     */
    public int getPostsCount() {
        return getElementsCount(postElements);
    }

    /**
    * Returns the author of the post at the given index (1-based).
    *
    * @param index the 1-based index of the post
    * @return the post author name String
    */
    public String getPostAuthorByIndex(int index) {
        By postAuthorLocator = By.xpath("(//div[contains(@class, 'post-list-container')]//app-post-detail//a[contains(@class, 'post-user')])[" + index + "]");
        return waitAndVisible(postAuthorLocator).getText();
    }

    public boolean hasPostByAuthorName(String authorName) {
        return isElementVisible(By.xpath("//app-post-detail//a[text()=\"" + authorName + "\"]"));
    }
}
