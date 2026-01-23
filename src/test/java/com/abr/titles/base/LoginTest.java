package com.abr.titles.base;

import com.abr.titles.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import com.abr.titles.utils.SharedDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void verifyLogin() {
        WebDriver driver = SharedDriver.getDriver();
        LoginPage loginPage = new LoginPage(driver);

        try {
            System.out.println("🔹 Starting Login Verification Test...");
            driver.get("https://titles-dev.abramsbooks.com/");
            Thread.sleep(2000);

            String currentUrl = driver.getCurrentUrl().toLowerCase();

            // If already logged in
            if (currentUrl.contains("titles-dev.abramsbooks.com") && !currentUrl.contains("login.microsoftonline.com")) {
                System.out.println("✅ Already logged in — homepage visible.");
                System.out.println("📄 Page Title: " + driver.getTitle());
                System.out.println("🌐 URL: " + currentUrl);
                return;
            }

            // If Microsoft login appeared again
            if (currentUrl.contains("login.microsoftonline.com")) {
                System.out.println("⚠️ Login screen appeared again — reauthenticating...");
                loginPage.loginWithCredentials();
                Thread.sleep(5000);
            }

            // Verify successful login
            currentUrl = driver.getCurrentUrl().toLowerCase();
            Assert.assertTrue(currentUrl.contains("titles-dev.abramsbooks.com"),
                    "❌ Login failed — did not reach Titles Dev homepage.");
            System.out.println("✅ Login successful — Titles Dev homepage loaded!");

        } catch (Exception e) {
            Assert.fail("❌ Exception during login test: " + e.getMessage());
        }
    }
}
