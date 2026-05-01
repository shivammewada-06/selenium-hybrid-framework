package base;

import factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;


import java.util.Properties;

public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected Properties prop;

    @BeforeMethod
    public void setUp() {
        logger.info("Initializing WebDriver...");
        prop = ConfigReader.getInstance().getProperties();  // Fixed: Use singleton instance
        boolean headless = Boolean.parseBoolean(prop.getProperty("headless", "false"));
        DriverFactory.initDriver(headless);
        driver = DriverFactory.getDriver();
        //String url = prop.getProperty("url");
        String url = prop.getProperty("baseUrl");
        System.out.println("URL is: " + url);
        if (url != null) {
            driver.get(url);
            logger.info("Navigated to URL: " + url);
        } else {
            logger.warn("URL not found in config.properties");
        }
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Quitting WebDriver...");
        DriverFactory.quitDriver();
    }
}