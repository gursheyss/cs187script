package com.aysa.automation.data;

import org.testng.annotations.DataProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.aysa.automation.data.TestData.ExpectedResultType;

/**
 * Provides test data for all skin condition images including variations.
 * Dynamically scans total_images/ folder for original images and their variations:
 * - Lighting: low_light, bright
 * - Photo conditions: distance, cropped, angled
 *
 * Diseases: eczema, melanoma, psoriasis, fungal_infection
 */
public class TestDataProvider {

    // Image source directory (with variations)
    private static final String IMAGES_DIR = "total_images";

    // Disease folders
    private static final String[] DISEASES = {"melanoma", "eczema", "psoriasis", "fungal_infection"};

    // Variation suffixes
    private static final String[] VARIATIONS = {"", "_low_light", "_bright", "_distance", "_cropped", "_angled"};

    // Questionnaire configurations per disease type
    private static final QuestionnaireConfig ECZEMA_CONFIG = new QuestionnaireConfig(
            "YES", // flakyBumpy
            "Limited Area", // bodyCoverage
            "arm-lower-right", // bodyLocation
            "Weeks to Months", // duration
            "Yes", // itches
            "No" // fever
    );

    private static final QuestionnaireConfig MELANOMA_CONFIG = new QuestionnaireConfig(
            "NO", // flakyBumpy
            "Single Lesion", // bodyCoverage
            "arm-lower-right", // bodyLocation
            "Months to Years", // duration
            "No", // itches
            "No" // fever
    );

    private static final QuestionnaireConfig PSORIASIS_CONFIG = new QuestionnaireConfig(
            "YES", // flakyBumpy
            "Limited Area", // bodyCoverage
            "arm-lower-right", // bodyLocation
            "Weeks to Months", // duration
            "Yes", // itches
            "No" // fever
    );

    private static final QuestionnaireConfig FUNGAL_CONFIG = new QuestionnaireConfig(
            "YES", // flakyBumpy
            "Limited Area", // bodyCoverage
            "arm-lower-right", // bodyLocation
            "Days to Weeks", // duration
            "Yes", // itches
            "No" // fever
    );

    @DataProvider(name = "diseaseTestData")
    public static Object[][] getDiseaseTestData() {
        return toData(generateAllTestData());
    }

    @DataProvider(name = "eczemaTestData")
    public static Object[][] getEczemaTestData() {
        return toData(generateDiseaseCases("eczema"));
    }

    @DataProvider(name = "melanomaTestData")
    public static Object[][] getMelanomaTestData() {
        return toData(generateDiseaseCases("melanoma"));
    }

    @DataProvider(name = "psoriasisTestData")
    public static Object[][] getPsoriasisTestData() {
        return toData(generateDiseaseCases("psoriasis"));
    }

    @DataProvider(name = "fungalTestData")
    public static Object[][] getFungalTestData() {
        return toData(generateDiseaseCases("fungal_infection"));
    }

    /**
     * Generates test data for all images in total_images/ (originals + variations).
     */
    private static List<TestData> generateAllTestData() {
        List<TestData> testDataList = new ArrayList<>();
        for (String disease : DISEASES) {
            testDataList.addAll(generateDiseaseCases(disease));
        }
        return testDataList;
    }

    /**
     * Generates test cases for a specific disease by scanning the folder.
     */
    private static List<TestData> generateDiseaseCases(String disease) {
        List<TestData> cases = new ArrayList<>();
        QuestionnaireConfig config = getConfigForDisease(disease);
        String expectedDisease = getExpectedDiseaseLabel(disease);

        File diseaseDir = new File(IMAGES_DIR, disease);
        if (!diseaseDir.exists() || !diseaseDir.isDirectory()) {
            System.err.println("Warning: Disease directory not found: " + diseaseDir.getAbsolutePath());
            return cases;
        }

        // Get all jpg files in the directory
        File[] imageFiles = diseaseDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
        if (imageFiles == null || imageFiles.length == 0) {
            System.err.println("Warning: No images found in: " + diseaseDir.getAbsolutePath());
            return cases;
        }

        // Sort files for consistent ordering
        Arrays.sort(imageFiles, (a, b) -> {
            // Extract base number and variation for sorting
            String nameA = a.getName().replace(".jpg", "");
            String nameB = b.getName().replace(".jpg", "");
            int numA = extractBaseNumber(nameA);
            int numB = extractBaseNumber(nameB);
            if (numA != numB) return Integer.compare(numA, numB);
            return nameA.compareTo(nameB);
        });

        int id = getBaseIdForDisease(disease);
        for (File imageFile : imageFiles) {
            String imageName = imageFile.getName();
            String description = generateDescription(disease, imageName);
            ExpectedResultType resultType = getExpectedResultType(disease, imageName);

            TestData testData = new TestData(id++, disease, imageName, expectedDisease);
            testData.setDescription(description);
            testData.setExpectedResultType(resultType);
            testData.withQuestionnaireAnswers(
                    config.flakyBumpy,
                    config.bodyCoverage,
                    config.bodyLocation,
                    config.duration,
                    config.itches,
                    config.fever);
            cases.add(testData);
        }

        return cases;
    }

