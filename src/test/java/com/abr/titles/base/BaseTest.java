package com.abr.titles.base;

import com.abr.titles.pages.LoginPage;
import com.abr.titles.utils.SharedDriver;
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

        driver.get("https://titles-dev.abramsbooks.com/");
        System.out.println("🔐 Performing one-time login...");

        try {
            String currentUrl = driver.getCurrentUrl().toLowerCase();
            String pageSource = driver.getPageSource().toLowerCase();

            // Only login if we're on login page
            if (currentUrl.contains("login") || pageSource.contains("sign in")) {
                LoginPage loginPage = new LoginPage(driver);
                loginPage.loginWithCredentials();
                
                // Verify we're actually logged in
                if (!driver.getCurrentUrl().contains("titles-dev.abramsbooks.com") || 
                    driver.getCurrentUrl().contains("login")) {
                    throw new RuntimeException("Login verification failed - still on login page");
                }
                
                System.out.println("✅ Login completed and verified");
            } else {
                System.out.println("✅ Already logged in");
            }

        } catch (Exception e) {
            System.err.println("❌ CRITICAL: Login failed in BeforeSuite");
            e.printStackTrace();
            // Cleanup and fail fast
            SharedDriver.quitDriver();
            throw new RuntimeException("Suite setup failed due to login failure", e);
        }
    }

    protected WebDriver initializeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        return new ChromeDriver(options);
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("🔹 Closing shared WebDriver...");
        SharedDriver.quitDriver();
    }
}