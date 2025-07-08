package stepDefinitions;

import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import factory.BaseClass;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import utilities.DataReader;

public class LoginSteps {

    WebDriver driver;
    HomePage hp;
    LoginPage lp;
    MyAccountPage macc;

    List<HashMap<String, String>> datamap; //Data driven

    @Given("the user navigates to login page")
    public void user_navigate_to_login_page() {
        BaseClass.getLogger().info("➡️ Navigating to login page via My Account...");
        hp = new HomePage(BaseClass.getDriver());
        hp.clickMyAccount();
        BaseClass.getLogger().info("👤 Clicked on 'My Account'");
        hp.clickLogin();
        BaseClass.getLogger().info("🔐 Clicked on 'Login'");
    }

    @When("user enters email as {string} and password as {string}")
    public void user_enters_email_as_and_password_as(String email, String pwd) {
        BaseClass.getLogger().info("✏️ Entering email and password...");
        lp = new LoginPage(BaseClass.getDriver());
        lp.setEmail(email);
        lp.setPassword(pwd);
    }

    @When("the user clicks on the Login button")
    public void click_on_login_button() {
        lp.clickLogin();
        BaseClass.getLogger().info("✅ Clicked on 'Login' button");
    }

    @Then("the user should be redirected to the MyAccount Page")
    public void user_navigates_to_my_account_page() {
        macc = new MyAccountPage(BaseClass.getDriver());
        boolean targetpage = macc.isMyAccountPageExists();
        BaseClass.getLogger().info("🔍 Checking if user is on My Account page: " + targetpage);
        Assert.assertEquals(targetpage, true);
    }

    //*******   Data Driven test **************
    @Then("the user should be redirected to the MyAccount Page by passing email and password with excel row {string}")
    public void check_user_navigates_to_my_account_page_by_passing_email_and_password_with_excel_data(String rows) {

        datamap = DataReader.data(System.getProperty("user.dir") + "\\testData\\Opencart_LoginData.xlsx", "Sheet1");

        int index = Integer.parseInt(rows) - 1;
        String email = datamap.get(index).get("username");
        String pwd = datamap.get(index).get("password");
        String exp_res = datamap.get(index).get("res");

        BaseClass.getLogger().info("📊 Reading Excel row: " + rows + " -> Email: " + email + ", Password: " + pwd + ", Expected: " + exp_res);

        lp = new LoginPage(BaseClass.getDriver());
        lp.setEmail(email);
        lp.setPassword(pwd);

        lp.clickLogin();
        BaseClass.getLogger().info("✅ Clicked on 'Login' button");

        macc = new MyAccountPage(BaseClass.getDriver());

        try {
            boolean targetpage = macc.isMyAccountPageExists();
            BaseClass.getLogger().info("🔍 My Account page exists: " + targetpage);

            if (exp_res.equals("Valid")) {
                if (targetpage) {
                    BaseClass.getLogger().info("🎉 Login successful (Expected Valid)");
                    macc.clickLogout();
                    BaseClass.getLogger().info("🔓 Logged out");
                    Assert.assertTrue(true);
                } else {
                    BaseClass.getLogger().error("❌ Login failed unexpectedly for Valid credentials");
                    Assert.assertTrue(false);
                }
            }

            if (exp_res.equals("Invalid")) {
                if (targetpage) {
                    BaseClass.getLogger().error("❌ Login succeeded unexpectedly for Invalid credentials");
                    macc.clickLogout();
                    Assert.assertTrue(false);
                } else {
                    BaseClass.getLogger().info("🚫 Login failed as expected for Invalid credentials");
                    Assert.assertTrue(true);
                }
            }

        } catch (Exception e) {
            BaseClass.getLogger().error("🔥 Exception during login DDT: " + e.getMessage());
            Assert.assertTrue(false);
        }
    }
}
