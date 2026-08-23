package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.RewardsPage;

public class RewardsTest extends BaseTest {

    private RewardsPage rewardsPage;

    // TestNG always runs the superclass's @BeforeMethod (BaseTest#setUp) before
    // this one, so the driver is already initialized by the time this runs.
    // Logging in also resets points back to the demo starting balance (750),
    // so every test below starts from the same, predictable state.
    @BeforeMethod
    public void goToRewardsPage() {
        DashboardPage dashboardPage = loginAsDemoEmployee();
        dashboardPage.goToRewards();
        rewardsPage = new RewardsPage(driver);
    }

    @Test
    public void testRewardPointsDisplayed() {
        Assert.assertEquals(rewardsPage.getCurrentPoints(), 750,
                "Rewards page should show the employee's current point balance.");
    }

    @Test
    public void testRewardsAreDisplayed() {
        Assert.assertEquals(rewardsPage.getVisibleRewardCount(), 5,
                "All 5 demo rewards should be displayed.");
        Assert.assertTrue(rewardsPage.isRewardDisplayed("Coffee Voucher"));
        Assert.assertTrue(rewardsPage.isRewardDisplayed("Travel Voucher"));
    }

    @Test
    public void testSuccessfulRewardRedemption() {
        rewardsPage.redeemReward("Coffee Voucher");

        String message = rewardsPage.getRedeemMessage("Coffee Voucher");
        Assert.assertTrue(message.toLowerCase().contains("success"),
                "Redeeming an affordable reward (100 pts) should succeed.");
    }

    @Test
    public void testPointsDeductedAfterRedemption() {
        rewardsPage.redeemReward("Movie Voucher"); // costs 250 points
        rewardsPage.getRedeemMessage("Movie Voucher");

        Assert.assertEquals(rewardsPage.getCurrentPoints(), 500,
                "Points should drop from 750 to 500 after redeeming the 250-point Movie Voucher.");
    }

    @Test
    public void testInsufficientPointsValidation() {
        rewardsPage.redeemReward("Travel Voucher"); // costs 1000 points, balance is only 750

        String message = rewardsPage.getRedeemMessage("Travel Voucher");
        Assert.assertTrue(message.toLowerCase().contains("insufficient"),
                "Redeeming a reward that costs more than the current balance should show an insufficient-points error.");
    }
}
