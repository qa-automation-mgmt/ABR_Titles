package com.seg.titles.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object Model class for the Microsoft Login page
 */
public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Email input field
    @FindBy(id = "i0116")
    private WebElement usernameField;

    // "Next" button after entering email
    @FindBy(id = "idSIButton9")
    private WebElement nextButton;

    // Password input field
    @FindBy(id = "i0118")
    private WebElement passwordField;

    // "Sign in" button after entering password
    @FindBy(id = "idSIButton9")
    private WebElement signInButton;

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    /** Wait for email field and enter username */
    public void enterUsername(String username) {
        try {
            driver.switchTo().defaultContent();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("i0116")));
            wait.until(ExpectedConditions.visibilityOf(usernameField));
            usernameField.clear();
            usernameField.sendKeys(username);
            System.out.println("✅ Entered username successfully");
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to locate or type into username field: " + e.getMessage());
        }
    }

    /** Click on the "Next" button after username */
    public void clickNextButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            nextButton.click();
            System.out.println("✅ Clicked on Next button");
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to click Next button: " + e.getMessage());
        }
    }

    /** Wait for password field and enter password */
    public void enterPassword(String password) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("i0118")));
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            passwordField.clear();
            passwordField.sendKeys(password);
            System.out.println("✅ Entered password successfully");
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to locate or type into password field: " + e.getMessage());
        }
    }

    /** Click the "Sign In" button */
    public void clickSignInButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signInButton));
            signInButton.click();
            System.out.println("✅ Clicked Sign In button");
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to click Sign In button: " + e.getMessage());
        }
    }

    /** Complete login flow */
    public void login(String username, String password) {
        enterUsername(username);
        clickNextButton();
        enterPassword(password);
        clickSignInButton();
    }

    /** Automatically logs in with stored credentials (for automated tests) */
    public void loginWithCredentials() {
        try {
            System.out.println("🔹 Starting auto login with stored credentials...");
            String username = "abr.qa@primotech.com";
            String password = "AbRqu@l!ty#rt5$";

            // Perform login
            login(username, password);

            // Wait for redirect and handle MFA or “Stay signed in”
            for (int i = 0; i < 18; i++) { // ~90 seconds
                Thread.sleep(5000);
                String currentUrl = driver.getCurrentUrl().toLowerCase();

                if (currentUrl.contains("titles-dev.abramsbooks.com")) {
                    System.out.println("✅ Login successful — reached Titles Dev homepage!");
                    return;
                }

                if (driver.getPageSource().toLowerCase().contains("stay signed in")) {
                    try {
                        driver.findElement(By.id("idSIButton9")).click();
                        System.out.println("✅ Clicked 'Yes' on Stay signed in prompt");
                    } catch (Exception ignored) {}
                }

                System.out.println("⏳ Waiting for MFA approval or redirect...");
            }

            System.out.println("⚠️ Login might have succeeded, but redirect not confirmed.");
        } catch (Exception e) {
            System.out.println("❌ Auto login failed: " + e.getMessage());
        }
    }

    /** Return page title */
    public String getPageTitle() {
        return driver.getTitle();
    }
}
