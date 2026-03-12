package org.dani.tests.advanced;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * Demonstrates <b>Browser Capabilities and Options</b> in Selenium WebDriver.
 * <p>
 * Capabilities (previously {@code DesiredCapabilities}, now replaced by
 * browser-specific Options classes) allow you to configure browser behavior
 * before launching the session.
 * </p>
 *
 * <h3>Evolution of Capabilities API:</h3>
 * <table border="1">
 *   <tr><th>Selenium 3 (legacy)</th><th>Selenium 4+ (current)</th></tr>
 *   <tr>
 *     <td>{@code DesiredCapabilities caps = new DesiredCapabilities();}</td>
 *     <td>{@code ChromeOptions options = new ChromeOptions();}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code caps.setCapability("browserName", "chrome");}</td>
 *     <td>Implicit — {@code ChromeOptions} already targets Chrome</td>
 *   </tr>
 * </table>
 *
 * <h3>Browser-specific Options classes (Selenium 4):</h3>
 * <ul>
 *   <li>{@link ChromeOptions} — for Google Chrome / Chromium</li>
 *   <li>{@code FirefoxOptions} — for Mozilla Firefox (GeckoDriver)</li>
 *   <li>{@code EdgeOptions} — for Microsoft Edge (Chromium-based)</li>
 *   <li>{@code SafariOptions} — for Apple Safari</li>
 * </ul>
 *
 * <h3>Common Chrome arguments:</h3>
 * <ul>
 *   <li>{@code --headless=new} — run without a visible browser window</li>
 *   <li>{@code --start-maximized} — start the browser maximized</li>
 *   <li>{@code --disable-notifications} — suppress browser notifications</li>
 *   <li>{@code --incognito} — launch in incognito mode</li>
 *   <li>{@code --window-size=1920,1080} — set specific window dimensions</li>
 *   <li>{@code --disable-gpu} — disable GPU acceleration (useful in CI)</li>
 *   <li>{@code --no-sandbox} — required in some Docker/Linux environments</li>
 *   <li>{@code --disable-dev-shm-usage} — overcome limited shared memory in Docker</li>
 * </ul>
 *
 * <h3>Documentation:</h3>
 * <ul>
 *   <li><a href="https://www.selenium.dev/documentation/webdriver/browsers/chrome/">Selenium Chrome Docs</a></li>
 *   <li><a href="https://chromedriver.chromium.org/capabilities">ChromeDriver Capabilities</a></li>
 *   <li><a href="https://peter.sh/experiments/chromium-command-line-switches/">All Chrome CLI Switches</a></li>
 * </ul>
 *
 * <p>
 * <b>Note:</b> Each test in this class creates its own driver with custom options,
 * so we do NOT use the inherited {@code BaseTest.setUp()} driver.
 * </p>
 */
public class BrowserCapabilitiesTest {

