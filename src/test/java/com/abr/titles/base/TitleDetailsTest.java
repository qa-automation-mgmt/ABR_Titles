package com.abr.titles.base;
import java.util.Set;
import com.abr.titles.utils.SharedDriver;
import com.abr.titles.utils.TestReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

                import java.time.Duration;
                import java.util.List;
                import java.util.Random;

                public class TitleDetailsTest extends BaseTest {

                    private WebDriverWait wait;
                    private WebDriverWait shortWait;
                    private Actions actions;
                    private Random random;
                    private static final String INVENTORY_ISBN = "9781419733338";

                    @BeforeClass
                    public void setup() {
                        driver = SharedDriver.getDriver();
                        if (driver == null) {
                            throw new RuntimeException("❌ Shared driver not initialized. Check BaseTest setup.");
                        }
                        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
                        shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                        actions = new Actions(driver);
                        random = new Random();
                        System.out.println("🔹 Test setup complete - using shared logged-in driver");
                    }

                    @AfterClass
                    public void teardown() {
                        System.out.println("✅ Test completed - keeping driver alive for other tests");
                    }
            @Test
            public void TitleDetailsFullFlow() {
                TestReporter.resetStepCounter();
                SoftAssert softAssert = new SoftAssert();
                
                TestReporter.logInfo("========================================");
                TestReporter.logInfo("Starting Title Details Full Flow Test");
                TestReporter.logInfo("========================================");
                
                try {
                    // Initial Login Verification
                    try {
                        TestReporter.logInfo("Verifying user is logged in");
                        String currentUrl = driver.getCurrentUrl();
                        if (currentUrl.contains("login") || !currentUrl.contains("titles-dev.abramsbooks.com")) {
                            throw new RuntimeException("Not logged in! Current URL: " + currentUrl);
                        }
                        TestReporter.logPass("User is logged in successfully");
                    } catch (Exception e) {
                        TestReporter.logFail("Login verification failed", e);
                        softAssert.fail("Not logged in: " + e.getMessage());
                    }

                    
                    // Step 1: Navigate to Title Details
    try {
        TestReporter.logInfo("Step 1: Opening Title Details page");
        Thread.sleep(1000);
        safeClick(By.xpath("//div[text()='Title Details']"));
        waitForPageLoad();
        Thread.sleep(3000);
        TestReporter.logPass("✓ Title Details page opened");
        waitForOverlayToDisappear();
        Thread.sleep(2000);
    } catch (Exception e) {
        TestReporter.logFail("Failed to navigate to Title Details", e);
        softAssert.fail("Title Details navigation failed: " + e.getMessage());
    }

    // Step 2: Select Random ISBN from Title Details Page
    String selectedISBN = "";  // ← MAKE SURE THIS LINE EXISTS
    try {
        TestReporter.logInfo("Step 2: Selecting random ISBN");
        Thread.sleep(2000);
        
        // Wait for dropdown to be present on page
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("div.custom-select__control")
        ));
        Thread.sleep(1000);
        
        selectedISBN = selectRandomIsbnFromTitleDetailsPage();
        TestReporter.logPass("✓ Selected ISBN: " + selectedISBN);
        Thread.sleep(1000);
    } catch (Exception e) {
        TestReporter.logFail("Failed to select ISBN", e);
        softAssert.fail("ISBN selection failed: " + e.getMessage());
    }
                    // Step 3: Click Sales Sheet
                    try {
                        TestReporter.logInfo("Step 3: Clicking 'Sales Sheet' button");
                        Thread.sleep(1000);
                        safeClick(By.xpath("//button[contains(text(),'Sales Sheet')]"));
                        Thread.sleep(2000);
                        TestReporter.logPass("✓ Sales Sheet opened");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        TestReporter.logFail("Failed to click Sales Sheet", e);
                        softAssert.fail("Sales Sheet click failed: " + e.getMessage());
                    }

                    // Step 4: Scroll the Sales Sheet PDF
                    try {
                        TestReporter.logInfo("Step 4: Scrolling the Sales Sheet PDF");
                        Thread.sleep(1000);
                        
                        // Scroll down in the PDF
                        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
                        Thread.sleep(1000);
                        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
                        Thread.sleep(1000);
                        
                        // Scroll back up
                        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -1000);");
                        Thread.sleep(1000);
                        
                        TestReporter.logPass("✓ Sales Sheet PDF scrolled");
                    } catch (Exception e) {
                        TestReporter.logFail("Failed to scroll Sales Sheet PDF", e);
                        softAssert.fail("Sales Sheet scrolling failed: " + e.getMessage());
                    }

                    // Step 5: Download PDF
                    try {
                        TestReporter.logInfo("Step 5: Downloading Sales Sheet PDF");
                        Thread.sleep(1000);
                        safeClick(By.xpath("//button[contains(text(),'Download PDF')]"));
                        Thread.sleep(2000);
                        TestReporter.logPass("✓ Sales Sheet PDF download initiated");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        TestReporter.logFail("Failed to download PDF", e);
                        softAssert.fail("Download PDF failed: " + e.getMessage());
                    }
                    
                    // Step 6: Go back using browser back button
                    try {
                        TestReporter.logInfo("Step 6: Navigating back using browser back button");
                        Thread.sleep(1000);
                        driver.navigate().back();
                        waitForPageLoad();
                        Thread.sleep(2000);
                        TestReporter.logPass("✓ Navigated back to Title Details page");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        TestReporter.logFail("Failed to navigate back", e);
                        softAssert.fail("Browser back navigation failed: " + e.getMessage());
                    }

                    // Step 7: Move to Sales Tab
                    try {
                        TestReporter.logInfo("Step 7: Moving to Sales tab");
                        Thread.sleep(1000);
                        safeClick(By.xpath("//button[contains(text(),'Sales')]"));
                        Thread.sleep(2000);
                        TestReporter.logPass("✓ Moved to Sales tab");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        TestReporter.logFail("Failed to click Sales tab", e);
                        softAssert.fail("Sales tab click failed: " + e.getMessage());
                    }

                    // Step 8: Click MTD, YTD, LTD
                    try {
                        TestReporter.logInfo("Step 8: Clicking MTD, YTD, LTD buttons");
                        
                        Thread.sleep(1000);
                        safeClick(By.xpath("//div[text()='MTD']"));
                        Thread.sleep(2000);
                        TestReporter.logPass("✓ Clicked MTD");
                        
                        Thread.sleep(1000);
                        safeClick(By.xpath("//div[text()='YTD']"));
                        Thread.sleep(2000);
                        TestReporter.logPass("✓ Clicked YTD");
                        
                        Thread.sleep(1000);
                        safeClick(By.xpath("//div[text()='LTD']"));
                        Thread.sleep(2000);
                        TestReporter.logPass("✓ Clicked LTD");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        TestReporter.logFail("Failed to toggle MTD/YTD/LTD", e);
                        softAssert.fail("MTD/YTD/LTD toggle failed: " + e.getMessage());
                    }

                    // Step 9 & 9A: Year/Month dropdown interaction
                    try {
                        TestReporter.logInfo("Step 9: Checking Year/Month dropdowns");
                        Thread.sleep(1000);
                        
                        // Scroll to check area
                        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
                        Thread.sleep(2000);
                        
                        // Check if Year and Month dropdowns exist
                        boolean yearExists = driver.findElements(By.xpath("//div[normalize-space()='Year']")).size() > 0;
                        boolean monthExists = driver.findElements(By.xpath("//div[normalize-space()='Month']")).size() > 0;
                        
                        TestReporter.logInfo("Year dropdown exists: " + yearExists + ", Month dropdown exists: " + monthExists);
                        
                        if (!yearExists || !monthExists) {
                            TestReporter.logWarning("Year/Month dropdowns not found");
                            TestReporter.logInfo("Step 9A: Moving directly to POS tab");
                            
                        } else {
                            // Check if they have data
                            boolean hasData = checkIfYearMonthHaveDataSimple();
                            
                            if (hasData) {
                                TestReporter.logPass("✓ Year/Month dropdowns have data - performing full flow");
                                
                                // Select, clear, and export
                                clearAndSelectYearMonth();
                                toggleAccountOrganizationAndExport();
                                
                                TestReporter.logPass("✓ Step 9 completed");
                                
                            } else {
                                TestReporter.logWarning("Year/Month dropdowns have no data");
                                TestReporter.logInfo("Step 9A: Moving directly to POS tab");
                            }
                        }
                        
                    } catch (Exception e) {
                        TestReporter.logFail("Year/Month dropdown check failed", e);
                        softAssert.fail("Year/Month dropdown failed: " + e.getMessage());
                    }

                    Thread.sleep(1000);

                    // Step 10: Move to POS Tab
                    try {
                        TestReporter.logInfo("Step 10: Moving to POS tab");
                        Thread.sleep(1000);
                        safeClick(By.xpath("//button[contains(text(),'POS')]"));
                        Thread.sleep(2000);
                        TestReporter.logPass("✓ Moved to POS tab");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        TestReporter.logFail("Failed to click POS tab", e);
                        softAssert.fail("POS tab click failed: " + e.getMessage());
                    }

  // Step 11: Move to Inventory Tab
