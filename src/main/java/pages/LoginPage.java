package pages;

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

public class LoginPage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators (assuming standard IDs; adjust as per actual application)
    private By usernameField = By.name("username ");
    private By passwordField = By.name("password");
    private By loginButton = By.xpath("//button[@type='submit']");
    private By errorMessage = By.xpath("//p[contains(@class,'oxd-alert-content-text')]");

    private By dashboardHeader = By.xpath("//h6[text()='Dashboard']");

    public LoginPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInstance().getTimeout()));
    }

    /**
     * Enters the username into the username field.
     * @param username The username to enter.
     */
    public void enterUsername(String username) {
        logger.info("Entering username: " + username);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        element.clear();
        element.sendKeys(username);
    }

    /**
     * Enters the password into the password field.
     * @param password The password to enter.
     */
    public void enterPassword(String password) {
        logger.info("Entering password");
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        element.clear();
        element.sendKeys(password);
    }

    /**
     * Clicks the login button.
     */
    public void clickLogin() {
        logger.info("Clicking login button");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        element.click();
    }

    /**
     * Performs a complete login by entering username, password, and clicking login.
     * @param username The username.
     * @param password The password.
     */
    public void login(String username, String password) {
        logger.info("Performing login for user: " + username);
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
    public boolean isErrorDisplayed() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMessage)
        ).isDisplayed();
    }

    public boolean isDashboardDisplayed() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(dashboardHeader)
        ).isDisplayed();
    }
}