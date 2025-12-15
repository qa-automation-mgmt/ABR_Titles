package com.seg.titles.tests;

import com.seg.titles.base.BaseTest;
import com.seg.titles.utils.SharedDriver;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class TitleSearchTest extends BaseTest {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    Random random = new Random();

    @Test
    public void runRecorderFlow() throws Exception {

        driver = SharedDriver.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        actions = new Actions(driver);

        System.out.println(">>> STARTING RECORDER JSON FLOW (RANDOM FILTERS) <<<");

        waitForOverlay();

        // ---------------------------------------------------------------
        // Example: Navigate to page
        // ---------------------------------------------------------------
        driver.get("https://titles-dev.abramsbooks.com");
        waitForOverlay();

        // ---------------------------------------------------------------
        // Randomly select filters (simulate your JSON clicks)
        // ---------------------------------------------------------------
        randomSelect(By.id("isbn-selector"));        // Random ISBN
        randomSelect(By.id("title-selector"));       // Random Title
        randomSelect(By.id("author-selector"));      // Random Author
        randomSelect(By.id("season"));               // Random Season
        randomSelect(By.id("division"));             // Random Division
        randomSelect(By.id("imprint"));              // Random Imprint
        randomSelect(By.id("format"));               // Random Format
        randomSelect(By.id("managingEditor"));       // Random Managing Editor
        randomSelect(By.id("editor"));               // Random Editor
        randomSelect(By.id("bisac_status"));         // Random BISAC Status
        randomSelect(By.id("licensor"));             // Random Licensor
        randomSelect(By.id("series"));               // Random Series

        // ---------------------------------------------------------------
        // Export button click
        // ---------------------------------------------------------------
        safeClick(By.xpath("//button[contains(text(),'Export')]"));

        System.out.println(">>> RECORDER FLOW WITH RANDOM FILTERS COMPLETED <<<");
    }

    // =================================================================
    // SAFE CLICK
    // =================================================================
    private void safeClick(By locator) throws Exception {
        waitForOverlay();

        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollIntoView(locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator));

        try {
            el.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }

        waitForOverlay();
    }

    // =================================================================
    // TYPE TEXT
    // =================================================================
    private void typeText(By locator, String text) {
        waitForOverlay();

        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        scrollIntoView(locator);

        el.clear();
        el.sendKeys(text);
    }

    // =================================================================
    // SCROLL INTO VIEW
    // =================================================================
    private void scrollIntoView(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior:'smooth', block:'center'});",
                    element
            );
        } catch (Exception ignored) {}
    }

    private void scrollIntoViewElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", element
            );
        } catch (Exception ignored) {}
    }

    // =================================================================
    // RANDOM SELECTOR PICKER
    // =================================================================
    private void randomSelect(By inputLocator) throws Exception {
        safeClick(inputLocator);

        List<WebElement> options = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//li[not(contains(@class,'disabled'))]"))
        );

        if (options.isEmpty()) return;

        WebElement chosen = options.get(random.nextInt(options.size()));

        scrollIntoViewElement(chosen);

        try {
            chosen.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", chosen);
        }

        waitForOverlay();
    }

    // =================================================================
    // INLINE WAIT FOR OVERLAY
    // =================================================================
    private void waitForOverlay() {
        try {
            WebDriverWait overlayWait = new WebDriverWait(driver, Duration.ofSeconds(20));
            overlayWait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".overlay, .loading-spinner") // adjust selector for your app
            ));
        } catch (Exception ignored) {}
    }
}
