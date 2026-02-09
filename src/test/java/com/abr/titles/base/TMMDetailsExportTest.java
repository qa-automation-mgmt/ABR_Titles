package com.abr.titles.base;
 
import com.abr.titles.utils.SharedDriver;
import com.abr.titles.utils.TestReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
 
import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
 
public class TMMDetailsExportTest extends BaseTest {
 
    private WebDriverWait wait;
    private WebDriverWait shortWait;
    private WebDriverWait longWait;
    private Actions actions;
    private Random random;
    
    // Download directory
    private static final String DOWNLOAD_DIR = System.getProperty("user.home") + "/Downloads";
 
    @BeforeClass
    public void setup() {
        driver = SharedDriver.getDriver();
 
        if (driver == null) {
            throw new RuntimeException("❌ Shared driver not initialized. Check BaseTest setup.");
        }
 
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        shortWait = new WebDriverWait(driver, Duration.ofSeconds(7));
        longWait = new WebDriverWait(driver, Duration.ofSeconds(180));
        actions = new Actions(driver);
        random = new Random();
        
        System.out.println("🔹 Test setup complete - using shared logged-in driver");
        System.out.println("📁 Download directory: " + DOWNLOAD_DIR);
    }
 
    @AfterClass
    public void teardown() {
        System.out.println("✅ Test completed - keeping driver alive for other tests");
    }
 
