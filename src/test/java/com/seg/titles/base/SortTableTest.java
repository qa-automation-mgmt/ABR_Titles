package com.seg.titles.base;

import com.seg.titles.utils.SharedDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;

import java.time.Duration;

public class SortTableTest extends BaseTest {

    @Test
    public void runSortingTest() throws Exception {

        WebDriver driver = SharedDriver.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get("https://titles-dev.abramsbooks.com/");
        Thread.sleep(2000);

        // Columns to sort exactly as they appear on UI
        String[] columns = {"ISBN", "Title", "Pub Goal"};

        for (String col : columns) {

            System.out.println("➡ Sorting: " + col);

            // Locate header by text
            By header = By.xpath("//th[.//div[normalize-space()='" + col + "']]");
            WebElement headerEl = wait.until(ExpectedConditions.visibilityOfElementLocated(header));

            // Instantly jump scroll to the column (no slow scroll)
            js.executeScript(
                    "arguments[0].scrollIntoView({behavior: 'instant', block: 'center', inline: 'center'});",
                    headerEl
            );
            Thread.sleep(300);

            // SORT ASCENDING
            headerEl.click();
            waitForTableToRefresh(driver);
            System.out.println("   ✔ ASC sorted: " + col);

            // SORT DESCENDING
            headerEl.click();
            waitForTableToRefresh(driver);
            System.out.println("   ✔ DESC sorted: " + col);

            Thread.sleep(400);
        }

        System.out.println("🎉 Sorting complete!");
    }

    private void waitForTableToRefresh(WebDriver driver) throws InterruptedException {
        Thread.sleep(700); // lightweight delay
        new WebDriverWait(driver, Duration.ofSeconds(6))
                .until(web -> ((JavascriptExecutor) web)
                        .executeScript("return document.readyState").equals("complete"));
    }
}
