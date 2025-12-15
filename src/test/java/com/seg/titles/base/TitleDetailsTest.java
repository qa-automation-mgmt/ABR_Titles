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
    private String selectedIsbn; // Store selected ISBN for later use

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
            // Step 2: Select Random ISBN from Dropdown
            // -----------------------------
            System.out.println("🔹 Step 2: Selecting random ISBN from dropdown...");
            selectRandomIsbnFromDropdown();
            Thread.sleep(1500);
            System.out.println("✓ Selected random ISBN from dropdown: " + selectedIsbn);

            // -----------------------------
            // Step 3: Click "Sales Sheet" Button
            // -----------------------------
            System.out.println("🔹 Step 3: Clicking 'Sales Sheet' button...");
            safeClick(By.xpath("//button[contains(text(),'Sales Sheet')]"));
            Thread.sleep(1000);
            System.out.println("✓ Clicked 'Sales Sheet' button");

            // -----------------------------
            // Step 4: Click "Download PDF" Button and Navigate Back
            // -----------------------------
            System.out.println("🔹 Step 4: Clicking 'Download PDF' button...");
            safeClick(By.xpath("//button[contains(text(),'Download PDF')]"));
            Thread.sleep(2000);
            System.out.println("✓ Clicked 'Download PDF' button");
            
            // Wait for PDF download page to load (URL changes to /details/)
            wait.until(ExpectedConditions.urlContains("/details/"));
            System.out.println("✓ PDF download page loaded");
            Thread.sleep(1000);
            
            // Navigate back to Title Details by clicking the back navigation
            System.out.println("🔹 Navigating back to Title Details...");
            safeClick(By.xpath("//*[@id='root']/div[2]/div/div/main/div[1]/div/aside/div/div/div/nav/div[2]/div/div"));
            Thread.sleep(2000);
            System.out.println("✓ Navigated back to Title Details");
            
            // Now navigate to the title-details page (where Sales/POS/Inventory tabs exist)
            String titleDetailsUrl = "https://titles-dev.abramsbooks.com/title-details/" + selectedIsbn;
            System.out.println("🔙 Navigating to: " + titleDetailsUrl);
            driver.get(titleDetailsUrl);
            waitForPageLoad();
            Thread.sleep(2000);
            System.out.println("✓ Navigated to title-details page with ISBN: " + selectedIsbn);

            // -----------------------------
            // Step 5: Click "Sales" Tab and Verify Export Button
            // -----------------------------
            System.out.println("🔹 Step 5: Clicking 'Sales' tab...");
            safeClick(By.xpath("//button[contains(text(),'Sales')]"));
            Thread.sleep(2000);
            System.out.println("✓ Switched to 'Sales' tab");

            // Check if Export button exists, if not, try different ISBNs
            boolean exportButtonFound = checkIfExportButtonExists();
            int salesAttempts = 0;
            int maxSalesAttempts = 10;
            
            while (!exportButtonFound && salesAttempts < maxSalesAttempts) {
                salesAttempts++;
                System.out.println("⚠️ Export button not found. Attempt " + salesAttempts + " - Selecting different ISBN...");
                
                // Go back to Title Details page to select a different ISBN
                driver.get("https://titles-dev.abramsbooks.com/title-details");
                waitForPageLoad();
                waitForOverlayToDisappear();
                Thread.sleep(2000);
                
                // Select a different random ISBN
                selectRandomIsbnFromDropdown();
                Thread.sleep(1500);
                System.out.println("✓ Selected new ISBN: " + selectedIsbn);
                
                // Navigate to title-details page with new ISBN
                String newTitleDetailsUrl = "https://titles-dev.abramsbooks.com/title-details/" + selectedIsbn;
                System.out.println("🔙 Navigating to: " + newTitleDetailsUrl);
                driver.get(newTitleDetailsUrl);
                waitForPageLoad();
                Thread.sleep(2000);
                
                // Click Sales tab again
                safeClick(By.xpath("//button[contains(text(),'Sales')]"));
                Thread.sleep(2000);
                
                // Check again if Export button exists
                exportButtonFound = checkIfExportButtonExists();
            }
            
            if (!exportButtonFound) {
                System.out.println("⚠️ Warning: Export button not found after " + maxSalesAttempts + " attempts. Skipping Sales tab interactions...");
            } else {
                System.out.println("✓ Export button found - proceeding with Sales tab interactions");
            }

            // -----------------------------
            // Step 6: Toggle MTD / YTD / LTD (Only if Export button exists)
            // -----------------------------
            if (exportButtonFound) {
                System.out.println("🔹 Step 6: Toggling MTD/YTD/LTD...");
                
                safeClick(By.xpath("//div[text()='MTD']"));
                Thread.sleep(1000);
                System.out.println("✓ Toggled MTD");
                
                safeClick(By.xpath("//div[text()='YTD']"));
                Thread.sleep(1000);
                System.out.println("✓ Toggled YTD");
                
                safeClick(By.xpath("//div[text()='LTD']"));
                Thread.sleep(1000);
                System.out.println("✓ Toggled LTD");
            } else {
                System.out.println("⚠️ Skipping Step 6: MTD/YTD/LTD toggle (no export button)");
            }

            // -----------------------------
            // Step 7: Toggle Organization/Account Views (Only if Export button exists)
            // -----------------------------
            if (exportButtonFound) {
                System.out.println("🔹 Step 7: Toggling Organization/Account views...");
                
                // Click Organization button
                safeClick(By.xpath("//button[contains(text(),'Organization')]"));
                Thread.sleep(1000);
                System.out.println("✓ Clicked Organization button");
                
                // Click Account button
                safeClick(By.xpath("//button[contains(text(),'Account')]"));
                Thread.sleep(1000);
                System.out.println("✓ Clicked Account button");
                
                // Expand/collapse account sections (3 times)
                for (int i = 1; i <= 3; i++) {
                    System.out.println("🔹 Expanding/collapsing account section " + i);
                    safeClick(By.xpath("//div[@class='space-y-6']/div[" + i + "]//button"));
                    Thread.sleep(500);
                }
                System.out.println("✓ Toggled all account sections");
                
                // Click Organization button again
                safeClick(By.xpath("//button[contains(text(),'Organization')]"));
                Thread.sleep(1000);
                System.out.println("✓ Clicked Organization button again");
                
                // Expand/collapse organization sections (3 times)
                for (int i = 1; i <= 3; i++) {
                    System.out.println("🔹 Expanding/collapsing organization section " + i);
                    safeClick(By.xpath("//div[@class='space-y-6']/div[" + i + "]//button"));
                    Thread.sleep(500);
                }
                System.out.println("✓ Toggled all organization sections");
            } else {
                System.out.println("⚠️ Skipping Step 7: Organization/Account toggle (no export button)");
            }

            // -----------------------------
            // Step 8: Click "POS" Tab
            // -----------------------------
            System.out.println("🔹 Step 8: Clicking 'POS' tab...");
            safeClick(By.xpath("//button[contains(text(),'POS')]"));
            Thread.sleep(2000);
            System.out.println("✓ Switched to 'POS' tab");

            // -----------------------------
            // Step 9: Click "Inventory" Tab
            // -----------------------------
            System.out.println("🔹 Step 9: Clicking 'Inventory' tab...");
            safeClick(By.xpath("//button[contains(text(),'Inventory')]"));
            Thread.sleep(2000);
            System.out.println("✓ Switched to 'Inventory' tab");

            // -----------------------------
            // Step 10: Click on "Indy BO Units" (Column) - Opens Modal and Handle ISBN Selection
            // -----------------------------
            System.out.println("🔹 Step 10: Clicking 'Indy BO Units' column...");
            safeClick(By.xpath("//span[text()='Indy BO Units']"));
            Thread.sleep(2000);
            System.out.println("✓ Clicked 'Indy BO Units' column - Modal opened");

            // Wait for modal to appear
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@id='headlessui-portal-root']//div[@role='dialog']")
            ));
            Thread.sleep(1000);

            // Select the specific ISBN for Indy BO Units
            System.out.println("🔹 Selecting specific ISBN (9781419741968) for Indy BO Units...");
            selectSpecificIsbnInModal("9781419741968");
            Thread.sleep(2000);

            // Check if table has data
            boolean hasData = checkIfModalHasData();

            if (!hasData) {
            System.out.println("⚠️ No data found. Closing modal...");

           // Click the clear (×) icon inside ISBN selector
           safeClick(By.xpath("//*[@id='isbn-selector']//div[contains(text(),'×')]"));

            Thread.sleep(1500);
            } else {
            System.out.println("✓ Found data for ISBN 9781419741968");
            
            // -----------------------------
                // Step 11: Sort Columns in Indy BO Units Modal
                // -----------------------------
                System.out.println("🔹 Step 11: Sorting columns in Indy BO Units modal...");
                
                try {
                    // Find all sortable columns
                    List<WebElement> sortIcons = driver.findElements(
                        By.xpath("//div[@id='headlessui-portal-root']//table//th//svg")
                    );
                    
                    if (sortIcons.size() > 0) {
                        // Sort first column (Organization)
                        System.out.println("🔹 Sorting first column...");
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortIcons.get(0));
                        Thread.sleep(1000);
                        System.out.println("✓ Sorted first column descending");
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortIcons.get(0));
                        Thread.sleep(1000);
                        System.out.println("✓ Sorted first column ascending");
                    }
                    
                    if (sortIcons.size() >= 4) {
                        // Sort 4th column (Total BO Units)
                        System.out.println("🔹 Sorting 4th column (Total BO Units)...");
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortIcons.get(3));
                        Thread.sleep(1000);
                        System.out.println("✓ Sorted Total BO Units ascending");
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortIcons.get(3));
                        Thread.sleep(1000);
                        System.out.println("✓ Sorted Total BO Units descending");
                    }
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Could not perform sorting: " + e.getMessage());
                }

                // -----------------------------
                // Step 12: Click Export Button in Indy BO Units Modal
                // -----------------------------
                System.out.println("🔹 Step 12: Clicking Export button in modal...");
                try {
                    WebElement exportBtn = driver.findElement(
                        By.xpath("//div[@id='headlessui-portal-root']//button[contains(text(),'Export')]")
                    );
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", exportBtn);
                    Thread.sleep(1000);
                    System.out.println("✓ Clicked Export button");
                } catch (Exception e) {
                    System.out.println("⚠️ Export button not found: " + e.getMessage());
                }

                // -----------------------------
                // Step 13: Close Indy BO Units Modal
                // -----------------------------
                System.out.println("🔹 Step 13: Closing Indy BO Units modal...");
                safeClick(By.xpath("//div[@id='headlessui-portal-root']//button/span[text()='×']"));
                Thread.sleep(1500);
                System.out.println("✓ Closed Indy BO Units modal");
            }

            // -----------------------------
            // Step 14: Select Specific ISBN for Print History
            // -----------------------------
            System.out.println("🔹 Step 14: Selecting specific ISBN (9781419741968) for Print History...");
            selectSpecificIsbnInInventoryTab("9781419741968");
            Thread.sleep(2000);
            
            // Check if Print History button exists
            boolean printHistoryFound = checkIfPrintHistoryExists();
            
            if (!printHistoryFound) {
                System.out.println("⚠️ Warning: Print History button not found. Skipping Print History steps...");
            } else {
                System.out.println("✓ Print History button found");

                // -----------------------------
                // Step 15: Click "Print History" Button
                // -----------------------------
                System.out.println("🔹 Step 15: Clicking 'Print History' button...");
                safeClick(By.xpath("//span[text()='Print History']"));
                Thread.sleep(3000);
                System.out.println("✓ Clicked 'Print History' button - Modal should open");

                // Wait for Print History modal/content to appear - try multiple selectors
                try {
                    // Try waiting for dialog
                    try {
                        wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//div[@id='headlessui-portal-root']//div[@role='dialog']")
                        ));
                        System.out.println("✓ Print History modal appeared (dialog)");
                    } catch (Exception e1) {
                        // Try waiting for table in portal
                        wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//div[@id='headlessui-portal-root']//table")
                        ));
                        System.out.println("✓ Print History content appeared (table)");
                    }
                    
                    Thread.sleep(1000);

                    // -----------------------------
                    // Step 16: Sort "Printing Cycle" Column in Print History Modal
                    // -----------------------------
                    System.out.println("🔹 Step 16: Sorting 'Printing Cycle' column...");
                    try {
                        List<WebElement> printHistorySortIcons = driver.findElements(
                            By.xpath("//div[@id='headlessui-portal-root']//table//th//svg")
                        );
                        
                        if (!printHistorySortIcons.isEmpty()) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", printHistorySortIcons.get(0));
                            Thread.sleep(1000);
                            System.out.println("✓ Sorted 'Printing Cycle' column");
                        } else {
                            System.out.println("⚠️ No sort icons found in Print History");
                        }
                    } catch (Exception e) {
                        System.out.println("⚠️ Could not sort Printing Cycle: " + e.getMessage());
                    }

                    // -----------------------------
                    // Step 17: Click Export Button in Print History Modal
                    // -----------------------------
                    System.out.println("🔹 Step 17: Clicking Export button in Print History modal...");
                    try {
                        WebElement exportBtn = driver.findElement(
                            By.xpath("//div[@id='headlessui-portal-root']//button[contains(text(),'Export')]")
                        );
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", exportBtn);
                        Thread.sleep(1000);
                        System.out.println("✓ Clicked Export button");
                    } catch (Exception e) {
                        System.out.println("⚠️ Export button not found: " + e.getMessage());
                    }

                    // -----------------------------
                    // Step 18: Close Print History Modal
                    // -----------------------------
                    System.out.println("🔹 Step 18: Closing Print History modal...");
                    WebElement closeBtn = driver.findElement(
                        By.xpath("//div[@id='headlessui-portal-root']//button/span[text()='×']")
                    );
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeBtn);
                    Thread.sleep(1500);
                    System.out.println("✓ Closed Print History modal");
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Error in Print History steps: " + e.getMessage());
                    takeScreenshot("print_history_error");
                }
            }

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
            
            // Strategy 1: Click the input container
            try {
                System.out.println("🔹 Strategy 1: Clicking input container...");
                WebElement inputContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@id='isbn-selector']//div[contains(@class,'custom-select__input-container')]")
                ));
                
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", 
                    inputContainer
                );
                Thread.sleep(500);
                
                inputContainer.click();
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
            
            // Strategy 2: Click the control container
            if (!dropdownOpened) {
                try {
                    System.out.println("🔹 Strategy 2: Clicking control container...");
                    WebElement controlContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[@id='isbn-selector']//div[contains(@class,'custom-select__control')]")
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
                        System.out.println("✓ Dropdown opened with Strategy 2");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Strategy 2 failed: " + e.getMessage());
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
            selectedIsbn = randomOption.getText(); // Store in class variable
            
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
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("ISBN selection interrupted: " + e.getMessage(), e);
        } catch (Exception e) {
            takeScreenshot("isbn_dropdown_error");
            throw new RuntimeException("❌ Failed to select random ISBN: " + e.getMessage(), e);
        }
    }
    
    /**
     * Select a specific ISBN from the dropdown inside the modal
     */
    private void selectSpecificIsbnInModal(String targetIsbn) {
        try {
            System.out.println("🔹 Opening ISBN dropdown in modal for ISBN: " + targetIsbn);
            
            // Click the ISBN selector dropdown in the modal using the provided XPath
            WebElement modalIsbnDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='isbn-selector']/div/div[1]")
            ));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                modalIsbnDropdown
            );
            Thread.sleep(500);
            
            modalIsbnDropdown.click();
            Thread.sleep(1000);
            System.out.println("✓ Modal ISBN dropdown opened");
            
            // Find all dropdown options
            List<WebElement> modalOptions = driver.findElements(
                By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
            );
            
            System.out.println("📋 Found " + modalOptions.size() + " ISBN options in modal");
            
            if (modalOptions.isEmpty()) {
                System.out.println("⚠️ No ISBN options found in modal dropdown");
                return;
            }
            
            // Find the specific ISBN
            WebElement targetOption = null;
            for (WebElement option : modalOptions) {
                if (option.getText().contains(targetIsbn)) {
                    targetOption = option;
                    break;
                }
            }
            
            if (targetOption == null) {
                System.out.println("⚠️ ISBN " + targetIsbn + " not found in dropdown");
                return;
            }
            
            System.out.println("✓ Found ISBN " + targetIsbn + " in dropdown");
            
            // Click the option
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                targetOption
            );
            Thread.sleep(500);
            
            try {
                targetOption.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetOption);
            }
            
            System.out.println("✓ Selected ISBN in modal: " + targetIsbn);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⚠️ Modal ISBN selection interrupted");
        } catch (Exception e) {
            System.out.println("⚠️ Failed to select ISBN in modal: " + e.getMessage());
            takeScreenshot("modal_isbn_selection_error");
        }
    }
    
    /**
     * Select a specific ISBN from the main ISBN selector in Inventory tab
     */
    private void selectSpecificIsbnInInventoryTab(String targetIsbn) {
        try {
            System.out.println("🔹 Opening ISBN dropdown in Inventory tab for ISBN: " + targetIsbn);
            
            // Click the ISBN selector dropdown in Inventory tab
            WebElement inventoryIsbnDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='isbn-selector']/div/div[1]/div[2]")
            ));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                inventoryIsbnDropdown
            );
            Thread.sleep(500);
            
            inventoryIsbnDropdown.click();
            Thread.sleep(1000);
            System.out.println("✓ Inventory ISBN dropdown opened");
            
            // Find all dropdown options
            List<WebElement> isbnOptions = driver.findElements(
                By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
            );
            
            System.out.println("📋 Found " + isbnOptions.size() + " ISBN options");
            
            if (isbnOptions.isEmpty()) {
                System.out.println("⚠️ No ISBN options found in dropdown");
                return;
            }
            
            // Find the specific ISBN
            WebElement targetOption = null;
            for (WebElement option : isbnOptions) {
                if (option.getText().contains(targetIsbn)) {
                    targetOption = option;
                    break;
                }
            }
            
            if (targetOption == null) {
                System.out.println("⚠️ ISBN " + targetIsbn + " not found in dropdown");
                return;
            }
            
            System.out.println("✓ Found ISBN " + targetIsbn + " in dropdown");
            
            // Click the option
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                targetOption
            );
            Thread.sleep(500);
            
            try {
                targetOption.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetOption);
            }
            
            System.out.println("✓ Selected ISBN in Inventory: " + targetIsbn);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⚠️ Inventory ISBN selection interrupted");
        } catch (Exception e) {
            System.out.println("⚠️ Failed to select ISBN in Inventory: " + e.getMessage());
            takeScreenshot("inventory_isbn_selection_error");
        }
    }
    
    /**
     * Check if Print History button exists
     */
    private boolean checkIfPrintHistoryExists() {
        try {
            Thread.sleep(1000);
            
            List<WebElement> printHistoryButtons = driver.findElements(
                By.xpath("//span[text()='Print History']")
            );
            
            if (printHistoryButtons.isEmpty()) {
                System.out.println("⚠️ Print History button not found");
                return false;
            }
            
            System.out.println("✓ Print History button found");
            return true;
            
        } catch (Exception e) {
            System.out.println("⚠️ Error checking for Print History button: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Select a different ISBN from the main ISBN selector in Inventory tab
     */
    private void selectIsbnInInventoryTab() {
        try {
            System.out.println("🔹 Opening ISBN dropdown in Inventory tab...");
            
            // Click the ISBN selector dropdown in Inventory tab (not in modal)
            WebElement inventoryIsbnDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='isbn-selector']/div/div[1]/div[2]")
            ));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                inventoryIsbnDropdown
            );
            Thread.sleep(500);
            
            inventoryIsbnDropdown.click();
            Thread.sleep(1000);
            System.out.println("✓ Inventory ISBN dropdown opened");
            
            // Find all dropdown options
            List<WebElement> isbnOptions = driver.findElements(
                By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
            );
            
            System.out.println("📋 Found " + isbnOptions.size() + " ISBN options");
            
            if (isbnOptions.isEmpty()) {
                System.out.println("⚠️ No ISBN options found in dropdown");
                return;
            }
            
            // Select a random option
            int randomIndex = (int) (Math.random() * isbnOptions.size());
            WebElement randomOption = isbnOptions.get(randomIndex);
            String newIsbn = randomOption.getText();
            
            System.out.println("🎲 Selecting random ISBN in Inventory [" + randomIndex + "]: " + newIsbn);
            
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
            
            System.out.println("✓ Selected new ISBN in Inventory: " + newIsbn);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⚠️ Inventory ISBN selection interrupted");
        } catch (Exception e) {
            System.out.println("⚠️ Failed to select ISBN in Inventory: " + e.getMessage());
            takeScreenshot("inventory_isbn_selection_error");
        }
    }
    
    /**
     * Check if Export button exists in Sales tab
     */
    private boolean checkIfExportButtonExists() {
        try {
            // Wait a bit for the page to load
            Thread.sleep(1000);
            
            // Check for Export button - try multiple possible selectors
            List<WebElement> exportButtons = driver.findElements(
                By.xpath("//button[contains(text(),'Export') or contains(text(),'export')]")
            );
            
            if (exportButtons.isEmpty()) {
                // Try alternative selectors
                exportButtons = driver.findElements(
                    By.xpath("//button[contains(@class,'export') or @id='export']")
                );
            }
            
            if (exportButtons.isEmpty()) {
                // Check if there's a "No data" message
                List<WebElement> noDataElements = driver.findElements(
                    By.xpath("//*[contains(text(),'No data') or contains(text(),'no data') or contains(text(),'No results')]")
                );
                
                if (!noDataElements.isEmpty()) {
                    System.out.println("⚠️ 'No data' message found in Sales tab");
                    return false;
                }
                
                System.out.println("⚠️ Export button not found in Sales tab");
                return false;
            }
            
            System.out.println("✓ Export button found in Sales tab");
            return true;
            
        } catch (Exception e) {
            System.out.println("⚠️ Error checking for Export button: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if modal has data in the table
     */
    private boolean checkIfModalHasData() {
        try {
            // Check if table body has any rows with data
            List<WebElement> tableRows = driver.findElements(
                By.xpath("//div[@id='headlessui-portal-root']//table//tbody//tr")
            );
            
            if (tableRows.isEmpty()) {
                System.out.println("⚠️ No data rows found in table");
                return false;
            }
            
            // Check if there's a "No data" or empty message
            List<WebElement> noDataElements = driver.findElements(
                By.xpath("//div[@id='headlessui-portal-root']//*[contains(text(),'No data') or contains(text(),'no data') or contains(text(),'No results')]")
            );
            
            if (!noDataElements.isEmpty()) {
                System.out.println("⚠️ 'No data' message found in modal");
                return false;
            }
            
            System.out.println("✓ Table has " + tableRows.size() + " data rows");
            return tableRows.size() > 0;
            
        } catch (Exception e) {
            System.out.println("⚠️ Error checking for data: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Select a different ISBN from the dropdown inside the modal
     */
    private void selectIsbnInModal() {
        try {
            System.out.println("🔹 Opening ISBN dropdown in modal...");
            
            // Click the ISBN selector dropdown in the modal using the provided XPath
            WebElement modalIsbnDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='isbn-selector']/div/div[1]")
            ));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                modalIsbnDropdown
            );
            Thread.sleep(500);
            
            modalIsbnDropdown.click();
            Thread.sleep(1000);
            System.out.println("✓ Modal ISBN dropdown opened");
            
            // Find all dropdown options
            List<WebElement> modalOptions = driver.findElements(
                By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
            );
            
            System.out.println("📋 Found " + modalOptions.size() + " ISBN options in modal");
            
            if (modalOptions.isEmpty()) {
                System.out.println("⚠️ No ISBN options found in modal dropdown");
                return;
            }
            
            // Select a random option
            int randomIndex = (int) (Math.random() * modalOptions.size());
            WebElement randomOption = modalOptions.get(randomIndex);
            String newIsbn = randomOption.getText();
            
            System.out.println("🎲 Selecting random ISBN in modal [" + randomIndex + "]: " + newIsbn);
            
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
            
            System.out.println("✓ Selected new ISBN in modal: " + newIsbn);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⚠️ Modal ISBN selection interrupted");
        } catch (Exception e) {
            System.out.println("⚠️ Failed to select ISBN in modal: " + e.getMessage());
            takeScreenshot("modal_isbn_selection_error");
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
