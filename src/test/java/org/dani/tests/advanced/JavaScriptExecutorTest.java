package org.dani.tests.advanced;

import org.dani.pages.LoginPage;
import org.dani.tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Demonstrates using Selenium's {@link JavascriptExecutor} interface.
 * <p>
 * {@code JavascriptExecutor} lets you run arbitrary JavaScript in the browser context.
 * It is useful when standard WebDriver commands are insufficient — for example,
 * scrolling, modifying the DOM, reading computed styles, or interacting with
 * elements hidden behind overlays.
 * </p>
 *
 * <h3>Key methods:</h3>
 * <ul>
 *   <li>{@code executeScript(String script, Object... args)} — runs synchronous JS and returns the result.</li>
 *   <li>{@code executeAsyncScript(String script, Object... args)} — runs asynchronous JS (e.g., AJAX callbacks).</li>
 * </ul>
 *
 * <h3>Common use-cases:</h3>
 * <ol>
 *   <li>Scrolling the page or scrolling an element into view</li>
 *   <li>Clicking elements that are obscured by other elements</li>
 *   <li>Getting / setting element attributes or properties</li>
 *   <li>Highlighting elements for debugging</li>
 *   <li>Reading the page title or other document properties</li>
 * </ol>
 */
public class JavaScriptExecutorTest extends BaseTest {

    private JavascriptExecutor js;

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        // Cast the WebDriver to JavascriptExecutor (ChromeDriver implements both)
        js = (JavascriptExecutor) driver;

