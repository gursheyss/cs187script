package com.aysa.automation.tests;

import com.aysa.automation.base.BaseTest;
import com.aysa.automation.data.TestData;
import com.aysa.automation.data.TestDataProvider;
import com.aysa.automation.pages.QuestionnairePage;
import com.aysa.automation.pages.ResultsPage;
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
 * Tests all 48 skin condition images through the complete diagnosis flow.
 */
public class DiseaseDetectionTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    @Override
    public void setUpClass() {
        super.setUpClass();
        logger.info("Disease Detection Test Suite initialized");
    }

    /**
     * Full test flow for all 48 images.
     */
    @Test(dataProvider = "diseaseTestData", dataProviderClass = TestDataProvider.class,
            description = "Test complete disease detection flow")
    public void testDiseaseDetection(TestData testData) {
        logger.info("========================================");
        logger.info("Starting Test Case #{}: {}", testData.getId(), testData.getDescription());
        logger.info("========================================");

        // Step 1: Click the "I UNDERSTAND. LET'S GET STARTED" button (if visible)
        clickOnboardingButtonIfPresent();

        // Step 2: Click "Start a new case" button
        clickStartNewCase();

        // Step 3: Open image picker
        clickSelectImageButton();

        // Step 4: Select folder and image
        selectFolder(testData.getFolderName());
        selectImage(testData.getImageName());

        // Step 5: Complete questionnaire flow
        QuestionnairePage questionnairePage = new QuestionnairePage(driver);
        ResultsPage resultsPage = questionnairePage.completeQuestionnaire(
                testData.getFlakyBumpy(),
                testData.getProfileName(),
                testData.getBodyCoverage(),
                testData.getBodyLocation(),
                testData.getDuration(),
                testData.getItches(),
                testData.getFever()
        );

        // Step 6: Verify results
        logger.info("Checking results for expected disease: {}", testData.getExpectedDisease());
        boolean foundExpectedDisease = resultsPage.containsDisease(testData.getExpectedDisease());

        // Log all detected diseases
        logger.info("All detected diseases: {}", resultsPage.getAllDetectedDiseases());

        // Click DONE to dismiss results
        resultsPage.clickDone();
        sleep(1000);

        // Step 7: Navigate back to start new test
        navigateBackToCases();

        // Assert at the end so we clean up first
        Assert.assertTrue(foundExpectedDisease,
            "Expected disease '" + testData.getExpectedDisease() + "' not found in results");

        logger.info("Test Case #{} COMPLETED - {}", testData.getId(), testData.getDescription());
    }

    /**
     * Test just eczema images.
     */
    @Test(dataProvider = "eczemaTestData", dataProviderClass = TestDataProvider.class,
            description = "Test eczema detection", groups = {"eczema"})
    public void testEczemaDetection(TestData testData) {
        testDiseaseDetection(testData);
    }

    /**
     * Test just melanoma images.
     */
    @Test(dataProvider = "melanomaTestData", dataProviderClass = TestDataProvider.class,
            description = "Test melanoma detection", groups = {"melanoma"})
    public void testMelanomaDetection(TestData testData) {
        testDiseaseDetection(testData);
    }

    /**
     * Test just psoriasis images.
     */
    @Test(dataProvider = "psoriasisTestData", dataProviderClass = TestDataProvider.class,
            description = "Test psoriasis detection", groups = {"psoriasis"})
    public void testPsoriasisDetection(TestData testData) {
        testDiseaseDetection(testData);
    }

    /**
     * Test just fungal infection images.
     */
    @Test(dataProvider = "fungalTestData", dataProviderClass = TestDataProvider.class,
            description = "Test fungal infection detection", groups = {"fungal"})
    public void testFungalDetection(TestData testData) {
        testDiseaseDetection(testData);
    }

    /**
     * Smoke test to verify basic flow works.
     */
    @Test(description = "Smoke test for basic flow", priority = -1, groups = {"smoke"})
    public void smokeTest() {
        logger.info("Running smoke test");

        clickOnboardingButtonIfPresent();
        clickStartNewCase();
        clickSelectImageButton();

        logger.info("Smoke test passed - Basic navigation working");
    }

    // ==================== Helper Methods ====================

    /**
     * Clicks the onboarding button if present (first launch only).
     */
    private void clickOnboardingButtonIfPresent() {
        logger.info("Checking for onboarding button...");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.accessibilityId("getStarted")
            ));
            logger.info("Found onboarding button, clicking...");
            button.click();
            sleep(1000);
        } catch (Exception e) {
            logger.info("Onboarding button not present, continuing...");
        }
    }

    /**
     * Clicks the "Start a new case" button using accessibility id "new case".
     */
    private void clickStartNewCase() {
        logger.info("Clicking Start a new case...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId("new case")
        ));
        button.click();
        logger.info("New case started");
        sleep(1000);
    }

    /**
     * Clicks the image picker button on the camera screen.
     */
    private void clickSelectImageButton() {
        logger.info("Opening image picker...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId("selectImageButton")
        ));
        button.click();
        logger.info("Image picker opened");
        sleep(1000);
    }

    /**
     * Selects a folder from the image picker dropdown.
     */
    private void selectFolder(String folderName) {
        logger.info("Selecting folder: {}", folderName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait for image picker to fully load
        sleep(1500);

        // Click the album spinner to open dropdown
        WebElement spinner = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId("vdxAlbumSelect")
        ));
        spinner.click();
        sleep(1000);

        // Select the folder
        WebElement folder = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.TextView[@text='" + folderName + "']")
        ));
        folder.click();
        logger.info("Folder selected: {}", folderName);
        sleep(1500);
    }

    /**
     * Selects an image by its name (e.g., "1.jpg").
     */
    private void selectImage(String imageName) {
        logger.info("Selecting image: {}", imageName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait for images to load in the picker
        sleep(1000);

        WebElement image = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId(imageName)
        ));
        image.click();
        logger.info("Image selected: {}", imageName);
        sleep(1500);
    }

    /**
     * Navigates back to the cases screen for the next test.
     */
    private void navigateBackToCases() {
        logger.info("Navigating back to cases screen...");

        for (int i = 0; i < 5; i++) {
            try {
                // Check if we're already on cases screen
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(1));
                shortWait.until(ExpectedConditions.presenceOfElementLocated(
                        AppiumBy.accessibilityId("new case")
                ));
                logger.info("Back on cases screen");
                return;
            } catch (Exception e) {
                // Not on cases screen, go back
                driver.navigate().back();
                sleep(500);
            }
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
