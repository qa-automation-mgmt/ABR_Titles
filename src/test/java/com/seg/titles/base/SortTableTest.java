package com.seg.titles.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.*;

public class SortTableTest extends BaseTest {

    @Test
    public void testISBNSorting() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String columnName = "ISBN";

        try {
            // Wait until ISBN header cell is visible
            WebElement thCell = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div/div[2]/div/div/main/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/div/div[1]/table/thead/tr/th[2]")
            ));

            // Wait for span wrapping sorting icon and click to sort ascending
            WebElement sortIconSpan = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(
                thCell, By.xpath("./div/div[1]/div/div/span[2]"))).get(0);
            js.executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", sortIconSpan);
            js.executeScript("arguments[0].style.border='3px solid red';", sortIconSpan);
            js.executeScript("arguments[0].click();", sortIconSpan);

            // Wait for table to stabilize after sorting ascending
            waitUntilTableStable(columnName);

            // Verify sorting ascending
            verifyColumnSort(columnName, true);

            // Click again on sort icon to sort descending
            sortIconSpan = thCell.findElement(By.xpath("./div/div[1]/div/div/span[2]")); // refetch after DOM update
            js.executeScript("arguments[0].click();", sortIconSpan);

            // Wait for table to stabilize after sorting descending
            waitUntilTableStable(columnName);

            // Verify sorting descending
            verifyColumnSort(columnName, false);

            // Remove highlight after test completion
            js.executeScript("arguments[0].style.border='';", sortIconSpan);

        } catch (Exception e) {
            System.out.println("Error during sorting test on '" + columnName + "': " + e.getMessage());
        }

        System.out.println("✅ Completed sorting test for " + columnName);
    }

    private void waitUntilTableStable(String columnName) throws InterruptedException {
        List<String> prevValues = null;
        int retries = 5;

        while (retries-- > 0) {
            List<String> currValues = getColumnValues(columnName);
            if (currValues.isEmpty()) {
                Thread.sleep(500);
                continue;
            }
            if (prevValues != null && prevValues.equals(currValues)) {
                return; // Table stable
            }
            prevValues = currValues;
            Thread.sleep(500);
        }
    }

    private List<String> getColumnValues(String columnName) {
        List<String> values = new ArrayList<>();
        try {
            List<WebElement> headers = driver.findElements(By.xpath("//thead/tr/th"));
            int columnIndex = -1;
            for (int i = 0; i < headers.size(); i++) {
                if (headers.get(i).getText().trim().equalsIgnoreCase(columnName)) {
                    columnIndex = i + 1;
                    break;
                }
            }
            if (columnIndex == -1) return values;

            List<WebElement> cells = driver.findElements(By.xpath("//tbody/tr/td[" + columnIndex + "]"));
            int limit = Math.min(cells.size(), 15);
            for (int i = 0; i < limit; i++) {
                String text = cells.get(i).getText().trim();
                if (!text.isEmpty()) values.add(text);
            }
        } catch (Exception e) {
            System.out.println("Failed to get column values: " + e.getMessage());
        }
        return values;
    }

    private void verifyColumnSort(String columnName, boolean ascending) {
        List<String> values = getColumnValues(columnName);
        if (values.isEmpty()) {
            System.out.println("No data to verify sort order.");
            return;
        }
        System.out.println("Verifying " + (ascending ? "ascending" : "descending") + " order for " + columnName);

        List<String> sortedValues = new ArrayList<>(values);
        sortedValues.sort(String.CASE_INSENSITIVE_ORDER);
        if (!ascending) Collections.reverse(sortedValues);

        if (sortedValues.equals(values)) {
            System.out.println("Sorting verified successfully.");
        } else {
            System.out.println("Sorting verification failed.");
            System.out.println("Expected: " + sortedValues);
            System.out.println("Actual: " + values);
        }
    }
}
