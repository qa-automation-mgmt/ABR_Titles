package com.seg.titles.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "i0116")
    private WebElement usernameField;

    @FindBy(id = "idSIButton9")
    private WebElement nextButton;

    @FindBy(id = "i0118")
    private WebElement passwordField;

    @FindBy(id = "idSIButton9")
    private WebElement signInButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    public void enterUsername(String username) {
        try {
            driver.switchTo().defaultContent();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i0116")));
            usernameField.clear();
            usernameField.sendKeys(username);
            System.out.println("✅ Entered username successfully");
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to enter username: " + e.getMessage());
        }
    }

    public void clickNextButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            nextButton.click();
            System.out.println("✅ Clicked Next button");
            Thread.sleep(2000); // Brief pause for page transition
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to click Next button: " + e.getMessage());
        }
    }

    public void enterPassword(String password) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i0118")));
            passwordField.clear();
            passwordField.sendKeys(password);
            System.out.println("✅ Entered password successfully");
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to enter password: " + e.getMessage());
        }
    }

    public void clickSignInButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signInButton));
            signInButton.click();
            System.out.println("✅ Clicked Sign In button");
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to click Sign In button: " + e.getMessage());
        }
    }

    public void login(String username, String password) {
        enterUsername(username);
        clickNextButton();
        enterPassword(password);
        clickSignInButton();
    }

    /** Improved login with better error handling and verification */
    public void loginWithCredentials() {
        try {
            System.out.println("🔹 Starting auto login...");
            String username = "abr.qa@primotech.com";
            String password = "AbRqu@l!ty#rt5$";

            login(username, password);

            // Wait for successful login - max 2 minutes
            boolean loginSuccessful = false;
            int maxAttempts = 24; // 24 * 5 seconds = 120 seconds
            
            for (int i = 0; i < maxAttempts; i++) {
                Thread.sleep(5000);
                String currentUrl = driver.getCurrentUrl().toLowerCase();
                String pageSource = driver.getPageSource().toLowerCase();

                // Check if we've reached the target site
                if (currentUrl.contains("titles-dev.abramsbooks.com") && 
                    !currentUrl.contains("login") && 
                    !pageSource.contains("sign in")) {
                    System.out.println("✅ Login successful - reached Titles Dev!");
                    loginSuccessful = true;
                    break;
                }

                // Handle "Stay signed in" prompt
                if (pageSource.contains("stay signed in")) {
                    try {
                        WebElement yesButton = driver.findElement(By.id("idSIButton9"));
                        if (yesButton.isDisplayed()) {
                            yesButton.click();
                            System.out.println("✅ Clicked 'Yes' on Stay signed in");
                            Thread.sleep(3000);
                        }
                    } catch (Exception ignored) {}
                }

                System.out.println("⏳ Attempt " + (i + 1) + "/" + maxAttempts + " - Waiting for MFA/redirect...");
            }

            if (!loginSuccessful) {
                throw new RuntimeException("❌ Login failed - timed out after 2 minutes. Current URL: " + driver.getCurrentUrl());
            }

            // Additional verification - wait for page to be stable
            Thread.sleep(3000);
            System.out.println("✅ Login verification complete");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("❌ Login interrupted: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("❌ Login failed: " + e.getMessage());
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}