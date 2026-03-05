package org.dani.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Login page using the <b>Page Object Model (POM)</b> pattern.
 * <p>
 * Locators are stored as {@link By} objects and wrapped in action methods.
 * </p>
 *
 * @see org.dani.pagefactory.LoginPageFactory for the Page Factory equivalent
 */
public class LoginPage extends BasePage {

    // 1. Locators (Private so tests can't mess with them)
    private final By usernameField = By.cssSelector("#defaultLoginFormUsername");
    private final By passwordField = By.cssSelector("#defaultLoginFormPassword");
    private final By signInButton = By.cssSelector("#sign-in-button");
    // Const
    private final String loginPageUrl = "/users/login";

    /**
     * Creates a new LoginPage and initializes the base page.
     *
     * @param driver the WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver); // Calls the BasePage constructor
    }

    // 2. Actions (Methods that the tests will call)

    /**
     * Returns the expected full URL of the login page.
     *
     * @return expected login page URL
     */
    public String getPageExpectedUrl() {
        return super.getExpectedUrl(loginPageUrl);
    }

    /**
     * Waits until the login page URL is fully loaded.
     */
    public void verifyPageLoaded() {
        waitForUrl(getExpectedUrl(loginPageUrl));
    }

    /**
     * Navigates the browser to the login page.
     */
    public void navigateToPage() {
        navigateTo(loginPageUrl);
    }

    /**
     * Types the given username into the username input field.
     *
     * @param username the username to enter
     */
    public void enterUsername(String username) {
        typeText(usernameField, username);
    }

    /**
     * Types the given password into the password input field.
     *
     * @param password the password to enter
     */
    public void enterPassword(String password) {
        typeText(passwordField, password);
    }

    /**
     * Clicks the Sign In button.
     */
    public void clickSignIn() {
        click(signInButton);
    }

    /**
     * Performs the complete login flow: enters credentials and clicks Sign In.
     *
     * @param user the username
     * @param pass the password
     */
    public void login(String user, String pass) {
        enterUsername(user);
        enterPassword(pass);
        clickSignIn();
    }
}