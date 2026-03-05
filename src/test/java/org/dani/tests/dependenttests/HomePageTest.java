package org.dani.tests.dependenttests;

import org.dani.pages.HomePage;
import org.dani.pages.LoginPage;
import org.dani.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

    @BeforeClass
    public void loginOnce() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToPage();
        loginPage.login("daniel11", "1qaz!QAZ");
        // After this, the session is active for ALL tests below
    }

    @Test
    public void testPostsCount() {
        HomePage homePage = new HomePage(driver);
        Assert.assertEquals(homePage.getPostsCount(), 3);
     }

    @Test
    public void testPostAuthorNameIsCorrect() {
        HomePage homePage = new HomePage(driver);
        Assert.assertEquals(homePage.getPostAuthorByIndex(1), "Agripina");
    }

    @Test
    public void testPostAuthorNameIsCorrect2() {
        HomePage homePage = new HomePage(driver);
        Assert.assertEquals(homePage.getPostAuthorByIndex(2), "galenchii");
    }
}