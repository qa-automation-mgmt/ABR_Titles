package com.seg.titles.base;

import com.seg.titles.utils.SharedDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class TitleDetailsTest extends BaseTest {

    private WebDriverWait wait;
    private Actions actions;

    @BeforeClass
    public void setup() {
        driver = SharedDriver.getDriver();

        if (driver == null) {
            throw new RuntimeException("❌ Shared driver not initialized. Check BaseTest setup.");
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        actions = new Actions(driver);
        System.out.println("🔹 Test setup complete - using shared logged-in driver");
    }

    @AfterClass
    public void teardown() {
        System.out.println("✅ Test completed - keeping driver alive for other tests");
    }

    @Test
    public void TitleDetailsFullFlow() {
        try {
            // Verify we're logged in
            String loginUrl = driver.getCurrentUrl();
            if (loginUrl.contains("login") || !loginUrl.contains("titles-dev.abramsbooks.com")) {
                throw new RuntimeException("❌ Not logged in! Current URL: " + loginUrl);
            }

            System.out.println("✅ Confirmed logged in - starting test flow");
            driver.get("https://titles-dev.abramsbooks.com/");
            waitForPageLoad();
            
            // -----------------------------
            // Step 1: Click "Title Details" in Navigation
            // -----------------------------
            System.out.println("🔹 Step 1: Clicking 'Title Details' in navigation...");
            safeClick(By.xpath("//div[text()='Title Details']"));
            waitForPageLoad();
            System.out.println("✓ Navigated to Title Details page");

            // Wait for overlay to disappear
            waitForOverlayToDisappear();
            Thread.sleep(2000);

            // -----------------------------
            // Step 2: Select ISBN from Dropdown
            // -----------------------------
            System.out.println("🔹 Step 2: Selecting random ISBN...");
            selectRandomIsbnFromDropdown();

            // -----------------------------
            // Step 3: Click "Sales Sheet" Button
            // -----------------------------
            System.out.println("🔹 Step 3: Clicking 'Sales Sheet' button...");
            safeClick(By.xpath("//button[contains(text(),'Sales Sheet')]"));
            Thread.sleep(1000);
            System.out.println("✓ Clicked 'Sales Sheet' button");

            // -----------------------------
            // Step 4: Click "Download PDF" Button
            // -----------------------------
            System.out.println("🔹 Step 4: Clicking 'Download PDF' button...");
            
            // Store the ISBN from current URL for navigation back
            String currentDetailsUrl = driver.getCurrentUrl();
            System.out.println("📍 Current URL: " + currentDetailsUrl);
            
            // Extract ISBN from URL (e.g., /details/9781592700806)
            String isbn = currentDetailsUrl.substring(currentDetailsUrl.lastIndexOf("/") + 1);
            System.out.println("📖 ISBN: " + isbn);
            
            safeClick(By.xpath("//button[contains(text(),'Download PDF')]"));
            Thread.sleep(2000);
            System.out.println("✓ Clicked 'Download PDF' button");
            
            // Navigate to the title-details page (where Sales/POS/Inventory tabs exist)
            String titleDetailsUrl = "https://titles-dev.abramsbooks.com/title-details/" + isbn;
            System.out.println("🔙 Navigating to: " + titleDetailsUrl);
            driver.get(titleDetailsUrl);
            waitForPageLoad();
            Thread.sleep(2000);
            System.out.println("✓ Navigated to title-details page");

            // -----------------------------
            // Step 5: Click "Sales" Tab
            // -----------------------------
            System.out.println("🔹 Step 5: Clicking 'Sales' tab...");
            safeClick(By.xpath("//button[contains(text(),'Sales')]"));
            Thread.sleep(2000);
            System.out.println("✓ Switched to 'Sales' tab");

            // -----------------------------
            // Step 6: Toggle MTD / YTD / LTD
            // -----------------------------
            System.out.println("🔹 Step 6: Toggling MTD/YTD/LTD...");
            
            safeClick(By.xpath("//div[text()='MTD']"));
            Thread.sleep(2000);
            System.out.println("✓ Toggled MTD");
            
            safeClick(By.xpath("//div[text()='YTD']"));
            Thread.sleep(2000);
            System.out.println("✓ Toggled YTD");
            
            safeClick(By.xpath("//div[text()='LTD']"));
            Thread.sleep(2000);
            System.out.println("✓ Toggled LTD");

            // -----------------------------
            // Step 7: Click "POS" Tab
            // -----------------------------
            System.out.println("🔹 Step 7: Clicking 'POS' tab...");
            safeClick(By.xpath("//button[contains(text(),'POS')]"));
            Thread.sleep(2000);
            System.out.println("✓ Switched to 'POS' tab");

            // -----------------------------
            // Step 8: Click "Inventory" Tab
            // -----------------------------
            System.out.println("🔹 Step 8: Clicking 'Inventory' tab...");
            safeClick(By.xpath("//button[contains(text(),'Inventory')]"));
            Thread.sleep(2000);
            System.out.println("✓ Switched to 'Inventory' tab");

            // -----------------------------
            // Step 9: Click on "Indy BO Units" (Column Sort)
            // -----------------------------
            System.out.println("🔹 Step 9: Clicking 'Indy BO Units' column...");
            safeClick(By.xpath("//span[text()='Indy BO Units']"));
            Thread.sleep(2000);
            System.out.println("✓ Clicked 'Indy BO Units' column");

            // -----------------------------
            // Step 10: Close Detail Panel (Back Navigation)
            // -----------------------------
            System.out.println("🔹 Step 10: Closing detail panel (clicking back arrow)...");
            
            boolean backClicked = false;
            
            // Strategy 1: Try the provided XPath
            try {
                System.out.println("🔹 Strategy 1: Using provided XPath...");
                WebElement backButton = driver.findElement(
                    By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div[1]/div/button/svg")
                );
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backButton);
                backClicked = true;
                System.out.println("✓ Clicked back button with Strategy 1");
            } catch (Exception e) {
                System.out.println("⚠️ Strategy 1 failed: " + e.getMessage());
            }
            
            // Strategy 2: Try clicking the button parent element
            if (!backClicked) {
                try {
                    System.out.println("🔹 Strategy 2: Clicking button element...");
                    WebElement backButton = driver.findElement(
                        By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div[1]/div/button")
                    );
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backButton);
                    backClicked = true;
                    System.out.println("✓ Clicked back button with Strategy 2");
                } catch (Exception e) {
                    System.out.println("⚠️ Strategy 2 failed: " + e.getMessage());
                }
            }
            
            // Strategy 3: Find button with aria-label or title
            if (!backClicked) {
                try {
                    System.out.println("🔹 Strategy 3: Finding back button by attributes...");
                    List<WebElement> buttons = driver.findElements(By.tagName("button"));
                    for (WebElement btn : buttons) {
                        String ariaLabel = btn.getAttribute("aria-label");
                        String title = btn.getAttribute("title");
                        if ((ariaLabel != null && (ariaLabel.toLowerCase().contains("back") || ariaLabel.toLowerCase().contains("close"))) ||
                            (title != null && (title.toLowerCase().contains("back") || title.toLowerCase().contains("close")))) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                            backClicked = true;
                            System.out.println("✓ Clicked back button with Strategy 3");
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Strategy 3 failed: " + e.getMessage());
                }
            }
            
            // Strategy 4: Use browser back navigation
            if (!backClicked) {
                System.out.println("🔹 Strategy 4: Using browser back navigation...");
                driver.navigate().back();
                backClicked = true;
                System.out.println("✓ Used browser back navigation");
            }
            
            Thread.sleep(2000);
            System.out.println("✓ Closed detail panel - returned to previous page");

            System.out.println("✅ Full Title Details flow completed successfully!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Test interrupted: " + e.getMessage(), e);
        } catch (Exception e) {
            takeScreenshot("test_error");
            throw new RuntimeException("Test failed: " + e.getMessage(), e);
        }
    }

    // -----------------------------
    // Helper Methods
    // -----------------------------
    
    /**
     * Wait for loading overlay to disappear
     */
    private void waitForOverlayToDisappear() {
        try {
            System.out.println("🔹 Waiting for loading overlay to disappear...");
            
            WebDriverWait overlayWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            overlayWait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("div[class*='z-50'][style*='pointer-events']")
            ));
            
            System.out.println("✓ Overlay disappeared");
        } catch (TimeoutException e) {
            System.out.println("⚠️ Overlay not found or already gone");
        }
    }
    
    /**
     * Select a random ISBN from the dropdown
     */
    private void selectRandomIsbnFromDropdown() {
        try {
            System.out.println("🔹 Opening ISBN dropdown...");
            
            // Wait for any overlays to clear
            waitForOverlayToDisappear();
            Thread.sleep(1000);
            
            // Try multiple strategies to open the dropdown
            boolean dropdownOpened = false;
            
            // Strategy 1: Click the input field directly
            try {
                System.out.println("🔹 Strategy 1: Clicking input field...");
                WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@id,'react-select') and @role='combobox']")
                ));
                
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", 
                    inputField
                );
                Thread.sleep(500);
                
                inputField.click();
                Thread.sleep(1000);
                
                // Check if dropdown opened
                List<WebElement> options = driver.findElements(
                    By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                );
                if (options.size() > 0) {
                    dropdownOpened = true;
                    System.out.println("✓ Dropdown opened with Strategy 1");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Strategy 1 failed: " + e.getMessage());
            }
            
            // Strategy 2: Click the dropdown indicator (arrow icon)
            if (!dropdownOpened) {
                try {
                    System.out.println("🔹 Strategy 2: Clicking dropdown indicator...");
                    WebElement dropdownIndicator = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'custom-select__indicator')]//button")
                    ));
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", 
                        dropdownIndicator
                    );
                    Thread.sleep(500);
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownIndicator);
                    Thread.sleep(1000);
                    
                    // Check if dropdown opened
                    List<WebElement> options = driver.findElements(
                        By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                    );
                    if (options.size() > 0) {
                        dropdownOpened = true;
                        System.out.println("✓ Dropdown opened with Strategy 2");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Strategy 2 failed: " + e.getMessage());
                }
            }
            
            // Strategy 3: Click the entire control container
            if (!dropdownOpened) {
                try {
                    System.out.println("🔹 Strategy 3: Clicking control container...");
                    WebElement controlContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("div.custom-select__control")
                    ));
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", 
                        controlContainer
                    );
                    Thread.sleep(500);
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", controlContainer);
                    Thread.sleep(1000);
                    
                    // Check if dropdown opened
                    List<WebElement> options = driver.findElements(
                        By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                    );
                    if (options.size() > 0) {
                        dropdownOpened = true;
                        System.out.println("✓ Dropdown opened with Strategy 3");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Strategy 3 failed: " + e.getMessage());
                }
            }
            
            if (!dropdownOpened) {
                throw new RuntimeException("❌ Failed to open dropdown with all strategies");
            }
            
            System.out.println("✓ Dropdown is now open");
            Thread.sleep(1000);
            
            // Find all dropdown options
            List<WebElement> allOptions = driver.findElements(
                By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
            );
            
            System.out.println("📋 Found " + allOptions.size() + " dropdown options");
            
            if (allOptions.isEmpty()) {
                throw new RuntimeException("❌ No ISBN options found in dropdown");
            }
            
            // Select a random option
            int randomIndex = (int) (Math.random() * allOptions.size());
            WebElement randomOption = allOptions.get(randomIndex);
            String selectedIsbn = randomOption.getText();
            
            System.out.println("🎲 Randomly selected option [" + randomIndex + "]: " + selectedIsbn);
            
            // Click the random option
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                randomOption
            );
            Thread.sleep(500);
            
            try {
                randomOption.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", randomOption);
            }
            
            System.out.println("✓ Selected random ISBN: " + selectedIsbn);
            Thread.sleep(1500);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("ISBN selection interrupted: " + e.getMessage(), e);
        } catch (Exception e) {
            takeScreenshot("isbn_dropdown_error");
            throw new RuntimeException("❌ Failed to select random ISBN: " + e.getMessage(), e);
        }
    }
    
    /**
     * Select specific ISBN from dropdown with multiple click strategies
     */
    private void selectIsbnFromDropdown(String isbn) {
        try {
            System.out.println("🔹 Opening ISBN dropdown...");
            
            // Wait for any overlays to clear
            waitForOverlayToDisappear();
            Thread.sleep(1000);
            
            // Try multiple strategies to open the dropdown
            boolean dropdownOpened = false;
            
            // Strategy 1: Click the input field directly
            try {
                System.out.println("🔹 Strategy 1: Clicking input field...");
                WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@id,'react-select') and @role='combobox']")
                ));
                
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", 
                    inputField
                );
                Thread.sleep(500);
                
                inputField.click();
                Thread.sleep(1000);
                
                // Check if dropdown opened
                List<WebElement> options = driver.findElements(
                    By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                );
                if (options.size() > 0) {
                    dropdownOpened = true;
                    System.out.println("✓ Dropdown opened with Strategy 1");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Strategy 1 failed: " + e.getMessage());
            }
            
            // Strategy 2: Click the dropdown indicator (arrow icon)
            if (!dropdownOpened) {
                try {
                    System.out.println("🔹 Strategy 2: Clicking dropdown indicator...");
                    WebElement dropdownIndicator = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'custom-select__indicator')]//button")
                    ));
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", 
                        dropdownIndicator
                    );
                    Thread.sleep(500);
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownIndicator);
                    Thread.sleep(1000);
                    
                    // Check if dropdown opened
                    List<WebElement> options = driver.findElements(
                        By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                    );
                    if (options.size() > 0) {
                        dropdownOpened = true;
                        System.out.println("✓ Dropdown opened with Strategy 2");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Strategy 2 failed: " + e.getMessage());
                }
            }
            
            // Strategy 3: Click the entire control container
            if (!dropdownOpened) {
                try {
                    System.out.println("🔹 Strategy 3: Clicking control container...");
                    WebElement controlContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("div.custom-select__control")
                    ));
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", 
                        controlContainer
                    );
                    Thread.sleep(500);
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", controlContainer);
                    Thread.sleep(1000);
                    
                    // Check if dropdown opened
                    List<WebElement> options = driver.findElements(
                        By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                    );
                    if (options.size() > 0) {
                        dropdownOpened = true;
                        System.out.println("✓ Dropdown opened with Strategy 3");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Strategy 3 failed: " + e.getMessage());
                }
            }
            
            if (!dropdownOpened) {
                throw new RuntimeException("❌ Failed to open dropdown with all strategies");
            }
            
            System.out.println("✓ Dropdown is now open");
            Thread.sleep(1000);
            
            // Find all dropdown options
            List<WebElement> allOptions = driver.findElements(
                By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
            );
            
            System.out.println("📋 Found " + allOptions.size() + " dropdown options");
            
            // Search for the ISBN
            WebElement isbnOption = null;
            for (WebElement option : allOptions) {
                String optionText = option.getText();
                System.out.println("  - Option: " + optionText);
                
                if (optionText.contains(isbn)) {
                    isbnOption = option;
                    System.out.println("✓ Found matching ISBN option");
                    break;
                }
            }
            
            if (isbnOption == null) {
                throw new RuntimeException(
                    "❌ ISBN '" + isbn + "' not found. Available: " + allOptions.size() + " options"
                );
            }
            
            // Click the option
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                isbnOption
            );
            Thread.sleep(500);
            
            try {
                isbnOption.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", isbnOption);
            }
            
            System.out.println("✓ Selected ISBN: " + isbn);
            Thread.sleep(1500);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("ISBN selection interrupted: " + e.getMessage(), e);
        } catch (Exception e) {
            takeScreenshot("isbn_dropdown_error");
            throw new RuntimeException("❌ Failed to select ISBN: " + e.getMessage(), e);
        }
    }

    /**
     * Safe click with JavaScript fallback
     */
    private void safeClick(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            
            // Scroll into view
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                element
            );
            Thread.sleep(300);
            
            // Wait for element to be clickable
            wait.until(ExpectedConditions.elementToBeClickable(element));
            
            try {
                element.click();
            } catch (ElementClickInterceptedException e) {
                System.out.println("⚠️ Click intercepted, using JavaScript click");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } catch (ElementNotInteractableException e) {
                System.out.println("⚠️ Element not interactable, using JavaScript click");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Click interrupted: " + locator, e);
        } catch (TimeoutException e) {
            throw new RuntimeException("Element not found or not clickable: " + locator, e);
        } catch (Exception e) {
            throw new RuntimeException("Error clicking element: " + locator, e);
        }
    }

    /**
     * Wait for page to fully load
     */
    private void waitForPageLoad() {
        try {
            wait.until(driver1 -> 
                ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete")
            );
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⚠️ Page load wait interrupted");
        } catch (Exception e) {
            System.out.println("⚠️ Page load wait error: " + e.getMessage());
        }
    }
    
    /**
     * Take screenshot for debugging
     */
    private void takeScreenshot(String filename) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            System.out.println("📸 Screenshot captured: " + filename);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to take screenshot: " + e.getMessage());
        }
    }
}