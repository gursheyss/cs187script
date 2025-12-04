package com.aysa.automation.tests;

import com.aysa.automation.base.BaseTest;
import com.aysa.automation.data.TestData;
import com.aysa.automation.data.TestDataProvider;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Test class for disease detection functionality in Aysa app.
 */
public class DiseaseDetectionTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    @Override
    public void setUpClass() {
        super.setUpClass();
        logger.info("Disease Detection Test Suite initialized");
    }

    /**
     * Test clicking the onboarding button after app launch.
     */
    @Test(dataProvider = "diseaseTestData", dataProviderClass = TestDataProvider.class,
            description = "Test onboarding button click")
    public void testDiseaseDetection(TestData testData) {
        logger.info("========================================");
        logger.info("Starting Test Case #{}: {}", testData.getId(), testData.getDescription());
        logger.info("========================================");

        // Step 1: Click the "I understand. Let's get started" button
        clickOnboardingButton();

        // Step 2: Click "Start a new case" button
        clickStartNewCase();

        // Step 3: Open image library
        openImageLibrary();

        // Step 4: Select the test image
        selectImage(testData.getImageName());

        logger.info("Test Case #{} PASSED - Flow completed", testData.getId());
    }

    /**
     * Smoke test to verify onboarding button works.
     */
    @Test(description = "Smoke test for onboarding flow",
            priority = -1, groups = {"smoke"})
    public void smokeTestDiseaseDetection() {
        logger.info("Running smoke test");

        clickOnboardingButton();
        clickStartNewCase();

        logger.info("Smoke test passed - Flow completed");
    }

    /**
     * Clicks the "I understand. Let's get started" button on the onboarding screen.
     */
    private void clickOnboardingButton() {
        logger.info("Looking for onboarding button...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Try to find by text content
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.androidUIAutomator("new UiSelector().textContains(\"I understand\")")));

        logger.info("Found onboarding button, clicking...");
        button.click();
        logger.info("Onboarding button clicked successfully");

        // Small wait for transition
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Clicks the "Start a new case" button (the + icon square).
     */
    private void clickStartNewCase() {
        logger.info("Looking for Start a new case button...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Find the text element and then click its clickable parent
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"Start a new case\").fromParent(new UiSelector().clickable(true))")));

        logger.info("Found Start a new case button, clicking...");
        button.click();
        logger.info("Start a new case button clicked");

        // Small wait for transition
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Opens the image library (clicks the thumbnail in bottom left).
     */
    private void openImageLibrary() {
        logger.info("Opening image library...");

        // Debug: dump page source
        logger.info("=== CAMERA PAGE SOURCE START ===");
        logger.info(driver.getPageSource());
        logger.info("=== CAMERA PAGE SOURCE END ===");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // The image library button is typically an ImageView in the bottom left
        // Try finding by content description or by class
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.androidUIAutomator(
                    "new UiSelector().className(\"android.widget.ImageView\").clickable(true).instance(0)")));

        logger.info("Found image library button, clicking...");
        button.click();
        logger.info("Image library opened");

        // Wait for gallery to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Selects an image from the gallery by name.
     */
    private void selectImage(String imageName) {
        logger.info("Selecting image: {}", imageName);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Find the image by its name/description
        WebElement image = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.androidUIAutomator(
                    "new UiSelector().descriptionContains(\"" + imageName + "\")")));

        logger.info("Found image, clicking...");
        image.click();
        logger.info("Image selected: {}", imageName);

        // Wait for image to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
