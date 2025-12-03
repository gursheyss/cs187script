package com.aysa.automation.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * Page Object for the image upload/preview screen.
 *
 * NOTE: Element locators are placeholders - update based on actual app.
 */
public class ImageUploadPage extends BasePage {

    // PLACEHOLDER: Update these locators based on actual app elements
    @AndroidFindBy(id = "com.visualdx.aysa:id/image_preview")
    private WebElement imagePreview;

    @AndroidFindBy(id = "com.visualdx.aysa:id/btn_analyze")
    private WebElement analyzeButton;

    @AndroidFindBy(id = "com.visualdx.aysa:id/btn_confirm")
    private WebElement confirmButton;

    @AndroidFindBy(id = "com.visualdx.aysa:id/btn_retake")
    private WebElement retakeButton;

    @AndroidFindBy(id = "com.visualdx.aysa:id/btn_submit")
    private WebElement submitButton;

    // Alternative locators using XPath
    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text, 'Analyze')]")
    private WebElement analyzeButtonByText;

    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text, 'Submit')]")
    private WebElement submitButtonByText;

    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text, 'Confirm')]")
    private WebElement confirmButtonByText;

    // Loading indicator
    @AndroidFindBy(id = "com.visualdx.aysa:id/progress_bar")
    private WebElement progressBar;

    @AndroidFindBy(className = "android.widget.ProgressBar")
    private WebElement progressBarGeneric;

    public ImageUploadPage(AndroidDriver driver) {
        super(driver);
        logger.info("Initializing Image Upload Page");
    }

    @Override
    public boolean isPageDisplayed() {
        return isDisplayed(imagePreview) ||
                isDisplayed(analyzeButton) ||
                isDisplayed(confirmButton);
    }

    public boolean isImagePreviewDisplayed() {
        return isDisplayed(imagePreview);
    }

    public ResultsPage clickAnalyze() {
        logger.info("Clicking analyze button");

        // Try different button locators
        if (isDisplayed(analyzeButton)) {
            click(analyzeButton);
        } else if (isDisplayed(submitButton)) {
            click(submitButton);
        } else if (isDisplayed(confirmButton)) {
            click(confirmButton);
        } else if (isDisplayed(analyzeButtonByText)) {
            click(analyzeButtonByText);
        } else if (isDisplayed(submitButtonByText)) {
            click(submitButtonByText);
        } else if (isDisplayed(confirmButtonByText)) {
            click(confirmButtonByText);
        } else {
            throw new RuntimeException("Analyze/Submit button not found");
        }

        // Wait for loading to complete
        waitForAnalysisToComplete();

        return new ResultsPage(driver);
    }

    private void waitForAnalysisToComplete() {
        logger.info("Waiting for analysis to complete");

        int maxWaitSeconds = 60;
        int waitedSeconds = 0;

        // Wait for loading to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        // Wait for loading to finish
        while (waitedSeconds < maxWaitSeconds) {
            if (!isDisplayed(progressBar) && !isDisplayed(progressBarGeneric)) {
                logger.info("Analysis completed after {} seconds", waitedSeconds);
                return;
            }

            try {
                Thread.sleep(1000);
                waitedSeconds++;
            } catch (InterruptedException ignored) {
            }
        }

        logger.warn("Analysis may not have completed within timeout");
    }

    public HomePage clickRetake() {
        logger.info("Clicking retake button");
        click(retakeButton);
        return new HomePage(driver);
    }
}
