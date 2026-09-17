package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TransactionsPage;

public class TransactionsTest extends BaseTest {

    private TransactionsPage transactionsPage;

    // TestNG always runs the superclass's @BeforeMethod (BaseTest#setUp) before
    // this one, so the driver is already initialized by the time this runs.
    @BeforeMethod
    public void goToTransactionsPage() {
        DashboardPage dashboardPage = loginAsDemoEmployee();
        dashboardPage.goToTransactions();
        transactionsPage = new TransactionsPage(driver);
    }

    @Test
    public void testTransactionsPageOpens() {
        Assert.assertTrue(transactionsPage.isLoaded(), "Transactions page should open and show its heading.");
    }

    @Test
    public void testTransactionsAreDisplayed() {
        Assert.assertEquals(transactionsPage.getTransactionRowCount(), 4,
                "The 4 seeded demo transactions should be displayed.");
        Assert.assertTrue(transactionsPage.isTransactionDisplayed("Coffee Voucher"));
    }

    @Test
    public void testSearchTransaction() {
        transactionsPage.searchTransaction("Amazon");

        Assert.assertEquals(transactionsPage.getTransactionRowCount(), 1,
                "Only the matching transaction should remain visible after the search.");
        Assert.assertTrue(transactionsPage.isTransactionDisplayed("Amazon Shopping"));
    }
}
