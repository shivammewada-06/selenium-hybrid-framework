package utils;

import factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private static final Logger logger = LogManager.getLogger(WaitUtils.class);

    /**
     * Waits for the element located by the given By locator to be visible.
     * @param locator The By locator of the element.
     * @return The WebElement once it is visible.
     */
    public static WebElement waitForVisibility(By locator) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new RuntimeException("WebDriver is not initialized. Call DriverFactory.initDriver() first.");
        }
        int timeout = ConfigReader.getInstance().getTimeout();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        logger.info("Waiting for visibility of element: " + locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for the element located by the given By locator to be clickable.
     * @param locator The By locator of the element.
     * @return The WebElement once it is clickable.
     */
    public static WebElement waitForClickable(By locator) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new RuntimeException("WebDriver is not initialized. Call DriverFactory.initDriver() first.");
        }
        int timeout = ConfigReader.getInstance().getTimeout();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        logger.info("Waiting for element to be clickable: " + locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
}