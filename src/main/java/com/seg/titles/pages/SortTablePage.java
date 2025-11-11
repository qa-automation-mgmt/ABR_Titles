package com.seg.titles.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for table sorting operations.
 */
public class SortTablePage {

    private WebDriver driver;
    private WebDriverWait wait;

    public SortTablePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void waitForTable() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));
    }

    public List<String> getSortableHeaders() {
        List<WebElement> headers = driver.findElements(By.cssSelector("thead th"));
        List<String> sortableHeaders = new ArrayList<>();

        for (WebElement header : headers) {
            if (header.getAttribute("aria-sort") != null || header.getText().trim().length() > 0) {
                sortableHeaders.add(header.getText().trim());
            }
        }
        return sortableHeaders;
    }

    public void sortColumnAscending(String headerText) {
        WebElement header = getHeaderByText(headerText);
        header.click(); // first click → ascending
    }

    public void sortColumnDescending(String headerText) {
        WebElement header = getHeaderByText(headerText);
        header.click(); // second click → descending
    }

    public void scrollToHeader(String headerText) {
        WebElement header = getHeaderByText(headerText);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", header);
    }

    private WebElement getHeaderByText(String headerText) {
        List<WebElement> headers = driver.findElements(By.cssSelector("thead th"));
        for (WebElement header : headers) {
            if (header.getText().trim().equalsIgnoreCase(headerText)) {
                return header;
            }
        }
        throw new NoSuchElementException("Header not found: " + headerText);
    }
}
