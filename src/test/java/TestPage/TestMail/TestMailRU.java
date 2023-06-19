package TestPage.TestMail;

import TestPage.EnvContainer;
import TestPageLocator.Mail.MailLocators;
import io.qameta.allure.Description;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestMailRU extends EnvContainer {
    private WebDriver _driver;
    private MailLocators _page;

    @BeforeClass
    public void suiteSetUp()
    {
        _driver = EnvContainer.Driver;
        _page = PageFactory.initElements(_driver, MailLocators.class);

    }

    private void openUrl(String url) {
       _ctx.openUrl(url);
    }

    @Description("WHEN we enter an empty login THEN an error is displayed in the login field")
    @Test( enabled = true)
    public void testCheckErrorEmptyLogin() {
        // prepare
        openUrl("https://mail.ru/");

        // action
        _ctx.current(_page.LoginIn).click();
        _ctx.current(_page.NextBtn).click();

        // verification
        Assert.assertEquals(_ctx.current(_page.ErrorText).getText(),"Введите имя ящика","Text error must be display.");

    }

    @Description("WHEN we enter an empty password THEN an error is displayed in the password field")
    @Test( enabled = true)
    public void testCheckErrorEmptyPassword() {
        // prepare
        openUrl("https://mail.ru/");

        // action
        _ctx.current(_page.LoginIn).click();
        _ctx.current(_page.LoginField).setValue("123").
                current(_page.NextBtn).click().
                current(_page.PasswordField).setValue("").
                current(_page.SignInBtn).click();


        // verification
        Assert.assertEquals(_ctx.current(_page.ErrorText).getText(),"Введите пароль","Text error must be display.");

    }

}