    /**
     * Extracts the base image number from a filename (e.g., "3_low_light" -> 3).
     */
    private static int extractBaseNumber(String fileName) {
        String baseName = fileName.split("_")[0];
        try {
            return Integer.parseInt(baseName);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Generates a human-readable description for the test case.
     */
    private static String generateDescription(String disease, String imageName) {
        String baseName = imageName.replace(".jpg", "");
        String diseaseLabel = disease.replace("_", " ");

        if (baseName.contains("_low_light")) {
            return diseaseLabel + " - Low lighting variation";
        } else if (baseName.contains("_bright")) {
            return diseaseLabel + " - Bright lighting variation";
        } else if (baseName.contains("_distance")) {
            return diseaseLabel + " - Distance (zoomed out) variation";
        } else if (baseName.contains("_cropped")) {
            return diseaseLabel + " - Cropped (zoomed in) variation";
        } else if (baseName.contains("_angled")) {
            return diseaseLabel + " - Angled photo variation";
        } else {
            return diseaseLabel + " - Original image";
        }
    }

    /**
     * Returns the expected result type for a test case.
     * Special case: psoriasis image 1 expects NONE (clear skin).
     */
    private static ExpectedResultType getExpectedResultType(String disease, String imageName) {
        // psoriasis/1.jpg and its variations are clear skin images
        if (disease.equals("psoriasis") && imageName.startsWith("1")) {
            return ExpectedResultType.NONE;
        }
        return ExpectedResultType.DISEASE;
    }

    /**
     * Returns the questionnaire config for a disease.
     */
    private static QuestionnaireConfig getConfigForDisease(String disease) {
        switch (disease) {
            case "eczema":
                return ECZEMA_CONFIG;
            case "melanoma":
                return MELANOMA_CONFIG;
            case "psoriasis":
                return PSORIASIS_CONFIG;
            case "fungal_infection":
                return FUNGAL_CONFIG;
            default:
                return ECZEMA_CONFIG;
        }
    }

    /**
     * Returns the expected disease label as shown in app results.
     */
    private static String getExpectedDiseaseLabel(String disease) {
        switch (disease) {
            case "eczema":
                return "Eczema";
            case "melanoma":
                return "Melanoma";
            case "psoriasis":
                return "Psoriasis";
            case "fungal_infection":
                return "Fungal Infection";
            default:
                return disease;
        }
    }

    /**
     * Returns the base ID for a disease (for consistent ID ranges).
     */
    private static int getBaseIdForDisease(String disease) {
        switch (disease) {
            case "melanoma":
                return 0;
            case "eczema":
                return 1000;
            case "psoriasis":
                return 2000;
            case "fungal_infection":
                return 3000;
            default:
                return 0;
        }
    }

    private static Object[][] toData(List<TestData> testDataList) {
        Object[][] data = new Object[testDataList.size()][1];
        for (int i = 0; i < testDataList.size(); i++) {
            data[i][0] = testDataList.get(i);
        }
        return data;
    }

    public static TestData getTestDataById(int id) {
        List<TestData> allData = generateAllTestData();
        return allData.stream()
                .filter(td -> td.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Helper class to hold questionnaire configuration per disease type.
     */
    private static class QuestionnaireConfig {
        final String flakyBumpy;
        final String bodyCoverage;
        final String bodyLocation;
        final String duration;
        final String itches;
        final String fever;

        QuestionnaireConfig(String flakyBumpy, String bodyCoverage, String bodyLocation,
                String duration, String itches, String fever) {
            this.flakyBumpy = flakyBumpy;
            this.bodyCoverage = bodyCoverage;
            this.bodyLocation = bodyLocation;
            this.duration = duration;
            this.itches = itches;
            this.fever = fever;
        }
    }
}
