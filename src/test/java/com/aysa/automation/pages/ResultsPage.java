package com.aysa.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for the disease detection results screen.
 * Results are displayed as cards with content-desc containing the disease name.
 */
public class ResultsPage extends BasePage {

    public ResultsPage(AndroidDriver driver) {
        super(driver);
        logger.info("Initializing Results Page");
        waitForResultsToLoad();
    }

    @Override
    public boolean isPageDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//android.widget.TextView[@text='Results']")
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForResultsToLoad() {
        logger.info("Waiting for results page to load");
        try {
            Thread.sleep(3000); // Wait for API response and page render
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.id("com.visualdx.aysa:id/recycler_view")
            ));
        } catch (Exception e) {
            logger.warn("Results may not have loaded completely: {}", e.getMessage());
        }
    }

    /**
     * Gets all disease names from the results.
     * Each result card has content-desc with the disease name.
     */
    public List<String> getAllDetectedDiseases() {
        logger.info("Getting all detected diseases from results");
        List<String> diseases = new ArrayList<>();

        try {
            // Find all result cards by their title_text resource-id
            List<WebElement> titleElements = driver.findElements(
                AppiumBy.id("com.visualdx.aysa:id/title_text")
            );

            for (WebElement title : titleElements) {
                String text = title.getText();
                if (text != null && !text.isEmpty()) {
                    diseases.add(text);
                    logger.info("Found disease result: {}", text);
                }
            }
        } catch (Exception e) {
            logger.error("Error getting diseases: {}", e.getMessage());
        }

        return diseases;
    }

    /**
     * Checks if the results contain the expected disease.
     * @param expectedDisease The disease to look for (case-insensitive partial match)
     * @return true if found in results
     */
    public boolean containsDisease(String expectedDisease) {
        logger.info("Checking if results contain: {}", expectedDisease);

        List<String> diseases = getAllDetectedDiseases();

        for (String disease : diseases) {
            if (disease.toLowerCase().contains(expectedDisease.toLowerCase())) {
                logger.info("MATCH FOUND: {} contains {}", disease, expectedDisease);
                return true;
            }
        }

        // Also check page source as fallback
        String pageSource = driver.getPageSource();
        if (pageSource.toLowerCase().contains(expectedDisease.toLowerCase())) {
            logger.info("Found {} in page source", expectedDisease);
            return true;
        }

        logger.warn("Disease {} NOT found in results", expectedDisease);
        return false;
    }

    /**
     * Gets the first/primary disease result.
     */
    public String getPrimaryDisease() {
        List<String> diseases = getAllDetectedDiseases();
        return diseases.isEmpty() ? null : diseases.get(0);
    }

    /**
     * Detects if the results screen is showing an image quality error instead of diseases.
     */
    public boolean hasQualityError() {
        String pageSource = driver.getPageSource().toLowerCase();
        return pageSource.contains("quality error")
                || pageSource.contains("image too dark")
                || pageSource.contains("image to dark")
                || pageSource.contains("too blurry")
                || pageSource.contains("unable to detect");
    }

    /**
     * Clicks the DONE button to finish viewing results.
     */
    public void clickDone() {
        logger.info("Clicking DONE button");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement doneButton = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.id("com.visualdx.aysa:id/action_done")
            ));
            doneButton.click();
            sleep(1000);
        } catch (Exception e) {
            logger.warn("DONE button not found, trying back navigation");
            driver.navigate().back();
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