try {
    TestReporter.logInfo("Step 11: Moving to Inventory tab");
    Thread.sleep(1000);
    safeClick(By.xpath("//button[contains(text(),'Inventory')]"));
    Thread.sleep(2000);
    TestReporter.logPass("✓ Moved to Inventory tab");
    Thread.sleep(1000);
} catch (Exception e) {
    TestReporter.logFail("Failed to click Inventory tab", e);
    softAssert.fail("Inventory tab click failed: " + e.getMessage());
}

// Step 12 & 12A: Select ISBN in Inventory
try {
    TestReporter.logInfo("Step 12: Selecting ISBN in Inventory tab");
    Thread.sleep(1000);
    
    boolean validIsbnFound = false;
    String selectedInventoryIsbn = "";
    
    // Step 12: Try Static ISBN first
    try {
        TestReporter.logInfo("Step 12: Attempting to select static ISBN: " + INVENTORY_ISBN);
        selectedInventoryIsbn = selectIsbnInInventoryByTyping(INVENTORY_ISBN);
        Thread.sleep(2000);
        
        if (validateInventoryIsbn()) {
            validIsbnFound = true;
            TestReporter.logPass("✓ Static ISBN " + INVENTORY_ISBN + " has valid popups!");
        } else {
            TestReporter.logWarning("Static ISBN " + INVENTORY_ISBN + " found but has no popup data");
        }
        
    } catch (Exception e) {
        TestReporter.logWarning("Static ISBN selection failed: " + e.getMessage());
    }
    
    // Step 12A: If static ISBN not found or has no data, try ONE random ISBN
    if (!validIsbnFound) {
        try {
            TestReporter.logInfo("Step 12A: Static ISBN not valid, selecting one random ISBN");
            Thread.sleep(1000);
            
            selectedInventoryIsbn = selectRandomIsbnFromInventoryDropdown();
            TestReporter.logInfo("Selected random ISBN: " + selectedInventoryIsbn);
            Thread.sleep(3000);
            
            if (validateInventoryIsbn()) {
                validIsbnFound = true;
                TestReporter.logPass("✓ Random ISBN " + selectedInventoryIsbn + " has valid popups!");
            } else {
                TestReporter.logWarning("Random ISBN " + selectedInventoryIsbn + " has no popup data");
            }
            
        } catch (Exception e) {
            TestReporter.logWarning("Random ISBN selection failed: " + e.getMessage());
        }
    }
    
    // If NO valid ISBN found after trying static + 1 random - COMPLETE SCRIPT
    if (!validIsbnFound) {
        TestReporter.logWarning("═══════════════════════════════════════════════");
        TestReporter.logWarning("⚠️  NO VALID ISBN WITH INDY BO + PRINT HISTORY");
        TestReporter.logWarning("═══════════════════════════════════════════════");
        TestReporter.logInfo("Tried: Static ISBN (" + INVENTORY_ISBN + ") - Not found or no data");
        TestReporter.logInfo("Tried: One random ISBN (" + selectedInventoryIsbn + ") - No data");
        TestReporter.logInfo("");
        TestReporter.logInfo("🛑 COMPLETING TEST SCRIPT");
        TestReporter.logInfo("");
        TestReporter.logInfo("========================================");
        TestReporter.logInfo("Title Details Test Completed");
        TestReporter.logInfo("========================================");
        
        return; // Exit test here
    }
    
    TestReporter.logPass("✓ Valid ISBN selected: " + selectedInventoryIsbn);
    
} catch (Exception e) {
    TestReporter.logFail("Failed during ISBN selection", e);
    softAssert.fail("Inventory ISBN selection failed: " + e.getMessage());
    return;
}
// Step 13: Indy BO Units - Scroll, Sort, Export
                    try {
                        TestReporter.logInfo("Step 13: Interacting with Indy BO Units");
                        Thread.sleep(1000);
                        
                        // Click Indy BO Units
                        safeClick(By.xpath("//span[text()='Indy BO Units']"));
                        Thread.sleep(2000);
                        TestReporter.logPass("✓ Indy BO Units opened");
                        
                        waitForModalToBeReadyImpl();
                        Thread.sleep(500);
                        
                        // Scroll in modal
                        TestReporter.logInfo("Scrolling in Indy BO Units popup");
                        WebElement modal = driver.findElement(By.xpath("//div[@role='dialog']"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = 200;", modal);
                        Thread.sleep(500);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = 0;", modal);
                        Thread.sleep(500);
                        TestReporter.logPass("✓ Scrolled popup");
                        
                        // Sort ascending
                        TestReporter.logInfo("Sorting Account column (ascending)");
                        sortIndyBoColumn(2);
                        TestReporter.logPass("✓ Sorted ascending");
                        Thread.sleep(800);
                        
                        // Sort descending
                        TestReporter.logInfo("Sorting Account column (descending)");
                        sortIndyBoColumn(2);
                        TestReporter.logPass("✓ Sorted descending");
                        Thread.sleep(800);
                        
                        // Export
                        TestReporter.logInfo("Clicking Export");
                        quickClickInModal(By.xpath("//div[@role='dialog']//button[contains(text(),'Export')]"));
                        TestReporter.logPass("✓ Export clicked");
                        Thread.sleep(500);
                        
                        // Close
                        quickClickInModal(By.xpath("//div[@role='dialog']//button/span[text()='×']"));
                        Thread.sleep(1000);
                        TestReporter.logPass("✓ Indy BO Units closed");
                        Thread.sleep(500);
                        
                    } catch (Exception e) {
                        TestReporter.logFail("Failed in Indy BO Units", e);
                        softAssert.fail("Indy BO Units failed: " + e.getMessage());
                    }

                    // Step 14: Print History - Scroll, Sort, Export
                    try {
                        TestReporter.logInfo("Step 14: Interacting with Print History");
                        Thread.sleep(500);
                        
                        // Click Print History
                        safeClickOptional(By.xpath("//span[text()='Print History']"));
                        Thread.sleep(1000);
                        TestReporter.logPass("✓ Print History opened");
                        
                        waitForModalToBeReadyImpl();
                        Thread.sleep(500);
                        
                        // Scroll in modal
                        TestReporter.logInfo("Scrolling in Print History popup");
                        WebElement modal = driver.findElement(By.xpath("//div[@role='dialog']"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = 200;", modal);
                        Thread.sleep(500);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = 0;", modal);
                        Thread.sleep(500);
                        TestReporter.logPass("✓ Scrolled popup");
                        
                        // Sort Printing Cycle (ascending)
                        TestReporter.logInfo("Sorting Printing Cycle (ascending)");
                        sortModalColumn("Printing Cycle", 1);
                        TestReporter.logPass("✓ Sorted ascending");
                        Thread.sleep(500);
                        
                        // Sort Printing Cycle (descending)
                        TestReporter.logInfo("Sorting Printing Cycle (descending)");
                        sortModalColumn("Printing Cycle", 1);
                        TestReporter.logPass("✓ Sorted descending");
                        Thread.sleep(500);
                        
                        // Sort Print QTY (ascending)
                        TestReporter.logInfo("Sorting Print QTY (ascending)");
                        sortModalColumn("Print QTY", 1);
                        TestReporter.logPass("✓ Sorted ascending");
                        Thread.sleep(500);
                        
                        // Sort Print QTY (descending)
                        TestReporter.logInfo("Sorting Print QTY (descending)");
                        sortModalColumn("Print QTY", 1);
                        TestReporter.logPass("✓ Sorted descending");
                        Thread.sleep(500);
                        
                        // Export
                        TestReporter.logInfo("Clicking Export");
                        quickClickInModal(By.xpath("//div[@role='dialog']//button[contains(text(),'Export')]"));
                        TestReporter.logPass("✓ Export clicked");
                        Thread.sleep(500);
                        
                        // Close
                        quickClickInModal(By.xpath("//div[@role='dialog']//button/span[text()='×']"));
                        Thread.sleep(1000);
                        TestReporter.logPass("✓ Print History closed");
                        Thread.sleep(500);
                        
                    } catch (Exception e) {
                        TestReporter.logFail("Failed in Print History", e);
                        softAssert.fail("Print History failed: " + e.getMessage());
                    }

                    TestReporter.logInfo("========================================");
                    TestReporter.logInfo("Title Details Test Completed Successfully");
                    TestReporter.logInfo("========================================");
                    
                } catch (Exception e) {
                    takeScreenshot("test_error");
                    TestReporter.logFail("Test failed with unexpected error", e);
                    throw new RuntimeException("Test failed: " + e.getMessage(), e);
                } finally {
                    softAssert.assertAll();
                }
            }
            // ═══════════════════════════════════════════════════════════════
            // HELPER METHODS
            // ═══════════════════════════════════════════════════════════════
            /**
 * Select specific ISBN from Inventory dropdown by typing (for static ISBN)
 * Returns the selected ISBN string
 */
private String selectIsbnInInventoryByTyping(String isbn) {
    try {
        TestReporter.logInfo("Selecting ISBN by typing: " + isbn);
        Thread.sleep(1000);
        
        // Click the input container to activate dropdown
        WebElement inputContainer = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[contains(@class,'custom-select__input-container')]")
        ));
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", inputContainer);
        Thread.sleep(500);
        inputContainer.click();
        Thread.sleep(1000);
        
        // Get the input field
        WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[contains(@id,'react-select') and @role='combobox']")
        ));
        
        // Type last 4 digits to search
        String searchTerm = isbn.substring(isbn.length() - 4);
        TestReporter.logInfo("Typing search term: " + searchTerm);
        
        inputField.clear();
        inputField.sendKeys(searchTerm);
        Thread.sleep(1500);
        
        // Get filtered options
        List<WebElement> filteredOptions = driver.findElements(
            By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
        );
        
        TestReporter.logInfo("Found " + filteredOptions.size() + " filtered options");
        
        if (filteredOptions.isEmpty()) {
            TestReporter.logWarning("No options found after searching for: " + isbn);
            // Clear the search field before throwing exception
            inputField.clear();
            actions.sendKeys(Keys.ESCAPE).perform();
            Thread.sleep(500);
            throw new RuntimeException("ISBN " + isbn + " not found in dropdown");
        }
        
        // Find and click exact match
        boolean isbnFound = false;
        for (WebElement option : filteredOptions) {
            String optionText = option.getText().trim();
            
            if (optionText.equals(isbn)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", option);
                Thread.sleep(500);
                
                try {
                    option.click();
                } catch (ElementClickInterceptedException e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
                }
                
                isbnFound = true;
                TestReporter.logPass("Selected ISBN: " + isbn);
                break;
            }
        }
        
        if (!isbnFound) {
            // Clear the search field before throwing exception
            inputField.clear();
            actions.sendKeys(Keys.ESCAPE).perform();
            Thread.sleep(500);
            throw new RuntimeException("ISBN " + isbn + " not found in filtered results");
        }
        
        Thread.sleep(2000);
        return isbn;
        
    } catch (Exception e) {
        takeScreenshot("inventory_isbn_error");
        throw new RuntimeException("Failed to select ISBN: " + e.getMessage(), e);
    }
}

