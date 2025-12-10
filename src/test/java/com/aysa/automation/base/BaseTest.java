package com.aysa.automation.base;

import com.aysa.automation.config.AppConfig;
import com.aysa.automation.config.CapabilitiesManager;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public abstract class BaseTest {

    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected AppConfig config;

    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        logger.info("Initializing test class setup");
        config = AppConfig.getInstance();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("Setting up Appium driver for test");
        initializeDriver();
        configureTimeouts();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down Appium driver");
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                logger.error("Error quitting driver: {}", e.getMessage());
            }
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        logger.info("Test class teardown complete");
    }

    private void initializeDriver() {
        int maxRetries = 3;
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                CapabilitiesManager capManager = new CapabilitiesManager();
                URL appiumServerUrl = new URL(config.getAppiumServerUrl());

                driver = new AndroidDriver(appiumServerUrl, capManager.getAndroidCapabilities());

                wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));

                logger.info("Android driver initialized successfully");
                return; // Success - exit the retry loop
            } catch (MalformedURLException e) {
                logger.error("Invalid Appium server URL: {}", e.getMessage());
                throw new RuntimeException("Failed to initialize driver", e);
            } catch (Exception e) {
                lastException = e;
                logger.warn("Driver init attempt {}/{} failed: {}", attempt, maxRetries, e.getMessage());
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(2000); // Wait 2 seconds before retry
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        // All retries failed
        logger.error("Failed to initialize driver after {} attempts", maxRetries);
        throw new RuntimeException("Failed to initialize driver after retries", lastException);
    }

    private void configureTimeouts() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
    }

    protected AndroidDriver getDriver() {
        return driver;
    }

    protected WebDriverWait getWait() {
        return wait;
    }

    protected void resetApp() {
        if (driver != null) {
            driver.terminateApp(config.getAppPackage());
            driver.activateApp(config.getAppPackage());
        }
    }
}
