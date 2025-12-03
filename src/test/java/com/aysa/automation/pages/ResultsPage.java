package com.aysa.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for the disease detection results screen.
 *
 * NOTE: Element locators are placeholders - update based on actual app.
 */
public class ResultsPage extends BasePage {

    // PLACEHOLDER: Update these locators based on actual app elements
    @AndroidFindBy(id = "com.visualdx.aysa:id/disease_name")
    private WebElement primaryDiseaseName;

    @AndroidFindBy(id = "com.visualdx.aysa:id/disease_title")
    private WebElement diseaseTitle;

    @AndroidFindBy(id = "com.visualdx.aysa:id/result_text")
    private WebElement resultText;

    @AndroidFindBy(id = "com.visualdx.aysa:id/diagnosis_result")
    private WebElement diagnosisResult;

    // List of all detected diseases (if multiple results shown)
    @AndroidFindBy(id = "com.visualdx.aysa:id/disease_item_name")
    private List<WebElement> diseaseListItems;

    // Confidence/probability elements
    @AndroidFindBy(id = "com.visualdx.aysa:id/confidence_value")
    private WebElement confidenceValue;

    // Navigation buttons
    @AndroidFindBy(id = "com.visualdx.aysa:id/btn_new_scan")
    private WebElement newScanButton;

    @AndroidFindBy(id = "com.visualdx.aysa:id/btn_details")
    private WebElement detailsButton;

    @AndroidFindBy(id = "com.visualdx.aysa:id/btn_back")
    private WebElement backButton;

    // Alternative locators using XPath
    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@resource-id, 'disease')]")
    private WebElement diseaseTextByPartialId;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@resource-id, 'result')]")
    private WebElement resultTextByPartialId;

    public ResultsPage(AndroidDriver driver) {
        super(driver);
        logger.info("Initializing Results Page");
        waitForResultsToLoad();
    }

    @Override
    public boolean isPageDisplayed() {
        return isDisplayed(primaryDiseaseName) ||
                isDisplayed(diseaseTitle) ||
                isDisplayed(resultText) ||
                isDisplayed(diagnosisResult);
    }

    private void waitForResultsToLoad() {
        logger.info("Waiting for results page to load");
        try {
            Thread.sleep(2000); // Initial wait for page transition
            waitUtils.waitForElementToBeVisible(primaryDiseaseName);
        } catch (Exception e) {
            logger.debug("Primary disease element not found, checking alternatives");
        }
    }

    public String getDetectedDiseaseName() {
        logger.info("Getting detected disease name");

        String diseaseName = null;

        // Try multiple locator strategies
        if (isDisplayed(primaryDiseaseName)) {
            diseaseName = getText(primaryDiseaseName);
        } else if (isDisplayed(diseaseTitle)) {
            diseaseName = getText(diseaseTitle);
        } else if (isDisplayed(resultText)) {
            diseaseName = getText(resultText);
        } else if (isDisplayed(diagnosisResult)) {
            diseaseName = getText(diagnosisResult);
        } else if (isDisplayed(diseaseTextByPartialId)) {
            diseaseName = getText(diseaseTextByPartialId);
        }

        if (diseaseName == null || diseaseName.isEmpty()) {
            // Last resort: search entire page for disease name patterns
            diseaseName = searchPageForDiseaseName();
        }

        logger.info("Detected disease: {}", diseaseName);
        return diseaseName;
    }

    public boolean containsDisease(String expectedDisease) {
        logger.info("Checking if results contain disease: {}", expectedDisease);

        String detectedDisease = getDetectedDiseaseName();
        if (detectedDisease != null &&
                detectedDisease.toLowerCase().contains(expectedDisease.toLowerCase())) {
            return true;
        }

        // Also check the list of diseases if multiple results
        for (WebElement diseaseItem : diseaseListItems) {
            String itemText = getText(diseaseItem);
            if (itemText != null &&
                    itemText.toLowerCase().contains(expectedDisease.toLowerCase())) {
                return true;
            }
        }

        // Search entire page source as fallback
        String pageSource = driver.getPageSource();
        return pageSource.toLowerCase().contains(expectedDisease.toLowerCase());
    }

    public List<String> getAllDetectedDiseases() {
        logger.info("Getting all detected diseases");
        return diseaseListItems.stream()
                .map(this::getText)
                .filter(text -> text != null && !text.isEmpty())
                .collect(Collectors.toList());
    }

    public String getConfidenceValue() {
        if (isDisplayed(confidenceValue)) {
            return getText(confidenceValue);
        }
        return null;
    }

    public HomePage clickNewScan() {
        logger.info("Clicking new scan button");
        click(newScanButton);
        return new HomePage(driver);
    }

    public HomePage clickBack() {
        logger.info("Clicking back button");
        if (isDisplayed(backButton)) {
            click(backButton);
        } else {
            driver.navigate().back();
        }
        return new HomePage(driver);
    }

    private String searchPageForDiseaseName() {
        logger.debug("Searching page for disease name");

        try {
            // Find all TextView elements and search for likely disease names
            List<WebElement> textViews = driver.findElements(
                    AppiumBy.className("android.widget.TextView")
            );

            for (WebElement tv : textViews) {
                String text = tv.getText();
                // Skip obvious non-disease text
                if (text != null && text.length() > 3 &&
                        !text.matches(".*[0-9]{2,}.*") && // Skip texts with numbers
                        !text.toLowerCase().contains("upload") &&
                        !text.toLowerCase().contains("scan") &&
                        !text.toLowerCase().contains("button")) {
                    return text;
                }
            }
        } catch (Exception e) {
            logger.error("Error searching page: {}", e.getMessage());
        }

        return null;
    }
}