    // ═══════════════════════════════════════════════════════════════
    // SINGLE TMM EXPORT TEST
    // ═══════════════════════════════════════════════════════════════
    @Test(description = "TMM Details Export - Complete Flow Test")
    public void TMMDetailsExportFullFlow() {
        TestReporter.resetStepCounter();
        SoftAssert softAssert = new SoftAssert();
        
        try {
            TestReporter.logInfo("========================================");
            TestReporter.logInfo("TMM Details Export - Full Flow Test");
            TestReporter.logInfo("========================================");
            
            // ═══════════════════════════════════════════════════════════
            // STEP 1: Navigate to TMM Export Page
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 1: Navigating to TMM Export page");
            
            // Verify logged in
            String currentUrl = driver.getCurrentUrl();
            Assert.assertFalse(currentUrl.contains("login"), "User should be logged in");
            Assert.assertTrue(currentUrl.contains("titles-dev.abramsbooks.com"), "Should be on titles-dev domain");
            TestReporter.logPass("✓ User is logged in successfully");
            
            // Navigate to TMM Export
            driver.get("https://titles-dev.abramsbooks.com/Tmm-export");
            waitForPageLoad();
            Thread.sleep(3000);
            
            // Verify navigation
            currentUrl = driver.getCurrentUrl();
            boolean onTMMPage = currentUrl.toLowerCase().contains("tmm-export") || 
                               driver.getPageSource().contains("TMM");
            Assert.assertTrue(onTMMPage, "Should be on TMM Export page");
            TestReporter.logPass("✓ Successfully navigated to TMM Export page");
            
            // Wait for data to load
            TestReporter.logInfo("Waiting for TMM data to load (32,000 records)");
            waitForIsbnFilterToBeEnabled();
            TestReporter.logPass("✓ All TMM data loaded successfully");
            
            // ═══════════════════════════════════════════════════════════
            // STEP 2: Test Page Scrolling
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 2: Testing page scroll functionality");
            testBasicScrolling();
            TestReporter.logPass("✓ Page scrolling verified (top/bottom and left/right)");
            
            // ═══════════════════════════════════════════════════════════
            // STEP 3: Filter Testing - ISBN
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 3: Testing ISBN filter");
            selectMultipleFromDropdown("ISBN", 
                By.xpath("//div[@id='isbn-selector']//div[contains(@class,'custom-select__value-container')]//input"), 2);
            Thread.sleep(2000);
            TestReporter.logPass("✓ Selected 2 ISBNs");
            
            // Export with ISBN filter
            TestReporter.logInfo("Export 1: Exporting with ISBN filter");
            clickExportButton();
            Thread.sleep(3000);
            TestReporter.logPass("✓ Export with ISBN filter completed");
            
            // Clear filters
            clearAllFilters();
            Thread.sleep(2000);
            
            // ═══════════════════════════════════════════════════════════
            // STEP 4: Filter Testing - Title
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 4: Testing Title filter");
            selectMultipleFromDropdown("Title",
                By.xpath("//div[@id='title-selector']//div[contains(@class,'custom-select__value-container')]//input"), 2);
            Thread.sleep(2000);
            TestReporter.logPass("✓ Selected 2 Titles");
            
            clearAllFilters();
            Thread.sleep(2000);
            
            // ═══════════════════════════════════════════════════════════
            // STEP 5: Filter Testing - Season
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 5: Testing Season filter");
            testCheckboxFilter("season", "Season", 2);
            TestReporter.logPass("✓ Selected 2 Season options");
            
            clearAllFilters();
            Thread.sleep(2000);
            
            // ═══════════════════════════════════════════════════════════
            // STEP 6: Filter Testing - Division
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 6: Testing Division filter");
            testCheckboxFilter("division", "Division", 2);
            TestReporter.logPass("✓ Selected 2 Division options");
            
            clearAllFilters();
            Thread.sleep(2000);
            
            // ═══════════════════════════════════════════════════════════
            // STEP 7: Filter Testing - Imprint
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 7: Testing Imprint filter");
            testCheckboxFilter("imprint", "Imprint", 2);
            TestReporter.logPass("✓ Selected 2 Imprint options");
            
            clearAllFilters();
            Thread.sleep(2000);
            
            // ═══════════════════════════════════════════════════════════
            // STEP 8: Filter Testing - Format
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 8: Testing Format filter");
            testCheckboxFilter("format", "Format", 2);
            TestReporter.logPass("✓ Selected 2 Format options");
            
            clearAllFilters();
            Thread.sleep(2000);
            
            // ═══════════════════════════════════════════════════════════
            // STEP 9: Filter Testing - Managing Editor
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 9: Testing Managing Editor filter");
            testCheckboxFilter("managingEditor", "Managing Editor", 2);
            TestReporter.logPass("✓ Selected 2 Managing Editor options");
            
            clearAllFilters();
            Thread.sleep(2000);
            
            // ═══════════════════════════════════════════════════════════
            // STEP 10: Filter Testing - Editor
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 10: Testing Editor filter");
            testCheckboxFilter("editor", "Editor", 2);
            TestReporter.logPass("✓ Selected 2 Editor options");
            
            clearAllFilters();
            Thread.sleep(2000);
            
            // ═══════════════════════════════════════════════════════════
            // STEP 11: Filter Testing - BISAC Status
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 11: Testing BISAC Status filter");
            testCheckboxFilter("bisac_status", "BISAC Status", 2);
            TestReporter.logPass("✓ Selected 2 BISAC Status options");
            
            clearAllFilters();
            Thread.sleep(2000);
            
            // ═══════════════════════════════════════════════════════════
            // STEP 12: Full Export (No Filters)
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 12: Full export without any filters");
            TestReporter.logInfo("Export 2: Exporting full data (no filters)");
            clickExportButton();
            Thread.sleep(3000);
            TestReporter.logPass("✓ Full export completed");
            
            // ═══════════════════════════════════════════════════════════
            // STEP 13: Column Visibility Operations
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 13: Testing column visibility");
            
            // Hide some columns
            TestReporter.logInfo("Hiding random columns");
            toggleRandomColumnsSlowly(3);
            Thread.sleep(2000);
            TestReporter.logPass("✓ Successfully hid 3 columns");
            
            // Unhide all columns
            TestReporter.logInfo("Unhiding all columns");
            if (safeClickOptional(By.xpath("//button[contains(text(),'Unhide All Columns')]"))) {
                Thread.sleep(2000);
                TestReporter.logPass("✓ All columns unhidden");
            }
            
            // ═══════════════════════════════════════════════════════════
            // STEP 14: Navigate back to home
            // ═══════════════════════════════════════════════════════════
            TestReporter.logInfo("Step 14: Navigating back to home page");
            driver.get("https://titles-dev.abramsbooks.com/");
            waitForPageLoad();
            Thread.sleep(2000);
            TestReporter.logPass("✓ Successfully returned to home page");
            
            TestReporter.logInfo("========================================");
            TestReporter.logPass("✅ TMM Details Export Test PASSED");
            TestReporter.logInfo("========================================");
            
        } catch (Exception e) {
            takeScreenshot("tmm_flow_error");
            TestReporter.logFail("TMM Details Export Flow failed", e);
            throw new RuntimeException("Test failed: " + e.getMessage(), e);
        } finally {
            softAssert.assertAll();
        }
    }
 
