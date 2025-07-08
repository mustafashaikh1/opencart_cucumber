package hooks;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import factory.BaseClass;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    WebDriver driver;
    Properties p;

    @Before
    public void setup() throws IOException {
        BaseClass.getLogger().info("🚀✨ Starting test execution - Initializing browser...");
        BaseClass.getLogger().debug("🔧 Loading configuration properties...");

        driver = BaseClass.initilizeBrowser();
        p = BaseClass.getProperties();

        String appUrl = p.getProperty("appURL");
        driver.get(appUrl);
        driver.manage().window().maximize();

        BaseClass.getLogger().info("🌍🔛 Navigating to URL: " + appUrl);
        BaseClass.getLogger().info("🖥️⬛ Maximizing browser window");
    }

    @After
    public void tearDown(Scenario scenario) {
        String scenarioName = scenario.getName();
        String status = scenario.isFailed() ? "❌ FAILED" : "✅ PASSED";

        BaseClass.getLogger().info("🏁 " + status + " - Scenario: " + scenarioName);
        BaseClass.getLogger().info("🧹🛑 Closing browser and cleaning up resources...");

        driver.quit();
        BaseClass.getLogger().info("👋 Browser session terminated");
    }

    @AfterStep
    public void addScreenshot(Scenario scenario) {
        String stepName = scenario.getName();

        if (scenario.isFailed()) {
            BaseClass.getLogger().warn("⚠️📸 Capture screenshot for failed step: " + stepName);

            try {
                TakesScreenshot ts = (TakesScreenshot) driver;
                byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", stepName);

                BaseClass.getLogger().info("🖼️✅ Screenshot saved for failed step: " + stepName);
            } catch (Exception e) {
                BaseClass.getLogger().error("❌📷 Failed to capture screenshot: " + e.getMessage());
            }
        } else {
            BaseClass.getLogger().debug("✔️ Step passed: " + stepName);
        }
    }
}