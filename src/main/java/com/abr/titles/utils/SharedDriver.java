package com.abr.titles.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SharedDriver {
    private static WebDriver driver;
    private static boolean isLoggedIn = false;

    public static WebDriver getDriver() {
        if (driver == null) {
            System.out.println("🔹 Starting shared WebDriver for all tests...");
            
            // Set up Chrome options with incognito mode
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito"); // Enable incognito mode
            options.addArguments("--start-maximized");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);
            
            // Initialize Chrome driver with options
            driver = new ChromeDriver(options);
            
            System.out.println("✅ Chrome browser opened in incognito mode");
        }
        return driver;
    }

    public static void setDriver(WebDriver webDriver) {
        driver = webDriver;
    }

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public static void quitDriver() {
        if (driver != null) {
            System.out.println("🔹 Closing shared WebDriver...");
            driver.quit();
            driver = null;
            isLoggedIn = false;
        }
    }
}