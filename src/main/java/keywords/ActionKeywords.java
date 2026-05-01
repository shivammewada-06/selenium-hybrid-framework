package keywords;

import factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

public class ActionKeywords {

    private static final Logger logger = LogManager.getLogger(ActionKeywords.class);

    /**
     * Performs the specified action on the web element identified by locatorType and locatorValue.
     * Uses testData for actions that require input (e.g., type, open).
     *
     * @param action       The action to perform (open, click, type, wait, clear).
     * @param locatorType  The type of locator (id, name, xpath, css).
     * @param locatorValue The value of the locator.
     * @param testData     Additional data for the action (e.g., URL for open, text for type).
     */
    public static void performAction(String action, String locatorType, String locatorValue, String testData) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new RuntimeException("WebDriver is not initialized. Call DriverFactory.initDriver() first.");
        }

        By locator = getLocator(locatorType, locatorValue);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInstance().getTimeout()));

        try {
            switch (action.toLowerCase()) {
                case "open":
                    logger.info("Opening URL: " + testData);
                    driver.get(testData);
                    break;
                case "click":
                    logger.info("Clicking on element: " + locatorType + " = " + locatorValue);
                    WebElement clickElement = wait.until(ExpectedConditions.elementToBeClickable(locator));
                    clickElement.click();
                    break;
                case "type":
                    logger.info("Typing '" + testData + "' into element: " + locatorType + " = " + locatorValue);
                    WebElement typeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    typeElement.clear();
                    typeElement.sendKeys(testData);
                    break;
                case "wait":
                    logger.info("Waiting for element to be visible: " + locatorType + " = " + locatorValue);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    break;
                case "clear":
                    logger.info("Clearing element: " + locatorType + " = " + locatorValue);
                    WebElement clearElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    clearElement.clear();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported action: " + action);
            }
        } catch (Exception e) {
            logger.error("Error performing action '" + action + "' on element " + locatorType + " = " + locatorValue, e);
            throw new RuntimeException("Failed to perform action: " + action, e);
        }
    }

    /**
     * Creates a By locator based on the locatorType and locatorValue.
     *
     * @param locatorType  The type of locator (id, name, xpath, css).
     * @param locatorValue The value of the locator.
     * @return The By locator.
     */
    private static By getLocator(String locatorType, String locatorValue) {
        switch (locatorType.toLowerCase()) {
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            case "css":
                return By.cssSelector(locatorValue);
            default:
                throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        }
    }
}