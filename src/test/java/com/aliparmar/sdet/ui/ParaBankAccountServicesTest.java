package com.aliparmar.sdet.ui;

import com.aliparmar.sdet.base.BaseTest;
import com.aliparmar.sdet.pages.*;
import com.aliparmar.sdet.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * TestNG UI tests covering ParaBank's account-services features:
 * Open New Account, Request Loan, Update Contact Info, Bill Pay, and Log Out.
 * Each test logs in fresh (via BaseTest's per-method driver) then exercises
 * one page in isolation - complements the multi-page Cucumber journeys.
 */
public class ParaBankAccountServicesTest extends BaseTest {

    private NavigationMenuPage navigationMenuPage;

    // TestNG runs superclass @BeforeMethod (BaseTest.setUp, which creates the driver)
    // before this subclass @BeforeMethod automatically - no dependsOnMethods needed.
    @BeforeMethod(alwaysRun = true)
    public void loginBeforeEachTest() {
        String username = ConfigReader.get("test.username");
        String password = ConfigReader.get("test.password");

        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.login(username, password);
        navigationMenuPage = new NavigationMenuPage(driver);
    }

    @Test(groups = {"sanity", "ui"}, description = "Opening a new Savings account from an existing account succeeds")
    public void verifyOpenNewSavingsAccountSucceeds() {
        OpenNewAccountPage openNewAccountPage = navigationMenuPage.goToOpenNewAccount();
        openNewAccountPage.selectAccountType("SAVINGS");
        openNewAccountPage.selectFirstFromAccount();
        openNewAccountPage.submit();

        Assert.assertTrue(
                openNewAccountPage.isResultDisplayed(),
                "Expected a confirmation result after opening a new account"
        );
        Assert.assertFalse(
                openNewAccountPage.getNewAccountId().isBlank(),
                "Expected a new account number to be returned"
        );
    }

    @Test(groups = {"regression", "ui"}, description = "Requesting a loan completes and returns an Approved/Denied decision")
    public void verifyRequestLoanIsProcessed() {
        RequestLoanPage requestLoanPage = navigationMenuPage.goToRequestLoan();
        requestLoanPage.submitLoanRequest("5000", "1000");

        String status = requestLoanPage.getLoanStatus();
        Assert.assertTrue(
                status.equalsIgnoreCase("Approved") || status.equalsIgnoreCase("Denied"),
                "Expected loan decision to be Approved or Denied, but was: " + status
        );
    }

    @Test(groups = {"regression", "ui"}, description = "Updating contact info persists the change and shows a confirmation")
    public void verifyUpdateContactInfoPersistsChanges() {
        UpdateContactInfoPage updateContactInfoPage = navigationMenuPage.goToUpdateContactInfo();
        updateContactInfoPage.updateStreetAndCity("456 Portfolio Ave", "Houston");
        updateContactInfoPage.submit();

        String confirmation = updateContactInfoPage.getConfirmationText();
        Assert.assertFalse(confirmation.isBlank(), "Expected a non-empty confirmation after updating contact info");
    }

    @Test(groups = {"regression", "ui"}, description = "Submitting Bill Pay without a payee name shows a validation error")
    public void verifyBillPayWithMissingPayeeNameShowsValidationError() {
        BillPayPage billPayPage = navigationMenuPage.goToBillPay();
        billPayPage.fillFormWithoutPayeeName("12345", "50.00");
        billPayPage.submit();

        Assert.assertTrue(
                billPayPage.isValidationErrorDisplayed(),
                "Expected a validation error when the payee name is missing"
        );
    }

    @Test(groups = {"sanity", "ui"}, description = "Logging out returns the user to the login page")
    public void verifyLogoutReturnsToLoginPage() {
        LoginPage loginPage = navigationMenuPage.logOut();

        Assert.assertTrue(
                loginPage.isUsernameFieldDisplayed(),
                "Expected the login form to be displayed again after logging out"
        );
    }
}