        // Navigate to the app and log in so we have content to work with
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToPage();
        loginPage.login("daniel11", "1qaz!QAZ");
    }

    // -----------------------------------------------------------------------
    // 1. Get page title via JavaScript
    // -----------------------------------------------------------------------

    /**
     * Uses {@code document.title} to read the page title from the DOM.
     * <p>
     * Equivalent to {@code driver.getTitle()}, but demonstrates how to
     * execute a simple JS expression and cast the result.
     * </p>
     */
    @Test(priority = 1, description = "Get page title using JS executor")
    public void testGetPageTitleViaJS() throws InterruptedException {
        Thread.sleep(5555);
        String title = (String) js.executeScript("return document.title;");
        System.out.println("Page title via JS: " + title);
        Assert.assertNotNull(title, "Page title should not be null");
    }

    // -----------------------------------------------------------------------
    // 2. Scroll to the bottom / top of the page
    // -----------------------------------------------------------------------

    /**
     * Scrolls to the very bottom of the page using {@code window.scrollTo()}.
     * <p>
     * Useful for infinite-scroll pages or when elements are below the viewport.
     * </p>
     */
    @Test(priority = 2, description = "Scroll to bottom of page via JS")
    public void testScrollToBottom() throws InterruptedException {
        Thread.sleep(5555);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        System.out.println("Scrolled to bottom of the page");
        Thread.sleep(3333);

        // Scroll back to top
        js.executeScript("window.scrollTo(0, 0);");
        System.out.println("Scrolled back to top");
        Thread.sleep(3333);
    }

    // -----------------------------------------------------------------------
    // 3. Scroll an element into view
    // -----------------------------------------------------------------------

    /**
     * Uses {@code Element.scrollIntoView()} to bring a specific element
     * into the visible viewport.
     */
    @Test(priority = 3, description = "Scroll element into view via JS")
    public void testScrollElementIntoView() throws InterruptedException {
        Thread.sleep(5555);
        WebElement element = driver.findElement(By.xpath("(//div[contains(@class,'post-feed-container')])[3]"));
        js.executeScript("arguments[0].scrollIntoView();", element);
        System.out.println("Element scrolled into view");
        Assert.assertTrue(element.isDisplayed(), "Element should be visible after scrolling");
    }

    // -----------------------------------------------------------------------
    // 4. Click an element via JavaScript (bypasses overlay issues)
    // -----------------------------------------------------------------------

    /**
     * Performs a JS-level click on an element.
     * <p>
     * This bypasses Selenium's built-in click which requires the element to be
     * visible and not obscured. Useful as a <b>last resort</b> when normal
     * {@code element.click()} throws {@code ElementClickInterceptedException}.
     * </p>
     */
    @Test(priority = 4, description = "Click element via JS executor")
    public void testClickElementViaJS() {
        WebElement homeLink = driver.findElement(By.cssSelector("#nav-link-home"));
        js.executeScript("arguments[0].click();", homeLink);
        System.out.println("Clicked Home link via JavaScript");
    }

    // -----------------------------------------------------------------------
    // 5. Highlight an element (useful for debugging)
    // -----------------------------------------------------------------------

    /**
     * Temporarily changes an element's border to make it visually stand out.
     * <p>
     * A common debugging technique — highlight the element before interacting
     * with it so you can visually confirm the correct element was found.
     * </p>
     */
    @Test(priority = 5, description = "Highlight element via JS for debugging")
    public void testHighlightElement() throws InterruptedException {
        WebElement element = driver.findElement(By.cssSelector("#nav-link-home"));

        // Add a red border
        js.executeScript(
                "arguments[0].style.border = '3px solid red';",
                element
        );
        System.out.println("Element highlighted with red border");
        Thread.sleep(5555);

        // Remove it after a moment
        js.executeScript(
                "arguments[0].style.border = '';",
                element
        );
        Thread.sleep(5555);
        System.out.println("Highlight removed");
    }

    // -----------------------------------------------------------------------
    // 6. Get / set element attributes
    // -----------------------------------------------------------------------

    /**
     * Reads and modifies an element's attribute using JavaScript.
     * <p>
     * {@code getAttribute()} works in Selenium too, but JS gives you full control
     * to <b>set</b> attributes — useful for removing {@code disabled}, changing
     * {@code value}, etc.
     * </p>
     */
    @Test(priority = 6, description = "Read and modify element attribute via JS")
    public void testGetAndSetAttribute() throws InterruptedException {
        // Navigate to login page to interact with input fields
        driver.get("http://training.skillo-bg.com:4200/users/login");

        WebElement usernameField = driver.findElement(By.cssSelector("#defaultLoginFormUsername"));

        // Read the placeholder attribute via JS
        String placeholder = (String) js.executeScript(
                "return arguments[0].getAttribute('placeholder');",
                usernameField
        );
        System.out.println("Placeholder: " + placeholder);

        // Set a custom attribute
        js.executeScript(
                "arguments[0].setAttribute('data-test-id', 'username-input');",
                usernameField
        );

        String customAttr = (String) js.executeScript(
                "return arguments[0].getAttribute('data-test-id');",
                usernameField
        );
        Thread.sleep(5555);
        Assert.assertEquals(customAttr, "username-input",
                "Custom attribute should have been set via JS");
    }

    // -----------------------------------------------------------------------
    // 7. Execute async script (wait for a callback)
    // -----------------------------------------------------------------------

    /**
     * Demonstrates {@code executeAsyncScript()} which waits for a callback.
     * <p>
     * The last argument injected by Selenium is the <b>callback function</b>.
     * The script must call it to signal completion. This is useful for waiting
     * on AJAX requests, animations, or custom async operations.
     * </p>
     */
    @Test(priority = 7, description = "Execute async JS with callback")
    public void testAsyncScriptExecution() {
        // Set a script timeout so Selenium knows how long to wait
        driver.manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(10));

        // The async script: waits 1 second, then calls the callback with a message
        String result = (String) js.executeAsyncScript(
                "var callback = arguments[arguments.length - 1];" +
                "setTimeout(function() { callback('Async done!'); }, 1000);"
        );

        System.out.println("Async script returned: " + result);
        Assert.assertEquals(result, "Async done!");
    }
}
