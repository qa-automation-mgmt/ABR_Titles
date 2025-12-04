package com.seg.titles.base;

import com.seg.titles.utils.SharedDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;

import java.time.Duration;

public class MenuClickTest extends BaseTest {

    @Test
    public void testMenuButtonClick() throws Exception {

        WebDriver driver = SharedDriver.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Locator for Main Menu button
        By mainMenu = By.xpath("//button[normalize-space()='Main Menu']");

        // Click Main Menu
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(mainMenu));
        btn.click();
        System.out.println("✔ Main Menu clicked!");

        // Wait after click
        Thread.sleep(3000);

        // 👍 Correct expected redirect URL
        wait.until(ExpectedConditions.urlContains("partnerportal-dev.abramsbooks.com"));
        System.out.println("✔ Redirected to Partner Portal");

        // Another short wait for stabilization
        Thread.sleep(3000);

        System.out.println("✔ Test completed successfully!");
    }
}
