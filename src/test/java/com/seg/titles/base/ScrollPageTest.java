package com.seg.titles.base;
import com.seg.titles.utils.SharedDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;

public class ScrollPageTest extends BaseTest {

    @Test(priority = 3)
    public void scrollPage() {
        WebDriver driver = SharedDriver.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            System.out.println("🔹 Starting ScrollPageTest...");

            WebElement tableContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".MuiTableContainer-root")));
            System.out.println("✅ Table found! Starting deep scroll...");

            JavascriptExecutor js = (JavascriptExecutor) driver;
            int previousCount = 0;
            int sameCountCounter = 0;

            // === Vertical Scroll (Down) ===
            while (sameCountCounter < 3) {
                js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", tableContainer);
                Thread.sleep(2000);

                List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
                int currentCount = rows.size();
                System.out.println("📈 Loaded more records: now " + currentCount + " total rows.");

                if (currentCount > previousCount) {
                    previousCount = currentCount;
                    sameCountCounter = 0;
                } else {
                    sameCountCounter++;
                    System.out.println("⏳ No new records detected (" + sameCountCounter + "/3).");
                }
            }

            System.out.println("✅ Reached last record — total " + previousCount + " rows loaded.");

            // === Horizontal Scroll (Right and Left) ===
            System.out.println("↔️ Scrolling horizontally to view all columns...");

            for (int i = 0; i < 6; i++) {
                js.executeScript("arguments[0].scrollLeft += 400;", tableContainer);
                Thread.sleep(250); // faster horizontal scroll
            }

            System.out.println("⬅️ Scrolling back left...");
            for (int i = 0; i < 6; i++) {
                js.executeScript("arguments[0].scrollLeft -= 400;", tableContainer);
                Thread.sleep(200);
            }

            // === Vertical Scroll (Up) ===
            System.out.println("🔁 Now scrolling back up slowly...");
            for (int i = 0; i < 10; i++) {
                js.executeScript("arguments[0].scrollTop -= arguments[0].scrollHeight / 10;", tableContainer);
                Thread.sleep(400);
            }

            System.out.println("✅ Scrolled all the way up. Test complete.");

        } catch (Exception e) {
            throw new RuntimeException("❌ Scroll test failed: " + e.getMessage());
        }
    }
}