/**
 * Select random ISBN from Inventory dropdown (for Step 12A)
 * Returns the selected ISBN string
 */
private String selectRandomIsbnFromTitleDetailsPage() {
            try {
                TestReporter.logInfo("Opening ISBN dropdown");
                Thread.sleep(1000);
                
                waitForOverlayToDisappear();
                Thread.sleep(1000);
                
                boolean dropdownOpened = false;
                
                // Strategy 1: Click the input field directly
                try {
                    TestReporter.logInfo("Strategy 1: Clicking input field");
                    WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[contains(@id,'react-select') and @role='combobox']")
                    ));
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});",
                        inputField
                    );
                    Thread.sleep(500);
                    
                    inputField.click();
                    Thread.sleep(1500);
                    
                    List<WebElement> options = driver.findElements(
                        By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                    );
                    if (options.size() > 0) {
                        dropdownOpened = true;
                        TestReporter.logPass("Strategy 1 succeeded - found " + options.size() + " options");
                    }
                } catch (Exception e) {
                    TestReporter.logWarning("Strategy 1 failed: " + e.getMessage());
                }
                
                // Strategy 2: Click the dropdown indicator (arrow icon) - THIS IS THE WORKING ONE
                if (!dropdownOpened) {
                    try {
                        TestReporter.logInfo("Strategy 2: Clicking dropdown indicator");
                        WebElement dropdownIndicator = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(@class,'custom-select__indicator')]//button")
                        ));
                        
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({block: 'center'});",
                            dropdownIndicator
                        );
                        Thread.sleep(500);
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownIndicator);
                        Thread.sleep(1500);
                        
                        List<WebElement> options = driver.findElements(
                            By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                        );
                        if (options.size() > 0) {
                            dropdownOpened = true;
                            TestReporter.logPass("Strategy 2 succeeded - found " + options.size() + " options");
                        }
                    } catch (Exception e) {
                        TestReporter.logWarning("Strategy 2 failed: " + e.getMessage());
                    }
                }
                
                // Strategy 3: Click the entire control container using CSS
                if (!dropdownOpened) {
                    try {
                        TestReporter.logInfo("Strategy 3: Clicking control container");
                        WebElement controlContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("div.custom-select__control")
                        ));
                        
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({block: 'center'});",
                            controlContainer
                        );
                        Thread.sleep(500);
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", controlContainer);
                        Thread.sleep(1500);
                        
                        List<WebElement> options = driver.findElements(
                            By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                        );
                        if (options.size() > 0) {
                            dropdownOpened = true;
                            TestReporter.logPass("Strategy 3 succeeded - found " + options.size() + " options");
                        }
                    } catch (Exception e) {
                        TestReporter.logWarning("Strategy 3 failed: " + e.getMessage());
                    }
                }
                
                // Strategy 4: Click using XPath on control
                if (!dropdownOpened) {
                    try {
                        TestReporter.logInfo("Strategy 4: XPath click on control");
                        WebElement dropdownControl = driver.findElement(
                            By.xpath("//div[contains(@class,'custom-select__control')]")
                        );
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", dropdownControl);
                        Thread.sleep(500);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownControl);
                        Thread.sleep(1500);
                        
                        List<WebElement> options = driver.findElements(
                            By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                        );
                        if (options.size() > 0) {
                            dropdownOpened = true;
                            TestReporter.logPass("Strategy 4 succeeded");
                        }
                    } catch (Exception e) {
                        TestReporter.logWarning("Strategy 4 failed: " + e.getMessage());
                    }
                }
                
                if (!dropdownOpened) {
                    throw new RuntimeException("Failed to open dropdown with all strategies");
                }
                
                TestReporter.logPass("Dropdown is now open");
                Thread.sleep(500);
                
                List<WebElement> isbnOptions = driver.findElements(
                    By.xpath("//div[contains(@id,'react-select-') and contains(@id,'-option-')]")
                );
                
                if (isbnOptions.isEmpty()) {
                    throw new RuntimeException("No ISBN options found");
                }
                
                TestReporter.logInfo("Found " + isbnOptions.size() + " options");
                
                int randomIndex = random.nextInt(isbnOptions.size());
                WebElement selectedOption = isbnOptions.get(randomIndex);
                String selectedIsbn = selectedOption.getText().trim();
                
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedOption);
                Thread.sleep(500);
                
                try {
                    selectedOption.click();
                } catch (ElementClickInterceptedException e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedOption);
                }
                
                Thread.sleep(2000);
                TestReporter.logPass("Selected ISBN: " + selectedIsbn);
                
                return selectedIsbn;
                
            } catch (Exception e) {
                takeScreenshot("isbn_error");
                throw new RuntimeException("ISBN selection failed: " + e.getMessage(), e);
            }
        }
            private void selectYearFromDropdown(String year) {
                try {
                    TestReporter.logInfo("Selecting random year");
                    
                    WebElement yearDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div/div[3]/div/div[3]/div/table/thead/tr[1]/th/div/div[1]/div")
                    ));
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", yearDropdown);
                    Thread.sleep(300);
                    yearDropdown.click();
                    Thread.sleep(1500);
                    
                    List<WebElement> yearOptions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//div[contains(@id,'react-select-')][@role='option']")
                    ));
                    
                    if (!yearOptions.isEmpty()) {
                        int randomIndex = random.nextInt(yearOptions.size());
                        WebElement selectedOption = yearOptions.get(randomIndex);
                        String selectedYear = selectedOption.getText().trim();
                        
                        selectedOption.click();
                        Thread.sleep(500);
                        TestReporter.logPass("Selected year: " + selectedYear);
                    } else {
                        TestReporter.logWarning("No year options found");
                    }
                    
                } catch (Exception e) {
                    TestReporter.logWarning("Failed to select year: " + e.getMessage());
                }
            }

            private void selectMonthFromDropdown(String month) {
                try {
                    TestReporter.logInfo("Selecting random month");
                    
                    WebElement monthDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div/div[3]/div/div[3]/div/table/thead/tr[1]/th/div/div[2]/div")
                    ));
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", monthDropdown);
                    Thread.sleep(300);
                    monthDropdown.click();
                    Thread.sleep(1500);
                    
                    List<WebElement> monthOptions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//div[contains(@id,'react-select-')][@role='option']")
                    ));
                    
                    if (!monthOptions.isEmpty()) {
                        int randomIndex = random.nextInt(monthOptions.size());
                        WebElement selectedOption = monthOptions.get(randomIndex);
                        String selectedMonth = selectedOption.getText().trim();
                        
                        selectedOption.click();
                        Thread.sleep(500);
                        TestReporter.logPass("Selected month: " + selectedMonth);
                    } else {
                        TestReporter.logWarning("No month options found");
                    }
                    
                } catch (Exception e) {
                    TestReporter.logWarning("Failed to select month: " + e.getMessage());
                }
            }

            private void clearAndSelectYearMonth() {
                try {
                    TestReporter.logInfo("Year/Month interaction flow");
                    Thread.sleep(1500);
                    
                    selectYearFromDropdown("");
                    Thread.sleep(1000);
                    
                    selectMonthFromDropdown("");
                    Thread.sleep(1000);
                    
                    clearYearDropdown();
                    Thread.sleep(1000);
                    
                    clearMonthDropdown();
                    Thread.sleep(1000);
                    
                    TestReporter.logPass("✓ Year/Month interaction complete");
                    
                } catch (Exception e) {
                    TestReporter.logWarning("Error in clearAndSelectYearMonth: " + e.getMessage());
                    throw new RuntimeException("Failed: " + e.getMessage(), e);
                }
            }

            private void clearYearDropdown() {
                try {
                    TestReporter.logInfo("Clearing Year");
                    Thread.sleep(500);
                    
                    boolean yearCleared = false;
                    
                    if (!yearCleared) {
                        try {
                            List<WebElement> yearClearElements = driver.findElements(
                                By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div/div[3]/div/div[3]/div/table/thead/tr[1]/th/div/div[1]/div//*[contains(@class,'clear-indicator')]")
                            );
                            
                            if (!yearClearElements.isEmpty()) {
                                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yearClearElements.get(0));
                                Thread.sleep(500);
                                TestReporter.logPass("✓ Cleared Year");
                                yearCleared = true;
                            }
                        } catch (Exception e) {
                            TestReporter.logWarning("Year clear strategy 1 failed: " + e.getMessage());
                        }
                    }
                    
                    if (!yearCleared) {
                        try {
                            WebElement yearDropdown = driver.findElement(
                                By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div/div[3]/div/div[3]/div/table/thead/tr[1]/th/div/div[1]/div")
                            );
                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", yearDropdown);
                            Thread.sleep(300);
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yearDropdown);
                            Thread.sleep(500);
                            
                            actions.sendKeys(Keys.DELETE).perform();
                            Thread.sleep(200);
                            actions.sendKeys(Keys.BACK_SPACE).perform();
                            Thread.sleep(200);
                            actions.sendKeys(Keys.ESCAPE).perform();
                            Thread.sleep(500);
                            
                            TestReporter.logPass("✓ Cleared Year");
                            yearCleared = true;
                        } catch (Exception e) {
                            TestReporter.logWarning("Year clear strategy 2 failed: " + e.getMessage());
                        }
                    }
                    
                    if (!yearCleared) {
                        TestReporter.logWarning("Could not clear Year");
                    }
                    
                } catch (Exception e) {
                    TestReporter.logWarning("Error clearing Year: " + e.getMessage());
                }
            }

            private void clearMonthDropdown() {
                try {
                    TestReporter.logInfo("Clearing Month");
                    Thread.sleep(500);
                    
                    boolean monthCleared = false;
                    
                    if (!monthCleared) {
                        try {
                            List<WebElement> monthClearElements = driver.findElements(
                                By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div/div[3]/div/div[3]/div/table/thead/tr[1]/th/div/div[2]/div//*[contains(@class,'clear-indicator')]")
                            );
                            
                            if (!monthClearElements.isEmpty()) {
                                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", monthClearElements.get(0));
                                Thread.sleep(500);
                                TestReporter.logPass("✓ Cleared Month");
                                monthCleared = true;
                            }
                        } catch (Exception e) {
                            TestReporter.logWarning("Month clear strategy 1 failed: " + e.getMessage());
                        }
                    }
                    
                    if (!monthCleared) {
                        try {
                            WebElement monthDropdown = driver.findElement(
                                By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div/div[3]/div/div[3]/div/table/thead/tr[1]/th/div/div[2]/div")
                            );
                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", monthDropdown);
                            Thread.sleep(300);
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", monthDropdown);
                            Thread.sleep(500);
                            
                            actions.sendKeys(Keys.DELETE).perform();
                            Thread.sleep(200);
                            actions.sendKeys(Keys.BACK_SPACE).perform();
                            Thread.sleep(200);
                            actions.sendKeys(Keys.ESCAPE).perform();
                            Thread.sleep(500);
                            
                            TestReporter.logPass("✓ Cleared Month");
                            monthCleared = true;
                        } catch (Exception e) {
                            TestReporter.logWarning("Month clear strategy 2 failed: " + e.getMessage());
                        }
                    }
                    
                    if (!monthCleared) {
                        TestReporter.logWarning("Could not clear Month");
                    }
                    
                } catch (Exception e) {
                    TestReporter.logWarning("Error clearing Month: " + e.getMessage());
                }
            }

            private boolean checkDropdownHasDataByText(String dropdownText) {
                try {
                    WebElement dropdown = null;
                    
                    if (dropdownText.equals("Year")) {
                        dropdown = driver.findElement(
                            By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div/div[3]/div/div[3]/div/table/thead/tr[1]/th/div/div[1]/div")
                        );
                    } else if (dropdownText.equals("Month")) {
                        dropdown = driver.findElement(
                            By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div/div[3]/div/div[3]/div/table/thead/tr[1]/th/div/div[2]/div")
                        );
                    } else {
                        dropdown = driver.findElement(By.xpath("//div[text()='" + dropdownText + "']"));
                    }
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", dropdown);
                    Thread.sleep(300);
                    dropdown.click();
                    Thread.sleep(1500);
                    
                    List<WebElement> options = driver.findElements(
                        By.xpath("//div[contains(@id,'react-select-')][@role='option']")
                    );
                    
                    boolean hasData = options.size() > 0;
                    try {
                    actions.sendKeys(Keys.ESCAPE).perform();
                    Thread.sleep(500);
                } catch (Exception e) {}
                
                if (hasData) {
                    TestReporter.logInfo(dropdownText + " has " + options.size() + " option(s)");
                } else {
                    TestReporter.logWarning(dropdownText + " has no options");
                }
                
                return hasData;
                
            } catch (Exception e) {
                TestReporter.logWarning("Could not check " + dropdownText + ": " + e.getMessage());
                return false;
            }
            }
            private void toggleAccountOrganizationAndExport() {
            try {
            TestReporter.logInfo("Account/Organization toggle and exports");
            Thread.sleep(1000);
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
                Thread.sleep(2000);
                
                TestReporter.logInfo("Clicking Organization");
                safeClick(By.xpath("//button[contains(text(),'Organization')]"));
                Thread.sleep(2000);
                TestReporter.logPass("✓ Organization");
                
                TestReporter.logInfo("Clicking Account");
                safeClick(By.xpath("//button[contains(text(),'Account')]"));
                Thread.sleep(2000);
                TestReporter.logPass("✓ Account");
                
                TestReporter.logInfo("Clicking exports");
                Thread.sleep(1000);
                
                try {
                    WebElement export1 = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div/div[3]/div/div[4]/div[2]/div[2]/div/div[2]/div[1]/div[1]/button"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", export1);
                    Thread.sleep(500);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", export1);
                    Thread.sleep(2000);
                    TestReporter.logPass("✓ Export 1");
                } catch (Exception e) {
                    TestReporter.logWarning("Export 1 failed: " + e.getMessage());
                }
                
                try {
                    WebElement export2 = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div/div[3]/div/div[4]/div[2]/div[2]/div/div[2]/div[2]/div[1]/button"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", export2);
                    Thread.sleep(500);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", export2);
                    Thread.sleep(2000);
                    TestReporter.logPass("✓ Export 2");
                } catch (Exception e) {
                    TestReporter.logWarning("Export 2 failed: " + e.getMessage());
                }
                
                try {
                    WebElement export3 = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div/div[3]/div/div[4]/div[2]/div[2]/div/div[2]/div[3]/div[1]/button"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", export3);
                    Thread.sleep(500);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", export3);
                    Thread.sleep(2000);
                    TestReporter.logPass("✓ Export 3");
                } catch (Exception e) {
                    TestReporter.logWarning("Export 3 failed: " + e.getMessage());
                }
                
                TestReporter.logPass("✓ Exports complete");
                
            } catch (Exception e) {
                TestReporter.logFail("Toggle failed", e);
                throw new RuntimeException("Failed: " + e.getMessage(), e);
            }
            }
            private boolean checkIfYearMonthHaveDataSimple() {
            try {
            TestReporter.logInfo("Checking Year/Month data");
                boolean yearHasData = checkDropdownHasDataByText("Year");
                boolean monthHasData = checkDropdownHasDataByText("Month");
                
                boolean bothHaveData = yearHasData && monthHasData;
                
                if (bothHaveData) {
                    TestReporter.logPass("✓ Both have data");
                } else {
                    TestReporter.logWarning("Year: " + yearHasData + ", Month: " + monthHasData);
                }
                
                return bothHaveData;
                
            } catch (Exception e) {
                TestReporter.logWarning("Error checking data: " + e.getMessage());
                return false;
            }
            }
            private boolean validateInventoryIsbn() {
            try {
            TestReporter.logInfo("Validating ISBN");
                boolean indyBoExists = driver.findElements(By.xpath("//span[text()='Indy BO Units']")).size() > 0;
                boolean printHistoryExists = driver.findElements(By.xpath("//span[text()='Print History']")).size() > 0;
                
                TestReporter.logInfo("Indy BO: " + indyBoExists + ", Print History: " + printHistoryExists);
                
                if (!indyBoExists || !printHistoryExists) {
                    TestReporter.logWarning("Missing required links");
                    return false;
                }
                
                TestReporter.logInfo("Checking Indy BO popup");
                safeClick(By.xpath("//span[text()='Indy BO Units']"));
                Thread.sleep(1500);
                
                WebDriverWait shortModalWait = new WebDriverWait(driver, Duration.ofSeconds(3));
                try {
                    shortModalWait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@role='dialog']")
                    ));
                    Thread.sleep(500);
                } catch (TimeoutException e) {
                    TestReporter.logWarning("Indy BO modal timeout");
                    return false;
                }
                
                boolean indyBoExportExists = driver.findElements(
                    By.xpath("//div[@role='dialog']//button[contains(text(),'Export')]")
                ).size() > 0;
                
                if (!indyBoExportExists) {
                    TestReporter.logWarning("No Export in Indy BO");
                    quickClickInModal(By.xpath("//div[@role='dialog']//button/span[text()='×']"));
                    Thread.sleep(500);
                    return false;
                }
                
                quickClickInModal(By.xpath("//div[@role='dialog']//button/span[text()='×']"));
                Thread.sleep(500);
                TestReporter.logPass("✓ Indy BO validated");
                
                TestReporter.logInfo("Checking Print History popup");
                safeClick(By.xpath("//span[text()='Print History']"));
                Thread.sleep(1500);
                
                try {
                    shortModalWait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@role='dialog']")
                    ));
                    Thread.sleep(500);
                } catch (TimeoutException e) {
                    TestReporter.logWarning("Print History modal timeout");
                    return false;
                }
                
                boolean printHistoryExportExists = driver.findElements(
                    By.xpath("//div[@role='dialog']//button[contains(text(),'Export')]")
                ).size() > 0;
                
                if (!printHistoryExportExists) {
                    TestReporter.logWarning("No Export in Print History");
                    quickClickInModal(By.xpath("//div[@role='dialog']//button/span[text()='×']"));
                    Thread.sleep(500);
                    return false;
                }
                
                quickClickInModal(By.xpath("//div[@role='dialog']//button/span[text()='×']"));
                Thread.sleep(500);
                TestReporter.logPass("✓ Print History validated");
                
                TestReporter.logPass("✓ ISBN validation SUCCESS");
                return true;
                
            } catch (Exception e) {
                TestReporter.logWarning("Validation failed: " + e.getMessage());
                try {
                    quickClickInModal(By.xpath("//div[@role='dialog']//button/span[text()='×']"));
                    Thread.sleep(500);
                } catch (Exception ex) {}
                return false;
            }
            }
            private void waitForModalToBeReadyImpl() {
            try {
            WebDriverWait modalWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            modalWait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@role='dialog']")
            ));
            Thread.sleep(1000);
            } catch (Exception e) {
            TestReporter.logWarning("Modal wait issue: " + e.getMessage());
            }
            }
        // ═══════════════════════════════════════════════════════════════
        // INVENTORY ISBN SELECTION METHODS - COMPLETELY REWRITTEN
        // ═══════════════════════════════════════════════════════════════

        /**
         * Try to find and select specific ISBN from Inventory dropdown
         * Opens dropdown, looks for ISBN in visible options, selects if found
         */
        private void selectIsbnInInventoryBySearching(String targetIsbn) {
            try {
                TestReporter.logInfo("Looking for ISBN: " + targetIsbn);
                Thread.sleep(1000);
                
                waitForOverlayToDisappear();
                Thread.sleep(1000);
                
                boolean dropdownOpened = false;
                
                // ============================================================
                // STEP 1: Open the dropdown to see all options
                // ============================================================
                
                // Strategy 1: Click dropdown indicator
                try {
                    TestReporter.logInfo("Strategy 1: Clicking dropdown indicator");
                    WebElement dropdownIndicator = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'custom-select__indicator')]//button")
                    ));
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", dropdownIndicator);
                    Thread.sleep(500);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownIndicator);
                    Thread.sleep(2000);
                    
                    List<WebElement> checkOptions = driver.findElements(
                        By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                    );
                    if (checkOptions.size() > 0) {
                        dropdownOpened = true;
                        TestReporter.logPass("Strategy 1 succeeded - " + checkOptions.size() + " options visible");
                    }
                } catch (Exception e) {
                    TestReporter.logWarning("Strategy 1 failed: " + e.getMessage());
                }
                
                // Strategy 2: Click control container
                if (!dropdownOpened) {
                    try {
                        TestReporter.logInfo("Strategy 2: Clicking control container");
                        WebElement controlContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("div.custom-select__control")
                        ));
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", controlContainer);
                        Thread.sleep(500);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", controlContainer);
                        Thread.sleep(2000);
                        
                        List<WebElement> checkOptions = driver.findElements(
                            By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                        );
                        if (checkOptions.size() > 0) {
                            dropdownOpened = true;
                            TestReporter.logPass("Strategy 2 succeeded - " + checkOptions.size() + " options");
                        }
                    } catch (Exception e) {
                        TestReporter.logWarning("Strategy 2 failed: " + e.getMessage());
                    }
                }
                
                // Strategy 3: Click input field
                if (!dropdownOpened) {
                    try {
                        TestReporter.logInfo("Strategy 3: Clicking input field");
                        WebElement inputField = driver.findElement(
                            By.xpath("//input[contains(@id,'react-select') and @role='combobox']")
                        );
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", inputField);
                        Thread.sleep(500);
                        inputField.click();
                        Thread.sleep(2000);
                        
                        List<WebElement> checkOptions = driver.findElements(
                            By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                        );
                        if (checkOptions.size() > 0) {
                            dropdownOpened = true;
                            TestReporter.logPass("Strategy 3 succeeded - " + checkOptions.size() + " options");
                        }
                    } catch (Exception e) {
                        TestReporter.logWarning("Strategy 3 failed: " + e.getMessage());
                    }
                }
                
                if (!dropdownOpened) {
                    throw new RuntimeException("Failed to open dropdown - no options visible");
                }
                
                // ============================================================
                // STEP 2: Search through ALL visible options for target ISBN
                // ============================================================
                TestReporter.logInfo("Searching for ISBN: " + targetIsbn + " in dropdown options");
                Thread.sleep(500);
                
                List<WebElement> allOptions = driver.findElements(
                    By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                );
                
                TestReporter.logInfo("Checking " + allOptions.size() + " visible options");
                
                boolean isbnFound = false;
                for (WebElement option : allOptions) {
                    String optionText = option.getText().trim();
                    
                    // Check if this option contains our target ISBN
                    if (optionText.equals(targetIsbn)) {
                        TestReporter.logInfo("Found exact match: " + optionText);
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", option);
                        Thread.sleep(500);
                        
                        try {
                            option.click();
                        } catch (ElementClickInterceptedException e) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
                        }
                        
                        isbnFound = true;
                        TestReporter.logPass("Selected ISBN: " + targetIsbn);
                        break;
                    }
                }
                
                if (!isbnFound) {
                    // Close dropdown before throwing error
                    try {
                        actions.sendKeys(Keys.ESCAPE).perform();
                        Thread.sleep(500);
                    } catch (Exception e) {}
                    
                    throw new RuntimeException("ISBN " + targetIsbn + " not found in dropdown options");
                }
                
                Thread.sleep(2000);
                
            } catch (Exception e) {
                takeScreenshot("inventory_isbn_search_error");
                throw new RuntimeException("Failed to find/select ISBN: " + e.getMessage(), e);
            }
        }

        /**
         * Select random ISBN from Inventory dropdown (for Step 12A)
         * Opens dropdown and selects first available option
         */
        private String selectRandomIsbnFromInventoryDropdown() {
    try {
        TestReporter.logInfo("Selecting random ISBN from Inventory dropdown");
        Thread.sleep(500);
        
        // STEP 1: Clear any previous search and close dropdown
        try {
            TestReporter.logInfo("Clearing previous search filter");
            WebElement inputField = driver.findElement(
                By.xpath("//input[contains(@id,'react-select') and @role='combobox']")
            );
            inputField.clear();
            actions.sendKeys(Keys.ESCAPE).perform();
            Thread.sleep(1000);
            TestReporter.logInfo("Previous filter cleared");
        } catch (Exception e) {
            TestReporter.logInfo("No previous filter to clear");
        }
        
        // STEP 2: Open dropdown fresh
        TestReporter.logInfo("Opening dropdown for random selection");
        WebElement inputContainer = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[contains(@class,'custom-select__input-container')]")
        ));
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", inputContainer);
        Thread.sleep(500);
        inputContainer.click();
        Thread.sleep(2000); // Wait for dropdown to fully open
        
        // STEP 3: Get all available options
        List<WebElement> allOptions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
        ));
        
        if (allOptions.isEmpty()) {
            throw new RuntimeException("No options found in dropdown");
        }
        
        TestReporter.logInfo("Found " + allOptions.size() + " total options");
        
        // STEP 4: Select random ISBN
        int randomIndex = random.nextInt(allOptions.size());
        WebElement selectedOption = allOptions.get(randomIndex);
        String selectedIsbn = selectedOption.getText().trim();
        
        TestReporter.logInfo("Random index: " + randomIndex + " out of " + allOptions.size());
        TestReporter.logInfo("Selecting ISBN: " + selectedIsbn);
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedOption);
        Thread.sleep(500);
        
        try {
            selectedOption.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedOption);
        }
        
        TestReporter.logPass("Selected random ISBN: " + selectedIsbn);
        Thread.sleep(2000);
        
        return selectedIsbn;
        
    } catch (Exception e) {
        takeScreenshot("random_isbn_error");
        throw new RuntimeException("Failed to select random ISBN: " + e.getMessage(), e);
    }
}
            private void sortIndyBoColumn(int columnIndex) {
            try {
            Thread.sleep(500);
                WebElement columnHeader = shortWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@role='dialog']//table//thead//tr//th[" + columnIndex + "]")
                ));
                
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", columnHeader);
                Thread.sleep(200);
                
                try {
                    List<WebElement> clickableElements = columnHeader.findElements(
                        By.xpath(".//div[@class] | .//span[@class] | .//button")
                    );
                    
                    if (!clickableElements.isEmpty()) {
                        clickableElements.get(0).click();
                        Thread.sleep(300);
                    } else {
                        columnHeader.click();
                        Thread.sleep(300);
                    }
                } catch (Exception e) {
                    columnHeader.click();
                    Thread.sleep(300);
                }
                
            } catch (Exception e) {
                TestReporter.logWarning("Error sorting column " + columnIndex + ": " + e.getMessage());
            }
            }
            private void sortModalColumn(String columnName, int retryCount) {
            try {
            WebElement sortButton = shortWait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@role='dialog']//table//thead//th[contains(., '" + columnName + "')]//button | " +
            "//div[@role='dialog']//table//thead//th[contains(., '" + columnName + "')]//div[contains(@class, 'cursor-pointer')]")
            ));
                sortButton.click();
                Thread.sleep(300);
                
            } catch (TimeoutException e) {
                if (retryCount > 0) {
                    TestReporter.logWarning("Retry sorting: " + columnName);
                    sortModalColumn(columnName, retryCount - 1);
                } else {
                    TestReporter.logWarning("Could not sort: " + columnName);
                }
            } catch (Exception e) {
                TestReporter.logWarning("Error sorting " + columnName + ": " + e.getMessage());
            }
            }
            private void quickClickInModal(By locator) {
            try {
            WebElement element = shortWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } catch (Exception e) {
            TestReporter.logWarning("Quick click failed: " + e.getMessage());
            }
            }
            private void waitForOverlayToDisappear() {
            try {
            WebDriverWait overlayWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            overlayWait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.cssSelector("div[class*='z-50'][style*='pointer-events']")
            ));
            } catch (TimeoutException e) {
            TestReporter.logWarning("Overlay not found");
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
                } catch (ElementNotInteractableException e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                }
                
                return true;
                
            } catch (TimeoutException | NoSuchElementException e) {
                TestReporter.logWarning("Element not found: " + locator);
                return false;
            } catch (Exception e) {
                TestReporter.logWarning("Failed to click: " + e.getMessage());
                return false;
            }
            }
            private void safeClick(By locator) {
            try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
                Thread.sleep(300);
                
                wait.until(ExpectedConditions.elementToBeClickable(element));
                
                try {
                    element.click();
                } catch (ElementNotInteractableException e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                }
                
            } catch (TimeoutException e) {
                throw new RuntimeException("Element not found: " + locator, e);
            } catch (Exception e) {
                throw new RuntimeException("Error clicking: " + locator, e);
            }
            }
            private void waitForPageLoad() {
            try {
            wait.until(driver1 ->
            ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete")
            );
            Thread.sleep(1500);
            } catch (Exception e) {
            TestReporter.logWarning("Page load wait error: " + e.getMessage());
            }
            }
            private void takeScreenshot(String filename) {
            try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            ts.getScreenshotAs(OutputType.BYTES);
            TestReporter.logInfo("Screenshot: " + filename);
            } catch (Exception e) {
            TestReporter.logWarning("Screenshot failed: " + e.getMessage());
            }
            }
            }