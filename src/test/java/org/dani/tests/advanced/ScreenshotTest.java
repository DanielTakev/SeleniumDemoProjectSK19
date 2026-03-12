package org.dani.tests.advanced;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.dani.pages.LoginPage;
import org.dani.tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Demonstrates <b>taking screenshots</b> in Selenium WebDriver.
 * <p>
 * Screenshots are essential for debugging test failures. Selenium provides the
 * {@link TakesScreenshot} interface which is implemented by most WebDriver
 * implementations (ChromeDriver, FirefoxDriver, etc.).
 * </p>
 *
 * <h3>Types of screenshots:</h3>
 * <ol>
 *   <li><b>Full page screenshot</b> — captures the entire visible viewport</li>
 *   <li><b>Element screenshot</b> — captures only a specific WebElement</li>
 *   <li><b>Screenshot as different output types:</b>
 *       <ul>
 *           <li>{@code OutputType.FILE} — saves to a temporary file</li>
 *           <li>{@code OutputType.BYTES} — returns raw bytes (useful for reports)</li>
 *           <li>{@code OutputType.BASE64} — returns Base64 encoded string (for embedding in HTML)</li>
 *       </ul>
 *   </li>
 * </ol>
 *
 * <h3>Best practice:</h3>
 * <p>
 * In real projects, screenshots are typically taken automatically on test failure
 * using a TestNG listener ({@code ITestListener.onTestFailure()}) — see the
 * {@code listeners} package for an example.
 * </p>
 *
 * <h3>Output directory:</h3>
 * <p>
 * All screenshots in this demo are saved to {@code target/screenshots/}.
 * </p>
 */
public class ScreenshotTest extends BaseTest {

    /** Directory where screenshots will be stored. */
    private static final String SCREENSHOTS_DIR = "target/screenshots";

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();

        // Ensure the screenshots directory exists
        new File(SCREENSHOTS_DIR).mkdirs();

