package com.abr.titles.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait longWait;

    // Credentials
    private static final String USERNAME = "abr.qa@primotech.com";
    private static final String PASSWORD = "AbRqu@l!ty#rt5$";

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(40));
    }

    /**
     * Main login method with enhanced debugging and error handling
     */
    public void loginWithCredentials() {
        try {
            System.out.println("🔹 Starting auto login...");
            
            // Step 1: Wait for page to fully load
            waitForPageLoad();
            
            // Step 2: Capture debug info
            captureDebugInfo("before_login");
            
            // Step 3: Enter username
            enterUsername();
            
            // Step 4: Click Next button
            clickNextButton();
            
            // Step 5: Enter password
            enterPassword();
            
            // Step 6: Click Sign In
            clickSignInButton();
            
            // Step 7: Handle "Stay signed in?" prompt and wait for MFA/redirect
            waitForLoginComplete();
            
            System.out.println("✅ Login completed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Login failed: " + e.getMessage());
            captureDebugInfo("login_failed");
            throw new RuntimeException("❌ Login failed: " + e.getMessage(), e);
        }
    }

    /**
     * Wait for page to fully load
     */
    private void waitForPageLoad() {
        try {
            System.out.println("⏳ Waiting for page to load...");
            
            // Wait for document ready state
            longWait.until(driver -> 
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")
            );
            
            // Additional wait for dynamic content
            Thread.sleep(3000);
            
            System.out.println("✅ Page loaded");
            
        } catch (Exception e) {
            System.err.println("⚠️ Page load wait failed: " + e.getMessage());
        }
    }

    /**
     * Capture debug information including screenshot and page details
     */
    private void captureDebugInfo(String stage) {
        try {
            System.out.println("📸 Capturing debug info: " + stage);
            System.out.println("  Current URL: " + driver.getCurrentUrl());
            System.out.println("  Page Title: " + driver.getTitle());
            
            // Take screenshot
            if (driver instanceof TakesScreenshot) {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String filename = "screenshot_" + stage + "_" + timestamp + ".png";
                String targetPath = "target/screenshots/" + filename;
                
                // Create directory if it doesn't exist
                new File("target/screenshots").mkdirs();
                
                Files.copy(screenshot.toPath(), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("  Screenshot saved: " + targetPath);
            }
            
            // Save page source for debugging
            try {
                String pageSource = driver.getPageSource();
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String filename = "pagesource_" + stage + "_" + timestamp + ".html";
                String targetPath = "target/screenshots/" + filename;
                Files.write(Paths.get(targetPath), pageSource.getBytes());
                System.out.println("  Page source saved: " + targetPath);
            } catch (Exception e) {
                System.err.println("  Could not save page source: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("⚠️ Could not capture debug info: " + e.getMessage());
        }
    }

    /**
     * Enter username with multiple selector fallbacks
     */
    private void enterUsername() {
        try {
            System.out.println("📧 Entering username...");
            
            // Switch to default content in case we're in an iframe
            try {
                driver.switchTo().defaultContent();
            } catch (Exception e) {
                // Ignore if not in iframe
            }
            
            WebElement usernameField = findUsernameField();
            
            if (usernameField == null) {
                throw new RuntimeException("❌ Failed to find username field");
            }
            
            // Clear and enter username
            usernameField.clear();
            Thread.sleep(500);
            usernameField.sendKeys(USERNAME);
            Thread.sleep(1000);
            
            System.out.println("✅ Username entered: " + USERNAME);
            
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to enter username: " + e.getMessage(), e);
        }
    }

    /**
     * Find username field using multiple strategies
     */
    private WebElement findUsernameField() {
        By[] selectors = {
            By.id("i0116"),
            By.name("loginfmt"),
            By.xpath("//input[@type='email']"),
            By.cssSelector("input[name='loginfmt']"),
            By.xpath("//input[@placeholder='Email, phone, or Skype']"),
            By.xpath("//input[contains(@placeholder, 'email')]"),
            By.cssSelector("input[type='email']")
        };
        
        for (By selector : selectors) {
            try {
                System.out.println("  Trying selector: " + selector);
                WebElement element = longWait.until(ExpectedConditions.visibilityOfElementLocated(selector));
                if (element != null && element.isDisplayed()) {
                    System.out.println("  ✅ Found username field using: " + selector);
                    return element;
                }
            } catch (Exception e) {
                System.out.println("  ❌ Selector failed: " + selector);
            }
        }
        
        return null;
    }

    /**
     * Click the Next button after entering username
     */
    private void clickNextButton() {
        try {
            System.out.println("👆 Clicking Next button...");
            
            By[] nextButtonSelectors = {
                By.id("idSIButton9"),
                By.xpath("//input[@type='submit' and @value='Next']"),
                By.xpath("//button[contains(text(), 'Next')]"),
                By.cssSelector("input[type='submit'][value='Next']"),
                By.xpath("//input[@value='Next']")
            };
            
            WebElement nextButton = null;
            for (By selector : nextButtonSelectors) {
                try {
                    nextButton = wait.until(ExpectedConditions.elementToBeClickable(selector));
                    if (nextButton != null) {
                        System.out.println("  Found Next button using: " + selector);
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            if (nextButton == null) {
                throw new RuntimeException("Next button not found");
            }
            
            nextButton.click();
            Thread.sleep(3000);
            System.out.println("✅ Next button clicked");
            
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to click Next button: " + e.getMessage(), e);
        }
    }

    /**
     * Enter password with multiple selector fallbacks
     */
    private void enterPassword() {
        try {
            System.out.println("🔑 Entering password...");
            
            WebElement passwordField = findPasswordField();
            
            if (passwordField == null) {
                throw new RuntimeException("❌ Failed to find password field");
            }
            
            // Clear and enter password
            passwordField.clear();
            Thread.sleep(500);
            passwordField.sendKeys(PASSWORD);
            Thread.sleep(1000);
            
            System.out.println("✅ Password entered");
            
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to enter password: " + e.getMessage(), e);
        }
    }

    /**
     * Find password field using multiple strategies
     */
    private WebElement findPasswordField() {
        By[] selectors = {
            By.id("i0118"),
            By.name("passwd"),
            By.xpath("//input[@type='password']"),
            By.cssSelector("input[name='passwd']"),
            By.cssSelector("input[type='password']")
        };
        
        for (By selector : selectors) {
            try {
                System.out.println("  Trying selector: " + selector);
                WebElement element = longWait.until(ExpectedConditions.visibilityOfElementLocated(selector));
                if (element != null && element.isDisplayed()) {
                    System.out.println("  ✅ Found password field using: " + selector);
                    return element;
                }
            } catch (Exception e) {
                System.out.println("  ❌ Selector failed: " + selector);
            }
        }
        
        return null;
    }

    /**
     * Click the Sign In button after entering password
     */
    private void clickSignInButton() {
        try {
            System.out.println("👆 Clicking Sign In button...");
            
            By[] signInButtonSelectors = {
                By.id("idSIButton9"),
                By.xpath("//input[@type='submit' and @value='Sign in']"),
                By.xpath("//button[contains(text(), 'Sign in')]"),
                By.cssSelector("input[type='submit'][value='Sign in']"),
                By.xpath("//input[@value='Sign in']")
            };
            
            WebElement signInButton = null;
            for (By selector : signInButtonSelectors) {
                try {
                    signInButton = wait.until(ExpectedConditions.elementToBeClickable(selector));
                    if (signInButton != null) {
                        System.out.println("  Found Sign In button using: " + selector);
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            if (signInButton == null) {
                throw new RuntimeException("Sign In button not found");
            }
            
            signInButton.click();
            System.out.println("✅ Sign In button clicked");
            
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to click Sign In button: " + e.getMessage(), e);
        }
    }

    /**
     * Wait for login to complete, handling MFA and "Stay signed in" prompts
     */
    private void waitForLoginComplete() {
        try {
            System.out.println("⏳ Waiting for login completion (MFA/redirect)...");
            
            boolean loginSuccessful = false;
            int maxAttempts = 24; // 24 * 5 seconds = 120 seconds (2 minutes)
            
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
                if (pageSource.contains("stay signed in") || currentUrl.contains("kmsi")) {
                    try {
                        System.out.println("  Handling 'Stay signed in' prompt...");
                        WebElement yesButton = driver.findElement(By.id("idSIButton9"));
                        if (yesButton.isDisplayed()) {
                            yesButton.click();
                            System.out.println("✅ Clicked 'Yes' on Stay signed in");
                            Thread.sleep(3000);
                        }
                    } catch (Exception e) {
                        // Try alternative selectors
                        try {
                            WebElement yesBtn = driver.findElement(By.xpath("//input[@value='Yes']"));
                            yesBtn.click();
                            System.out.println("✅ Clicked 'Yes' using alternative selector");
                            Thread.sleep(3000);
                        } catch (Exception ex) {
                            System.out.println("  Could not click 'Stay signed in' button");
                        }
                    }
                }
                
                System.out.println("⏳ Attempt " + (i + 1) + "/" + maxAttempts + " - Waiting for MFA/redirect...");
                System.out.println("  Current URL: " + currentUrl);
            }
            
            if (!loginSuccessful) {
                throw new RuntimeException("❌ Login failed - timed out after 2 minutes. Current URL: " + driver.getCurrentUrl());
            }
            
            // Additional verification - wait for page to be stable
            Thread.sleep(3000);
            System.out.println("✅ Login verification complete");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("❌ Login interrupted: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("❌ Login completion failed: " + e.getMessage(), e);
        }
    }

    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
}