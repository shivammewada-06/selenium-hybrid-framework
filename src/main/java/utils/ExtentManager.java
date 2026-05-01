package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ExtentManager {

    private static final Logger logger = LogManager.getLogger(ExtentManager.class);
    private static final String REPORT_PATH = System.getProperty("user.dir") + "/extent-report.html";
    private static ExtentReports extent;

    // Private constructor to prevent instantiation
    private ExtentManager() {}

    /**
     * Returns the singleton instance of ExtentReports, creating it if necessary.
     * This method is thread-safe.
     *
     * @return The ExtentReports instance.
     */
    public static synchronized ExtentReports getExtent() {
        if (extent == null) {
            logger.info("Initializing ExtentReports...");
            extent = new ExtentReports();

            // Configure the HTML reporter
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Hybrid Selenium Framework Report");
            sparkReporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.DARK);

            extent.attachReporter(sparkReporter);

            // Add system information
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
            extent.setSystemInfo("Browser", ConfigReader.getInstance().getBrowser());
            extent.setSystemInfo("Environment", "QA"); // Can be made configurable

            logger.info("ExtentReports initialized successfully. Report will be saved to: " + REPORT_PATH);
        }
        return extent;
    }

    /**
     * Flushes the report. Call this at the end of test execution.
     */
    public static void flush() {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports flushed.");
        }
    }
}