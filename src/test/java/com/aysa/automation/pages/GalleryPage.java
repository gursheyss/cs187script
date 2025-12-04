package com.aysa.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the Aysa app's custom image picker.
 *
 * NOTE: This is NOT the system gallery - Aysa uses a custom image picker
 * with its own UI elements.
 */
public class GalleryPage extends BasePage {

    // Aysa custom image picker - main image grid
    @AndroidFindBy(id = "com.visualdx.aysa:id/image")
    private List<WebElement> galleryImages;

    // Image recycler view container
    @AndroidFindBy(id = "com.visualdx.aysa:id/image_recycler_view")
    private WebElement imageRecyclerView;

    // Album/folder selector spinner
    @AndroidFindBy(id = "com.visualdx.aysa:id/gallery_spinner")
    private WebElement albumSpinner;

    // Navigation buttons
    @AndroidFindBy(id = "com.visualdx.aysa:id/button_back")
    private WebElement backButton;

    @AndroidFindBy(id = "com.visualdx.aysa:id/button_cancel")
    private WebElement cancelButton;

    // Fallback: clickable images by class
    @AndroidFindBy(xpath = "//android.widget.ImageView[@clickable='true']")
    private List<WebElement> clickableImages;

    // Album selector text (shows current album like "Pictures")
    @AndroidFindBy(xpath = "//android.widget.Spinner[@resource-id='com.visualdx.aysa:id/gallery_spinner']//android.widget.TextView")
    private WebElement currentAlbumText;

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

        return isDisplayed(imageRecyclerView) || !galleryImages.isEmpty();
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
        } else if (!clickableImages.isEmpty()) {
            return clickableImages;
        }
        return galleryImages;
    }

    public void selectAlbum(String albumName) {
        logger.info("Selecting album: {}", albumName);

        if (isDisplayed(albumSpinner)) {
            click(albumSpinner);
            // Wait for dropdown to appear
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
            // Try to find and click the album
            try {
                WebElement albumOption = driver.findElement(
                        AppiumBy.androidUIAutomator(
                                String.format("new UiSelector().textContains(\"%s\")", albumName)
                        )
                );
                click(albumOption);
            } catch (Exception e) {
                logger.warn("Could not find album: {}", albumName);
            }
        }
    }

    public String getCurrentAlbum() {
        if (isDisplayed(currentAlbumText)) {
            return currentAlbumText.getText();
        }
        return "";
    }

    public void clickBack() {
        logger.info("Clicking back button");
        if (isDisplayed(backButton)) {
            click(backButton);
        }
    }

    public void clickCancel() {
        logger.info("Clicking cancel button");
        if (isDisplayed(cancelButton)) {
            click(cancelButton);
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

    public int getImageCount() {
        return galleryImages.size();
    }
}
