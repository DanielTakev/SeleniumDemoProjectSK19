package org.dani.tests.pagefactory;

import org.dani.pagefactory.HomePageFactory;
import org.dani.pagefactory.LoginPageFactory;
import org.dani.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Login tests using the <b>Page Factory</b> page objects.
 * <p>
 * Mirrors the logic of {@link org.dani.tests.LoginTest} but uses
 * {@link LoginPageFactory} and {@link HomePageFactory} — which rely on
 * {@code @FindBy} annotations and {@code PageFactory.initElements()} instead
 * of manual {@link org.openqa.selenium.By} locators.
 * </p>
 *
 * <h3>Comparison at a glance:</h3>
 * <table border="1">
 *   <tr><th></th><th>Classic POM</th><th>Page Factory</th></tr>
 *   <tr>
 *     <td>Element declaration</td>
 *     <td>{@code By.cssSelector(...)}</td>
 *     <td>{@code @FindBy(css = "...")}</td>
 *   </tr>
 *   <tr>
 *     <td>Initialization</td>
 *     <td>Manual in constructor</td>
 *     <td>{@code PageFactory.initElements(...)}</td>
 *   </tr>
 *   <tr>
 *     <td>Lazy loading</td>
 *     <td>Custom wait helpers</td>
 *     <td>{@code AjaxElementLocatorFactory} (built-in)</td>
 *   </tr>
 * </table>
 *
 * @see org.dani.tests.LoginTest for the classic POM version
 */
public class LoginPageFactoryTest extends BaseTest {

    /**
     * Verifies a successful login flow using Page Factory page objects.
     * <p>
     * Steps:
     * <ol>
     *   <li>Navigate to the login page</li>
     *   <li>Enter valid credentials</li>
     *   <li>Assert redirect to the Home Page</li>
     * </ol>
     * </p>
     */
    @Test(priority = 1, description = "Login via Page Factory and verify redirect to Home")
    public void testLoginWithPageFactory() {
        LoginPageFactory loginPage = new LoginPageFactory(driver);
        HomePageFactory homePage = new HomePageFactory(driver);

        loginPage.navigateToPage();
        loginPage.login("daniel11", "1qaz!QAZ");

        Assert.assertTrue(homePage.isUrlLoaded(),
                "User was not redirected to the Home Page!");
    }

    /**
     * Verifies that the home page contains navigation links.
     * <p>
     * Demonstrates that {@code @FindAll} (OR logic) collects elements
     * from multiple locator strategies into a single {@code List<WebElement>}.
     * </p>
     */
    @Test(priority = 2,
          dependsOnMethods = "testLoginWithPageFactory",
          description = "Verify @FindAll navigation links on Home page")
    public void testNavigationLinksPresent() {
        HomePageFactory homePage = new HomePageFactory(driver);
        int navLinksCount = homePage.getNavigationLinksCount();

        Assert.assertTrue(navLinksCount > 0,
                "Expected navigation links on Home page, but found: " + navLinksCount);
    }
}
