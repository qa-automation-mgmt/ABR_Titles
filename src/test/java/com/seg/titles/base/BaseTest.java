package com.seg.titles.base;

import com.seg.titles.utils.SharedDriver;
import com.seg.titles.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    protected static WebDriver driver;

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("🔹 Starting shared WebDriver for all tests...");
        driver = initializeDriver();
        SharedDriver.setDriver(driver);

        // ✅ Go to Titles Dev login page
        driver.get("https://titles-dev.abramsbooks.com/");
        System.out.println("🔐 Performing one-time login before suite...");

        try {
            LoginPage loginPage = new LoginPage(driver);

            // If already logged in, this will just continue
            if (driver.getCurrentUrl().contains("login") || driver.getPageSource().contains("Sign in")) {
                loginPage.loginWithCredentials();
            } else {
                System.out.println("✅ Already logged in — skipping login.");
            }

            System.out.println("✅ Logged in successfully — session ready for all tests.");
        } catch (Exception e) {
            System.out.println("⚠️ Login step failed or already logged in: " + e.getMessage());
        }
    }

    protected WebDriver initializeDriver() {
    WebDriverManager.chromedriver().browserVersion("142").setup();  // Force version match
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--start-maximized");
    options.addArguments("--disable-notifications");
    options.addArguments("--disable-infobars");
    options.addArguments("--remote-allow-origins=*");
    return new ChromeDriver(options);
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("🔹 Closing shared WebDriver...");
        SharedDriver.quitDriver();
    }
}
