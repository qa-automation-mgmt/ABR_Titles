package com.abr.titles.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.abr.titles.utils.ExtentManager;
import com.abr.titles.utils.TestReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Base64;

public class TestListener implements ITestListener {
    
    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("========================================");
        System.out.println("📊 Test Suite Started: " + context.getName());
        System.out.println("========================================");
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        // Reset step counter for each new test
        TestReporter.resetStepCounter();
        
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
        ExtentManager.setTest(extentTest);
        
        extentTest.info("Test Started: " + result.getMethod().getMethodName());
        extentTest.info("Class: " + result.getTestClass().getName());
        extentTest.info("Description: " + (result.getMethod().getDescription() != null ? 
                       result.getMethod().getDescription() : "No description provided"));
        
        System.out.println("\n🧪 Test Started: " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest extentTest = test.get();
        long duration = (result.getEndMillis() - result.getStartMillis()) / 1000;
        
        extentTest.log(Status.PASS, MarkupHelper.createLabel("✅ Test PASSED: " + result.getMethod().getMethodName(), ExtentColor.GREEN));
        extentTest.info("Execution Time: " + duration + " seconds");
        
        System.out.println("✅ Test PASSED: " + result.getMethod().getMethodName() + " (" + duration + "s)");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest extentTest = test.get();
        long duration = (result.getEndMillis() - result.getStartMillis()) / 1000;
        
        extentTest.log(Status.FAIL, MarkupHelper.createLabel("❌ Test FAILED: " + result.getMethod().getMethodName(), ExtentColor.RED));
        extentTest.fail("Failure Reason: " + result.getThrowable().getMessage());
        extentTest.fail(result.getThrowable());
        extentTest.info("Execution Time: " + duration + " seconds");
        
        // Capture screenshot on failure
        try {
            Object testClass = result.getInstance();
            WebDriver driver = (WebDriver) testClass.getClass().getField("driver").get(testClass);
            
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                String base64Screenshot = Base64.getEncoder().encodeToString(screenshot);
                extentTest.addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot - " + result.getMethod().getMethodName());
                System.out.println("📸 Screenshot captured for failed test");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not capture screenshot: " + e.getMessage());
        }
        
        // Log the full stack trace for debugging
        System.err.println("❌ Test FAILED: " + result.getMethod().getMethodName() + " (" + duration + "s)");
        System.err.println("   Failure Reason: " + result.getThrowable().getMessage());
        
        // Print assertion errors in detail
        if (result.getThrowable() instanceof AssertionError) {
            System.err.println("\n   ========== ASSERTION FAILURES ==========");
            System.err.println(result.getThrowable().getMessage());
            System.err.println("   =========================================\n");
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest extentTest = test.get();
        extentTest.log(Status.SKIP, MarkupHelper.createLabel("⏭️ Test SKIPPED: " + result.getMethod().getMethodName(), ExtentColor.YELLOW));
        extentTest.skip(result.getThrowable());
        
        System.out.println("⏭️ Test SKIPPED: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            System.out.println("   Reason: " + result.getThrowable().getMessage());
        }
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n========================================");
        System.out.println("📊 Test Suite Finished: " + context.getName());
        System.out.println("========================================");
        System.out.println("✅ Passed: " + context.getPassedTests().size());
        System.out.println("❌ Failed: " + context.getFailedTests().size());
        System.out.println("⏭️ Skipped: " + context.getSkippedTests().size());
        System.out.println("========================================");
        
        // Print failed test names if any
        if (context.getFailedTests().size() > 0) {
            System.out.println("\n❌ Failed Tests:");
            context.getFailedTests().getAllResults().forEach(result -> {
                System.out.println("   - " + result.getMethod().getMethodName());
            });
            System.out.println("========================================");
        }
        
        ExtentManager.flush();
        System.out.println("📄 Extent Report generated: " + ExtentManager.getReportPath());
        System.out.println("📄 TestNG Report available in: target/surefire-reports/index.html");
        System.out.println("========================================\n");
    }
}