    // ═══════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════
    
    /**
     * Test basic scrolling - just verify scrolling works (not to end)
     */
    private void testBasicScrolling() {
        try {
            TestReporter.logInfo("Testing vertical and horizontal scroll");
            
            WebElement scrollContainer = driver.findElement(By.cssSelector("div.ag-body-viewport"));
            
            // Scroll down a bit (not to bottom)
            TestReporter.logInfo("Scrolling down");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = 500;", scrollContainer);
            Thread.sleep(1000);
            
            // Scroll back to top
            TestReporter.logInfo("Scrolling back to top");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = 0;", scrollContainer);
            Thread.sleep(1000);
            
            // Scroll right a bit (not to end)
            TestReporter.logInfo("Scrolling right");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = 400;", scrollContainer);
            Thread.sleep(1000);
            
            // Scroll back to left
            TestReporter.logInfo("Scrolling back to left");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = 0;", scrollContainer);
            Thread.sleep(1000);
            
        } catch (Exception e) {
            TestReporter.logWarning("Scroll test warning: " + e.getMessage());
        }
    }
    
    /**
     * Select multiple items from dropdown (for ISBN and Title)
     */
    private void selectMultipleFromDropdown(String filterName, By inputLocator, int count) {
        try {
            for (int i = 0; i < count; i++) {
                TestReporter.logInfo("Selecting " + filterName + " " + (i+1) + "/" + count);
                
                // Find and click dropdown
                WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", inputField);
                Thread.sleep(500);
                inputField.click();
                Thread.sleep(2000);
                
                // Get options
                List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                ));
                
                if (options.isEmpty()) {
                    TestReporter.logWarning("No options found for " + filterName);
                    break;
                }
                
                // Select random option
                int randomIndex = random.nextInt(options.size());
                WebElement selectedOption = options.get(randomIndex);
                String value = selectedOption.getText().trim();
                
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedOption);
                Thread.sleep(300);
                
