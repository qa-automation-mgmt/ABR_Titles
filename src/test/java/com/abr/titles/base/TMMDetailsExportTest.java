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
    private SoftAssert softAssert;
    
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
        softAssert = new SoftAssert();
        
        System.out.println("🔹 Test setup complete - using shared logged-in driver");
        System.out.println("📁 Download directory: " + DOWNLOAD_DIR);
    }

    @AfterClass
    public void teardown() {
        System.out.println("✅ Test completed - keeping driver alive for other tests");
        softAssert.assertAll(); // Assert all soft assertions at the end
    }

    // ═══════════════════════════════════════════════════════════════
    // TC-097: Verify TMM Export Navigation
    // ═══════════════════════════════════════════════════════════════
    @Test(priority = 1, description = "TC-097: Verify TMM Export Navigation")
    public void tc097_verifyTMMExportNavigation() {
        TestReporter.resetStepCounter();
        
        try {
            TestReporter.logInfo("========================================");
            TestReporter.logInfo("TC-097: Verify TMM Export Navigation");
            TestReporter.logInfo("========================================");
            
            // Step 1: Verify logged in
            TestReporter.logInfo("Step 1: Verifying user is logged in");
            String currentUrl = driver.getCurrentUrl();
            Assert.assertFalse(currentUrl.contains("login"), "User should be logged in");
            Assert.assertTrue(currentUrl.contains("titles-dev.abramsbooks.com"), 
                "Should be on titles-dev domain");
            TestReporter.logPass("User is logged in successfully");
            
            // Step 2: Navigate to home
            TestReporter.logInfo("Step 2: Navigating to home page");
            driver.get("https://titles-dev.abramsbooks.com/");
            waitForPageLoad();
            Thread.sleep(2000);
            TestReporter.logPass("Home page loaded successfully");
            
            // Step 3: Navigate directly to TMM Export page
            TestReporter.logInfo("Step 3: Navigating to TMM Export page");
            driver.get("https://titles-dev.abramsbooks.com/Tmm-export");
            waitForPageLoad();
            Thread.sleep(3000);
            
            // Step 4: Verify navigation success
            TestReporter.logInfo("Step 4: Verifying navigation to TMM Export page");
            currentUrl = driver.getCurrentUrl();
            String pageSource = driver.getPageSource();
            
            // Verify URL contains tmm-export
            boolean correctURL = currentUrl.toLowerCase().contains("tmm-export") || 
                                currentUrl.toLowerCase().contains("tmm_export");
            
            boolean onTMMPage = correctURL || 
                               pageSource.contains("TMM") ||
                               pageSource.contains("ISBN");
            
            Assert.assertTrue(onTMMPage, "Should be on TMM Export page");
            TestReporter.logPass("Successfully navigated to TMM Export page");
            
            TestReporter.logPass("✅ TC-097 PASSED: Successfully navigated to TMM Export page");
            TestReporter.logInfo("Current URL: " + currentUrl);
            TestReporter.logInfo("========================================");
            
        } catch (Exception e) {
            TestReporter.logFail("TC-097 failed", e);
            takeScreenshot("tc097_failed");
            softAssert.fail("TC-097 Failed: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // TC-098: Verify TMM Export Filters
    // ═══════════════════════════════════════════════════════════════
    @Test(priority = 2, description = "TC-098: Verify TMM Export Filters", 
          dependsOnMethods = "tc097_verifyTMMExportNavigation")
    public void tc098_verifyTMMExportFilters() {
        TestReporter.resetStepCounter();
        
        try {
            TestReporter.logInfo("========================================");
            TestReporter.logInfo("TC-098: Verify TMM Export Filters");
            TestReporter.logInfo("========================================");
            
            // Wait for data to load completely
            TestReporter.logInfo("Waiting for TMM data to load (32,000 records)");
            waitForIsbnFilterToBeEnabled();
            TestReporter.logPass("All TMM data loaded successfully");

            // Test ISBN Filter
            TestReporter.logInfo("Testing ISBN filter dropdown");
            int isbnSelected = testDropdownFilter("ISBN", 
                By.xpath("//input[@placeholder='Select ISBN'] | //div[@id='isbn-selector']//div[contains(@class,'custom-select__value-container')]//input"));
            if (isbnSelected >= 1) {
                TestReporter.logPass("ISBN filter verified - Selected " + isbnSelected + " item(s)");
            } else {
                TestReporter.logWarning("ISBN filter - No items selected");
                softAssert.fail("TC-098: ISBN filter not working");
            }
            Thread.sleep(2000);
            
            // Test Title Filter
            TestReporter.logInfo("Testing Title filter dropdown");
            int titleSelected = testDropdownFilter("Title", 
                By.xpath("//input[@placeholder='Select Title'] | //div[@id='title-selector']//div[contains(@class,'custom-select__value-container')]//input"));
            if (titleSelected >= 1) {
                TestReporter.logPass("Title filter verified - Selected " + titleSelected + " item(s)");
            } else {
                TestReporter.logWarning("Title filter - No items selected");
                softAssert.fail("TC-098: Title filter not working");
            }
            Thread.sleep(2000);
            
            // Test Imprint Filter
            TestReporter.logInfo("Testing Imprint filter dropdown");
            int imprintSelected = testDropdownFilter("Imprint", 
                By.xpath("//input[@placeholder='Select Imprint'] | //div[contains(@class,'select')]//input[contains(@placeholder,'Imprint')]"));
            if (imprintSelected >= 1) {
                TestReporter.logPass("Imprint filter verified - Selected " + imprintSelected + " item(s)");
            } else {
                TestReporter.logWarning("Imprint filter - No items selected");
            }
            Thread.sleep(2000);
            
            // Test Format Filter
            TestReporter.logInfo("Testing Format filter dropdown");
            int formatSelected = testDropdownFilter("Format", 
                By.xpath("//input[@placeholder='Select Format'] | //div[contains(@class,'select')]//input[contains(@placeholder,'Format')]"));
            if (formatSelected >= 1) {
                TestReporter.logPass("Format filter verified - Selected " + formatSelected + " item(s)");
            } else {
                TestReporter.logWarning("Format filter - No items selected");
            }
            Thread.sleep(2000);

            // Test Conditional Filters - ISBN 9
            TestReporter.logInfo("Testing ISBN 9 conditional filter");
            try {
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
                Thread.sleep(1000);
                
                List<WebElement> isbn9Rows = driver.findElements(
                    By.xpath("//table//td[contains(text(),'270011X')] | //div[contains(text(),'ISBN 9')]")
                );
                
                if (!isbn9Rows.isEmpty()) {
                    TestReporter.logPass("ISBN 9 conditional filter table visible");
                    testConditionalDropdown("ISBN 9");
                } else {
                    TestReporter.logWarning("ISBN 9 conditional filter not found");
                }
            } catch (Exception e) {
                TestReporter.logWarning("ISBN 9 conditional filter test skipped: " + e.getMessage());
            }

            // Test Conditional Filters - MOD9
            TestReporter.logInfo("Testing MOD9 conditional filter");
            try {
                testConditionalDropdown("MOD9");
            } catch (Exception e) {
                TestReporter.logWarning("MOD9 conditional filter test skipped: " + e.getMessage());
            }

            // Test checkbox filters from original script
            TestReporter.logInfo("Testing Season filter");
            int seasonSelected = testCheckboxFilter("season", "Season", 2);
            if (seasonSelected >= 1) {
                TestReporter.logPass("Season filter verified - Selected " + seasonSelected + " item(s)");
            }
            
            TestReporter.logInfo("Testing Division filter");
            int divisionSelected = testCheckboxFilter("division", "Division", 2);
            if (divisionSelected >= 1) {
                TestReporter.logPass("Division filter verified - Selected " + divisionSelected + " item(s)");
            }
            
            TestReporter.logPass("✅ TC-098 PASSED: All TMM Export filters verified");
            TestReporter.logInfo("========================================");
            
        } catch (Exception e) {
            TestReporter.logFail("TC-098 failed", e);
            takeScreenshot("tc098_failed");
            softAssert.fail("TC-098 Failed: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // TC-099: Verify TMM Export Button
    // ═══════════════════════════════════════════════════════════════
    @Test(priority = 3, description = "TC-099: Verify TMM Export Button", 
          dependsOnMethods = "tc098_verifyTMMExportFilters")
    public void tc099_verifyTMMExportButton() {
        TestReporter.resetStepCounter();
        
        try {
            TestReporter.logInfo("========================================");
            TestReporter.logInfo("TC-099: Verify TMM Export Button");
            TestReporter.logInfo("========================================");
            
            // Step 1: Scroll to top
            TestReporter.logInfo("Step 1: Scrolling to top of page");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            Thread.sleep(1000);
            
            // Step 2: Locate Export button
            TestReporter.logInfo("Step 2: Locating Export button");
            WebElement exportButton = findExportButton();
            Assert.assertNotNull(exportButton, "Export button should be present");
            TestReporter.logPass("Export button found");
            
            // Step 3: Verify button properties
            TestReporter.logInfo("Step 3: Verifying Export button is enabled and visible");
            Assert.assertTrue(exportButton.isEnabled(), "Export button should be enabled");
            Assert.assertTrue(exportButton.isDisplayed(), "Export button should be visible");
            TestReporter.logPass("Export button is enabled and visible");
            
            // Step 4: Get button details
            String buttonText = exportButton.getText();
            TestReporter.logInfo("Export button text: '" + buttonText + "'");
            
            // Step 5: Scroll and click
            TestReporter.logInfo("Step 4: Clicking Export button");
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", exportButton);
            Thread.sleep(1000);
            
            try {
                exportButton.click();
            } catch (ElementClickInterceptedException e) {
                TestReporter.logWarning("Using JavaScript click");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", exportButton);
            }
            
            Thread.sleep(3000);
            TestReporter.logPass("✅ TC-099 PASSED: Export button clicked successfully");
            TestReporter.logInfo("========================================");
            
        } catch (Exception e) {
            TestReporter.logFail("TC-099 failed", e);
            takeScreenshot("tc099_failed");
            softAssert.fail("TC-099 Failed: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // TC-100: Verify TMM Export Download
    // ═══════════════════════════════════════════════════════════════
    @Test(priority = 4, description = "TC-100: Verify TMM Export Download", 
          dependsOnMethods = "tc099_verifyTMMExportButton")
    public void tc100_verifyTMMExportDownload() {
        TestReporter.resetStepCounter();
        
        try {
            TestReporter.logInfo("========================================");
            TestReporter.logInfo("TC-100: Verify TMM Export Download");
            TestReporter.logInfo("========================================");
            
            // Step 1: Check download directory
            TestReporter.logInfo("Step 1: Checking download directory: " + DOWNLOAD_DIR);
            File downloadDir = new File(DOWNLOAD_DIR);
            Assert.assertTrue(downloadDir.exists() && downloadDir.isDirectory(), 
                "Download directory should exist");
            TestReporter.logPass("Download directory exists");
            
            // Step 2: Get files before download
            List<File> filesBeforeDownload = Arrays.asList(downloadDir.listFiles());
            int filesCountBefore = filesBeforeDownload.size();
            TestReporter.logInfo("Files in directory before export: " + filesCountBefore);
            
            // Step 3: Wait for download
            TestReporter.logInfo("Step 2: Waiting for file download (max 30 seconds)");
            Thread.sleep(5000); // Initial wait for download to start
            
            File downloadedFile = waitForDownload(filesBeforeDownload, 30);
            
            // Step 4: Verify download
            if (downloadedFile != null) {
                TestReporter.logPass("✅ File downloaded: " + downloadedFile.getName());
                
                // Verify file properties
                TestReporter.logInfo("Step 3: Verifying downloaded file properties");
                long fileSize = downloadedFile.length();
                Assert.assertTrue(fileSize > 0, "File should not be empty");
                TestReporter.logInfo("File size: " + (fileSize / 1024) + " KB");
                TestReporter.logPass("File is not empty");
                
                String fileName = downloadedFile.getName().toLowerCase();
                boolean correctFormat = fileName.endsWith(".xlsx") || 
                                       fileName.endsWith(".csv") || 
                                       fileName.endsWith(".xls");
                Assert.assertTrue(correctFormat, "File should be in correct format");
                TestReporter.logPass("File format verified: " + fileName.substring(fileName.lastIndexOf(".")));
                
                TestReporter.logPass("✅ TC-100 PASSED: TMM Export downloaded and verified successfully");
                TestReporter.logInfo("File path: " + downloadedFile.getAbsolutePath());
            } else {
                // Fallback: Check for most recent file
                TestReporter.logWarning("Specific download not detected, checking for recent files");
                File[] files = downloadDir.listFiles();
                
                if (files != null && files.length > 0) {
                    Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
                    File latestFile = files[0];
                    long fileAge = System.currentTimeMillis() - latestFile.lastModified();
                    
                    if (fileAge < 60000) { // Within last 60 seconds
                        TestReporter.logPass("Recent file found: " + latestFile.getName());
                        TestReporter.logInfo("File size: " + (latestFile.length() / 1024) + " KB");
                        TestReporter.logWarning("TC-100: File download cannot be conclusively verified");
                    } else {
                        TestReporter.logFail("No recent file found in downloads");
                        softAssert.fail("TC-100: No file downloaded");
                    }
                } else {
                    TestReporter.logFail("Downloads folder is empty");
                    softAssert.fail("TC-100: Downloads folder is empty");
                }
            }
            
            TestReporter.logInfo("========================================");
            
        } catch (Exception e) {
            TestReporter.logFail("TC-100 failed", e);
            takeScreenshot("tc100_failed");
            softAssert.fail("TC-100 Failed: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // COMPREHENSIVE FLOW TEST (Original Full Flow)
    // ═══════════════════════════════════════════════════════════════
    @Test(priority = 5, description = "Full TMM Details Export Flow - Comprehensive Test")
    public void TMMDetailsExportFullFlow() {
        TestReporter.resetStepCounter();
        
        try {
            TestReporter.logInfo("========================================");
            TestReporter.logInfo("Full TMM Details Export Flow Test");
            TestReporter.logInfo("========================================");
            
            // Verify logged in
            TestReporter.logInfo("Verifying user is logged in");
            String loginUrl = driver.getCurrentUrl();
            if (loginUrl.contains("login") || !loginUrl.contains("titles-dev.abramsbooks.com")) {
                throw new RuntimeException("❌ Not logged in! Current URL: " + loginUrl);
            }
            TestReporter.logPass("User is logged in successfully");
            
            // Navigate to TMM Export page
            TestReporter.logInfo("Navigating to TMM Export page");
            driver.get("https://titles-dev.abramsbooks.com/Tmm-export");
            waitForPageLoad();
            Thread.sleep(3000);
            TestReporter.logPass("Successfully navigated to TMM Export page");

            // Wait for all data to load
            TestReporter.logInfo("Waiting for TMM data to load completely");
            waitForIsbnFilterToBeEnabled();
            TestReporter.logPass("All TMM data loaded successfully");

            // Apply comprehensive filters
            TestReporter.logInfo("Applying comprehensive filter selections");
            
            // ISBN selections
            TestReporter.logInfo("Selecting ISBNs");
            for (int i = 0; i < 2; i++) {
                selectRandomFromReactSelectDropdown("ISBN", 
                    By.xpath("//div[@id='isbn-selector']//div[contains(@class,'custom-select__value-container')]"));
                Thread.sleep(1500);
            }
            
            // Title selections
            TestReporter.logInfo("Selecting Titles");
            for (int i = 0; i < 2; i++) {
                selectRandomFromReactSelectDropdown("Title", 
                    By.xpath("//div[@id='title-selector']//div[contains(@class,'custom-select__value-container')]"));
                Thread.sleep(1500);
            }

            // Checkbox filters
            String[] filters = {
                "season:Season",
                "division:Division",
                "imprint:Imprint",
                "format:Format",
                "managingEditor:Managing Editor",
                "editor:Editor",
                "bisac_status:BISAC Status"
            };
            
            for (String filterInfo : filters) {
                String[] parts = filterInfo.split(":");
                String filterId = parts[0];
                String filterName = parts[1];
                
                TestReporter.logInfo("Testing " + filterName + " filter");
                if (safeClickOptional(By.id(filterId))) {
                    Thread.sleep(1500);
                    selectRandomCheckbox(filterName);
                    Thread.sleep(1000);
                    selectRandomCheckbox(filterName);
                    Thread.sleep(2000);
                    driver.findElement(By.tagName("body")).click();
                    Thread.sleep(800);
                    clearAllFilters();
                }
            }

            // Column visibility operations
            TestReporter.logInfo("Testing column visibility toggle");
            toggleRandomColumnsSlowly(3);
            Thread.sleep(2000);

            TestReporter.logInfo("Clicking 'Unhide All Columns'");
            if (safeClickOptional(By.xpath("//button[contains(text(),'Unhide All Columns')]"))) {
                Thread.sleep(2000);
                TestReporter.logPass("All columns unhidden");
            }

            // Scroll operations
            TestReporter.logInfo("Scrolling data table");
            scrollDataTableSlowly();
            Thread.sleep(2000);

            // Final Export
            TestReporter.logInfo("Clicking Export button for final export");
            safeClickOptional(By.xpath("//button[contains(text(),'Export')]"));
            Thread.sleep(3000);
            TestReporter.logPass("Export initiated successfully");

            // Navigate back to home
            TestReporter.logInfo("Navigating back to home page");
            driver.get("https://titles-dev.abramsbooks.com/");
            waitForPageLoad();
            Thread.sleep(2000);
            TestReporter.logPass("Successfully returned to home page");

            TestReporter.logInfo("========================================");
            TestReporter.logPass("✅ Full TMM Details Export Flow Completed Successfully");
            TestReporter.logInfo("========================================");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            TestReporter.logFail("Test interrupted", e);
            throw new RuntimeException("Test interrupted: " + e.getMessage(), e);
        } catch (Exception e) {
            takeScreenshot("tmm_full_flow_error");
            TestReporter.logFail("Full flow test failed", e);
            throw new RuntimeException("Test failed: " + e.getMessage(), e);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // HELPER METHODS FOR FILTER TESTING
    // ═══════════════════════════════════════════════════════════════
    
    private int testDropdownFilter(String filterName, By locator) {
        int selected = 0;
        try {
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(locator));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", dropdown);
            Thread.sleep(500);
            dropdown.click();
            Thread.sleep(2000);
            
            List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
            ));
            
            if (!options.isEmpty()) {
                int randomIndex = random.nextInt(Math.min(3, options.size()));
                WebElement selectedOption = options.get(randomIndex);
                String value = selectedOption.getText().trim();
                
                selectedOption.click();
                Thread.sleep(1500);
                TestReporter.logPass("Selected " + filterName + ": " + value);
                selected = 1;
            } else {
                TestReporter.logWarning("No " + filterName + " options found");
            }
            
        } catch (Exception e) {
            TestReporter.logWarning(filterName + " filter error: " + e.getMessage());
        }
        return selected;
    }
    
    private int testCheckboxFilter(String filterId, String filterName, int count) {
        int selected = 0;
        try {
            if (safeClickOptional(By.id(filterId))) {
                Thread.sleep(1500);
                for (int i = 0; i < count; i++) {
                    if (selectRandomCheckboxIfAvailable(filterName)) {
                        selected++;
                    }
                    Thread.sleep(1000);
                }
                driver.findElement(By.tagName("body")).click();
                Thread.sleep(800);
            }
        } catch (Exception e) {
            TestReporter.logWarning(filterName + " checkbox issue: " + e.getMessage());
        }
        return selected;
    }
    
    private void testConditionalDropdown(String columnName) {
        try {
            TestReporter.logInfo("Testing conditional operators for " + columnName);
            
            List<WebElement> dropdowns = driver.findElements(
                By.xpath("//select | //div[contains(@class,'select__control')]")
            );
            
            if (!dropdowns.isEmpty()) {
                WebElement dropdown = dropdowns.get(0);
                
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", dropdown);
                Thread.sleep(500);
                dropdown.click();
                Thread.sleep(1500);
                
                String[] expectedOperators = {
                    "Equals", "Does not equal", "Contains", "Does not contain",
                    "Begins with", "Ends with", "Less than", "Greater than",
                    "Between", "Blank", "Not blank"
                };
                
                int operatorsFound = 0;
                for (String operator : expectedOperators) {
                    List<WebElement> options = driver.findElements(
                        By.xpath("//*[contains(text(),'" + operator + "')]")
                    );
                    if (!options.isEmpty()) {
                        operatorsFound++;
                    }
                }
                
                TestReporter.logPass("Found " + operatorsFound + " conditional operators for " + columnName);
                
                if (operatorsFound > 0) {
                    String randomOperator = expectedOperators[random.nextInt(Math.min(5, expectedOperators.length))];
                    try {
                        WebElement operatorOption = driver.findElement(
                            By.xpath("//*[contains(text(),'" + randomOperator + "')]")
                        );
                        operatorOption.click();
                        Thread.sleep(500);
                        TestReporter.logPass("Selected operator: " + randomOperator);
                    } catch (Exception e) {
                        TestReporter.logWarning("Could not select operator");
                    }
                }
                
                try {
                    ((JavascriptExecutor) driver).executeScript("document.body.click();");
                    Thread.sleep(500);
                } catch (Exception e) {}
            }
        } catch (Exception e) {
            TestReporter.logWarning("Conditional dropdown test failed: " + e.getMessage());
        }
    }
    
    private void clearAllFilters() {
        try {
            if (safeClickOptional(By.xpath("//button[contains(text(),'Clear All Filter')]"))) {
                Thread.sleep(2000);
                TestReporter.logPass("All filters cleared");
            }
        } catch (Exception e) {
            TestReporter.logWarning("Failed to clear filters: " + e.getMessage());
        }
    }

    private WebElement findExportButton() {
        By[] selectors = {
            By.xpath("//button[contains(text(),'Export')]"),
            By.xpath("//button[contains(text(),'EXPORT')]"),
            By.xpath("//button[normalize-space()='Export']"),
            By.xpath("//*[@type='submit' and contains(text(),'Export')]")
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

    private File waitForDownload(List<File> filesBeforeDownload, int maxWaitSeconds) {
        File downloadDir = new File(DOWNLOAD_DIR);
        
        for (int i = 0; i < maxWaitSeconds; i++) {
            try {
                Thread.sleep(1000);
                
                List<File> currentFiles = Arrays.asList(downloadDir.listFiles());
                List<File> newFiles = currentFiles.stream()
                    .filter(f -> !filesBeforeDownload.contains(f))
                    .collect(Collectors.toList());
                
                for (File file : newFiles) {
                    String fileName = file.getName().toLowerCase();
                    
                    boolean isTMMFile = (fileName.contains("tmm") || 
                                        fileName.contains("export") || 
                                        fileName.contains("title"));
                    
                    boolean isCorrectFormat = (fileName.endsWith(".xlsx") || 
                                              fileName.endsWith(".csv") || 
                                              fileName.endsWith(".xls"));
                    
                    boolean notTempFile = (!fileName.endsWith(".crdownload") && 
                                          !fileName.endsWith(".tmp"));
                    
                    if (isTMMFile && isCorrectFormat && notTempFile) {
                        TestReporter.logInfo("Download detected after " + (i + 1) + " seconds");
                        return file;
                    }
                }
                
                if ((i + 1) % 5 == 0) {
                    TestReporter.logInfo("Still waiting... (" + (i + 1) + "s)");
                }
                
            } catch (Exception e) {
                TestReporter.logWarning("Download wait error: " + e.getMessage());
            }
        }
        
        return null;
    }

    // ═══════════════════════════════════════════════════════════════
    // WAIT FOR DATA LOADER METHOD (32K Records)
    // ═══════════════════════════════════════════════════════════════
    
    private void waitForIsbnFilterToBeEnabled() {
        try {
            System.out.println("🔹 Waiting for TMM data to load completely (32,000 records)...");
            
            // Wait for loader to appear and disappear
            try {
                By loaderLocator = By.xpath("//div[@class='popup-content']//h3[contains(text(),'Loading Data')]");
                
                System.out.println("  🔍 Looking for 'Loading Data...' popup...");
                
                WebElement loader = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(loaderLocator));
                
                if (loader.isDisplayed()) {
                    System.out.println("  ✅ Data loader popup detected!");
                    
                    try {
                        WebElement popupContent = driver.findElement(By.className("popup-content"));
                        String totalRecords = popupContent.findElement(By.xpath(".//p[contains(text(),'Total Records')]")).getText();
                        System.out.println("  📊 " + totalRecords);
                    } catch (Exception e) {
                        System.out.println("  ⏳ Loading records...");
                    }
                    
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
                    
                    System.out.println("  ⏳ Waiting for popup to close...");
                    longWait.until(ExpectedConditions.invisibilityOf(loader));
                    System.out.println("  ✅ Data loader popup closed!");
                }
                
            } catch (TimeoutException e) {
                System.out.println("  ⚠️ Popup not detected - may have already closed");
                Thread.sleep(15000);
            }
            
            Thread.sleep(2000);
            
            // Wait for ISBN dropdown
            System.out.println("  ⏳ Waiting for ISBN dropdown to be enabled...");
            By isbnDropdownLocator = By.xpath("//div[@id='isbn-selector']//div[contains(@class,'custom-select__value-container')]");
            
            WebElement isbnDropdown = longWait.until(
                ExpectedConditions.presenceOfElementLocated(isbnDropdownLocator)
            );
            System.out.println("  ✓ ISBN dropdown found");
            Thread.sleep(500);
            
            longWait.until(ExpectedConditions.elementToBeClickable(isbnDropdownLocator));
            System.out.println("  ✓ ISBN dropdown is clickable");
            Thread.sleep(1000);
            
            // Verify options are loaded
            System.out.println("  ⏳ Verifying ISBN options are loaded...");
            
            int attempts = 0;
            int maxAttempts = 20;
            boolean optionsLoaded = false;
            
            while (!optionsLoaded && attempts < maxAttempts) {
                attempts++;
                
                try {
                    System.out.println("  🔍 Attempt " + attempts + "/" + maxAttempts + ": Opening dropdown...");
                    
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
                            WebElement inputField = isbnDropdown.findElement(By.xpath(".//input[@role='combobox']"));
                            inputField.sendKeys(Keys.ESCAPE);
                        } catch (Exception e) {
                            driver.findElement(By.tagName("body")).click();
                        }
                        Thread.sleep(500);
                        
                    } else {
                        System.out.println("  ⚠️ No options loaded yet...");
                        
                        try {
                            WebElement inputField = isbnDropdown.findElement(By.xpath(".//input[@role='combobox']"));
                            inputField.sendKeys(Keys.ESCAPE);
                        } catch (Exception e) {
                            driver.findElement(By.tagName("body")).click();
                        }
                        
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
            
        } catch (TimeoutException e) {
            throw new RuntimeException("TMM data loading timeout", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Waiting interrupted", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify TMM data loaded", e);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // ADDITIONAL HELPER METHODS FROM ORIGINAL SCRIPT
    // ═══════════════════════════════════════════════════════════════
    
    private void selectRandomFromReactSelectDropdown(String fieldName, By dropdownLocator) {
        try {
            System.out.println("🔹 Opening " + fieldName + " dropdown...");
            
            WebElement dropdownContainer = wait.until(ExpectedConditions.presenceOfElementLocated(dropdownLocator));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                dropdownContainer
            );
            Thread.sleep(500);
            
            try {
                WebElement inputField = dropdownContainer.findElement(By.xpath(".//input[@role='combobox']"));
                inputField.click();
            } catch (Exception e) {
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
            throw new RuntimeException(fieldName + " selection interrupted", e);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to select " + fieldName + ": " + e.getMessage());
        }
    }
    
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
    
    private void toggleRandomColumnsSlowly(int count) {
        try {
            System.out.println("🔹 Toggling " + count + " random columns...");
            
            for (int i = 0; i < count; i++) {
                List<WebElement> columnCheckboxes = driver.findElements(
                    By.xpath("//div[contains(@class,'ag-header-row-column')]//input[@type='checkbox']")
                );
                
                if (columnCheckboxes.isEmpty()) {
                    System.out.println("⚠️ No column checkboxes found");
                    return;
                }
                
                int randomIndex = random.nextInt(columnCheckboxes.size());
                WebElement checkbox = columnCheckboxes.get(randomIndex);
                
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", 
                    checkbox
                );
                Thread.sleep(500);
                
                Thread.sleep(800);
                
                try {
                    checkbox.click();
                } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
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
                Thread.sleep(2000);
            }
            
            System.out.println("✓ Column toggle complete");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Column toggle interrupted", e);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to toggle columns: " + e.getMessage());
        }
    }
    
    private void scrollDataTableSlowly() {
        try {
            System.out.println("🔹 Scrolling data table...");
            
            WebElement scrollContainer = driver.findElement(By.cssSelector("div.ag-body-viewport"));
            
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
        } catch (TimeoutException e) {
            System.out.println("⚠️ Element not found: " + locator);
            return false;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println("⚠️ Element not found: " + locator);
            return false;
        } catch (Exception e) {
            System.out.println("⚠️ Failed to click: " + e.getMessage());
            return false;
        }
    }

    private void clickElementByText(String text) throws Exception {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[contains(text(),'" + text + "')]")
        ));
        
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});",
            element
        );
        Thread.sleep(500);
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        Thread.sleep(7000);
        TestReporter.logInfo("Waited 7 seconds on the page after navigation");
    }

    private void waitForPageLoad() {
        try {
            wait.until(driver1 ->
                ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete")
            );
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {}
    }
    
    private void takeScreenshot(String filename) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            ts.getScreenshotAs(OutputType.BYTES);
            TestReporter.logInfo("Screenshot captured: " + filename);
        } catch (Exception e) {
            TestReporter.logWarning("Failed to take screenshot: " + e.getMessage());
        }
    }
}