package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

public class TransactionsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageHeading = By.cssSelector(".page-header h2");
    private final By searchBox = By.id("searchTransaction");
    private final By tableRows = By.cssSelector("#transactionsBody tr");

    public TransactionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
    }

    public boolean isLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading)).getText().equalsIgnoreCase("Transactions");
    }

    public int getTransactionRowCount() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("transactionsBody")));
        return driver.findElements(tableRows).size();
    }

    public void searchTransaction(String query) {
        WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        box.clear();
        box.sendKeys(query);
    }

    public boolean isTransactionDisplayed(String transactionName) {
        return driver.findElements(tableRows).stream()
                .anyMatch(row -> row.findElement(By.cssSelector("td:first-child")).getText().equalsIgnoreCase(transactionName));
    }
}
