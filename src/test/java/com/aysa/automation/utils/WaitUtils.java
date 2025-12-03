package com.aysa.automation.utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class WaitUtils {

    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);

    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private static final int DEFAULT_TIMEOUT = 30;

    public WaitUtils(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public WaitUtils(AndroidDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public WebElement waitForElementToBeVisible(WebElement element) {
        logger.debug("Waiting for element to be visible");
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            logger.error("Element not visible within timeout");
            throw e;
        }
    }

    public WebElement waitForElementToBeClickable(WebElement element) {
        logger.debug("Waiting for element to be clickable");
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            logger.error("Element not clickable within timeout");
            throw e;
        }
    }

    public boolean waitForElementToDisappear(WebElement element) {
        logger.debug("Waiting for element to disappear");
        try {
            return wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException e) {
            logger.error("Element still visible after timeout");
            return false;
        }
    }

    public boolean waitForTextToBePresent(WebElement element, String text) {
        logger.debug("Waiting for text '{}' to be present", text);
        try {
            return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
        } catch (TimeoutException e) {
            logger.error("Text not present within timeout");
            return false;
        }
    }

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean waitUntil(java.util.function.BooleanSupplier condition,
                             int timeoutSeconds,
                             long pollingMillis) {
        long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);

        while (System.currentTimeMillis() < endTime) {
            if (condition.getAsBoolean()) {
                return true;
            }
            sleep(pollingMillis);
        }

        return false;
    }
}
