package factory;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BaseClass {

	static WebDriver driver;
	static Properties p;
	static Logger logger;

	public static WebDriver initilizeBrowser() throws IOException {
		logger = getLogger();

		logger.info("🚀 Initializing browser setup...");
                             // Remote WebDriver setup
		if (getProperties().getProperty("execution_env").equalsIgnoreCase("remote")) {
			logger.info("☁️ Running in Remote environment");

			DesiredCapabilities capabilities = new DesiredCapabilities();

			// OS
			String os = getProperties().getProperty("os");
			if (os.equalsIgnoreCase("windows")) {
				logger.info("🖥️ OS: Windows 11");
				capabilities.setPlatform(Platform.WIN11);
			} else if (os.equalsIgnoreCase("mac")) {
				logger.info("🍏 OS: macOS");
				capabilities.setPlatform(Platform.MAC);
			} else {
				logger.warn("⚠️ No matching OS found in properties.");
			}

			// Browser
			String browser = getProperties().getProperty("browser").toLowerCase();
			switch (browser) {
				case "chrome":
					logger.info("🌍 Launched Chrome browser");
					capabilities.setBrowserName("chrome");
					break;
				case "edge":
					logger.info("🌐 Browser: Edge");
					capabilities.setBrowserName("MicrosoftEdge");
					break;
				case "firefox":
					driver = new FirefoxDriver();
					logger.info("🦊 Launched Firefox browser");
					break;
				default:
					logger.error("❌ No matching browser found.");
					return null;
			}

			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
			           logger.info("🌐 Remote WebDriver initialized successfully.");
                                       // Local WebDriver setup
		} else if (getProperties().getProperty("execution_env").equalsIgnoreCase("local")) {
			logger.info("🧪 Setting up Local WebDriver...");

			String browser = getProperties().getProperty("browser").toLowerCase();
			switch (browser) {
				case "chrome":
					logger.info("🌍 Launched Chrome browser");
					driver = new ChromeDriver();
					break;
				case "edge":
					logger.info("🌐 Browser: Edge");
					driver = new EdgeDriver();
					break;
				case "firefox":
					driver = new FirefoxDriver();
					logger.info("🦊 Launched Firefox browser");
					break;
				default:
					logger.error("❌ No matching browser found.");
					return null;
			}
		}

		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));

		logger.info("✅ Browser setup completed successfully.");
		return driver;
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static Properties getProperties() throws IOException {
		FileReader file = new FileReader(System.getProperty("user.dir") + "\\src\\test\\resources\\config.properties");

		p = new Properties();
		p.load(file);
		return p;
	}

	public static Logger getLogger() {
		logger = LogManager.getLogger(); // Log4j2
		return logger;
	}

	public static String randomeString() {
		String generatedString = RandomStringUtils.randomAlphabetic(5);
		logger.debug("🔡 Generated random string: " + generatedString);
		return generatedString;
	}

	public static String randomeNumber() {
		String generatedString = RandomStringUtils.randomNumeric(10);
		logger.debug("🔢 Generated random number: " + generatedString);
		return generatedString;
	}

	public static String randomAlphaNumeric() {
		String str = RandomStringUtils.randomAlphabetic(5);
		String num = RandomStringUtils.randomNumeric(10);
		logger.debug("🔠 Generated random alphanumeric: " + str + num);
		return str + num;
	}
}
