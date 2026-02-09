package com.abr.titles.base;
 
import com.abr.titles.utils.SharedDriver;
import com.abr.titles.utils.TestReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
 
import java.time.Duration;
import java.util.*;
 
public class TitleSearchTest extends BaseTest {
 
    private WebDriverWait wait;
    private WebDriverWait shortWait;
    private Actions actions;
    private Random random;
 
    @BeforeClass
    public void setup() {
        driver = SharedDriver.getDriver();
 
        if (driver == null) {
            throw new RuntimeException("❌ Shared driver not initialized. Check BaseTest setup.");
        }
 
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        shortWait = new WebDriverWait(driver, Duration.ofSeconds(7));
        actions = new Actions(driver);
        random = new Random();
        System.out.println("🔹 Test setup complete - using shared logged-in driver");
    }
 
    @AfterClass
    public void teardown() {
        System.out.println("✅ Test completed - keeping driver alive for other tests");
    }
 
    @Test
    public void TitleSearchFilterFlow() {
        TestReporter.resetStepCounter();
        SoftAssert softAssert = new SoftAssert();
        
        TestReporter.logInfo("========================================");
        TestReporter.logInfo("Starting Title Search Filter Flow Test");
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
 
            // Navigate to Home
            try {
                TestReporter.logInfo("Navigating to home page");
                driver.get("https://titles-dev.abramsbooks.com/");
                waitForPageLoad();
                Thread.sleep(2000);
                TestReporter.logPass("Successfully navigated to home page");
            } catch (Exception e) {
                TestReporter.logFail("Failed to navigate to home page", e);
                softAssert.fail("Home navigation failed: " + e.getMessage());
            }
            
            // Step 1: Click "Title Search"
            try {
                TestReporter.logInfo("Clicking 'Title Search' in navigation menu");
                clickElementByText("Title Search");
                waitForPageLoad();
                Thread.sleep(2000);
                TestReporter.logPass("Successfully navigated to Title Search page");
            } catch (Exception e) {
                TestReporter.logFail("Failed to navigate to Title Search", e);
                softAssert.fail("Title Search navigation failed: " + e.getMessage());
            }
 
            // Step 2: Sort All Columns
            try {
                TestReporter.logInfo("Sorting all columns");
                sortAllColumns();
                TestReporter.logPass("All columns sorted successfully");
            } catch (Exception e) {
                TestReporter.logFail("Failed to sort columns", e);
                softAssert.fail("Column sorting failed: " + e.getMessage());
            }
 
            // Step 3: Scroll Page
            try {
                TestReporter.logInfo("Scrolling through the data table");
                scrollPage();
                TestReporter.logPass("Successfully scrolled through entire table");
            } catch (Exception e) {
                TestReporter.logFail("Failed to scroll table", e);
                softAssert.fail("Table scrolling failed: " + e.getMessage());
            }
 
            // Step 4: Select ISBNs
            try {
                TestReporter.logInfo("Selecting ISBNs from dropdown");
                int selected = selectMultipleFromDropdown("ISBN", 2);
                TestReporter.logPass("Selected " + selected + " ISBN(s)");
                Thread.sleep(1500);
            } catch (Exception e) {
                TestReporter.logFail("Failed to select ISBNs", e);
                softAssert.fail("ISBN dropdown selection failed: " + e.getMessage());
            }
 
            // Step 5: Export
            try {
                TestReporter.logInfo("Clicking Export button (1st time)");
                clickButtonByTextWithScroll("Export");
                Thread.sleep(1500);
                TestReporter.logPass("Successfully clicked Export button");
            } catch (Exception e) {
                TestReporter.logFail("Failed to click Export button", e);
                softAssert.fail("Export button click failed: " + e.getMessage());
            }
 
            // Step 6: Clear All Filters
            try {
                TestReporter.logInfo("Clicking Clear All Filter button");
                clickClearAllFilterButton();
                Thread.sleep(2000);
                TestReporter.logPass("Successfully cleared all filters");
            } catch (Exception e) {
                TestReporter.logWarning("Clear filter button not found or already cleared");
            }
 
            // Step 7: Select Authors
            try {
                TestReporter.logInfo("Selecting Authors from dropdown");
                int selected = selectMultipleFromDropdown("Author", 2);
                TestReporter.logPass("Selected " + selected + " Author(s)");
                Thread.sleep(1500);
            } catch (Exception e) {
                TestReporter.logFail("Failed to select Authors", e);
                softAssert.fail("Author dropdown selection failed: " + e.getMessage());
            }
 
            // Step 8: Clear All Filters
            try {
                TestReporter.logInfo("Clicking Clear All Filter button");
                clickClearAllFilterButton();
                Thread.sleep(2000);
                TestReporter.logPass("Successfully cleared all filters");
            } catch (Exception e) {
                TestReporter.logWarning("Clear filter button not found or already cleared");
            }
 
            // Steps 9-17: Test Multiple Filter Types
            String[] filterTypes = {
                "season:Season",
                "division:Division",
                "imprint:Imprint",
                "format:Format",
                "managingEditor:Managing Editor",
                "editor:Editor",
                "bisac_status:BISAC Status",
                "licensor:Licensor",
                "series:Series"
            };
            
            for (String filterInfo : filterTypes) {
                String[] parts = filterInfo.split(":");
                String filterId = parts[0];
                String filterName = parts[1];
                
                testFilterWithCheckboxes(filterId, filterName, softAssert);
            }
 
            TestReporter.logInfo("All filters tested - proceeding to final export");
            Thread.sleep(2000);
 
            // Step 18: Final Export
            try {
                TestReporter.logInfo("Clicking Export button (2nd time)");
                clickButtonByTextWithScroll("Export");
                
                // Wait on partner portal page for 5-7 seconds
                Thread.sleep(6000);
                TestReporter.logInfo("Stayed on partner portal for 6 seconds");
                
                TestReporter.logPass("Successfully clicked Export button");
            } catch (Exception e) {
                TestReporter.logFail("Failed to click Export button", e);
                softAssert.fail("Final export failed: " + e.getMessage());
            }
 
            // Step 19: Navigate Home
            try {
                TestReporter.logInfo("Navigating back to home page");
                try {
                    clickButtonByTextWithScroll("Main Menu");
                    Thread.sleep(800);
                } catch (Exception e) {
                    TestReporter.logWarning("Main Menu button not found, using direct navigation");
                }
                
                driver.get("https://titles-dev.abramsbooks.com/");
                waitForPageLoad();
                Thread.sleep(1500);
                TestReporter.logPass("Successfully returned to home page");
            } catch (Exception e) {
                TestReporter.logFail("Failed to navigate back to home", e);
                softAssert.fail("Home navigation failed: " + e.getMessage());
            }
 
            TestReporter.logInfo("========================================");
            TestReporter.logInfo("Title Search Filter Flow Test Completed");
            TestReporter.logInfo("========================================");
            
        } catch (Exception e) {
            takeScreenshot("test_error");
            TestReporter.logFail("Test failed with unexpected error", e);
            throw new RuntimeException("Test failed: " + e.getMessage(), e);
        } finally {
            softAssert.assertAll();
        }
    }
 
    private void testFilterWithCheckboxes(String filterId, String filterName, SoftAssert softAssert) {
        try {
            TestReporter.logInfo("Testing " + filterName + " filter");
            
            try {
                clickFilterById(filterId);
                Thread.sleep(2500);
                TestReporter.logPass("Opened " + filterName + " filter");
            } catch (Exception e) {
                TestReporter.logFail("Failed to open " + filterName + " filter", e);
                softAssert.fail(filterName + " filter open failed: " + e.getMessage());
                return;
            }
            
            int selected = 0;
            try {
                selected = selectCheckboxesBasedOnAvailability();
                
                if (selected == 0) {
                    TestReporter.logWarning("No data found in " + filterName + " filter, moving to next filter");
                    try {
                        ((JavascriptExecutor) driver).executeScript("document.body.click();");
                        Thread.sleep(800);
                    } catch (Exception ex) {}
                } else {
                    TestReporter.logPass("Selected " + selected + " option(s) in " + filterName + " filter");
                }
            } catch (Exception e) {
                TestReporter.logFail("Failed to select " + filterName + " options", e);
                softAssert.fail(filterName + " checkbox selection failed: " + e.getMessage());
            }
            
            Thread.sleep(2000);
            
            if (selected > 0) {
                try {
                    try {
                        ((JavascriptExecutor) driver).executeScript("document.body.click();");
                        Thread.sleep(1500);
                    } catch (Exception ex) {}
                    
                    clickClearAllFilterButton();
                    Thread.sleep(2500);
                    TestReporter.logPass("Cleared " + filterName + " filter");
                } catch (Exception e) {
                    TestReporter.logWarning("Clear button not found for " + filterName + " - may already be cleared");
                }
            } else {
                try {
                    ((JavascriptExecutor) driver).executeScript("document.body.click();");
                    Thread.sleep(1000);
                } catch (Exception e) {}
            }
            
        } catch (Exception e) {
            TestReporter.logFail(filterName + " filter test failed completely", e);
            softAssert.fail(filterName + " filter test failed: " + e.getMessage());
        }
    }
 
    private void sortAllColumns() throws Exception {
        WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
            table
        );
        Thread.sleep(1500);
        
        TestReporter.logInfo("Sorting all table columns");
        
        int totalSvgs = driver.findElements(By.xpath("//*[name()='svg'][@class='cursor-pointer']")).size();
        TestReporter.logInfo("Found " + totalSvgs + " sortable SVG icons");
        
        int clickCount = 0;
        
        for (int i = 1; i <= totalSvgs; i++) {
            int columnNumber = (i + 1) / 2;
            
            // Ascending click
            boolean ascendingClicked = false;
            for (int attempt = 1; attempt <= 2; attempt++) {
                try {
                    WebElement sortIcon = driver.findElement(
                        By.xpath("(//*[name()='svg'][@class='cursor-pointer'])[" + i + "]")
                    );
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                        sortIcon
                    );
                    Thread.sleep(300);
                    
                    sortIcon.click();
                    clickCount++;
                    System.out.println("Column " + columnNumber + " | SVG Index " + i + " | Ascending clicked");
                    TestReporter.logPass("Column " + columnNumber + " | SVG Index " + i + " | Ascending clicked");
                    ascendingClicked = true;
                    Thread.sleep(2000);
                    break;
                } catch (StaleElementReferenceException e) {
                    if (attempt == 1) {
                        TestReporter.logWarning("Stale element at SVG index " + i + " (ascending) - retrying");
                        Thread.sleep(1000);
                    } else {
                        TestReporter.logWarning("Failed ascending click after retry for SVG " + i);
                    }
                }
            }
            
            // Descending click - only if ascending succeeded
            if (ascendingClicked) {
                for (int attempt = 1; attempt <= 2; attempt++) {
                    try {
                        WebElement sortIcon = driver.findElement(
                            By.xpath("(//*[name()='svg'][@class='cursor-pointer'])[" + i + "]")
                        );
                        
                        sortIcon.click();
                        clickCount++;
                        System.out.println("Column " + columnNumber + " | SVG Index " + i + " | Descending clicked");
                        TestReporter.logPass("Column " + columnNumber + " | SVG Index " + i + " | Descending clicked");
                        Thread.sleep(2000);
                        break;
                    } catch (StaleElementReferenceException e) {
                        if (attempt == 1) {
                            TestReporter.logWarning("Stale element at SVG index " + i + " (descending) - retrying");
                            Thread.sleep(1000);
                        } else {
                            TestReporter.logWarning("Failed descending click after retry for SVG " + i);
                        }
                    }
                }
            }
        }
        
        System.out.println("Total sort clicks performed: " + clickCount);
        TestReporter.logInfo("Total sort clicks performed: " + clickCount);
        
        TestReporter.logInfo("Scrolling back to first column");
        WebElement tableContainer = driver.findElement(By.cssSelector(".MuiTableContainer-root"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = 0;", tableContainer);
        Thread.sleep(1000);
        TestReporter.logPass("Scrolled back to first column");
        
        TestReporter.logInfo("Completed sorting all columns");
    }
 
    private void scrollPage() throws Exception {
        TestReporter.logInfo("Starting vertical scroll (loading all records)");
 
        WebElement tableContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".MuiTableContainer-root")
        ));
 
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int previousCount = 0;
        int sameCountCounter = 0;
        int scrollIteration = 0;
 
        while (sameCountCounter < 3) {
            scrollIteration++;
            js.executeScript("arguments[0].scrollTop += 1000;", tableContainer);
            Thread.sleep(1500);
 
            List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
            int currentCount = rows.size();
 
            if (currentCount > previousCount) {
                TestReporter.logInfo("Scroll " + scrollIteration + " - Loaded: " + currentCount + " total rows (+" + (currentCount - previousCount) + " new)");
                previousCount = currentCount;
                sameCountCounter = 0;
            } else {
                sameCountCounter++;
                TestReporter.logInfo("Scroll " + scrollIteration + " - No new records loaded (" + sameCountCounter + "/3)");
            }
        }
 
        TestReporter.logInfo("Reached end of table - " + previousCount + " total rows loaded");
 
        TestReporter.logInfo("Scrolling horizontally to view all columns");
        for (int i = 0; i < 5; i++) {
            js.executeScript("arguments[0].scrollLeft += 400;", tableContainer);
            Thread.sleep(200);
        }
 
        TestReporter.logInfo("Scrolling back to left");
        js.executeScript("arguments[0].scrollLeft = 0;", tableContainer);
        Thread.sleep(800);
 
        TestReporter.logInfo("Scrolling back to top");
        js.executeScript("arguments[0].scrollTop = 0;", tableContainer);
        Thread.sleep(1000);
        
        TestReporter.logPass("Completed scrolling - returned to top-left");
    }
 
    private void clickElementByText(String text) throws Exception {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[contains(text(),'" + text + "')]")
        ));
        
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});",
            element
        );
        Thread.sleep(400);
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        Thread.sleep(3000);
        TestReporter.logInfo("Clicked and waited for page load");
    }
    
    private void clickClearAllFilterButton() throws Exception {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        Thread.sleep(800);
        
        try {
            WebElement clearButton = shortWait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(text(),'Clear All Filter')]")
            ));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                clearButton
            );
            Thread.sleep(1000);
            
            try {
                clearButton.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clearButton);
            }
            
            Thread.sleep(1500);
        } catch (TimeoutException e) {
            TestReporter.logWarning("Clear All Filter button not found - filters may already be cleared");
            throw e;
        }
    }
    
    private void clickButtonByTextWithScroll(String buttonText) throws Exception {
        try {
            ((JavascriptExecutor) driver).executeScript("document.body.click();");
            Thread.sleep(400);
        } catch (Exception e) {}
        
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        Thread.sleep(800);
        
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'" + buttonText + "')]")
        ));
        
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
            button
        );
        Thread.sleep(1000);
        
        try {
            button.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
        
        Thread.sleep(800);
    }
    
    private void clickFilterById(String filterId) throws Exception {
        WebElement filter = wait.until(ExpectedConditions.elementToBeClickable(By.id(filterId)));
        
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
            filter
        );
        Thread.sleep(800);
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", filter);
        Thread.sleep(1500);
    }
    
    private int selectMultipleFromDropdown(String dropdownType, int requestedCount) {
        By[] dropdownSelectors;
        String dropdownName;
        
        switch (dropdownType.toLowerCase()) {
            case "isbn":
                dropdownSelectors = new By[]{
                    By.cssSelector("#isbn-selector .custom-select__control"),
                    By.cssSelector("div[id*='isbn'] .custom-select__control"),
                    By.xpath("//div[@id='isbn-selector']//div[contains(@class,'custom-select__control')]")
                };
                dropdownName = "ISBN";
                break;
            case "author":
                dropdownSelectors = new By[]{
                    By.cssSelector("div[id*='author'] .custom-select__control"),
                    By.xpath("//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div[2]/div[1]/div[1]/div[3]/div/div[1]/div[1]")
                };
                dropdownName = "Author";
                break;
            default:
                TestReporter.logWarning("Unknown dropdown type: " + dropdownType);
                return 0;
        }
        
        int successCount = 0;
        
        // Determine available options count first
        int availableOptions = 0;
        try {
            WebElement dropdown = findDropdown(dropdownSelectors);
            if (dropdown == null) {
                TestReporter.logWarning("Dropdown not found for " + dropdownName);
                return 0;
            }
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                dropdown
            );
            Thread.sleep(1000);
            
            openDropdown(dropdown);
            Thread.sleep(3000);
            
            List<WebElement> options = shortWait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                )
            );
            
            availableOptions = options.size();
            TestReporter.logInfo(dropdownName + " has " + availableOptions + " option(s) available");
            
            // Close dropdown after checking
            actions.sendKeys(Keys.ESCAPE).perform();
            Thread.sleep(1000);
            
        } catch (Exception e) {
            TestReporter.logWarning("Could not determine options for " + dropdownName);
            return 0;
        }
        
        if (availableOptions == 0) {
            TestReporter.logWarning("No options available in " + dropdownName + " dropdown");
            return 0;
        }
        
        int selectCount = Math.min(availableOptions, requestedCount);
        TestReporter.logInfo("Will select " + selectCount + " option(s) from " + dropdownName);
        
        // Now select items one by one - reopening dropdown each time
        Set<String> selectedValues = new HashSet<>();
        
        for (int i = 0; i < selectCount; i++) {
            try {
                Thread.sleep(1500);
                
                // Find and open dropdown for each selection
                WebElement dropdown = findDropdown(dropdownSelectors);
                if (dropdown == null) {
                    TestReporter.logWarning("Dropdown not found for selection " + (i+1));
                    break;
                }
                
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                    dropdown
                );
                Thread.sleep(800);
                
                openDropdown(dropdown);
                Thread.sleep(2500);
                
                // Get options
                List<WebElement> options = driver.findElements(
                    By.xpath("//*[contains(@id,'react-select-') and contains(@id,'-option-')]")
                );
                
                if (options.isEmpty()) {
                    TestReporter.logWarning("No options found in " + dropdownName);
                    break;
                }
                
                // Select random option that hasn't been selected
                WebElement selectedOption = null;
                String selectedValue = null;
                int attempts = 0;
                
                while (attempts < 20 && selectedOption == null) {
                    int randomIndex = random.nextInt(options.size());
                    WebElement option = options.get(randomIndex);
                    String optionText = option.getText();
                    
                    if (!selectedValues.contains(optionText)) {
                        selectedOption = option;
                        selectedValue = optionText;
                        selectedValues.add(optionText);
                    }
                    attempts++;
                }
                
                if (selectedOption == null) {
                    TestReporter.logWarning("Could not find unselected option");
                    break;
                }
                
                try {
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'nearest'});",
                        selectedOption
                    );
                    Thread.sleep(300);
                } catch (Exception e) {}
                
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedOption);
                
                TestReporter.logPass("Selected " + dropdownName + " " + (i+1) + ": " + selectedValue);
                successCount++;
                
                // Dropdown closes automatically after selection
                Thread.sleep(1500);
                
            } catch (Exception e) {
                TestReporter.logWarning("Failed to select " + dropdownName + " " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        return successCount;
    }
    
    private WebElement findDropdown(By[] selectors) {
        for (By selector : selectors) {
            try {
                List<WebElement> elements = driver.findElements(selector);
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    return elements.get(0);
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }
    
    private void openDropdown(WebElement dropdown) throws Exception {
        try {
            dropdown.click();
        } catch (Exception e) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);
            } catch (Exception e2) {
                actions.moveToElement(dropdown).click().perform();
            }
        }
    }
    
    private int selectCheckboxesBasedOnAvailability() {
        int successCount = 0;
        
        try {
            List<WebElement> checkboxes;
            try {
                checkboxes = shortWait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//li[not(contains(@class,'disabled'))]//input[@type='checkbox']")
                    )
                );
            } catch (TimeoutException e) {
                TestReporter.logWarning("No checkboxes found in filter");
                return 0;
            }
            
            if (checkboxes.isEmpty()) {
                return 0;
            }
            
            int availableCount = checkboxes.size();
            TestReporter.logInfo("Found " + availableCount + " checkbox option(s) in filter");
            
            int selectCount = Math.min(availableCount, 2);
            Set<Integer> selectedIndices = new HashSet<>();
            
            for (int i = 0; i < selectCount; i++) {
                try {
                    Thread.sleep(1200);
                    
                    List<WebElement> currentCheckboxes = driver.findElements(
                        By.xpath("//li[not(contains(@class,'disabled'))]//input[@type='checkbox']")
                    );
                    
                    if (currentCheckboxes.isEmpty()) {
                        break;
                    }
                    
                    // Select random checkbox that hasn't been selected
                    int randomIndex;
                    int attempts = 0;
                    do {
                        randomIndex = random.nextInt(currentCheckboxes.size());
                        attempts++;
                    } while (selectedIndices.contains(randomIndex) && attempts < 20);
                    
                    if (selectedIndices.contains(randomIndex)) {
                        break;
                    }
                    
                    selectedIndices.add(randomIndex);
                    WebElement checkbox = currentCheckboxes.get(randomIndex);
                    
                    String labelText = "Unknown";
                    try {
                        WebElement parent = checkbox.findElement(By.xpath("./ancestor::li"));
                        labelText = parent.getText();
                    } catch (Exception e) {}
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                        checkbox
                    );
                    Thread.sleep(600);
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
                    TestReporter.logPass("Selected checkbox: " + labelText);
                    successCount++;
                    Thread.sleep(1000);
                    
                } catch (Exception e) {
                    TestReporter.logWarning("Could not select checkbox " + (i+1));
                }
            }
            
            Thread.sleep(1500);
            
        } catch (Exception e) {
            TestReporter.logFail("Checkbox selection failed", e);
        }
        
        return successCount;
    }
 
    private void waitForPageLoad() {
        try {
            wait.until(driver1 ->
                ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete")
            );
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {}
    }
    
    private void takeScreenshot(String filename) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            ts.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {}
    }
}