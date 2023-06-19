package TestPageLocator.Mail;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MailLocators {
    WebDriver _driver;

    public MailLocators(WebDriver driver) {
        this._driver = driver;
    }

    @FindBy(xpath = "//*[@data-testid='enter-mail-primary']")
    public WebElement LoginIn;
    @FindBy(xpath = "//*[@name='username']")
    public WebElement LoginField;
    @FindBy(xpath = "//input[@name='password']")
    public WebElement PasswordField;
    @FindBy(xpath = "//*[@data-test-id='next-button']/span")
    public WebElement NextBtn;
    @FindBy(xpath = "//*[contains(@class,'second-button')]")
    public WebElement SignInBtn;
    @FindBy(xpath = "//*[@data-test-id='error-footer-text']/small")
    public WebElement ErrorText;

   /* public WebElement example(String taskId) {
        return _driver.findElement(By.xpath("//*[@class='example " + taskId + "\"]"));
    }*/
}
