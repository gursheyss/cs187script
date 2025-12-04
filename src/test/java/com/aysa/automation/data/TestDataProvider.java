package com.aysa.automation.data;

import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides test data for all 48 skin condition images.
 * 12 images each for: eczema, melanoma, psoriasis, fungal_infection
 */
public class TestDataProvider {

    private static final int IMAGES_PER_FOLDER = 12;

    // Expected disease names as shown in the app results
    // eczema -> "Atopic dermatitis" (app shows this medical term)
    // melanoma -> "Melanoma" (need to verify)
    // psoriasis -> "Psoriasis" (need to verify)
    // fungal_infection -> varies (need to verify)

    // Questionnaire configurations per disease type
    private static final QuestionnaireConfig ECZEMA_CONFIG = new QuestionnaireConfig(
            "YES",           // flakyBumpy
            "Limited Area",  // bodyCoverage
            "arm-lower-right", // bodyLocation
            "Weeks to Months", // duration
            "Yes",           // itches
            "No"             // fever
    );

    private static final QuestionnaireConfig MELANOMA_CONFIG = new QuestionnaireConfig(
            "NO",            // flakyBumpy
            "Single Lesion", // bodyCoverage
            "arm-lower-right", // bodyLocation
            "Months to Years", // duration
            "No",            // itches
            "No"             // fever
    );

    private static final QuestionnaireConfig PSORIASIS_CONFIG = new QuestionnaireConfig(
            "YES",           // flakyBumpy
            "Limited Area",  // bodyCoverage
            "arm-lower-right", // bodyLocation
            "Weeks to Months", // duration
            "Yes",           // itches
            "No"             // fever
    );

    private static final QuestionnaireConfig FUNGAL_CONFIG = new QuestionnaireConfig(
            "YES",           // flakyBumpy
            "Limited Area",  // bodyCoverage
            "arm-lower-right", // bodyLocation
            "Days to Weeks", // duration
            "Yes",           // itches
            "No"             // fever
    );

    @DataProvider(name = "diseaseTestData")
    public static Object[][] getDiseaseTestData() {
        List<TestData> testDataList = generateAllTestData();
        Object[][] data = new Object[testDataList.size()][1];

        for (int i = 0; i < testDataList.size(); i++) {
            data[i][0] = testDataList.get(i);
        }

        return data;
    }

    @DataProvider(name = "eczemaTestData")
    public static Object[][] getEczemaTestData() {
        return getTestDataForFolder("eczema", ECZEMA_CONFIG, "Atopic dermatitis");
    }

    @DataProvider(name = "melanomaTestData")
    public static Object[][] getMelanomaTestData() {
        return getTestDataForFolder("melanoma", MELANOMA_CONFIG, "melanoma");
    }

    @DataProvider(name = "psoriasisTestData")
    public static Object[][] getPsoriasisTestData() {
        return getTestDataForFolder("psoriasis", PSORIASIS_CONFIG, "psoriasis");
    }

    @DataProvider(name = "fungalTestData")
    public static Object[][] getFungalTestData() {
        return getTestDataForFolder("fungal_infection", FUNGAL_CONFIG, "fungal");
    }

    private static Object[][] getTestDataForFolder(String folderName, QuestionnaireConfig config, String expectedDisease) {
        Object[][] data = new Object[IMAGES_PER_FOLDER][1];

        for (int i = 0; i < IMAGES_PER_FOLDER; i++) {
            int imageNum = i + 1;
            TestData testData = new TestData(i, folderName, imageNum + ".jpg", expectedDisease);
            testData.withQuestionnaireAnswers(
                    config.flakyBumpy,
                    config.bodyCoverage,
                    config.bodyLocation,
                    config.duration,
                    config.itches,
                    config.fever
            );
            data[i][0] = testData;
        }

        return data;
    }

    /**
     * Generates test data for all 48 images (12 per folder).
     */
    private static List<TestData> generateAllTestData() {
        List<TestData> testDataList = new ArrayList<>();
        int id = 0;

        // Eczema (12 images) - app shows "Atopic dermatitis" for eczema
        for (int i = 1; i <= IMAGES_PER_FOLDER; i++) {
            TestData td = new TestData(id++, "eczema", i + ".jpg", "Atopic dermatitis");
            td.withQuestionnaireAnswers(
                    ECZEMA_CONFIG.flakyBumpy,
                    ECZEMA_CONFIG.bodyCoverage,
                    ECZEMA_CONFIG.bodyLocation,
                    ECZEMA_CONFIG.duration,
                    ECZEMA_CONFIG.itches,
                    ECZEMA_CONFIG.fever
            );
            testDataList.add(td);
        }

        // Melanoma (12 images)
        for (int i = 1; i <= IMAGES_PER_FOLDER; i++) {
            TestData td = new TestData(id++, "melanoma", i + ".jpg", "melanoma");
            td.withQuestionnaireAnswers(
                    MELANOMA_CONFIG.flakyBumpy,
                    MELANOMA_CONFIG.bodyCoverage,
                    MELANOMA_CONFIG.bodyLocation,
                    MELANOMA_CONFIG.duration,
                    MELANOMA_CONFIG.itches,
                    MELANOMA_CONFIG.fever
            );
            testDataList.add(td);
        }

        // Psoriasis (12 images)
        for (int i = 1; i <= IMAGES_PER_FOLDER; i++) {
            TestData td = new TestData(id++, "psoriasis", i + ".jpg", "psoriasis");
            td.withQuestionnaireAnswers(
                    PSORIASIS_CONFIG.flakyBumpy,
                    PSORIASIS_CONFIG.bodyCoverage,
                    PSORIASIS_CONFIG.bodyLocation,
                    PSORIASIS_CONFIG.duration,
                    PSORIASIS_CONFIG.itches,
                    PSORIASIS_CONFIG.fever
            );
            testDataList.add(td);
        }

        // Fungal infection (12 images)
        for (int i = 1; i <= IMAGES_PER_FOLDER; i++) {
            TestData td = new TestData(id++, "fungal_infection", i + ".jpg", "fungal");
            td.withQuestionnaireAnswers(
                    FUNGAL_CONFIG.flakyBumpy,
                    FUNGAL_CONFIG.bodyCoverage,
                    FUNGAL_CONFIG.bodyLocation,
                    FUNGAL_CONFIG.duration,
                    FUNGAL_CONFIG.itches,
                    FUNGAL_CONFIG.fever
            );
            testDataList.add(td);
        }

        return testDataList;
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
