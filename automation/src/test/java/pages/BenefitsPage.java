package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;

public class BenefitsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageHeading = By.cssSelector(".page-header h2");
    private final By searchBox = By.id("searchBenefit");
    private final By categoryDropdown = By.id("categoryFilter");
    private final By benefitCards = By.cssSelector(".benefit-card");
    private final By modal = By.id("benefitModal");
    private final By modalTitle = By.id("modalBenefitName");
    private final By modalCloseBtn = By.id("modalCloseBtn");

    public BenefitsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
    }

    public boolean isLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading)).getText().equalsIgnoreCase("Benefits");
    }

    public int getVisibleBenefitCount() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("benefitsList")));
        return driver.findElements(benefitCards).size();
    }

    public void searchBenefit(String query) {
        WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        box.clear();
        box.sendKeys(query);
    }

    public void filterByCategory(String category) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(categoryDropdown));
        new Select(dropdown).selectByVisibleText(category.equals("All") ? "All Categories" : category);
    }

    private WebElement findCardByName(String benefitName) {
        List<WebElement> cards = driver.findElements(benefitCards);
        for (WebElement card : cards) {
            String name = card.findElement(By.cssSelector(".benefit-name")).getText();
            if (name.equalsIgnoreCase(benefitName)) {
                return card;
            }
        }
        throw new RuntimeException("Benefit card not found: " + benefitName);
    }

    public boolean isBenefitDisplayed(String benefitName) {
        try {
            return findCardByName(benefitName).isDisplayed();
        } catch (RuntimeException e) {
            return false;
        }
    }

    public void openBenefitDetails(String benefitName) {
        WebElement card = findCardByName(benefitName);
        card.findElement(By.cssSelector("[data-action='details']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(modal));
    }

    public String getModalTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle)).getText();
    }

    public void closeModal() {
        wait.until(ExpectedConditions.elementToBeClickable(modalCloseBtn)).click();
    }

    public void redeemBenefit(String benefitName) {
        WebElement card = findCardByName(benefitName);
        card.findElement(By.cssSelector("[data-action='redeem']")).click();
    }

    public String getRedeemMessage(String benefitName) {
        WebElement card = findCardByName(benefitName);
        By messageLocator = By.cssSelector(".inline-message");
        wait.until(d -> !card.findElement(messageLocator).getAttribute("class").contains("hidden"));
        return card.findElement(messageLocator).getText();
    }
}
