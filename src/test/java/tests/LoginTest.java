package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ExcelUtils;
import utils.WaitUtils;
import base.BaseTest;

@Listeners(listeners.TestListener.class)

public class LoginTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(LoginTest.class);

    @DataProvider(name = "loginData", parallel = true) // Enable parallel execution for DataProvider
    public Object[][] getLoginData() {
        // Assuming the Excel sheet "LoginData" has columns: username, password
        return ExcelUtils.getTestData("LoginData");
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password,String expectedResult) {
        logger.info("Starting login test for user: " + username);

        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);
        if(expectedResult.equalsIgnoreCase("valid")) {

            Assert.assertTrue(loginPage.isDashboardDisplayed(),
                    "Expected successful login but dashboard not visible");

            logger.info("Positive login test passed for user: " + username);

        } else {

            Assert.assertTrue(loginPage.isErrorDisplayed(),
                    "Expected error message but not displayed");

            logger.info("Negative login test passed for user: " + username);
        }

/*
        // Assertion: Verify login success by checking for a post-login element (e.g., dashboard or logout button)
        // Adjust the locator based on your application's post-login page
        By dashboardElement = By.id("dashboard"); // Example locator; replace with actual
        WebElement element = WaitUtils.waitForVisibility(dashboardElement);
        Assert.assertNotNull(element, "Login failed: Dashboard element not found");
        Assert.assertTrue(element.isDisplayed(), "Login failed: Dashboard not displayed");

        logger.info("Login test passed for user: " + username);*/
    }
}