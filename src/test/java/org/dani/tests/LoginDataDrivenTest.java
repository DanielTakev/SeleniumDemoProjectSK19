package org.dani.tests;

import org.dani.pages.HomePage;
import org.dani.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Demonstrates the <b>Data-Driven Testing</b> approach with TestNG's {@link DataProvider}.
 * <p>
 * The same test logic is executed multiple times with different input data.
 * This avoids code duplication and makes it easy to add new test scenarios
 * by simply adding rows to the data provider.
 * </p>
 * <p>
 * Uses the classic <b>Page Object Model</b> pages ({@link LoginPage}, {@link HomePage}).
 * </p>
 *
 * <h3>How {@code @DataProvider} works:</h3>
 * <ol>
 *   <li>Define a method annotated with {@code @DataProvider} that returns a 2D array.</li>
 *   <li>Reference it in {@code @Test(dataProvider = "name")}.</li>
 *   <li>TestNG invokes the test method once per row, injecting column values as parameters.</li>
 * </ol>
 *
 * @see LoginTest for the basic (non-data-driven) login test
 */
public class LoginDataDrivenTest extends BaseTest {

    /**
     * Supplies login test data as a 2D {@code Object} array.
     * <p>
     * Each row: {@code {username, password, shouldPass, scenarioDescription}}.
     * </p>
     *
     * <ul>
     *   <li><b>username</b>    – the username to enter</li>
     *   <li><b>password</b>    – the password to enter</li>
     *   <li><b>shouldPass</b>  – whether the login is expected to succeed</li>
     *   <li><b>description</b> – human-readable label shown in the test report</li>
     * </ul>
     *
     * <p><i>Replace the demo credentials below with real ones when ready.</i></p>
     *
     * @return test data rows
     */
    @DataProvider(name = "loginCredentials")
    public Object[][] loginData() {
        return new Object[][]{
                // {username,     password,    shouldPass,    description               }
                {"daniel11",     "1qaz!QAZ",    true,       "Valid credentials"         },
                {"wrongUser",    "1qaz!QAZ",    false,      "Invalid username"          },
                {"daniel11",     "wrongPass",   false,      "Invalid password"          },
                {"",             "1qaz!QAZ",    false,      "Empty username"            },
                {"daniel11",     "",            false,      "Empty password"            },
        };
    }

    /**
     * Runs the login flow for each row provided by the {@code loginCredentials}
     * data provider.
     * <p>
     * If {@code shouldPass} is {@code true}, the test asserts redirection to
     * the home page. Otherwise, it asserts that the user <b>stays</b> on the
     * login page.
     * </p>
     *
     * @param username    the username to type
     * @param password    the password to type
     * @param shouldPass  expected outcome ({@code true} = redirect to Home)
     * @param description scenario label visible in the TestNG report
     */
    @Test(dataProvider = "loginCredentials",
          description = "Data-driven login with multiple credential combinations")
    public void testLoginWithCredentials(String username, String password,
                                         boolean shouldPass, String description) throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        // Navigate to login page (handles both fresh start and mid-session)
        loginPage.navigateToPage();
        loginPage.login(username, password);

        if (shouldPass) {
            homePage.verifyPageLoaded();
            Assert.assertTrue(homePage.isUrlLoaded(),
                    description + " — expected redirect to Home Page!");
        } else {
            // For invalid credentials the user should remain on the login page
            String currentUrl = loginPage.getActualCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/users/login"),
                    description + " — expected to stay on Login Page, but was: " + currentUrl);
        }
    }
}
