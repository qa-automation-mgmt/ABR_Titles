package com.seg.titles.base;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Handles visible sorting interactions for the Titles table
 */
public class SortTablePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public SortTablePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /** Wait until the table is visible */
    public void waitForTable() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table thead")));
        System.out.println("✅ Table detected. Beginning visible sorting...");
    }

    /** Scroll horizontally across table to show all columns */
    private void scrollHorizontally(int pixels) {
        ((JavascriptExecutor) driver).executeScript(
                "document.querySelector('.MuiTableContainer-root').scrollBy(arguments[0], 0);", pixels);
    }

    /** Sort all visible columns one by one */
    public void sortAllColumns() {
        try {
            List<WebElement> columns = driver.findElements(By.xpath("//thead//th//div[@role='columnheader'] | //thead//th"));
            System.out.println("🔹 Total columns detected: " + columns.size());

            Actions actions = new Actions(driver);

            for (int i = 0; i < columns.size(); i++) {
                try {
                    // Re-locate each header fresh each loop to avoid stale element
                    List<WebElement> freshColumns = driver.findElements(By.xpath("//thead//th"));
                    WebElement column = freshColumns.get(i);

                    String columnName = column.getText().trim();
                    if (columnName.isEmpty()) continue;

                    System.out.println("\n🔹 Sorting column: " + columnName);

                    // Scroll to make sure column is visible
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'center', inline:'center'});", column);
                    Thread.sleep(800);

                    // Find clickable div or icon inside header
                    WebElement sortButton = null;
                    try {
                        sortButton = column.findElement(By.cssSelector("div.Mui-TableHeadCell-Content-Actions"));
                    } catch (NoSuchElementException ignore) {}

                    if (sortButton == null) {
                        // Try fallback clickable area
                        sortButton = column;
                    }

                    // Click ascending
                    wait.until(ExpectedConditions.elementToBeClickable(sortButton));
                    actions.moveToElement(sortButton).pause(Duration.ofMillis(200)).click().perform();
                    System.out.println("⬆️ Clicked ascending sort for: " + columnName);
                    Thread.sleep(2000); // visible wait

                    // Click descending
                    wait.until(ExpectedConditions.elementToBeClickable(sortButton));
                    actions.moveToElement(sortButton).pause(Duration.ofMillis(200)).click().perform();
                    System.out.println("⬇️ Clicked descending sort for: " + columnName);
                    Thread.sleep(2000); // visible wait

                    // Scroll right after every few columns
                    if (i % 3 == 2) scrollHorizontally(400);

                } catch (StaleElementReferenceException e) {
                    System.out.println("⚠️ Skipping stale column — re-rendered table, continuing...");
                } catch (Exception e) {
                    System.out.println("⚠️ Could not click sort icon for column " + i + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Exception during sorting: " + e.getMessage());
        }
    }
}
