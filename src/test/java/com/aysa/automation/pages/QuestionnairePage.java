package com.aysa.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for the Aysa questionnaire flow.
 * Handles all diagnostic questions after image selection.
 */
public class QuestionnairePage extends BasePage {

    // Photo preview screen
    @AndroidFindBy(xpath = "//android.widget.Button[@text='USE THIS PHOTO']")
    private WebElement useThisPhotoButton;

    // First question - Is it flaky/bumpy
    @AndroidFindBy(xpath = "//android.widget.Button[@text='YES']")
    private WebElement yesButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='NO']")
    private WebElement noButton;

    // Continue button (used on multiple screens)
    @AndroidFindBy(xpath = "//android.widget.Button[@text='CONTINUE']")
    private WebElement continueButton;

    // Body coverage options
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Single Lesion']")
    private WebElement singleLesionOption;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Limited Area']")
    private WebElement limitedAreaOption;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Widespread']")
    private WebElement widespreadOption;

    // Cancel button
    @AndroidFindBy(accessibility = "Cancel")
    private WebElement cancelButton;

    public QuestionnairePage(AndroidDriver driver) {
        super(driver);
        logger.info("Initializing Questionnaire Page");
    }

    @Override
    public boolean isPageDisplayed() {
        return isDisplayed(useThisPhotoButton) ||
               isDisplayed(yesButton) ||
               isDisplayed(continueButton);
    }

    /**
     * Clicks "USE THIS PHOTO" button on the photo preview screen.
     */
    public QuestionnairePage clickUseThisPhoto() {
        logger.info("Clicking USE THIS PHOTO button");
        sleep(1500); // Wait for image to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(useThisPhotoButton));
        button.click();
        sleep(1500);
        return this;
    }

    /**
     * Answers the flaky/bumpy question.
     * @param answer "YES" or "NO"
     */
    public QuestionnairePage answerFlakyBumpyQuestion(String answer) {
        logger.info("Answering flaky/bumpy question: {}", answer);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        if ("YES".equalsIgnoreCase(answer)) {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(yesButton));
            button.click();
        } else {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(noButton));
            button.click();
        }
        sleep(1000);
        return this;
    }

    /**
     * Selects a profile by name.
     * @param profileName The profile name (e.g., "test")
     */
    public QuestionnairePage selectProfile(String profileName) {
        logger.info("Selecting profile: {}", profileName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId(profileName)
        ));
        profile.click();
        sleep(1000);
        return this;
    }

    /**
     * Selects how much of the body is affected.
     * @param option "Single Lesion", "Limited Area", or "Widespread"
     */
    public QuestionnairePage selectBodyCoverage(String option) {
        logger.info("Selecting body coverage: {}", option);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement optionElement = wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.widget.TextView[@text='" + option + "']")
        ));
        optionElement.click();
        sleep(1000);
        return this;
    }

    /**
     * Selects body location on the body diagram.
     * @param location The body part accessibility ID (e.g., "arm-lower-right")
     */
    public QuestionnairePage selectBodyLocation(String location) {
        logger.info("Selecting body location: {}", location);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait for body diagram to load
        sleep(2000);

        // First click zooms in
        WebElement bodyPart = wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId(location)
        ));
        bodyPart.click();
        logger.info("First click on {} - zooming in", location);
        sleep(2000);

        // Second click selects - re-find element after zoom
        bodyPart = wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId(location)
        ));
        bodyPart.click();
        logger.info("Second click on {} - selecting", location);
        sleep(2500);

        // Wait for CONTINUE button to appear after selection
        logger.info("Waiting for CONTINUE button...");
        sleep(1500);

        // Click CONTINUE
        clickContinue();
        return this;
    }

    /**
     * Selects duration of condition.
     * @param duration One of: "Minutes to Hours", "Days to Weeks", "Weeks to Months",
     *                 "Months to Years", "Recurring Episodes"
     */
    public QuestionnairePage selectDuration(String duration) {
        logger.info("Selecting duration: {}", duration);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement durationOption = wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId(duration)
        ));
        durationOption.click();
        sleep(1000);
        return this;
    }

    /**
     * Answers a yes/no question (itch, fever, etc.)
     * @param answer "Yes" or "No"
     */
    public QuestionnairePage answerYesNo(String answer) {
        logger.info("Answering yes/no question: {}", answer);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement answerButton = wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId(answer)
        ));
        answerButton.click();
        sleep(1000);
        return this;
    }

    /**
     * Clicks the CONTINUE button.
     */
    public QuestionnairePage clickContinue() {
        logger.info("Clicking CONTINUE button");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Wait a bit for button to become visible
        sleep(500);

        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        button.click();
        logger.info("CONTINUE button clicked");
        sleep(1500);
        return this;
    }

    /**
     * Completes the full questionnaire flow with given parameters.
     */
    public ResultsPage completeQuestionnaire(
            String flakyBumpy,
            String profileName,
            String bodyCoverage,
            String bodyLocation,
            String duration,
            String itches,
            String fever) {

        logger.info("Completing full questionnaire flow");

        clickUseThisPhoto();
        answerFlakyBumpyQuestion(flakyBumpy);
        selectProfile(profileName);
        selectBodyCoverage(bodyCoverage);
        selectBodyLocation(bodyLocation);
        selectDuration(duration);
        answerYesNo(itches);  // Does it itch?
        answerYesNo(fever);   // Do you have a fever?

        // Final CONTINUE to submit and get results
        logger.info("Submitting questionnaire for analysis...");
        sleep(1000);
        clickContinue();

        // Wait for API response and results to load
        logger.info("Waiting for results to load...");
        sleep(5000);

        return new ResultsPage(driver);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
