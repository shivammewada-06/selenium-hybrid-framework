package factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import utils.ConfigReader;

import java.util.Properties;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static Properties prop;

    // Initialize the driver based on config.properties and headless flag
    public static WebDriver initDriver(boolean headless) {
        prop = ConfigReader.getInstance().getProperties();  // Fixed: Use singleton instance

        String browserName = prop.getProperty("browser").toLowerCase().trim();

        WebDriver webDriver = null;

        if (browserName.equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if (headless) {
                options.addArguments("--headless");
            }
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            webDriver = new ChromeDriver(options);
        } else if (browserName.equals("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            if (headless) {
                options.addArguments("--headless");
            }
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            webDriver = new EdgeDriver(options);
        } else {
            throw new IllegalArgumentException("Browser not supported: " + browserName);
        }

        driver.set(webDriver);
        return webDriver;
    }

    // Get the current thread's driver instance
    public static WebDriver getDriver() {
        return driver.get();
    }

    // Quit the driver and remove from ThreadLocal
    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            webDriver.quit();
            driver.remove();
        }
    }
}