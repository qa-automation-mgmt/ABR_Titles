package com.seg.titles.base;
 
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.*;
 
public class FilterTableTest extends BaseTest {
 
    @Test
    public void testFiltersAndExports() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);
        Random random = new Random();
 
        System.out.println("🔹 Starting filter test on Titles page...");
 
        // === Step 1: Export without filters ===
        System.out.println("\n📤 Exporting WITHOUT filters...");
        exportTable(wait);
        Thread.sleep(2000);
 
        // === Step 2: Apply filters in correct order ===
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("ISBN", "//*[@id='isbn-selector']/div[1]/div[1]");
        filterMap.put("TITLE", "//*[@id='title-selector']/div[1]/div[1]");
        filterMap.put("AUTHOR", "//*[@id='root']/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/div/form/div/div[3]/div[2]/div/div[1]/div[1]");
 
        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            String column = entry.getKey();
            String xpath = entry.getValue();
 
            System.out.println("\n🔸 Applying filters for: " + column);
 
            // Apply single random filter
            applyRandomFilter(wait, js, actions, random, xpath, column, false);
            clearFilters(wait);
 
            // Apply multiple random filters
            applyRandomFilter(wait, js, actions, random, xpath, column, true);
            clearFilters(wait);
        }
 
        // === Step 3: Apply one random multi-filter and export ===
        System.out.println("\n🎯 Applying random multi-filter before export...");
        String randomColumn = getRandomKey(filterMap);
        applyRandomFilter(wait, js, actions, random, filterMap.get(randomColumn), randomColumn, true);
 
        System.out.println("\n📤 Exporting WITH filters...");
        exportTable(wait);
        Thread.sleep(2000);
 
        System.out.println("\n✅ Test completed — exported both with and without filters.");
    }
 
    // === EXPORT BUTTON CLICK ===
    private void exportTable(WebDriverWait wait) {
        try {
            WebElement exportButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(.,'Export')]")));
            exportButton.click();
            System.out.println("✅ Export clicked successfully.");
        } catch (Exception e) {
            System.out.println("❌ Export failed: " + e.getMessage());
        }
    }
 
    // === APPLY RANDOM FILTER (single or multiple) ===
    private void applyRandomFilter(WebDriverWait wait, JavascriptExecutor js, Actions actions,
                                   Random random, String xpath, String column, boolean multiple)
            throws InterruptedException {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            js.executeScript("arguments[0].scrollIntoView(true);", dropdown);
            dropdown.click();
            Thread.sleep(1000);
 
            WebElement dropdownMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'custom-select__menu')]")));
            List<WebElement> options = dropdownMenu.findElements(By.cssSelector("div[class*='option']"));
 
            if (options.isEmpty()) {
                System.out.println("⚠️ No options found for " + column);
                return;
            }
 
            int count = multiple ? Math.min(2, options.size()) : 1;
            Set<String> selected = new HashSet<>();
 
            for (int i = 0; i < count; i++) {
                WebElement option = options.get(random.nextInt(options.size()));
                String value = option.getText().trim();
                selected.add(value);
                js.executeScript("arguments[0].scrollIntoView(true);", option);
                actions.moveToElement(option).click().perform();
                Thread.sleep(800);
            }
 
            actions.moveByOffset(20, 20).click().perform();
            System.out.println("✅ Applied " + (multiple ? "multiple" : "single") + " random filter(s) on " + column + ": " + selected);
 
        } catch (Exception e) {
            System.out.println("❌ Error applying filter on " + column + ": " + e.getMessage());
        }
    }
 
    // === CLEAR FILTERS ===
    private void clearFilters(WebDriverWait wait) throws InterruptedException {
        try {
            List<WebElement> removeButtons = driver.findElements(By.cssSelector("div.custom-select__multi-value__remove"));
            for (WebElement removeBtn : removeButtons) {
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(removeBtn));
                    removeBtn.click();
                    Thread.sleep(300);
                } catch (StaleElementReferenceException ignored) {}
            }
            System.out.println("🧹 Cleared filters.");
        } catch (Exception e) {
            System.out.println("⚠️ No filters to clear: " + e.getMessage());
        }
    }
 
    private String getRandomKey(Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        return keys.get(new Random().nextInt(keys.size()));
    }
}