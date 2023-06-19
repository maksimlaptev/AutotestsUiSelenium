package TestPage;

import Helpers.Helper;
import TestPageLocator.Mail.MailLocators;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class EnvContainer {
    public static String PathDriver,Headless;
    public static WebDriver Driver;
    public static Helper _ctx;
    public static MailLocators pagecommon;

    @BeforeSuite
    @Parameters({"browsername","headless", "pathdriver"})
    public void suiteSetUp(String browserType,String headless, String pathDriver) throws Exception {
        Headless=headless;
        PathDriver = pathDriver;
        Driver = getDriverByName(browserType,Headless, pathDriver);
        _ctx = new Helper(Driver);
        pagecommon = PageFactory.initElements(Driver, MailLocators.class);

        Driver.manage().window().setSize(new Dimension(1920, 1080));
    }


    @AfterSuite
    public void suiteTearDown() {
        Driver.quit();
    }

    @AfterMethod

    public void testTearDown(Method method, ITestResult result) {
        if (! result.isSuccess()) {
            makeScreenshot(result.getName());
        }
        try {
            String nameMethod = result.getName();
            String nameClass = result.getTestClass().getName();
            _ctx.writeAllLogInFile(Driver, nameMethod, nameClass);
            EnvContainer.interceptionJSonPage(Driver);

        } catch (Exception ex) {

        }
    }

    @Attachment(value = "{0}", type = "image/png")
    public byte[] makeScreenshot(String nametest) {
        return ((TakesScreenshot) Driver).getScreenshotAs(OutputType.BYTES);
    }


        public static WebDriver getDriverByName (String browserType, String Headless, String pathDriver){
            WebDriver driver = null;

            if (browserType.equals("chrome")) {
                System.setProperty("webdriver.chrome.driver", pathDriver);
                System.setProperty("webdriver.chrome.verboseLogging", "false");
                System.setProperty("webdriver.chrome.args", "--disable-logging");

                DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                ChromeOptions options = new ChromeOptions();
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                if (Headless.equals("yes")) {
                    options.setHeadless(true);
                    options.addArguments("--headless");
                    options.addArguments("--window-size=1920,1080");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--disable-extensions");

                }
                LoggingPreferences logPrefs = new LoggingPreferences();
                logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
                options.setCapability("goog:loggingPrefs", logPrefs);

                driver = new ChromeDriver(options);
                return driver;
            } else {
                throw new RuntimeException("driver not found");
                // return null;
            }

        }


        /**
         * Intercept function JS ERROR
         */
        public static String interceptionJSonPage (WebDriver webDriver){
            Set<String> errorStrings = new HashSet<>();
            errorStrings.add("SyntaxError");
            errorStrings.add("EvalError");
            errorStrings.add("ReferenceError");
            errorStrings.add("RangeError");
            errorStrings.add("TypeError");
            errorStrings.add("URIError");
            // errorStrings.add("404");

            if (webDriver == null)
                return null;
            if (webDriver.manage() == null)
                return null;
            if (webDriver.manage().logs() == null)
                return null;

            LogEntries logEntries = webDriver.manage().logs().get(LogType.BROWSER);
            if (logEntries != null) {
                for (LogEntry logEntry : logEntries) {
                    for (String errorString : errorStrings) {
                        if (logEntry.getMessage().contains(errorString)) {
                            if (System.getProperty("stopOnJsFail") != null) {
                                Helper.log(new Date(logEntry.getTimestamp()) + " " + logEntry.getLevel()
                                        + " " + logEntry.getMessage());

                            }
                            Assert.fail(new Date(logEntry.getTimestamp()) + " " + logEntry.getLevel()
                                    + " " + logEntry.getMessage());
                        }
                    }
                }
            }
            return null;
        }

}
