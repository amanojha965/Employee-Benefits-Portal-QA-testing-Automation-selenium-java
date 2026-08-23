package tests;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.DashboardPage;
import pages.LoginPage;
import utils.DriverFactory;
import utils.ScreenshotUtil;

/**
 * Common setup shared by every test class:
 *  - starts a fresh browser before each test
 *  - quits the browser after each test
 *  - saves a screenshot automatically whenever a test fails
 *
 * Login itself is deliberately NOT done here, since LoginTest needs to
 * exercise the login page directly. Subclasses that need an already
 * logged-in session call loginAsDemoEmployee() at the start of the test.
 */
public class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;

    private static final String DEMO_EMAIL = "employee@test.com";
    private static final String DEMO_PASSWORD = "Test@123";

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        loginPage = new LoginPage(driver);
    }

    /** Logs in with the demo employee account and lands on the Dashboard. */
    protected DashboardPage loginAsDemoEmployee() {
        loginPage.login(DEMO_EMAIL, DEMO_PASSWORD);
        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.isLoaded();
        return dashboardPage;
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenshotUtil.capture(driver, result.getMethod().getMethodName());
        }
        DriverFactory.quitDriver(driver);
    }
}
