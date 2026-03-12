package org.dani.tests.advanced;

import org.dani.pages.LoginPage;
import org.dani.tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreatePostTest extends BaseTest {

    LoginPage loginPage;
    File postPicture = new File("src/test/resources/upload/fileUpload.jpeg");

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();

        // Navigate to the login page
        loginPage = new LoginPage(driver);
        loginPage.navigateToPage();
        loginPage.login("daniel11", "1qaz!QAZ");
    }

    @Test(priority = 1, description = "Capture full page screenshot and save as file")
    public void testFullPageScreenshot() throws InterruptedException {
        Thread.sleep(5333);
        loginPage.uploadPicture(postPicture);
    }
}
