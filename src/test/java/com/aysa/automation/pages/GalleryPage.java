package com.aysa.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the Android system gallery/photo picker.
 *
 * NOTE: Gallery UI varies by Android version and device manufacturer.
 * These locators target common patterns but may need adjustment.
 */
public class GalleryPage extends BasePage {

    // Common gallery image elements
    @AndroidFindBy(id = "com.google.android.documentsui:id/icon_thumb")
    private List<WebElement> galleryImages;

    @AndroidFindBy(className = "android.widget.ImageView")
    private List<WebElement> imageViews;

    // Google Photos specific
    @AndroidFindBy(id = "com.google.android.apps.photos:id/grid_item")
    private List<WebElement> googlePhotosImages;

    // Samsung Gallery specific
    @AndroidFindBy(id = "com.sec.android.gallery3d:id/thumbnail")
    private List<WebElement> samsungGalleryImages;

    // Generic file picker elements
    @AndroidFindBy(xpath = "//android.widget.ImageView[@clickable='true']")
    private List<WebElement> clickableImages;

    // Search/Browse elements
    @AndroidFindBy(id = "android:id/button1")
    private WebElement confirmButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'Pictures')]")
    private WebElement picturesFolder;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'Images')]")
    private WebElement imagesFolder;

    public GalleryPage(AndroidDriver driver) {
        super(driver);
        logger.info("Initializing Gallery Page");
    }

    @Override
    public boolean isPageDisplayed() {
        // Wait a moment for gallery to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        return !galleryImages.isEmpty() ||
                !googlePhotosImages.isEmpty() ||
                !clickableImages.isEmpty();
    }

    public ImageUploadPage selectImageByName(String imageName) {
        logger.info("Attempting to select image: {}", imageName);

        // Try to find by content description or text
        try {
            WebElement imageElement = driver.findElement(
                    AppiumBy.androidUIAutomator(
                            String.format("new UiSelector().descriptionContains(\"%s\")", imageName)
                    )
            );
            click(imageElement);
            return new ImageUploadPage(driver);
        } catch (Exception e) {
            logger.debug("Could not find image by description, trying text: {}", e.getMessage());
        }

        // Try by text
        try {
            WebElement imageElement = driver.findElement(
                    AppiumBy.androidUIAutomator(
                            String.format("new UiSelector().textContains(\"%s\")",
                                    imageName.replace(".jpg", "").replace(".png", ""))
                    )
            );
            click(imageElement);
            return new ImageUploadPage(driver);
        } catch (Exception e) {
            logger.debug("Could not find image by text: {}", e.getMessage());
        }

        throw new RuntimeException("Could not find image: " + imageName);
    }

    public ImageUploadPage selectImageByIndex(int index) {
        logger.info("Selecting image at index: {}", index);

        List<WebElement> images = getAvailableImages();

        if (images.isEmpty()) {
            throw new RuntimeException("No images found in gallery");
        }

        if (index >= images.size()) {
            throw new RuntimeException(
                    String.format("Image index %d out of bounds. Available images: %d", index, images.size())
            );
        }

        click(images.get(index));
        return new ImageUploadPage(driver);
    }

    private List<WebElement> getAvailableImages() {
        if (!galleryImages.isEmpty()) {
            return galleryImages;
        } else if (!googlePhotosImages.isEmpty()) {
            return googlePhotosImages;
        } else if (!samsungGalleryImages.isEmpty()) {
            return samsungGalleryImages;
        } else if (!clickableImages.isEmpty()) {
            return clickableImages;
        }
        return imageViews;
    }

    public void navigateToPicturesFolder() {
        logger.info("Navigating to Pictures folder");

        if (isDisplayed(picturesFolder)) {
            click(picturesFolder);
        } else if (isDisplayed(imagesFolder)) {
            click(imagesFolder);
        }

        // Wait for folder to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }

    public void scrollDown() {
        logger.info("Scrolling down in gallery");
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"
            ));
        } catch (Exception e) {
            logger.debug("Scroll failed or not needed: {}", e.getMessage());
        }
    }
}
