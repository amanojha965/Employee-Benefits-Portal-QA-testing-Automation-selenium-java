package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;

public class RewardsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageHeading = By.cssSelector(".page-header h2");
    private final By currentPoints = By.id("currentPoints");
    private final By rewardCards = By.cssSelector(".reward-card");

    public RewardsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
    }

    public boolean isLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading)).getText()
                .equalsIgnoreCase("Rewards");
    }

    public int getCurrentPoints() {
        return Integer
                .parseInt(wait.until(ExpectedConditions.visibilityOfElementLocated(currentPoints)).getText().trim());
    }

    public int getVisibleRewardCount() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rewardsList")));
        return driver.findElements(rewardCards).size();
    }

    public boolean isRewardDisplayed(String rewardName) {
        return driver.findElements(rewardCards).stream()
                .anyMatch(card -> card.findElement(By.cssSelector(".reward-name")).getText()
                        .equalsIgnoreCase(rewardName));
    }

    private WebElement findCardByName(String rewardName) {
        List<WebElement> cards = driver.findElements(rewardCards);
        for (WebElement card : cards) {
            String name = card.findElement(By.cssSelector(".reward-name")).getText();
            if (name.equalsIgnoreCase(rewardName)) {
                return card;
            }
        }
        throw new RuntimeException("Reward card not found: " + rewardName);
    }

    public void redeemReward(String rewardName) {
        WebElement card = findCardByName(rewardName);
        WebElement redeemButton = card.findElement(By.cssSelector(".redeem-btn"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", redeemButton);
    }

    public String getRedeemMessage(String rewardName) {
        WebElement card = findCardByName(rewardName);
        By messageLocator = By.cssSelector(".inline-message");
        wait.until(d -> !card.findElement(messageLocator).getAttribute("class").contains("hidden"));
        return card.findElement(messageLocator).getText();
    }
}
