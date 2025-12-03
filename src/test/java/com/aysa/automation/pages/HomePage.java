package com.aysa.automation.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * Page Object for the Aysa app home screen.
 *
 * NOTE: Element locators are placeholders. Update based on actual app
 * inspection using Appium Inspector.
 */
public class HomePage extends BasePage {

    // PLACEHOLDER: Update these locators based on actual app elements
    @AndroidFindBy(id = "com.visualdx.aysa:id/btn_upload_image")
    private WebElement uploadImageButton;

    @AndroidFindBy(id = "com.visualdx.aysa:id/btn_take_photo")
    private WebElement takePhotoButton;

    @AndroidFindBy(id = "com.visualdx.aysa:id/home_title")
    private WebElement homeTitle;

    @AndroidFindBy(accessibility = "Upload Image")
    private WebElement uploadImageAccessibility;

    // Alternative locators using XPath
    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text, 'Upload')]")
    private WebElement uploadButtonByText;

    @AndroidFindBy(xpath = "//android.widget.Button[contains(@text, 'Gallery')]")
    private WebElement galleryButtonByText;

    public HomePage(AndroidDriver driver) {
        super(driver);
        logger.info("Initializing Home Page");
    }

    @Override
    public boolean isPageDisplayed() {
        try {
            return waitUtils.waitForElementToBeVisible(homeTitle) != null
                    || isDisplayed(uploadImageButton);
        } catch (Exception e) {
            logger.warn("Home page check failed: {}", e.getMessage());
            return false;
        }
    }

    public GalleryPage clickUploadImage() {
        logger.info("Clicking upload image button");

        // Try multiple locator strategies
        if (isDisplayed(uploadImageButton)) {
            click(uploadImageButton);
        } else if (isDisplayed(uploadImageAccessibility)) {
            click(uploadImageAccessibility);
        } else if (isDisplayed(uploadButtonByText)) {
            click(uploadButtonByText);
        } else if (isDisplayed(galleryButtonByText)) {
            click(galleryButtonByText);
        } else {
            throw new RuntimeException("Upload image button not found");
        }

        return new GalleryPage(driver);
    }

    public ImageUploadPage clickTakePhoto() {
        logger.info("Clicking take photo button");
        click(takePhotoButton);
        return new ImageUploadPage(driver);
    }

    public void waitForPageLoad() {
        logger.info("Waiting for home page to load");
        waitUtils.waitForElementToBeVisible(uploadImageButton);
    }
}
