package com.seg.titles.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class AllCheckboxFiltersTest extends BaseTest {

    @Test
    public void testAllFiltersSequentially() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        System.out.println("🔹 Starting Titles filter automation...");

        // 1️⃣ SEASON
        applyCheckboxFilter(wait, js, "//*[@id='season']", new int[]{1, 5});
        clearFilters(wait, js);

        // 2️⃣ DIVISION
        applyCheckboxFilter(wait, js, "//*[@id='division']", new int[]{1, 4});
        clearFilters(wait, js);

        // 3️⃣ IMPRINT
        applyCheckboxFilter(wait, js, "//*[@id='imprint']", new int[]{4, 5});
        clearFilters(wait, js);

        // 4️⃣ EDITOR
        applyCheckboxFilter(wait, js, "//*[@id='editor']", new int[]{1, 7});
        clearFilters(wait, js);

        // 5️⃣ BISAC STATUS
        applyCheckboxFilter(wait, js, "//*[@id='bisac_status']", new int[]{2, 4});
        clearFilters(wait, js);

        // 6️⃣ FORMAT
        applyCheckboxFilter(wait, js, "//*[@id='format']", new int[]{4});
        clearFilters(wait, js);

        System.out.println("\n✅ All filters successfully applied and cleared!");
    }

    private void applyCheckboxFilter(WebDriverWait wait, JavascriptExecutor js, String fieldXpath, int[] checkboxIndices) throws InterruptedException {
        String fieldName = fieldXpath.substring(fieldXpath.indexOf("'") + 1, fieldXpath.lastIndexOf("'"));
        System.out.println("🔸 Applying filter: " + fieldName);

        waitForOverlayToDisappear(wait);

        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(fieldXpath)));
        scrollIntoView(js, field);
        js.executeScript("arguments[0].click();", field);

        waitForOverlayToDisappear(wait);
        Thread.sleep(800);

        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.MuiPopper-root")));
        List<WebElement> checkboxes = dropdown.findElements(By.cssSelector("li input[type='checkbox']"));

        if (checkboxes.isEmpty()) {
            System.out.println("⚠️ No checkboxes found for " + fieldName);
            return;
        }

        for (int index : checkboxIndices) {
            if (index <= checkboxes.size()) {
                WebElement checkbox = checkboxes.get(index - 1);
                scrollIntoView(js, checkbox);
                js.executeScript("arguments[0].click();", checkbox);
                Thread.sleep(500);
                System.out.println("✅ Selected checkbox #" + index + " in " + fieldName);
            } else {
                System.out.println("⚠️ Checkbox index " + index + " not found for " + fieldName);
            }
        }

        // Close dropdown
        field.sendKeys(Keys.ESCAPE);
        Thread.sleep(800);
    }

    private void clearFilters(WebDriverWait wait, JavascriptExecutor js) throws InterruptedException {
        waitForOverlayToDisappear(wait);
        try {
            WebElement clearBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(.,'Clear All Filter')]")));
            scrollIntoView(js, clearBtn);
            js.executeScript("arguments[0].click();", clearBtn);
            Thread.sleep(1000);
            System.out.println("🧹 Cleared all filters");
        } catch (TimeoutException e) {
            System.out.println("⚠️ Clear All Filter button not found or already cleared.");
        }
    }

    private void waitForOverlayToDisappear(WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("div.absolute.inset-0.opacity-30.z-50")));
        } catch (TimeoutException e) {
            System.out.println("⚠️ Overlay timeout — continuing...");
        }
    }

    private void scrollIntoView(JavascriptExecutor js, WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }
}