        // Navigate to the login page
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToPage();
    }

    // -----------------------------------------------------------------------
    // 1. Full page screenshot saved as a file
    // -----------------------------------------------------------------------

    /**
     * Captures a screenshot of the entire visible viewport and saves it to disk.
     * <p>
     * Steps:
     * <ol>
     *   <li>Cast the driver to {@link TakesScreenshot}</li>
     *   <li>Call {@code getScreenshotAs(OutputType.FILE)} to get a temp file</li>
     *   <li>Copy the temp file to a permanent location</li>
     * </ol>
     * </p>
     */
    @Test(priority = 1, description = "Capture full page screenshot and save as file")
    public void testFullPageScreenshot() throws IOException {
        // Cast driver to TakesScreenshot
        TakesScreenshot screenshotDriver = (TakesScreenshot) driver;

        // Take screenshot — Selenium saves it to a temporary file
        File tempFile = screenshotDriver.getScreenshotAs(OutputType.FILE);

        // Build a meaningful filename with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "full_page_" + timestamp + ".png";
        Path destination = Paths.get(SCREENSHOTS_DIR, fileName);

        // Copy from temp location to our screenshots directory
        Files.copy(tempFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Full page screenshot saved to: " + destination.toAbsolutePath());
        Assert.assertTrue(Files.exists(destination), "Screenshot file should exist");
        Assert.assertTrue(Files.size(destination) > 0, "Screenshot file should not be empty");
    }

    // -----------------------------------------------------------------------
    // 2. Element-level screenshot
    // -----------------------------------------------------------------------

    /**
     * Captures a screenshot of a <b>specific WebElement</b> rather than the whole page.
     * <p>
     * This is useful when you want to capture just a form, a button, or a specific
     * section for visual validation or documentation.
     * </p>
     * <p>
     * Available since Selenium 4 — calls {@code WebElement.getScreenshotAs()}.
     * </p>
     */
    @Test(priority = 2, description = "Capture screenshot of a specific element")
    public void testElementScreenshot() throws IOException {
        // Find the login form element
        WebElement loginForm = driver.findElement(By.cssSelector("form"));

        // Take screenshot of just this element
        File tempFile = loginForm.getScreenshotAs(OutputType.FILE);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "login_form_element_" + timestamp + ".png";
        Path destination = Paths.get(SCREENSHOTS_DIR, fileName);

        Files.copy(tempFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Element screenshot saved to: " + destination.toAbsolutePath());
        Assert.assertTrue(Files.exists(destination), "Element screenshot file should exist");
    }

    // -----------------------------------------------------------------------
    // 3. Screenshot as Base64 string (for embedding in reports)
    // -----------------------------------------------------------------------

    /**
     * Captures a screenshot as a <b>Base64-encoded string</b>.
     * <p>
     * This format is perfect for embedding screenshots directly in HTML reports,
     * email notifications, or logging systems — no need to manage separate files.
     * </p>
     *
     * <pre>{@code
     * // Example: embedding in HTML
     * String html = "<img src='data:image/png;base64," + base64Screenshot + "' />";
     * }</pre>
     */
    @Test(priority = 3, description = "Capture screenshot as Base64 string")
    public void testScreenshotAsBase64() {
        TakesScreenshot screenshotDriver = (TakesScreenshot) driver;

        // Get screenshot as Base64 string
        String base64Screenshot = screenshotDriver.getScreenshotAs(OutputType.BASE64);

        System.out.println("Base64 screenshot length: " + base64Screenshot.length() + " chars");
        System.out.println("First 100 chars: " + base64Screenshot.substring(0, 100) + "...");

        Assert.assertNotNull(base64Screenshot, "Base64 screenshot should not be null");
        Assert.assertTrue(base64Screenshot.length() > 0, "Base64 screenshot should not be empty");
    }

    // -----------------------------------------------------------------------
    // 4. Screenshot as byte array (for programmatic use)
    // -----------------------------------------------------------------------

    /**
     * Captures a screenshot as a <b>byte array</b>.
     * <p>
     * Useful when you need to attach the screenshot to a test report framework
     * (like Allure or ExtentReports) that accepts byte arrays.
     * </p>
     */
    @Test(priority = 4, description = "Capture screenshot as byte array")
    public void testScreenshotAsBytes() {
        TakesScreenshot screenshotDriver = (TakesScreenshot) driver;

        // Get screenshot as byte array
        byte[] screenshotBytes = screenshotDriver.getScreenshotAs(OutputType.BYTES);

        System.out.println("Screenshot byte array size: " + screenshotBytes.length + " bytes");

        Assert.assertNotNull(screenshotBytes, "Screenshot bytes should not be null");
        Assert.assertTrue(screenshotBytes.length > 0, "Screenshot should have content");
    }

    // -----------------------------------------------------------------------
    // 5. Screenshot with dynamic filename (practical utility method)
    // -----------------------------------------------------------------------

    /**
     * Demonstrates a reusable utility pattern for taking and saving screenshots.
     * <p>
     * In real projects, you would extract this into a utility class or the
     * BaseTest class so it can be called from any test or listener.
     * </p>
     */
    @Test(priority = 5, description = "Demo reusable screenshot utility pattern")
    public void testReusableScreenshotMethod() throws IOException {
        // This is how you'd call it from any test
        String filePath = takeAndSaveScreenshot("reusable_pattern_demo");

        Assert.assertNotNull(filePath, "Screenshot path should be returned");
        Assert.assertTrue(new File(filePath).exists(), "Screenshot file should exist at: " + filePath);
    }

    /**
     * Reusable utility method to take a screenshot and save it with a descriptive name.
     * <p>
     * In a real project, this would typically live in your BaseTest or a utility class.
     * </p>
     *
     * @param testName a descriptive name for the screenshot
     * @return the absolute path of the saved screenshot
     * @throws IOException if the file cannot be saved
     */
    private String takeAndSaveScreenshot(String testName) throws IOException {
        TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
        File tempFile = screenshotDriver.getScreenshotAs(OutputType.FILE);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = testName + "_" + timestamp + ".png";
        Path destination = Paths.get(SCREENSHOTS_DIR, fileName);

        Files.copy(tempFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Screenshot saved: " + destination.toAbsolutePath());

        return destination.toAbsolutePath().toString();
    }
}
