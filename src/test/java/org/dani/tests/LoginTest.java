package org.dani.tests;

import org.dani.pagefactory.HomePageFactory;
import org.dani.pages.HomePage;
import org.dani.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Basic login test using the <b>Page Object Model (POM)</b> pattern.
 *
 * @see org.dani.tests.LoginDataDrivenTest for the data-driven version
 * @see org.dani.tests.pagefactory.LoginPageFactoryTest for the Page Factory version
 */
public class LoginTest extends BaseTest {

    /**
     * Verifies that a user can log in with valid credentials
     * and is redirected to the Home Page.
     */
    @Test(priority = 1)
    public void testLoginFlow() {
        // 1. Page Object Initialization
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        // 2. Navigation
        loginPage.navigateToPage();

        // 3. Business Logic (Action)
        loginPage.login("daniel11", "1qaz!QAZ");
        homePage.verifyPageLoaded();

        // 4. Assertion (The "What")
//        String expectedUrl = homePage.getPageExpectedUrl();
//        Assert.assertEquals(expectedUrl, homePage.getActualCurrentUrl(), "Login failed!");

        // Second approach
        Assert.assertTrue(homePage.isUrlLoaded(), "User was not redirected to the Home Page!");
    }

    /**
     * Checks that the home page displays the expected number of posts.
     */
    @Test(priority = 2)
    public void homePagePostsCountIsCorrect() {
        HomePage homePage = new HomePage(driver);
        Assert.assertEquals(homePage.getPostsCount(), 3);
    }

    @Test(priority = 3)
    public void testValidPostAuthorNameIsVisible() {
        HomePageFactory homePage = new HomePageFactory(driver);
        String authorName = "galenchii";
        boolean hasPostAuthor = homePage.hasPostByAuthorName(authorName);

        Assert.assertTrue(hasPostAuthor, "Post by author " + authorName + " is not visible");
    }

    // @Test(priority = 4)
    // public void testPressKeyDemo() {
    //     HomePage homePage = new HomePage(driver);
    //     homePage.pressKeyboardKey(Keys.ESCAPE);
    // }
}
