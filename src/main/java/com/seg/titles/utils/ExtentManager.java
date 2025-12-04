package com.seg.titles.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    public static ExtentReports createInstance() {
        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("SEG Titles Automation Report");
        spark.config().setDocumentTitle("Automation Test Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Tester", "Ankush Rana");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Application", "SEG Titles");

        return extent;
    }
}
