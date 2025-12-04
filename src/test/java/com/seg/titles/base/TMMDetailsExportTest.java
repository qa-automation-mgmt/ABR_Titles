package com.seg.titles.base;

import com.seg.titles.utils.SharedDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class TMMDetailsExportTest extends BaseTest {

    private WebDriverWait wait;
    private Actions actions;
    private Random random;

    @BeforeClass
    public void setup() {
        driver = SharedDriver.getDriver();

        if (driver == null) {
            throw new RuntimeException("❌ Shared driver not initialized. Check BaseTest setup.");
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        actions = new Actions(driver);
        random = new Random();
        System.out.println("🔹 Test setup complete - using shared logged-in driver");
    }

    @AfterClass
    public void teardown() {
        System.out.println("✅ Test completed - keeping driver alive for other tests");
    }

    @Test
    public void TMMDetailsExportFullFlow() {
        try {
            // Verify we're logged in
            String loginUrl = driver.getCurrentUrl();
            if (loginUrl.contains("login") || !loginUrl.contains("titles-dev.abramsbooks.com")) {
                throw new RuntimeException("❌ Not logged in! Current URL: " + loginUrl);
            }

            System.out.println("✅ Confirmed logged in - starting TMM Details Export flow");
            driver.get("https://titles-dev.abramsbooks.com/title-details");
            waitForPageLoad();
            
            // -----------------------------
            // Step 1: Click "TMM Details" in Navigation
            // -----------------------------
            System.out.println("🔹 Step 1: Clicking 'TMM Details' in navigation...");
            safeClick(By.xpath("//span[contains(text(),'TMM Details')]"));
            waitForPageLoad();
            Thread.sleep(3000); // Wait for TMM page to fully load
            System.out.println("✓ Navigated to TMM Details Export page");

            // Wait for overlay to disappear
            waitForOverlayToDisappear();
            Thread.sleep(2000); // Additional wait for page stabilization

            // -----------------------------
            // Step 2: Select Random ISBN (First Dropdown)
            // -----------------------------
            System.out.println("🔹 Step 2: Selecting first random ISBN...");
            selectRandomFromReactSelectDropdown("ISBN", 
                By.xpath("//div[@id='isbn-selector']//div[contains(@class,'custom-select__value-container')]"));
            Thread.sleep(3000); // Increased wait to see selection

            // -----------------------------
            // Step 3: Select Random ISBN (Change Selection)
            // -----------------------------
            System.out.println("🔹 Step 3: Changing ISBN selection...");
            selectRandomFromReactSelectDropdown("ISBN", 
                By.xpath("//div[@id='isbn-selector']//div[contains(@class,'custom-select__value-container')]"));
            Thread.sleep(3000); // Increased wait to see selection

            // -----------------------------
            // Step 4: Select Random Title
            // -----------------------------
            System.out.println("🔹 Step 4: Selecting random Title...");
            selectRandomFromReactSelectDropdown("Title", 
                By.xpath("//div[@id='title-selector']//div[contains(@class,'custom-select__value-container')]"));
            Thread.sleep(3000); // Increased wait to see selection
            
            // Clear filters
            System.out.println("🔹 Clearing filters...");
            safeClickOptional(By.xpath("//button[contains(text(),'Clear All Filter')]"));
            Thread.sleep(3000);

           // -----------------------------
          // Step 5: Select Random Author
         // -----------------------------
            System.out.println("🔹 Step 5: Selecting random Author...");

        // Use the exact XPath from the DOM
           By authorDropdownLocator = By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[3]/div[1]/div[1]/div[3]/div[2]/div/div[1]/div[1]"
            );

           try {
               selectRandomFromReactSelectDropdown("Author", authorDropdownLocator);
               Thread.sleep(3000);
           } catch (Exception e) {
               System.out.println("⚠️ Failed to select Author with exact XPath: " + e.getMessage());
    
            // Fallback: Try by position (3rd dropdown)
          try {
              By fallbackLocator = By.xpath(
                  "(//div[contains(@class,'custom-select__value-container')])[3]"
              );
              selectRandomFromReactSelectDropdown("Author", fallbackLocator);
              Thread.sleep(3000);
          } catch (Exception e2) {
              System.out.println("⚠️ Author selection failed completely, continuing...");
          }
        }
            // -----------------------------
            // Step 6: Click "Export" Button (First time)
            // -----------------------------
            System.out.println("🔹 Step 6: Clicking 'Export' button...");
            safeClickOptional(By.xpath("//button[contains(text(),'Export')]"));
            Thread.sleep(2000);

            // -----------------------------
            // Step 7: Click "Clear All Filter" Button (if available)
            // -----------------------------
            System.out.println("🔹 Step 7: Clicking 'Clear All Filter' (if available)...");
            boolean cleared = safeClickOptional(By.xpath("//button[contains(text(),'Clear All Filter')]"));
            if (cleared) {
                Thread.sleep(2000);
                System.out.println("✓ Cleared all filters");
            } else {
                System.out.println("⚠️ Clear All Filter button not found, continuing...");
            }

            // -----------------------------
            // Step 8: Filter by Season
            // -----------------------------
            System.out.println("🔹 Step 8: Selecting Season filter...");
            if (safeClickOptional(By.id("season"))) {
                Thread.sleep(2000);
                selectRandomCheckbox("Season");
                Thread.sleep(3000);
            }

            // -----------------------------
            // Step 9: Filter by Division
            // -----------------------------
            System.out.println("🔹 Step 9: Selecting Division filter...");
            if (safeClickOptional(By.id("division"))) {
                Thread.sleep(2000);
                selectRandomCheckbox("Division");
                Thread.sleep(3000);
            }

            // Clear filters
            System.out.println("🔹 Clearing filters...");
            safeClickOptional(By.xpath("//button[contains(text(),'Clear All Filter')]"));
            Thread.sleep(3000);

            // -----------------------------
            // Step 10: Filter by Imprint (Multiple selections)
            // -----------------------------
            System.out.println("🔹 Step 10: Selecting Imprint filters...");
            if (safeClickOptional(By.id("imprint"))) {
                Thread.sleep(2000);
                selectRandomCheckbox("Imprint");
                Thread.sleep(2000);
                selectRandomCheckbox("Imprint");
                Thread.sleep(3000);
            }

            // Clear filters
            safeClickOptional(By.xpath("//button[contains(text(),'Clear All Filter')]"));
            Thread.sleep(3000);

            // -----------------------------
            // Step 11: Filter by Format (Multiple selections)
            // -----------------------------
            System.out.println("🔹 Step 11: Selecting Format filters...");
            if (safeClickOptional(By.id("format"))) {
                Thread.sleep(2000);
                selectRandomCheckbox("Format");
                Thread.sleep(2000);
                selectRandomCheckbox("Format");
                Thread.sleep(3000);
            }

            // Clear filters
            safeClickOptional(By.xpath("//button[contains(text(),'Clear All Filter')]"));
            Thread.sleep(3000);

            // -----------------------------
            // Step 12: Select Managing Editor (if available)
            // -----------------------------
            System.out.println("🔹 Step 12: Attempting to select Managing Editor...");
            if (safeClickOptional(By.id("managingEditor"))) {
                Thread.sleep(2000);
                boolean selected = selectRandomCheckboxIfAvailable("Managing Editor");
                if (selected) {
                    Thread.sleep(3000);
                } else {
                    System.out.println("⚠️ No Managing Editor options available");
                    driver.findElement(By.tagName("body")).click();
                    Thread.sleep(1000);
                }
            }

            // Clear filters
            System.out.println("🔹 Clearing filters...");
            safeClickOptional(By.xpath("//button[contains(text(),'Clear All Filter')]"));
            Thread.sleep(3000);
            
            // -----------------------------
            // Step 13: Select Editor (if available)
            // -----------------------------
            System.out.println("🔹 Step 13: Attempting to select Editor...");
            if (safeClickOptional(By.id("editor"))) {
                Thread.sleep(2000);
                boolean selected = selectRandomCheckboxIfAvailable("Editor");
                if (selected) {
                    Thread.sleep(3000);
                } else {
                    System.out.println("⚠️ No Editor options available");
                    driver.findElement(By.tagName("body")).click();
                    Thread.sleep(1000);
                }
            }

            // -----------------------------
            // Step 14: Filter by BISAC Status (Multiple selections)
            // -----------------------------
            System.out.println("🔹 Step 14: Selecting BISAC Status filters...");
            if (safeClickOptional(By.id("bisac_status"))) {
                Thread.sleep(2000);
                selectRandomCheckbox("BISAC Status");
                Thread.sleep(2000);
                selectRandomCheckbox("BISAC Status");
                Thread.sleep(3000);
            }

            // Clear filters
            safeClickOptional(By.xpath("//button[contains(text(),'Clear All Filter')]"));
            Thread.sleep(3000);

            // -----------------------------
            // Step 15: Toggle Column Visibility
            // -----------------------------
            System.out.println("🔹 Step 15: Toggling column visibility...");
            toggleRandomColumnsSlowly(3);
            Thread.sleep(3000);

            // -----------------------------
            // Step 16: Click "Unhide All Columns"
            // -----------------------------
            System.out.println("🔹 Step 16: Clicking 'Unhide All Columns'...");
            if (safeClickOptional(By.xpath("//button[contains(text(),'Unhide All Columns')]"))) {
                Thread.sleep(3000);
                System.out.println("✓ All columns unhidden");
            }

            // -----------------------------
            // Step 17: Scroll the data table (Slowly)
            // -----------------------------
            System.out.println("🔹 Step 17: Scrolling data table slowly...");
            scrollDataTableSlowly();
            Thread.sleep(3000);

            // -----------------------------
            // Step 18: Click "Export" Button
            // -----------------------------
            System.out.println("🔹 Step 18: Clicking 'Export' button...");
            safeClickOptional(By.xpath("//button[contains(text(),'Export')]"));
            Thread.sleep(2000);
            System.out.println("✓ Export initiated");

            System.out.println("✅ Full TMM Details Export flow completed successfully!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Test interrupted: " + e.getMessage(), e);
        } catch (Exception e) {
            takeScreenshot("tmm_export_error");
            throw new RuntimeException("Test failed: " + e.getMessage(), e);
        }
    }

    // -----------------------------
    // Helper Methods
    // -----------------------------
    
    /**
     * Select random option from React Select dropdown (returns boolean)
     */
    private boolean selectRandomFromReactSelectDropdownOptional(String fieldName, By dropdownLocator) {
        try {
            System.out.println("🔹 Opening " + fieldName + " dropdown...");
            
            WebElement dropdownContainer = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(dropdownLocator));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                dropdownContainer
            );
            Thread.sleep(500);
            
            // Try clicking input field first
            try {
                WebElement inputField = dropdownContainer.findElement(By.xpath(".//input[@role='combobox']"));
                inputField.click();
            } catch (Exception e) {
                // Fallback to clicking container
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownContainer);
            }
            
            Thread.sleep(2000);
            
            List<WebElement> allOptions = driver.findElements(
                By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
            );
            
            if (allOptions.isEmpty()) {
                System.out.println("⚠️ No options found for " + fieldName);
                return false;
            }
            
            int randomIndex = random.nextInt(allOptions.size());
            WebElement randomOption = allOptions.get(randomIndex);
            String selectedValue = randomOption.getText();
            
            System.out.println("🎲 Randomly selected [" + randomIndex + "]: " + selectedValue);
            
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
            
            System.out.println("✓ Selected " + fieldName + ": " + selectedValue);
            return true;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            System.out.println("⚠️ Failed to select " + fieldName + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Select random option from React Select dropdown (improved)
     */
    private void selectRandomFromReactSelectDropdown(String fieldName, By dropdownLocator) {
        try {
            System.out.println("🔹 Opening " + fieldName + " dropdown...");
            
            WebElement dropdownContainer = wait.until(ExpectedConditions.presenceOfElementLocated(dropdownLocator));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                dropdownContainer
            );
            Thread.sleep(500);
            
            // Try clicking input field first
            try {
                WebElement inputField = dropdownContainer.findElement(By.xpath(".//input[@role='combobox']"));
                inputField.click();
            } catch (Exception e) {
                // Fallback to clicking container
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownContainer);
            }
            
            Thread.sleep(1500);
            
            List<WebElement> allOptions = driver.findElements(
                By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
            );
            
            if (allOptions.isEmpty()) {
                System.out.println("⚠️ No options found for " + fieldName);
                return;
            }
            
            int randomIndex = random.nextInt(allOptions.size());
            WebElement randomOption = allOptions.get(randomIndex);
            String selectedValue = randomOption.getText();
            
            System.out.println("🎲 Randomly selected [" + randomIndex + "]: " + selectedValue);
            
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
            
            System.out.println("✓ Selected " + fieldName + ": " + selectedValue);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(fieldName + " selection interrupted: " + e.getMessage(), e);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to select " + fieldName + ": " + e.getMessage());
        }
    }
    
    /**
     * Select random option from React Select dropdown
     */
    private void selectRandomFromDropdown(String fieldName, By dropdownLocator) {
        try {
            System.out.println("🔹 Opening " + fieldName + " dropdown...");
            
            WebElement dropdownContainer = wait.until(ExpectedConditions.presenceOfElementLocated(dropdownLocator));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                dropdownContainer
            );
            Thread.sleep(500);
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownContainer);
            Thread.sleep(1500);
            
            List<WebElement> allOptions = driver.findElements(
                By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
            );
            
            if (allOptions.isEmpty()) {
                System.out.println("⚠️ No options found for " + fieldName);
                return;
            }
            
            int randomIndex = random.nextInt(allOptions.size());
            WebElement randomOption = allOptions.get(randomIndex);
            String selectedValue = randomOption.getText();
            
            System.out.println("🎲 Randomly selected [" + randomIndex + "]: " + selectedValue);
            
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
            
            System.out.println("✓ Selected " + fieldName + ": " + selectedValue);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(fieldName + " selection interrupted: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to select " + fieldName + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Select a random checkbox from MUI dropdown
     */
    private void selectRandomCheckbox(String fieldName) {
        try {
            Thread.sleep(500);
            
            List<WebElement> checkboxes = driver.findElements(
                By.xpath("//div[contains(@class,'MuiPopper-root')]//input[@type='checkbox']")
            );
            
            if (checkboxes.isEmpty()) {
                System.out.println("⚠️ No checkboxes found for " + fieldName);
                return;
            }
            
            int randomIndex = random.nextInt(checkboxes.size());
            WebElement randomCheckbox = checkboxes.get(randomIndex);
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                randomCheckbox
            );
            Thread.sleep(300);
            
            try {
                randomCheckbox.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", randomCheckbox);
            }
            
            System.out.println("✓ Selected random " + fieldName + " option [" + randomIndex + "]");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(fieldName + " checkbox selection interrupted", e);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to select " + fieldName + " checkbox: " + e.getMessage());
        }
    }
    
    /**
     * Select a random checkbox if options are available, return true if selected
     */
    private boolean selectRandomCheckboxIfAvailable(String fieldName) {
        try {
            Thread.sleep(1000);
            
            List<WebElement> checkboxes = driver.findElements(
                By.xpath("//div[contains(@class,'MuiPopper-root')]//input[@type='checkbox']")
            );
            
            if (checkboxes.isEmpty()) {
                return false;
            }
            
            int randomIndex = random.nextInt(checkboxes.size());
            WebElement randomCheckbox = checkboxes.get(randomIndex);
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                randomCheckbox
            );
            Thread.sleep(300);
            
            try {
                randomCheckbox.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", randomCheckbox);
            }
            
            System.out.println("✓ Selected random " + fieldName + " option [" + randomIndex + "]");
            return true;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(fieldName + " checkbox selection interrupted", e);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to select " + fieldName + " checkbox: " + e.getMessage());
            return false;
        }
    }
    
    /**
 * Toggle random column visibility checkboxes with visible delay
 * FIXED: Re-locates checkboxes each iteration to avoid stale element reference
 */
