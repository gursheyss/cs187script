package com.aysa.automation.tests;

import com.aysa.automation.base.BaseTest;
import com.aysa.automation.data.TestData;
import com.aysa.automation.data.TestDataProvider;
import com.aysa.automation.pages.GalleryPage;
import com.aysa.automation.pages.HomePage;
import com.aysa.automation.pages.ImageUploadPage;
import com.aysa.automation.pages.ResultsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test class for disease detection functionality in Aysa app.
 * Contains parameterized tests for 12 different image-disease test cases.
 *
 * Prerequisites:
 * 1. Test images must be pre-loaded on the emulator in /sdcard/Pictures/
 * 2. Appium server must be running
 * 3. Android emulator must be running with Aysa app installed
 */
public class DiseaseDetectionTest extends BaseTest {

    private HomePage homePage;

    @BeforeClass(alwaysRun = true)
    @Override
    public void setUpClass() {
        super.setUpClass();
        logger.info("Disease Detection Test Suite initialized");
    }

    /**
     * Main parameterized test for disease detection.
     * Runs 12 times with different test data (one per image-disease pair).
     *
     * Test Flow:
     * 1. Open Aysa app home screen
     * 2. Click upload image button
     * 3. Select test image from gallery
     * 4. Submit for analysis
     * 5. Verify expected disease name in results
     *
     * @param testData Test case data containing image name and expected disease
     */
    @Test(dataProvider = "diseaseTestData", dataProviderClass = TestDataProvider.class,
            description = "Verify disease detection for uploaded image")
    public void testDiseaseDetection(TestData testData) {
        logger.info("========================================");
        logger.info("Starting Test Case #{}: {}", testData.getId(), testData.getDescription());
        logger.info("Image: {}", testData.getImageName());
        logger.info("Expected Disease: {}", testData.getExpectedDisease());
        logger.info("========================================");

        // Step 1: Initialize home page
        homePage = new HomePage(driver);
        homePage.waitForPageLoad();
        logger.info("Home page loaded successfully");

        // Step 2: Click upload image
        GalleryPage galleryPage = homePage.clickUploadImage();
        logger.info("Navigated to gallery");

        // Step 3: Select the test image
        ImageUploadPage uploadPage = galleryPage.selectImageByName(testData.getImageName());
        logger.info("Image selected: {}", testData.getImageName());

        // Step 4: Submit for analysis
        ResultsPage resultsPage = uploadPage.clickAnalyze();
        logger.info("Analysis submitted, waiting for results");

        // Step 5: Verify results
        String detectedDisease = resultsPage.getDetectedDiseaseName();
        logger.info("Detected disease: {}", detectedDisease);

        // Assert the expected disease is detected
        assertDiseaseMatches(detectedDisease, testData.getExpectedDisease());

        logger.info("Test Case #{} PASSED", testData.getId());
    }

    /**
     * Alternative test using expanded data provider parameters.
     *
     * @param testId          Test case ID
     * @param imageName       Name of the image file
     * @param expectedDisease Expected disease name
     */
    @Test(dataProvider = "diseaseTestDataExpanded", dataProviderClass = TestDataProvider.class,
            description = "Verify disease detection (expanded parameters)",
            enabled = false) // Disabled - use main test above
    public void testDiseaseDetectionExpanded(int testId, String imageName, String expectedDisease) {
        logger.info("Test Case #{}: Image={}, Expected={}", testId, imageName, expectedDisease);

        homePage = new HomePage(driver);
        homePage.waitForPageLoad();

        GalleryPage galleryPage = homePage.clickUploadImage();
        ImageUploadPage uploadPage = galleryPage.selectImageByName(imageName);
        ResultsPage resultsPage = uploadPage.clickAnalyze();

        String detectedDisease = resultsPage.getDetectedDiseaseName();
        assertDiseaseMatches(detectedDisease, expectedDisease);
    }

    /**
     * Test using image index selection instead of name.
     *
     * @param testData Test case data
     */
    @Test(dataProvider = "diseaseTestData", dataProviderClass = TestDataProvider.class,
            description = "Verify disease detection using image index",
            enabled = false) // Disabled - use main test above
    public void testDiseaseDetectionByIndex(TestData testData) {
        logger.info("Test Case #{} (by index)", testData.getId());

        homePage = new HomePage(driver);
        homePage.waitForPageLoad();

        GalleryPage galleryPage = homePage.clickUploadImage();
        galleryPage.navigateToPicturesFolder();

        // Select by index (testId - 1 for 0-based index)
        ImageUploadPage uploadPage = galleryPage.selectImageByIndex(testData.getId() - 1);
        ResultsPage resultsPage = uploadPage.clickAnalyze();

        String detectedDisease = resultsPage.getDetectedDiseaseName();
        assertDiseaseMatches(detectedDisease, testData.getExpectedDisease());
    }

    /**
     * Smoke test to verify basic app functionality.
     */
    @Test(description = "Smoke test for disease detection flow",
            priority = -1, groups = {"smoke"})
    public void smokeTestDiseaseDetection() {
        logger.info("Running smoke test");

        TestData firstTestCase = TestDataProvider.getTestDataById(1);
        if (firstTestCase == null) {
            logger.error("No test data available for smoke test");
            return;
        }

        homePage = new HomePage(driver);

        // Verify home page loads
        Assert.assertTrue(homePage.isPageDisplayed(),
                "Home page should be displayed");

        logger.info("Smoke test passed - App is functional");
    }

    /**
     * Asserts that the detected disease matches the expected disease.
     * Uses case-insensitive partial matching.
     *
     * @param actualDisease   Disease detected by the app
     * @param expectedDisease Expected disease name
     */
    private void assertDiseaseMatches(String actualDisease, String expectedDisease) {
        logger.info("Asserting disease match - Expected: '{}', Actual: '{}'",
                expectedDisease, actualDisease);

        Assert.assertNotNull(actualDisease,
                "Detected disease is null. Expected: " + expectedDisease);

        Assert.assertFalse(actualDisease.isEmpty(),
                "Detected disease is empty. Expected: " + expectedDisease);

        boolean matches = actualDisease.toLowerCase().contains(expectedDisease.toLowerCase());

        Assert.assertTrue(matches,
                String.format("Disease mismatch. Expected to contain: '%s', but got: '%s'",
                        expectedDisease, actualDisease));

        logger.info("Disease assertion passed");
    }
}
