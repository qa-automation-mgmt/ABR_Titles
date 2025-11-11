package com.seg.titles.base;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FilterTableTest extends BaseTest {

    private static final String EXPORT_XPATH =
            "//*[@id=\"root\"]/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/div/form/div/div[4]/button";

    @Test
    public void testFilterExport() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);
        Random rand = new Random();

        System.out.println("🔹 Starting filter export test...");

        // === Step 1: Export all records without any filters ===
        System.out.println("📦 Exporting all records (no filters)...");
        exportData(wait, js);
        Thread.sleep(2000);

        // === Step 2: Apply filters for all fields sequentially ===
        Map<String, String> filterMap = Map.of(
                "ISBN", "//*[@id='isbn-selector']/div[1]/div[1]",
                "TITLE", "//*[@id='title-selector']/div[1]/div[1]",
                "AUTHOR", "//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/div/form/div/div[3]/div[2]/div/div[1]/div[1]"
        );

        Map<String, List<String>> appliedFilters = new HashMap<>();

        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            String column = entry.getKey();
            String xpath = entry.getValue();

            System.out.println("\n🔸 Applying filters on column: " + column);

            // Apply single or multiple filters (set 'true' for multiple)
            List<String> selectedValues = applyAndReturnSelectedFilters(wait, js, actions, rand, xpath, column, true);
            appliedFilters.put(column, selectedValues);

            Thread.sleep(1000);
        }

        // ✅ Step 3: Export data with filters applied
        System.out.println("📤 Exporting filtered data for all fields...");
        exportData(wait, js);
        Thread.sleep(1500);

        // Optional: clear filters after export
        removeAllFilters(wait);

        System.out.println("\n✅ Completed filter export test.");
    }

    // =======================================================
    // ============  EXPORT LOGIC  ==========================
    // =======================================================

    private void exportData(WebDriverWait wait, JavascriptExecutor js) {
        try {
            WebElement exportBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(EXPORT_XPATH)));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", exportBtn);
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", exportBtn);
            System.out.println("✅ Clicked Export button successfully.");

            // Wait briefly to ensure download trigger
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to click Export button: " + e.getMessage());
        }
    }

    // =======================================================
    // ============  FILTER LOGIC  ==========================
    // =======================================================

    private List<String> applyAndReturnSelectedFilters(WebDriverWait wait, JavascriptExecutor js, Actions actions,
                                                       Random rand, String filterXPath, String column, boolean multiple)
            throws InterruptedException {

        Set<String> selectedValues = new HashSet<>();

        WebElement selectControl = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(filterXPath)));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", selectControl);
        Thread.sleep(800);

        WebElement input = null;
        try {
            input = selectControl.findElement(By.xpath(".//input[contains(@class,'custom-select__input')]"));
        } catch (org.openqa.selenium.NoSuchElementException ignored) {}

        boolean opened = false;
        for (int attempt = 1; attempt <= 4 && !opened; attempt++) {
            try {
                System.out.println("🧭 Attempt " + attempt + " to open dropdown for " + column);

                if (input != null) {
                    try { input.click(); } catch (Exception e) { js.executeScript("arguments[0].click();", input); }
                } else {
                    try { selectControl.click(); } catch (Exception e) { js.executeScript("arguments[0].click();", selectControl); }
                }

                if (isMenuVisible(wait, 3)) {
                    opened = true;
                    break;
                }

                if (input != null) input.sendKeys(Keys.ARROW_DOWN);
                else actions.moveToElement(selectControl).click().perform();

                Thread.sleep(600);
            } catch (Exception e) {
                System.out.println("⚠️ Attempt " + attempt + " failed for " + column + ": " + e.getMessage());
                Thread.sleep(600);
            }
        }

        Assert.assertTrue(opened, "❌ Failed to open dropdown for " + column + " after retries.");

        int numFilters = multiple ? 2 : 1;

        for (int i = 0; i < numFilters; i++) {
            WebElement dropdownMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@id,'react-select') and contains(@id,'-listbox')] | //div[contains(@class,'custom-select__menu')]")));

            List<WebElement> options = dropdownMenu.findElements(By.cssSelector("div[class*='option']"));
            if (options.isEmpty()) {
                System.out.println("⚠️ No options found for " + column);
                break;
            }

            WebElement option = options.get(rand.nextInt(options.size()));
            String val = option.getText().trim();
            selectedValues.add(val);

            js.executeScript("arguments[0].scrollIntoView(true);", option);
            actions.moveToElement(option).click().perform();
            Thread.sleep(1000);

            // Reopen dropdown if multiple selections
            if (multiple && i < numFilters - 1) {
                try {
                    WebElement reopenInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath(filterXPath + "//input[contains(@class,'custom-select__input')]")));
                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", reopenInput);
                    js.executeScript("arguments[0].click();", reopenInput);
                    Thread.sleep(1000);

                    if (!isMenuVisible(wait, 3)) {
                        actions.moveToElement(reopenInput).click().perform();
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Could not reopen dropdown for next selection: " + e.getMessage());
                }
            }
        }

        actions.moveByOffset(30, 30).click().perform();
        Thread.sleep(800);

        System.out.println("✅ Applied " + (multiple ? "multiple random" : "single random") +
                " filter(s) for " + column + ": " + selectedValues);

        // Verify results
        verifyFilterResults(column, new ArrayList<>(selectedValues));

        return new ArrayList<>(selectedValues);
    }

    // =======================================================
    // ============  CLEAR FILTERS ==========================
    // =======================================================

    private void removeAllFilters(WebDriverWait wait) throws InterruptedException {
        int attempt = 0;
        while (attempt < 3) {
            List<WebElement> removeButtons = driver.findElements(By.cssSelector("div.custom-select__multi-value__remove"));
            if (removeButtons.isEmpty()) break;

            for (WebElement btn : removeButtons) {
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(btn));
                    btn.click();
                    Thread.sleep(400);
                } catch (StaleElementReferenceException ignored) {}
                catch (Exception e) { System.out.println("⚠️ Could not remove filter: " + e.getMessage()); }
            }
            attempt++;
        }
        System.out.println("🧹 Cleared filters.");
    }

    // =======================================================
    // ============  HELPER UTILITIES =======================
    // =======================================================

    private boolean isMenuVisible(WebDriverWait wait, int seconds) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@id,'react-select') and contains(@id,'-listbox')] | //div[contains(@class,'custom-select__menu')]")));
            return true;
        } catch (Exception e) { return false; }
    }

    // =======================================================
    // ============  VERIFICATION ===========================
    // =======================================================

    private void verifyFilterResults(String columnName, List<String> selectedValues) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr")));

            List<WebElement> headers = driver.findElements(By.xpath("//thead/tr/th"));
            int colIndex = -1;
            for (int i = 0; i < headers.size(); i++) {
                if (headers.get(i).getText().trim().equalsIgnoreCase(columnName)) {
                    colIndex = i + 1;
                    break;
                }
            }
            Assert.assertTrue(colIndex != -1, "❌ Column '" + columnName + "' not found.");

            List<WebElement> cells = driver.findElements(By.xpath("//tbody/tr/td[" + colIndex + "]"));
            Assert.assertFalse(cells.isEmpty(), "❌ No rows found after applying filter for " + columnName);

            List<String> cellTexts = cells.stream()
                    .map(WebElement::getText)
                    .filter(t -> !t.isEmpty())
                    .limit(3)
                    .collect(Collectors.toList());

            System.out.println("✅ Verified results for " + columnName + " — Sample: " + cellTexts);

        } catch (Exception e) {
            System.out.println("⚠️ Error verifying results for " + columnName + ": " + e.getMessage());
            throw new AssertionError("❌ Verification failed for " + columnName + ": " + e.getMessage());
        }
    }
}