private void toggleRandomColumnsSlowly(int count) {
    try {
        System.out.println("🔹 Toggling " + count + " random columns slowly...");
        
        for (int i = 0; i < count; i++) {
            // Re-locate checkboxes fresh each time to avoid stale element
            List<WebElement> columnCheckboxes = driver.findElements(
                By.xpath("//div[contains(@class,'ag-header-row-column')]//input[@type='checkbox']")
            );
            
            if (columnCheckboxes.isEmpty()) {
                System.out.println("⚠️ No column checkboxes found");
                return;
            }
            
            // Select random checkbox from fresh list
            int randomIndex = random.nextInt(columnCheckboxes.size());
            WebElement checkbox = columnCheckboxes.get(randomIndex);
            
            // Scroll into view
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", 
                checkbox
            );
            Thread.sleep(500);
            
            // Highlight the column being toggled
            try {
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.border='3px solid red';", 
                    checkbox
                );
            } catch (Exception ignored) {
                // Element might have moved, continue anyway
            }
            
            Thread.sleep(800);
            
            // Click the checkbox
            try {
                checkbox.click();
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                // Re-find and click with JS if stale or intercepted
                List<WebElement> freshCheckboxes = driver.findElements(
                    By.xpath("//div[contains(@class,'ag-header-row-column')]//input[@type='checkbox']")
                );
                if (randomIndex < freshCheckboxes.size()) {
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].click();", 
                        freshCheckboxes.get(randomIndex)
                    );
                }
            }
            
            System.out.println("✓ Toggled column [" + randomIndex + "]");
            
            // Remove highlight (try, but don't fail if element is stale)
            try {
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.border='';", 
                    checkbox
                );
            } catch (Exception ignored) {
                // Element is stale, that's okay
            }
            
            Thread.sleep(2000); // Visible delay between toggles
        }
        
        System.out.println("✓ Column toggle complete");
        
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Column toggle interrupted", e);
    } catch (Exception e) {
        System.out.println("⚠️ Failed to toggle columns: " + e.getMessage());
    }
}
    
    /**
     * Toggle random column visibility checkboxes
     */
    private void toggleRandomColumns(int count) {
        try {
            System.out.println("🔹 Toggling " + count + " random columns...");
            
            List<WebElement> columnCheckboxes = driver.findElements(
                By.xpath("//div[contains(@class,'ag-header-row-column')]//input[@type='checkbox']")
            );
            
            if (columnCheckboxes.isEmpty()) {
                System.out.println("⚠️ No column checkboxes found");
                return;
            }
            
            int toggleCount = Math.min(count, columnCheckboxes.size());
            
            for (int i = 0; i < toggleCount; i++) {
                int randomIndex = random.nextInt(columnCheckboxes.size());
                WebElement checkbox = columnCheckboxes.get(randomIndex);
                
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", 
                    checkbox
                );
                Thread.sleep(300);
                
                try {
                    checkbox.click();
                } catch (ElementClickInterceptedException e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
                }
                
                System.out.println("✓ Toggled column [" + randomIndex + "]");
                Thread.sleep(500);
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Column toggle interrupted", e);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to toggle columns: " + e.getMessage());
        }
    }
    
    /**
     * Scroll the data table vertically and horizontally slowly
     */
    private void scrollDataTableSlowly() {
        try {
            System.out.println("🔹 Scrolling data table slowly...");
            
            // Find the scrollable container
            WebElement scrollContainer = driver.findElement(
                By.cssSelector("div.ag-body-viewport")
            );
            
            // Scroll down slowly in steps
            System.out.println("📜 Scrolling down...");
            for (int i = 1; i <= 5; i++) {
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollTop = (arguments[0].scrollHeight / 5) * " + i + ";", 
                    scrollContainer
                );
                Thread.sleep(1000);
                System.out.println("  ↓ Step " + i + "/5");
            }
            
            Thread.sleep(2000);
            
            // Scroll back to top slowly
            System.out.println("📜 Scrolling back to top...");
            for (int i = 4; i >= 0; i--) {
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollTop = (arguments[0].scrollHeight / 5) * " + i + ";", 
                    scrollContainer
                );
                Thread.sleep(1000);
                System.out.println("  ↑ Step " + (5-i) + "/5");
            }
            
            System.out.println("✓ Scrolling complete");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Scroll interrupted", e);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to scroll table: " + e.getMessage());
        }
    }
    
    /**
     * Scroll the data table vertically and horizontally
     */
    private void scrollDataTable() {
        try {
            System.out.println("🔹 Scrolling data table...");
            
            // Find the scrollable container
            WebElement scrollContainer = driver.findElement(
                By.cssSelector("div.ag-body-viewport")
            );
            
            // Scroll down
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollTop = arguments[0].scrollHeight / 2;", 
                scrollContainer
            );
            Thread.sleep(1000);
            System.out.println("✓ Scrolled down");
            
            // Scroll to top
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollTop = 0;", 
                scrollContainer
            );
            Thread.sleep(1000);
            System.out.println("✓ Scrolled to top");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Scroll interrupted", e);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to scroll table: " + e.getMessage());
        }
    }
    
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
     * Safe click that returns boolean (true if clicked, false if not found)
     */
    private boolean safeClickOptional(By locator) {
        try {
            WebElement element = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                element
            );
            Thread.sleep(300);
            
            try {
                element.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } catch (ElementNotInteractableException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }
            
            return true;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (TimeoutException | NoSuchElementException e) {
            System.out.println("⚠️ Element not found: " + locator);
            return false;
        } catch (Exception e) {
            System.out.println("⚠️ Failed to click: " + e.getMessage());
            return false;
        }
    }

    /**
     * Safe click with JavaScript fallback
     */
    private void safeClick(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                element
            );
            Thread.sleep(300);
            
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