package utils;

import factory.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOTS_DIR = System.getProperty("user.dir") + "/screenshots";

    /**
     * Captures a screenshot of the current page and saves it to the screenshots folder.
     * The screenshot file name includes the test name and timestamp for uniqueness.
     *
     * @param testName The name of the test or scenario for which the screenshot is taken.
     * @return The absolute file path of the saved screenshot, or null if capture fails.
     */
    public static String captureScreenshot(String testName) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            logger.error("WebDriver is not initialized. Cannot capture screenshot.");
            return null;
        }

        // Create screenshots directory if it doesn't exist
        File screenshotsDir = new File(SCREENSHOTS_DIR);
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs();
        }

        // Generate timestamp for unique file name
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = SCREENSHOTS_DIR + File.separator + fileName;

        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(sourceFile, destFile);
            logger.info("Screenshot captured and saved to: " + filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("Failed to capture screenshot for test: " + testName, e);
            return null;
        }
    }
}