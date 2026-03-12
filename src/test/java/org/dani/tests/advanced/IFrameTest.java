package org.dani.tests.advanced;

import org.dani.tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Demonstrates working with <b>iFrames</b> (inline frames) in Selenium.
 * <p>
 * An iFrame is essentially a webpage embedded inside another webpage.
 * Selenium can only interact with elements in the <em>currently focused</em> frame.
 * To access elements inside an iFrame, you must first <b>switch</b> to it.
 * </p>
 *
 * <h3>Three ways to switch to an iFrame:</h3>
 * <ol>
 *   <li>{@code driver.switchTo().frame(int index)} — by zero-based index</li>
 *   <li>{@code driver.switchTo().frame(String nameOrId)} — by the {@code name} or {@code id} attribute</li>
 *   <li>{@code driver.switchTo().frame(WebElement)} — by a reference to the {@code <iframe>} element</li>
 * </ol>
 *
 * <h3>Switching back:</h3>
 * <ul>
 *   <li>{@code driver.switchTo().defaultContent()} — switch back to the <b>top-level</b> page</li>
 *   <li>{@code driver.switchTo().parentFrame()} — switch to the <b>parent</b> frame (useful for nested iFrames)</li>
 * </ul>
 *
 * <h3>Note:</h3>
 * <p>
 * This test uses a public demo page with iFrames so students can see the concept
 * without needing iFrame content on the Skillo training app.
 * </p>
 */
public class IFrameTest extends BaseTest {

    /**
     * A public demo page that contains iFrame examples.
     * We use the W3Schools TryIt editor which has a well-known iFrame structure.
     */
    private static final String IFRAME_DEMO_URL = "https://the-internet.herokuapp.com/iframe";

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        driver.get(IFRAME_DEMO_URL);
    }

    // -----------------------------------------------------------------------
    // 1. Switch to iFrame by ID and interact with its content
    // -----------------------------------------------------------------------

    /**
     * Switches to the TinyMCE editor iFrame by its {@code id} attribute,
     * reads its content, then switches back.
     * <p>
     * The page at {@code /iframe} contains a TinyMCE rich text editor
     * embedded in an iFrame with {@code id="mce_0_ifr"}.
     * </p>
     */
    @Test(priority = 1, description = "Switch to iFrame by ID and read content")
    public void testSwitchToIFrameById() throws InterruptedException {
        Thread.sleep(3333);
        // Switch to the iFrame containing the TinyMCE editor
        driver.switchTo().frame("mce_0_ifr");

        // Now we can access elements inside the iFrame
        WebElement editorBody = driver.findElement(By.cssSelector("#tinymce"));
        String text = editorBody.getText();
        System.out.println("Text inside iFrame: " + text);

        Assert.assertFalse(text.isEmpty(), "iFrame editor should contain some text");

        // IMPORTANT: Switch back to the main page
        driver.switchTo().defaultContent();
    }

    // -----------------------------------------------------------------------
    // 2. Switch to iFrame by WebElement reference
    // -----------------------------------------------------------------------

    /**
     * Locates the {@code <iframe>} element first, then switches to it.
     * <p>
     * This approach is the most flexible because you can use any locator
     * strategy to find the iFrame element.
     * </p>
     */
    @Test(priority = 2, description = "Switch to iFrame by WebElement")
    public void testSwitchToIFrameByWebElement() throws InterruptedException {
        WebElement iframeElement = driver.findElement(By.cssSelector("#mce_0_ifr"));

        // Switch using the WebElement
        driver.switchTo().frame(iframeElement);

        WebElement editorBody = driver.findElement(By.cssSelector("#tinymce"));

        String updatedText = editorBody.getText();
        Assert.assertEquals(updatedText, "Your content goes here.",
                "Typed text should appear in the iFrame editor");

        // Switch back to main content
        driver.switchTo().defaultContent();
    }

    // -----------------------------------------------------------------------
    // 3. Switch to iFrame by index
    // -----------------------------------------------------------------------

    /**
     * Switches to the first iFrame on the page using its zero-based index.
     * <p>
     * Using index is <b>fragile</b> — if the page adds/removes iFrames,
     * the index may change. Prefer using {@code id}, {@code name}, or
     * {@code WebElement} when possible.
     * </p>
     */
    @Test(priority = 3, description = "Switch to iFrame by zero-based index")
    public void testSwitchToIFrameByIndex() throws InterruptedException {
        // Switch to the first (index 0) iFrame on the page
        Thread.sleep(2000);
        driver.switchTo().frame(0);

        // Verify we are inside the iFrame by finding an element
        WebElement editorBody = driver.findElement(By.cssSelector("#tinymce"));
        Assert.assertNotNull(editorBody, "Should find the TinyMCE body inside the iFrame");

        // Switch back
        driver.switchTo().defaultContent();
    }

    // -----------------------------------------------------------------------
    // 4. Verify you CANNOT access iFrame content without switching
    // -----------------------------------------------------------------------

    /**
     * Demonstrates that trying to find an iFrame-internal element
     * <b>without switching first</b> will fail.
     * <p>
     * This is a common mistake for beginners — always remember to
     * {@code switchTo().frame()} before interacting with iFrame content.
     * </p>
     */
    @Test(priority = 4, description = "Demo: accessing iFrame content without switching fails")
    public void testCannotAccessIFrameWithoutSwitching() {
        // Make sure we are on the default content
        driver.switchTo().defaultContent();

        // Try to find the TinyMCE editor body WITHOUT switching to the iFrame
        boolean foundWithoutSwitch = false;
        try {
            driver.findElement(By.cssSelector("#tinymce"));
            foundWithoutSwitch = true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println("Expected: Cannot find #tinymce without switching to iFrame");
        }

        Assert.assertFalse(foundWithoutSwitch,
                "Should NOT be able to find iFrame content without switching!");
    }

    // -----------------------------------------------------------------------
    // 5. Work with elements OUTSIDE the iFrame after switching back
    // -----------------------------------------------------------------------

    /**
     * Shows the full workflow: switch into iFrame, interact, switch back,
     * then interact with elements on the main page.
     */
    @Test(priority = 5, description = "Switch in and out of iFrame and interact with both contexts")
    public void testSwitchInAndOutOfIFrame() {
        // 1. Interact with main page element first
        WebElement heading = driver.findElement(By.cssSelector("h3"));
        String headingText = heading.getText();
        System.out.println("Main page heading: " + headingText);
        Assert.assertFalse(headingText.isEmpty(), "Main page heading should exist");

        // 2. Switch into iFrame and interact
        driver.switchTo().frame("mce_0_ifr");
        WebElement editorBody = driver.findElement(By.cssSelector("#tinymce"));
        System.out.println("iFrame text: " + editorBody.getText());

        // 3. Switch back to main content
        driver.switchTo().defaultContent();

        // 4. Now we can access main page elements again
        WebElement headingAgain = driver.findElement(By.cssSelector("h3"));
        Assert.assertEquals(headingAgain.getText(), headingText,
                "Should be able to access main page elements after switching back");
    }
}
