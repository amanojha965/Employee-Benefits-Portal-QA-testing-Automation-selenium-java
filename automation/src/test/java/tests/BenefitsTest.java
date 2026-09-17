package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BenefitsPage;
import pages.DashboardPage;

public class BenefitsTest extends BaseTest {

    private BenefitsPage benefitsPage;

    // TestNG always runs the superclass's @BeforeMethod (BaseTest#setUp) before
    // this one, so the driver is already initialized by the time this runs.
    @BeforeMethod
    public void goToBenefitsPage() {
        DashboardPage dashboardPage = loginAsDemoEmployee();
        dashboardPage.goToBenefits();
        benefitsPage = new BenefitsPage(driver);
    }

    @Test
    public void testBenefitsPageOpens() {
        Assert.assertTrue(benefitsPage.isLoaded(), "Benefits page should open and show its heading.");
    }

    @Test
    public void testBenefitsAreDisplayed() {
        Assert.assertEquals(benefitsPage.getVisibleBenefitCount(), 10,
                "All 10 demo benefits should be displayed by default.");
    }

    @Test
    public void testSearchBenefit() {
        benefitsPage.searchBenefit("Zomato");

        Assert.assertTrue(benefitsPage.isBenefitDisplayed("Zomato Food"),
                "Searching 'Zomato' should show the Zomato Food benefit.");
        Assert.assertEquals(benefitsPage.getVisibleBenefitCount(), 1,
                "Only the matching benefit should remain visible after the search.");
    }

    @Test
    public void testFilterByCategory() {
        benefitsPage.filterByCategory("Fitness");

        Assert.assertEquals(benefitsPage.getVisibleBenefitCount(), 2,
                "There are 2 Fitness benefits (Cult Fitness, Decathlon Fitness).");
        Assert.assertTrue(benefitsPage.isBenefitDisplayed("Cult Fitness"));
        Assert.assertTrue(benefitsPage.isBenefitDisplayed("Decathlon Fitness"));
    }

    @Test
    public void testOpenBenefitDetails() {
        benefitsPage.openBenefitDetails("Amazon Shopping");

        Assert.assertEquals(benefitsPage.getModalTitle(), "Amazon Shopping",
                "The details modal should show the selected benefit's name.");
        benefitsPage.closeModal();
    }

    @Test
    public void testRedeemBenefit() {
        benefitsPage.redeemBenefit("Zomato Food");

        String message = benefitsPage.getRedeemMessage("Zomato Food");
        Assert.assertTrue(message.toLowerCase().contains("redeemed successfully"),
                "Redeeming an affordable benefit should show a success message.");
    }
}
