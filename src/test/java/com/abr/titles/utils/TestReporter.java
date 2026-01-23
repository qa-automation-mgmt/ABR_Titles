package com.abr.titles.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.Reporter;

/**
 * Central utility for logging test steps to both console and ExtentReports.
 * Provides detailed step-by-step reporting for better debugging.
 */
public class TestReporter {
    
    private static int stepCounter = 0;
    
    /**
     * Reset step counter at the start of each test
     */
    public static void resetStepCounter() {
        stepCounter = 0;
    }
    
    /**
     * Log a step that passed
     */
    public static void logPass(String message) {
        stepCounter++;
        String stepMsg = "Step " + stepCounter + " - ✅ PASS: " + message;
        System.out.println(stepMsg);
        Reporter.log(stepMsg, true);
        
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.PASS, MarkupHelper.createLabel(message, ExtentColor.GREEN));
        }
    }
    
    /**
     * Log a step that failed (but test continues with soft assertion)
     */
    public static void logFail(String message) {
        stepCounter++;
        String stepMsg = "Step " + stepCounter + " - ❌ FAIL: " + message;
        System.err.println(stepMsg);
        Reporter.log(stepMsg, true);
        
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
        }
    }
    
    /**
     * Log a step that failed with exception details
     */
    public static void logFail(String message, Exception e) {
        stepCounter++;
        String stepMsg = "Step " + stepCounter + " - ❌ FAIL: " + message;
        String errorDetails = "Error: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        
        System.err.println(stepMsg);
        System.err.println("    " + errorDetails);
        Reporter.log(stepMsg + " | " + errorDetails, true);
        
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
            test.fail("Exception: " + errorDetails);
            test.fail(e);
        }
    }
    
    /**
     * Log a warning (non-critical issue)
     */
    public static void logWarning(String message) {
        stepCounter++;
        String stepMsg = "Step " + stepCounter + " - ⚠️ WARNING: " + message;
        System.out.println(stepMsg);
        Reporter.log(stepMsg, true);
        
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.WARNING, MarkupHelper.createLabel(message, ExtentColor.ORANGE));
        }
    }
    
    /**
     * Log an informational message
     */
    public static void logInfo(String message) {
        String stepMsg = "ℹ️  INFO: " + message;
        System.out.println(stepMsg);
        Reporter.log(stepMsg, true);
        
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }
    
    /**
     * Log a skip message
     */
    public static void logSkip(String message) {
        stepCounter++;
        String stepMsg = "Step " + stepCounter + " - ⏭️ SKIP: " + message;
        System.out.println(stepMsg);
        Reporter.log(stepMsg, true);
        
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, MarkupHelper.createLabel(message, ExtentColor.YELLOW));
        }
    }
    
    /**
     * Create a node (sub-section) in the report for grouping related steps
     */
    public static ExtentTest createNode(String nodeName) {
        ExtentTest parentTest = ExtentManager.getTest();
        if (parentTest != null) {
            return parentTest.createNode(nodeName);
        }
        return null;
    }
    
    /**
     * Log step to a specific node
     */
    public static void logToNode(ExtentTest node, Status status, String message) {
        if (node != null) {
            node.log(status, message);
        }
    }
}

