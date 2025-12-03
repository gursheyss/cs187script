package com.aysa.automation.pages;

import com.aysa.automation.utils.WaitUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class BasePage {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected WaitUtils waitUtils;

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    public abstract boolean isPageDisplayed();

    protected void click(WebElement element) {
        waitUtils.waitForElementToBeClickable(element);
        element.click();
        logger.debug("Clicked on element: {}", element);
    }

    protected void enterText(WebElement element, String text) {
        waitUtils.waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(text);
        logger.debug("Entered text '{}' into element", text);
    }

    protected String getText(WebElement element) {
        waitUtils.waitForElementToBeVisible(element);
        return element.getText();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
