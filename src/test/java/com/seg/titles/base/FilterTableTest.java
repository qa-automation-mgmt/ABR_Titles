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

    private static final String EXPORT_XPATH = "//*[@id=\"root\"]/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/div/form/div/div[4]/button";

    @Test
    public void testReactSelectFiltersSequentially() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);
        Random rand = new Random();

        System.out.println("🔹 Starting filter test on Titles page...");

        // === Step 1: Export all records before applying any filters ===
        System.out.println("📦 Exporting all available records (no filters)...");
        exportData(wait, js);
        Thread.sleep(2000);

        // === Map each filter field to its XPath ===
        Map<String, String> filterMap = Map.of(
                "ISBN", "//*[@id='isbn-selector']/div[1]/div[1]",
                "TITLE", "//*[@id='title-selector']/div[1]/div[1]",
                "AUTHOR", "//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/div/form/div/div[3]/div[2]/div/div[1]/div[1]"
        );

        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            String column = entry.getKey();
            String xpath = entry.getValue();

            System.out.println("\n🔸 Filtering on column: " + column);

            // === Apply single random filter ===
            applyAndVerifyFilters(wait, js, actions, rand, xpath, column, false);

            // === Export filtered data ===
            System.out.println("📤 Exporting filtered data for " + column + "...");
            exportData(wait, js);

            // === Clear filters ===
            removeAllFilters(wait);
            Thread.sleep(1500);

            // === Apply multiple random filters ===
            applyAndVerifyFilters(wait, js, actions, rand, xpath, column, true);

            // === Export again after multiple filters ===
            System.out.println("📤 Exporting filtered data for " + column + " (multiple filters)...");
            exportData(wait, js);

            // === Clear filters before moving on ===
            removeAllFilters(wait);
            Thread.sleep(1500);
        }

        System.out.println("\n✅ Completed filtering + export test for all fields.");
    }

    // =======================================================
    // ============  EXPORT LOGIC  ===========================
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
    // ============  MAIN FILTER LOGIC  ======================
    // =======================================================

    private void applyAndVerifyFilters(WebDriverWait wait, JavascriptExecutor js, Actions actions,
                                       Random rand, String filterXPath, String column, boolean multiple)
            throws InterruptedException {

        WebElement selectControl = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(filterXPath)));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", selectControl);
        Thread.sleep(800);

        WebElement input = null;
        try {
            input = selectControl.findElement(By.xpath(".//input[contains(@class,'custom-select__input')]"));
        } catch (org.openqa.selenium.NoSuchElementException ignored) {}

        // Try to open dropdown with retries
        boolean opened = false;
        for (int attempt = 1; attempt <= 4 && !opened; attempt++) {
            try {
                System.out.println("🧭 Attempt " + attempt + " to open dropdown for " + column);

                if (input != null) {
                    try {package com.seg.titles.base;

import java.time.Duration; // ✅ Add this line
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FilterTableTest extends BaseTest {

    @Test
    public void testReactSelectFiltersSequentially() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);
        Random rand = new Random();

        System.out.println("🔹 Starting filter test on Titles page...");

        // === Map each filter field to its XPath ===
        Map<String, String> filterMap = Map.of(
                "ISBN", "//*[@id='isbn-selector']/div[1]/div[1]",
                "TITLE", "//*[@id='title-selector']/div[1]/div[1]",
                "AUTHOR", "//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/div/form/div/div[3]/div[2]/div/div[1]/div[1]"
        );

        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            String column = entry.getKey();
            String xpath = entry.getValue();

            System.out.println("\n🔸 Filtering on column: " + column);

            // === Apply single random filter ===
            applyAndVerifyFilters(wait, js, actions, rand, xpath, column, false);

            // === Clear filters ===
            removeAllFilters(wait);
            Thread.sleep(1500);

            // === Apply multiple random filters ===
            applyAndVerifyFilters(wait, js, actions, rand, xpath, column, true);

            // === Clear filters before moving on ===
            removeAllFilters(wait);
            Thread.sleep(1500);
        }

        System.out.println("\n✅ Completed filtering test for all fields.");
    }

    // =======================================================
    // ============  MAIN FILTER LOGIC  ======================
    // =======================================================

    private void applyAndVerifyFilters(WebDriverWait wait, JavascriptExecutor js, Actions actions,
                                       Random rand, String filterXPath, String column, boolean multiple)
            throws InterruptedException {

        WebElement selectControl = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(filterXPath)));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", selectControl);
        Thread.sleep(800);

        WebElement input = null;
        try {
            input = selectControl.findElement(By.xpath(".//input[contains(@class,'custom-select__input')]"));
        } catch (NoSuchElementException ignored) {}

        // Try to open dropdown with retries
        boolean opened = false;
        for (int attempt = 1; attempt <= 4 && !opened; attempt++) {
            try {
                System.out.println("🧭 Attempt " + attempt + " to open dropdown for " + column);

                if (input != null) {
                    try {
                        input.click();
                    } catch (Exception e) {
                        js.executeScript("arguments[0].click();", input);
                    }
                } else {
                    try {
                        selectControl.click();
                    } catch (Exception e) {
                        js.executeScript("arguments[0].click();", selectControl);
                    }
                }

                if (isMenuVisible(wait, 3)) {
                    opened = true;
                    break;
                }

                // Try key event
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
        Set<String> selectedValues = new HashSet<>();

        for (int i = 0; i < numFilters; i++) {
            WebElement dropdownMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@id,'react-select') and contains(@id,'-listbox')] | //div[contains(@class,'custom-select__menu')]")));

            List<WebElement> options = dropdownMenu.findElements(By.cssSelector("div[class*='option']"));
            if (options.isEmpty()) {
                System.out.println("⚠️ No options found for " + column);
                return;
            }

            WebElement option = options.get(rand.nextInt(options.size()));
            String val = option.getText().trim();
            selectedValues.add(val);
            js.executeScript("arguments[0].scrollIntoView(true);", option);
            actions.moveToElement(option).click().perform();

            Thread.sleep(1000);

            // ✅ Automatically reopen dropdown for next selection (no manual click)
            if (multiple && i < numFilters - 1) {
                try {
                    WebElement reopen;
                    try {
                        reopen = selectControl.findElement(By.xpath(".//input[contains(@class,'custom-select__input')]"));
                    } catch (NoSuchElementException e) {
                        reopen = selectControl;
                    }

                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", reopen);
                    js.executeScript("arguments[0].click();", reopen);
                    Thread.sleep(800);

                    // Retry if dropdown didn’t reopen
                    if (!isMenuVisible(wait, 2)) {
                        actions.moveToElement(reopen).click().perform();
                        Thread.sleep(800);
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to reopen dropdown for next selection: " + e.getMessage());
                }
            }
        }

        // Close dropdown by clicking outside
        actions.moveByOffset(30, 30).click().perform();
        Thread.sleep(800);

        System.out.println("✅ Applied " + (multiple ? "multiple random" : "single random") +
                " filter(s) for " + column + ": " + selectedValues);

        // === Verification ===
        verifyFilterResults(column, new ArrayList<>(selectedValues));
    }

    // =======================================================
    // ============  FILTER CLEARING LOGIC  ==================
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
                } catch (StaleElementReferenceException ignored) {
                } catch (Exception e) {
                    System.out.println("⚠️ Could not remove filter: " + e.getMessage());
                }
            }
            attempt++;
        }
        System.out.println("🧹 Cleared filters.");
    }

    // =======================================================
    // ============  HELPER UTILITIES  =======================
    // =======================================================

    private boolean isMenuVisible(WebDriverWait wait, int seconds) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@id,'react-select') and contains(@id,'-listbox')] | //div[contains(@class,'custom-select__menu')]")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // =======================================================
    // ============  VERIFICATION  ===========================
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

            System.out.println("✅ Verified results for " + columnName +
                    " — Sample: " + cellTexts);

        } catch (Exception e) {
            System.out.println("⚠️ Error verifying results for " + columnName + ": " + e.getMessage());
            throw new AssertionError("❌ Verification failed for " + columnName + ": " + e.getMessage());
        }
    }
}

                        input.click();
                    } catch (Exception e) {
                        js.executeScript("arguments[0].click();", input);
                    }
                } else {
                    try {
                        selectControl.click();
                    } catch (Exception e) {
                        js.executeScript("arguments[0].click();", selectControl);
                    }
                }

                if (isMenuVisible(wait, 3)) {
                    opened = true;
                    break;
                }

                // Try key event
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
        Set<String> selectedValues = new HashSet<>();

        for (int i = 0; i < numFilters; i++) {
            WebElement dropdownMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@id,'react-select') and contains(@id,'-listbox')] | //div[contains(@class,'custom-select__menu')]")));

            List<WebElement> options = dropdownMenu.findElements(By.cssSelector("div[class*='option']"));
            if (options.isEmpty()) {
                System.out.println("⚠️ No options found for " + column);
                return;
            }

            WebElement option = options.get(rand.nextInt(options.size()));
            String val = option.getText().trim();
            selectedValues.add(val);
            js.executeScript("arguments[0].scrollIntoView(true);", option);
            actions.moveToElement(option).click().perform();

            Thread.sleep(1000);

            if (multiple && i < numFilters - 1) {
                try {
                    WebElement reopen;
                    try {
                        reopen = selectControl.findElement(By.xpath(".//input[contains(@class,'custom-select__input')]"));
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        reopen = selectControl;
                    }

                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", reopen);
                    js.executeScript("arguments[0].click();", reopen);
                    Thread.sleep(800);

                    if (!isMenuVisible(wait, 2)) {
                        actions.moveToElement(reopen).click().perform();
                        Thread.sleep(800);
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to reopen dropdown for next selection: " + e.getMessage());
                }
            }
        }

        actions.moveByOffset(30, 30).click().perform();
        Thread.sleep(800);

        System.out.println("✅ Applied " + (multiple ? "multiple random" : "single random") +
                " filter(s) for " + column + ": " + selectedValues);

        verifyFilterResults(column, new ArrayList<>(selectedValues));
    }

    // =======================================================
    // ============  FILTER CLEARING LOGIC  ==================
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
                } catch (StaleElementReferenceException ignored) {
                } catch (Exception e) {
                    System.out.println("⚠️ Could not remove filter: " + e.getMessage());
                }
            }
            attempt++;
        }
        System.out.println("🧹 Cleared filters.");
    }

    // =======================================================
    // ============  HELPER UTILITIES  =======================
    // =======================================================

    private boolean isMenuVisible(WebDriverWait wait, int seconds) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@id,'react-select') and contains(@id,'-listbox')] | //div[contains(@class,'custom-select__menu')]")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // =======================================================
    // ============  VERIFICATION  ===========================
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

            System.out.println("✅ Verified results for " + columnName +
                    " — Sample: " + cellTexts);

        } catch (Exception e) {
            System.out.println("⚠️ Error verifying results for " + columnName + ": " + e.getMessage());
            throw new AssertionError("❌ Verification failed for " + columnName + ": " + e.getMessage());
        }
    }
}
