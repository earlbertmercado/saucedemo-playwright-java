package com.earlbertmercado.playwright.saucedemo.listeners;

import com.earlbertmercado.playwright.saucedemo.browser.BrowserManager;
import com.earlbertmercado.playwright.saucedemo.utils.InitializeProperties;
import com.earlbertmercado.playwright.saucedemo.utils.ScreenshotUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

/**
 * Listener class for TestNG that integrates Extent Reports and Log4j2.
 * It manages test lifecycle reporting, screenshots on failure, and MDC logging context.
 */
public class ExtentReportListener implements ITestListener, ISuiteListener {

    // Configuration constants
    private static final String DEFAULT_LOG_CONTEXT = "-";
    private static final String OUTPUT_FOLDER = "./reports/";
    private static final String REPORT_FILE_NAME = "extent-report.html";
    private static final String TEST_ID = "testId";

    private static final Logger logger = LogManager.getLogger(ExtentReportListener.class);

    // Static report instances; ThreadLocal ensures thread safety during parallel execution
    public static ExtentReports extentReports = init();
    public static ThreadLocal<ExtentTest> extentTestThread = new ThreadLocal<>();

    /**
     * Initializes the Extent Reports engine and the HTML Spark Reporter.
     * @return Configured ExtentReports instance.
     */
    private static ExtentReports init() {
        Properties properties = InitializeProperties.loadProperties();
        Path path = Paths.get(OUTPUT_FOLDER);

        // Ensure the reporting directory exists
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Couldn't create reports directory: " + e.getMessage(), e);
            }
        }

        ExtentReports extentReports = new ExtentReports();
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(OUTPUT_FOLDER + REPORT_FILE_NAME);

        // Configure UI look and feel
        sparkReporter.config().setDocumentTitle("Saucedemo Playwright Automation Report");
        sparkReporter.config().setReportName("Test Results");
        sparkReporter.config().setTheme(Theme.DARK);

        extentReports.attachReporter(sparkReporter);

        String browser = System.getProperty("browser", properties.getProperty("browser"));
        String headless = System.getProperty("isHeadless", properties.getProperty("isHeadless"));

        // Add environment details to the dashboard
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Browser", browser);
        extentReports.setSystemInfo("Headless", headless);

        return extentReports;
    }

    @Override
    public void onStart(ITestContext context) {
        // Set the global execution ID for log tracking
        ThreadContext.put(TEST_ID, DEFAULT_LOG_CONTEXT);
        logger.info("Test Execution Started");
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Generate a unique ID for the current test thread to help filter logs
        String testId = UUID.randomUUID().toString().split("-")[0];
        ThreadContext.put(TEST_ID, testId);

        // Register the test in the Extent Report
        ExtentTest test = extentReports.createTest(
                result.getMethod().getTestClass().getRealClass().getSimpleName()
                        + " / " + result.getMethod().getMethodName());

        extentTestThread.set(test);
        extentTestThread.get().log(Status.INFO, "Test ID: " + ThreadContext.get(TEST_ID));

        logger.info("Test started: {}/{}",
                result.getMethod().getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTestThread.get().log(Status.PASS, "Test passed: " + result.getMethod().getMethodName());
        logger.info("Test passed: {}/{}",
                result.getMethod().getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());

        // Reset log context to global ID
        ThreadContext.put(TEST_ID, DEFAULT_LOG_CONTEXT);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}/{}",
                result.getMethod().getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());

        ExtentTest currentTest = extentTestThread.get();
        currentTest.log(Status.FAIL, "Test failed: " + result.getMethod().getMethodName());
        currentTest.log(Status.FAIL, result.getThrowable().getMessage());
        currentTest.log(Status.FAIL, result.getThrowable());

        // Attempt to capture a screenshot using the current Playwright Page instance
        BrowserManager factory = BrowserManager.getFactoryInstance();
        Page page = (factory != null) ? factory.getPage() : null;

        if (page != null) {
            String screenshotPath = ScreenshotUtils.takeScreenshot(page, result.getMethod().getMethodName());
            if (screenshotPath != null) {
                currentTest.fail("Screenshot: ",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } else {
                currentTest.fail("Screenshot could not be captured.");
            }
        } else {
            currentTest.fail("Page instance was null. No screenshot taken.");
        }

        ThreadContext.put(TEST_ID, DEFAULT_LOG_CONTEXT);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTestThread.get().log(Status.SKIP, "Test skipped: " + result.getMethod().getMethodName());
        extentTestThread.get().log(Status.INFO, result.getThrowable());
        logger.warn("Test skipped: {}/{}",
                result.getMethod().getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
        ThreadContext.put(TEST_ID, DEFAULT_LOG_CONTEXT);
    }

    @Override
    public void onFinish(ITestContext context) {
        ThreadContext.put(TEST_ID, DEFAULT_LOG_CONTEXT);
        // Write all test information to the HTML file
        extentReports.flush();
        logger.info("Test Execution Finished");

        // Clean up ThreadContext to prevent memory leaks in thread pools
        ThreadContext.clearAll();
    }
}