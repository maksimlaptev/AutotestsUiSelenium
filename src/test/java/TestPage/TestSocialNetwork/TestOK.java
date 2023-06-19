package TestPage.TestSocialNetwork;

import TestPage.EnvContainer;
import TestPageLocator.SocialNetwork.SocialNetworkLocators;
import io.qameta.allure.Description;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestOK extends EnvContainer {
    private WebDriver _driver;
    private SocialNetworkLocators _page;

    @BeforeClass
    public void suiteSetUp()
    {
        _driver = EnvContainer.Driver;
        _page = PageFactory.initElements(_driver, SocialNetworkLocators.class);

    }

    private void openUrl(String url) {
       _ctx.openUrl(url);
    }

    @Description("WHEN we enter an empty login THEN an error is displayed in the login field")
    @Test( enabled = true)
    public void testCheckErrorEmptyLogin() {
        // prepare
        openUrl("https://ok.ru/");

        // action
        _ctx.current(_page.LoginField).setValue("");
        _ctx.current(_page.SignInBtn).click();

        // verification
        Assert.assertEquals(_ctx.current(_page.ErrorText).getText(),"Введите логин","Text error must be display.");

    }

    @Description("WHEN we enter an empty password THEN an error is displayed in the password field")
    @Test( enabled = true)
    public void testCheckErrorEmptyPassword() {
        // prepare
        openUrl("https://ok.ru/");

        // action
        _ctx.current(_page.LoginField).setValue("123").
                current(_page.PasswordField).setValue("").
                current(_page.SignInBtn).click();


        // verification
        Assert.assertEquals(_ctx.current(_page.ErrorText).getText(),"Введите пароль","Text error must be display.");

    }

}