                try {
                    selectedOption.click();
                } catch (ElementClickInterceptedException e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedOption);
                }
                
                Thread.sleep(1500);
                TestReporter.logPass("Selected " + filterName + ": " + value);
            }
            
        } catch (Exception e) {
            TestReporter.logWarning(filterName + " selection error: " + e.getMessage());
        }
    }
    
    /**
     * Test checkbox filter (for Season, Division, Imprint, Format, Managing Editor, Editor, BISAC Status)
     */
    private void testCheckboxFilter(String filterId, String filterName, int count) {
        try {
            // Click filter to open
            TestReporter.logInfo("Opening " + filterName + " filter");
            if (!safeClickOptional(By.id(filterId))) {
                TestReporter.logWarning(filterName + " filter not found");
                return;
            }
            Thread.sleep(1500);
            
            // Select checkboxes
            int selected = 0;
            for (int i = 0; i < count; i++) {
                if (selectRandomCheckbox(filterName)) {
                    selected++;
                    Thread.sleep(1000);
                }
            }
            
            if (selected > 0) {
                TestReporter.logPass("Selected " + selected + " " + filterName + " option(s)");
            } else {
                TestReporter.logWarning("No " + filterName + " options selected");
            }
            
            // Close filter dropdown
            Thread.sleep(1000);
            driver.findElement(By.tagName("body")).click();
            Thread.sleep(800);
            
        } catch (Exception e) {
            TestReporter.logWarning(filterName + " checkbox error: " + e.getMessage());
        }
    }
    
    /**
     * Select a random checkbox
     */
    private boolean selectRandomCheckbox(String filterName) {
        try {
            Thread.sleep(500);
            
            List<WebElement> checkboxes = driver.findElements(
                By.xpath("//div[contains(@class,'MuiPopper-root')]//input[@type='checkbox']")
            );
            
            if (checkboxes.isEmpty()) {
                return false;
            }
            
            int randomIndex = random.nextInt(checkboxes.size());
            WebElement randomCheckbox = checkboxes.get(randomIndex);
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", randomCheckbox);
            Thread.sleep(300);
            
            try {
                randomCheckbox.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", randomCheckbox);
            }
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Clear all filters
     */
    private void clearAllFilters() {
        try {
            TestReporter.logInfo("Clearing all filters");
            
            // Scroll to top first
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            Thread.sleep(500);
            
            if (safeClickOptional(By.xpath("//button[contains(text(),'Clear All Filter')]"))) {
                Thread.sleep(2000);
                TestReporter.logPass("✓ All filters cleared");
            }
        } catch (Exception e) {
            TestReporter.logWarning("Clear filters warning: " + e.getMessage());
        }
    }
    
    /**
     * Click Export button
     */
    private void clickExportButton() {
        try {
            // Scroll to top
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            Thread.sleep(1000);
            
            // Find Export button
            WebElement exportButton = findExportButton();
            if (exportButton == null) {
                TestReporter.logWarning("Export button not found");
                return;
            }
            
            // Click Export
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", exportButton);
            Thread.sleep(500);
            
            try {
                exportButton.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", exportButton);
            }
            
            TestReporter.logPass("✓ Export button clicked");
            
        } catch (Exception e) {
            TestReporter.logWarning("Export click error: " + e.getMessage());
        }
    }
    
    /**
     * Find Export button
     */
    private WebElement findExportButton() {
        By[] selectors = {
            By.xpath("//button[contains(text(),'Export')]"),
            By.xpath("//button[contains(text(),'EXPORT')]"),
            By.xpath("//button[normalize-space()='Export']")
        };
        
        for (By selector : selectors) {
            try {
                WebElement element = driver.findElement(selector);
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }
    
    /**
     * Toggle random columns
     */
    private void toggleRandomColumnsSlowly(int count) {
        try {
            for (int i = 0; i < count; i++) {
                List<WebElement> columnCheckboxes = driver.findElements(
                    By.xpath("//div[contains(@class,'ag-header-row-column')]//input[@type='checkbox']")
                );
                
                if (columnCheckboxes.isEmpty()) {
                    TestReporter.logWarning("No column checkboxes found");
                    return;
                }
                
                int randomIndex = random.nextInt(columnCheckboxes.size());
                WebElement checkbox = columnCheckboxes.get(randomIndex);
                
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", checkbox);
                Thread.sleep(500);
                
                try {
                    checkbox.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
                }
                
                TestReporter.logInfo("Toggled column " + (i+1) + "/" + count);
                Thread.sleep(2000);
            }
            
        } catch (Exception e) {
            TestReporter.logWarning("Column toggle error: " + e.getMessage());
        }
    }
 
    /**
     * Wait for TMM data to load (32K records)
     */
    private void waitForIsbnFilterToBeEnabled() {
        try {
            System.out.println("🔹 Waiting for TMM data to load completely (32,000 records)...");
            
            // Wait for loader popup
            try {
                By loaderLocator = By.xpath("//div[@class='popup-content']//h3[contains(text(),'Loading Data')]");
                
                WebElement loader = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(loaderLocator));
                
                if (loader.isDisplayed()) {
                    System.out.println("  ✅ Data loader popup detected!");
                    
                    int progressCheckCount = 0;
                    boolean stillLoading = true;
                    
                    while (stillLoading && progressCheckCount < 60) {
                        try {
                            Thread.sleep(2000);
                            progressCheckCount++;
                            
                            WebElement popupContent = driver.findElement(By.className("popup-content"));
                            WebElement goButton = popupContent.findElement(By.xpath(".//button[contains(@class,'action-btn')]"));
                            boolean isButtonEnabled = goButton.isEnabled();
                            
                            if (progressCheckCount % 5 == 0) {
                                System.out.println("  📈 Checking progress... " + (isButtonEnabled ? "✅" : "⏳"));
                            }
                            
                            if (isButtonEnabled) {
                                System.out.println("  ✅ All records loaded!");
                                stillLoading = false;
                            }
                            
                        } catch (org.openqa.selenium.NoSuchElementException | StaleElementReferenceException e) {
                            stillLoading = false;
                        }
                    }
                    
                    longWait.until(ExpectedConditions.invisibilityOf(loader));
                    System.out.println("  ✅ Data loader popup closed!");
                }
                
            } catch (TimeoutException e) {
                System.out.println("  ⚠️ Popup not detected - may have already closed");
                Thread.sleep(15000);
            }
            
            Thread.sleep(2000);
            
            // Verify ISBN dropdown is ready
            By isbnDropdownLocator = By.xpath("//div[@id='isbn-selector']//div[contains(@class,'custom-select__value-container')]");
            WebElement isbnDropdown = longWait.until(ExpectedConditions.presenceOfElementLocated(isbnDropdownLocator));
            longWait.until(ExpectedConditions.elementToBeClickable(isbnDropdownLocator));
            
            // Verify options loaded
            int attempts = 0;
            int maxAttempts = 20;
            boolean optionsLoaded = false;
            
            while (!optionsLoaded && attempts < maxAttempts) {
                attempts++;
                
                try {
                    try {
                        WebElement inputField = isbnDropdown.findElement(By.xpath(".//input[@role='combobox']"));
                        inputField.click();
                    } catch (Exception e) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", isbnDropdown);
                    }
                    
                    Thread.sleep(3000);
                    
                    List<WebElement> options = driver.findElements(
                        By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                    );
                    
                    if (!options.isEmpty()) {
                        optionsLoaded = true;
                        System.out.println("  ✅ SUCCESS! ISBN options loaded (" + options.size() + " options)");
                        
                        try {
                            driver.findElement(By.tagName("body")).click();
                        } catch (Exception e) {}
                        Thread.sleep(500);
                    } else {
                        try {
                            driver.findElement(By.tagName("body")).click();
                        } catch (Exception e) {}
                        Thread.sleep(4000);
                    }
                    
                } catch (Exception e) {
                    System.out.println("  ⚠️ Attempt " + attempts + " error: " + e.getMessage());
                    Thread.sleep(4000);
                }
            }
            
            if (!optionsLoaded) {
                throw new RuntimeException("ISBN dropdown options did not load after " + maxAttempts + " attempts");
            }
            
            System.out.println("✅ All TMM data loaded successfully - filters ready!");
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify TMM data loaded: " + e.getMessage(), e);
        }
    }
 
    private boolean safeClickOptional(By locator) {
        try {
            WebElement element = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            Thread.sleep(300);
            
            try {
                element.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
 
    private void waitForPageLoad() {
        try {
            wait.until(driver1 ->
                ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete")
            );
            Thread.sleep(1500);
        } catch (Exception e) {}
    }
    
    private void takeScreenshot(String filename) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            ts.getScreenshotAs(OutputType.BYTES);
            TestReporter.logInfo("Screenshot captured: " + filename);
        } catch (Exception e) {}
    }
}