    private WebDriver localDriver;

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (localDriver != null) {
            localDriver.quit();
            localDriver = null;
        }
    }

    // -----------------------------------------------------------------------
    // 1. Headless mode
    // -----------------------------------------------------------------------

    /**
     * Launches Chrome in <b>headless mode</b> — no visible browser window.
     * <p>
     * Headless mode is ideal for CI/CD pipelines where no display is available.
     * Tests run faster because the browser doesn't need to render pixels on screen.
     * </p>
     * <p>
     * Selenium 4 uses {@code --headless=new} (the "new" headless mode in Chrome 112+)
     * which behaves identically to headed Chrome. The older {@code --headless} flag
     * ran a separate headless shell with some behavioral differences.
     * </p>
     */
    @Test(priority = 1, description = "Run Chrome in headless mode")
    public void testHeadlessMode() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");

        localDriver = new ChromeDriver(options);
        localDriver.get("http://training.skillo-bg.com:4200");

        String title = localDriver.getTitle();
        System.out.println("Page title (headless): " + title);
        Assert.assertNotNull(title, "Should be able to get page title in headless mode");
    }

    // -----------------------------------------------------------------------
    // 2. Custom window size
    // -----------------------------------------------------------------------

    /**
     * Sets a specific browser window size at startup.
     * <p>
     * Useful for testing responsive layouts at specific breakpoints
     * (e.g., mobile 375px, tablet 768px, desktop 1920px).
     * </p>
     */
    @Test(priority = 2, description = "Launch browser with custom window size")
    public void testCustomWindowSize() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1024,768");

        localDriver = new ChromeDriver(options);
        localDriver.get("http://training.skillo-bg.com:4200");

        // Verify the window size was applied
        org.openqa.selenium.Dimension size = localDriver.manage().window().getSize();
        System.out.println("Window size: " + size.getWidth() + "x" + size.getHeight());

        // Note: actual size may differ slightly due to OS chrome/toolbars
        Assert.assertTrue(size.getWidth() <= 1024 + 50,
                "Width should be approximately 1024px");
    }

    // -----------------------------------------------------------------------
    // 3. Incognito / Private mode
    // -----------------------------------------------------------------------

    /**
     * Launches Chrome in <b>incognito mode</b>.
     * <p>
     * Incognito mode does not share cookies, cache, or session storage
     * with regular browsing sessions. Each incognito session starts clean.
     * Useful when testing that cookies/session handling works correctly.
     * </p>
     */
    @Test(priority = 3, description = "Launch Chrome in incognito mode")
    public void testIncognitoMode() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--start-maximized");

        localDriver = new ChromeDriver(options);
        localDriver.get("http://training.skillo-bg.com:4200");
        System.out.println("Browser running in incognito mode");
        Thread.sleep(4444);
        System.out.println("Current URL: " + localDriver.getCurrentUrl());
    }

    // -----------------------------------------------------------------------
    // 4. Read browser capabilities
    // -----------------------------------------------------------------------

    /**
     * Reads the <b>actual capabilities</b> of the running browser session.
     * <p>
     * After the driver is created, you can inspect what capabilities were
     * actually negotiated with the browser. This is useful for debugging
     * and logging in CI environments.
     * </p>
     */
    @Test(priority = 5, description = "Read browser capabilities from running session")
    public void testReadBrowserCapabilities() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        localDriver = new ChromeDriver(options);

        // Get capabilities from the running session
        Capabilities capabilities = ((RemoteWebDriver) localDriver).getCapabilities();

        String browserName = capabilities.getBrowserName();
        String browserVersion = capabilities.getBrowserVersion();
        String platformName = String.valueOf(capabilities.getPlatformName());

        System.out.println("====== Browser Capabilities ======");
        System.out.println("Browser Name:    " + browserName);
        System.out.println("Browser Version: " + browserVersion);
        System.out.println("Platform:        " + platformName);
        System.out.println("All Capabilities: " + capabilities.asMap());
        System.out.println("==================================");

        Assert.assertEquals(browserName, "chrome", "Browser should be Chrome");
        Assert.assertNotNull(browserVersion, "Browser version should not be null");
    }

    // -----------------------------------------------------------------------
    // 5. Multiple arguments and experimental options
    // -----------------------------------------------------------------------

    /**
     * Demonstrates combining multiple options for a CI/Docker-ready configuration.
     * <p>
     * This is a typical setup for running Selenium tests in a CI pipeline
     * (Jenkins, GitHub Actions, GitLab CI, etc.) where:
     * <ul>
     *   <li>No display is available → headless</li>
     *   <li>Running as root → no-sandbox</li>
     *   <li>Limited shared memory → disable-dev-shm-usage</li>
     * </ul>
     * </p>
     */
    @Test(priority = 6, description = "CI/Docker-ready Chrome configuration")
    public void testCIReadyConfiguration() {
        ChromeOptions options = new ChromeOptions();

        // Typical CI arguments
        options.addArguments(Arrays.asList(
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080",
                "--disable-notifications"
        ));

        // Accept insecure certs (useful for internal/staging environments with self-signed SSL)
        options.setAcceptInsecureCerts(true);

        // Set page load strategy
        // - NORMAL: waits for the entire page to load (default)
        // - EAGER: waits for DOM to be ready, doesn't wait for images/subresources
        // - NONE: does not wait at all
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        localDriver = new ChromeDriver(options);
        localDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        localDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        localDriver.get("http://training.skillo-bg.com:4200");

        System.out.println("CI-ready browser launched successfully");
        System.out.println("Page title: " + localDriver.getTitle());

        Assert.assertNotNull(localDriver.getTitle());
    }

    // -----------------------------------------------------------------------
    // 6. Exclude switches (disable automation flags)
    // -----------------------------------------------------------------------

    /**
     * Demonstrates removing the "Chrome is being controlled by automated test software"
     * info bar and other automation-related flags.
     * <p>
     * Some websites detect automated browsers. Excluding certain switches can
     * help reduce detection. <b>Note:</b> This should only be used for testing
     * purposes — never for bypassing website security.
     * </p>
     */
    @Test(priority = 7, description = "Exclude automation switches from Chrome")
    public void testExcludeAutomationSwitches() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();

        // Remove the "controlled by automated software" info bar
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));

        // Disable the AutomationControlled flag
        options.addArguments("--disable-blink-features=AutomationControlled");

        options.addArguments("--start-maximized");

        localDriver = new ChromeDriver(options);
        localDriver.get("http://training.skillo-bg.com:4200");
        Thread.sleep(8555);
        System.out.println("Browser launched with automation flags removed");
    }
}
