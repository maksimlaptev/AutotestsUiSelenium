package TestPageLocator.SocialNetwork;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SocialNetworkLocators {
    WebDriver _driver;

    public SocialNetworkLocators(WebDriver driver) {

        this._driver = driver;
    }

    @FindBy(id = "field_email")
    public WebElement LoginField;
    @FindBy(id = "field_password")
    public WebElement PasswordField;
    @FindBy(xpath = "//*[@class='login-form-actions']//input")
    public WebElement SignInBtn;
    @FindBy(xpath = "//*[@class='input-e login_error']")
    public WebElement ErrorText;
}
