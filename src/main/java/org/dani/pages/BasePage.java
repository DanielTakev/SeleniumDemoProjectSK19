package org.dani.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base page class for the <b>Page Object Model (POM)</b> pattern.
 * <p>
 * Provides shared WebDriver utilities, explicit wait wrappers and navigation helpers
 * that all page objects inherit. Locators are stored as {@link By} objects
 * and resolved at interaction time — no caching.
 * </p>
 *
 * @see org.dani.pagefactory.BasePageFactory for the Page Factory alternative
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected static final String baseUrl = "http://training.skillo-bg.com:4200";

    /**
     * Initializes the base page with a WebDriver instance and an explicit wait.
     *
     * @param driver the WebDriver instance shared across the test
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**
     * Constructs the full expected URL by appending a suffix to the base URL.
     *
     * @param urlSuffix the path suffix, e.g. {@code "/users/login"}
     * @return full URL string
     */
    protected String getExpectedUrl(String urlSuffix) {
        return baseUrl + urlSuffix;
    }

    /**
     * Returns the current URL from the browser's address bar.
     *
     * @return the current URL
     */
    public String getActualCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // --- REUSABLE WAIT WRAPPERS ---

    /**
     * Waits until the element identified by the locator is clickable.
     *
     * @param locator the {@link By} locator
     * @return the clickable {@link WebElement}
     */
    protected WebElement waitAndClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until the element identified by the locator is visible on the page.
     *
     * @param locator the {@link By} locator
     * @return the visible {@link WebElement}
     */
    protected WebElement waitAndVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Navigates the browser to the base URL plus the given suffix.
     *
     * @param urlSuffix the path to append, e.g. {@code "/users/login"}
     */
    protected void navigateTo(String urlSuffix) {
        driver.get(baseUrl + urlSuffix);
    }

    /**
     * Waits for the element to be clickable, then clicks it.
     *
     * @param locator the {@link By} locator of the element to click
     */
    protected void click(By locator) {
        waitAndClickable(locator).click();
    }

    /**
     * Waits for the element to be visible, clears it and types the given text.
     *
     * @param locator the {@link By} locator of the input field
     * @param text    the text to enter
     */
    protected void typeText(By locator, String text) {
        waitAndVisible(locator).clear();
        waitAndVisible(locator).sendKeys(text);
    }

    /**
     * Waits for the element to be visible and types text without clearing first.
     *
     * @param locator the {@link By} locator of the input field
     * @param text    the text to append
     */
    protected void typeTextWithoutClear(By locator, String text) {
        waitAndVisible(locator).sendKeys(text);
    }

    /**
     * Waits until the browser URL exactly matches the given URL.
     *
     * @param url the expected full URL
     */
    protected void waitForUrl(String url) {
        wait.until(ExpectedConditions.urlToBe(url));
    }

    /**
     * Waits until the browser URL contains the given substring.
     *
     * @param urlPart the expected URL substring
     */
    protected void waitForUrlToContain(String urlPart) {
        wait.until(ExpectedConditions.urlContains(urlPart));
    }

    /**
     * Counts the number of elements matching the locator (waits for visibility first).
     *
     * @param locator the {@link By} locator
     * @return the number of matching elements
     */
    protected int getElementsCount(By locator) {
        waitAndVisible(locator);
        return driver.findElements(locator).size();
    }

    protected boolean isElementVisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            System.out.println("Element: " + locator + "NOT FOUND!");
            return false;
        }
    }
    
    public void pressKeyboardKey(Keys key) {
        try {
            Actions action = new Actions(driver);
            action.sendKeys(key).build().perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}