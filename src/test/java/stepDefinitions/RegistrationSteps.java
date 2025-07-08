package stepDefinitions;

import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import factory.BaseClass;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import pageObjects.LoginPage;

public class RegistrationSteps {

    WebDriver driver;
    HomePage hp;
    LoginPage lp;
    AccountRegistrationPage regpage;

    @Given("the user navigates to Register Account page")
    public void user_navigates_to_register_account_page() {
        BaseClass.getLogger().info("➡️ Navigating to Register Account page...");
        hp = new HomePage(BaseClass.getDriver());
        hp.clickMyAccount();
        BaseClass.getLogger().info("👤 Clicked on 'My Account'");
        hp.clickRegister();
        BaseClass.getLogger().info("📝 Clicked on 'Register'");
    }

    @When("the user enters the details into below fields")
    public void user_enters_the_details_into_below_fields(DataTable dataTable) {
        Map<String, String> dataMap = dataTable.asMap(String.class, String.class);

        regpage = new AccountRegistrationPage(BaseClass.getDriver());
        BaseClass.getLogger().info("✏️ Entering registration details...");

        regpage.setFirstName(dataMap.get("firstName"));
        regpage.setLastName(dataMap.get("lastName"));

        String generatedEmail = BaseClass.randomAlphaNumeric() + "@gmail.com";
        regpage.setEmail(generatedEmail);
        BaseClass.getLogger().info("📧 Generated email: " + generatedEmail);

        regpage.setTelephone(dataMap.get("telephone"));
        regpage.setPassword(dataMap.get("password"));
        regpage.setConfirmPassword(dataMap.get("password"));
    }

    @When("the user selects Privacy Policy")
    public void user_selects_privacy_policy() {
        regpage.setPrivacyPolicy();
        BaseClass.getLogger().info("☑️ Selected Privacy Policy checkbox");
    }

    @When("the user clicks on Continue button")
    public void user_clicks_on_continue_button() {
        regpage.clickContinue();
        BaseClass.getLogger().info("➡️ Clicked on Continue button to submit the form");
    }

    @Then("the user account should get created successfully")
    public void user_account_should_get_created_successfully() {
        String confmsg = regpage.getConfirmationMsg();
        BaseClass.getLogger().info("🔍 Confirmation message received: " + confmsg);

        Assert.assertEquals("❌ Account creation failed!", "Your Account Has Been Created!", confmsg);
        BaseClass.getLogger().info("🎉 Account successfully created!");
    }
}
