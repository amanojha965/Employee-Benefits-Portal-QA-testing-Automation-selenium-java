package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

public class DashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By employeeName = By.id("employeeName");
    private final By rewardPoints = By.id("rewardPoints");
    private final By benefitsCount = By.id("benefitsCount");
    private final By recentTransactionsRows = By.cssSelector("#recentTransactionsBody tr");

    private final By navDashboard = By.id("navDashboard");
    private final By navBenefits = By.id("navBenefits");
    private final By navRewards = By.id("navRewards");
    private final By navTransactions = By.id("navTransactions");
    private final By navLogout = By.id("navLogout");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
    }

    public boolean isLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(employeeName)).isDisplayed();
    }

    public String getEmployeeName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(employeeName)).getText();
    }

    public int getRewardPoints() {
        return Integer.parseInt(wait.until(ExpectedConditions.visibilityOfElementLocated(rewardPoints)).getText().trim());
    }

    public int getBenefitsCount() {
        return Integer.parseInt(wait.until(ExpectedConditions.visibilityOfElementLocated(benefitsCount)).getText().trim());
    }

    public int getRecentTransactionsCount() {
        return driver.findElements(recentTransactionsRows).size();
    }

    public void goToBenefits() {
        wait.until(ExpectedConditions.elementToBeClickable(navBenefits)).click();
    }

    public void goToRewards() {
        wait.until(ExpectedConditions.elementToBeClickable(navRewards)).click();
    }

    public void goToTransactions() {
        wait.until(ExpectedConditions.elementToBeClickable(navTransactions)).click();
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(navLogout)).click();
    }
}
