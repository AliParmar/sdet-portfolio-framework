package com.aliparmar.sdet.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Owns the ExtentReports lifecycle.
 * Creates one shared report, hands out a test entry per test, and writes the HTML at the end.
 */
public class ExtentReportManager {

    private static ExtentReports extent;

    // Builds the report once and points it at the output HTML file.
    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark =
                    new ExtentSparkReporter("target/extent-reports/ExtentReport.html");
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("ParaBank Automation Report");
            spark.config().setReportName("SDET Portfolio Framework - Test Results");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Application", "ParaBank");
            extent.setSystemInfo("Environment", "Demo");
            extent.setSystemInfo("Tester", "Ali Parmar");
        }
        return extent;
    }

    // Creates a named entry in the report for a single test.
    public static ExtentTest createTest(String testName) {
        return getInstance().createTest(testName);
    }

    // Writes everything to the HTML file. Must be called at the end of the run.
    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}