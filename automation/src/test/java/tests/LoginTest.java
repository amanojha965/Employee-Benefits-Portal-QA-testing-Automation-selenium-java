package tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;

public class LoginTest extends BaseTest {

    private static final String VALID_EMAIL = "employee@test.com";
    private static final String VALID_PASSWORD = "Test@123";

    @Test
    public void testSuccessfulLogin() {
        loginPage.login(VALID_EMAIL, VALID_PASSWORD);
        DashboardPage dashboardPage = new DashboardPage(driver);

        Assert.assertTrue(dashboardPage.isLoaded(), "Dashboard should load after a successful login.");
        Assert.assertEquals(dashboardPage.getEmployeeName(), "Priya Sharma",
                "Employee name on the dashboard should match the demo account.");
    }

    @Test
    public void testInvalidUsername() {
        loginPage.login("wrong.user@test.com", VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorDisplayed(), "An error message should appear for an invalid email.");
        Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("invalid"),
                "Error message should indicate invalid credentials.");
    }

    @Test
    public void testInvalidPassword() {
        loginPage.login(VALID_EMAIL, "WrongPassword1");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "An error message should appear for an invalid password.");
        Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("invalid"),
                "Error message should indicate invalid credentials.");
    }

    @Test
    public void testLogout() {
        DashboardPage dashboardPage = loginAsDemoEmployee();
        dashboardPage.logout();

        boolean backOnLoginPage = driver.findElements(By.id("loginBtn")).size() > 0;
        Assert.assertTrue(backOnLoginPage, "Logging out should return the user to the login page.");
        Assert.assertTrue(driver.getCurrentUrl().contains("index.html"),
                "URL should point back to the login page after logout.");
    }
}
