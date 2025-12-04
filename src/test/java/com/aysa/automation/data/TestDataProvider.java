package com.aysa.automation.data;

import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

import com.aysa.automation.data.TestData.ExpectedResultType;

/**
 * Provides test data for all 48 skin condition images.
 * 12 images each for: eczema, melanoma, psoriasis, fungal_infection
 */
public class TestDataProvider {

    // Expected disease names as shown in the app results
    // eczema -> "Atopic dermatitis"
    // melanoma -> "Melanoma"
    // psoriasis -> "Psoriasis"
    // fungal_infection -> varies

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
        return toData(generateEczemaCases());
    }

    @DataProvider(name = "melanomaTestData")
    public static Object[][] getMelanomaTestData() {
        return toData(generateMelanomaCases());
    }

    @DataProvider(name = "psoriasisTestData")
    public static Object[][] getPsoriasisTestData() {
        return toData(generatePsoriasisCases());
    }

    @DataProvider(name = "fungalTestData")
    public static Object[][] getFungalTestData() {
        return toData(generateFungalCases());
    }

    /**
     * Generates test data for all 48 images (12 per folder).
     */
    private static List<TestData> generateAllTestData() {
        List<TestData> testDataList = new ArrayList<>();
        testDataList.addAll(generateMelanomaCases());
        testDataList.addAll(generateEczemaCases());
        testDataList.addAll(generatePsoriasisCases());
        testDataList.addAll(generateFungalCases());
        return testDataList;
    }

    private static List<TestData> generateMelanomaCases() {
        List<TestData> cases = new ArrayList<>();
        int id = 0;
        cases.add(buildCase(id++, "melanoma", 1, "Melanoma", "Light skin tone, low lighting",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(id++, "melanoma", 2, "Melanoma", "Light skin tone, normal lighting",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(id++, "melanoma", 3, "Melanoma", "Light skin tone, bright lighting",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(id++, "melanoma", 4, "Melanoma", "Medium skin tone, low lighting",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(id++, "melanoma", 5, "Melanoma", "Medium skin tone, normal lighting",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(id++, "melanoma", 6, "Melanoma", "Medium skin tone, bright lighting",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(id++, "melanoma", 7, "Melanoma", "Dark skin tone, low lighting",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(id++, "melanoma", 8, "Melanoma", "Dark skin tone, normal lighting",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(id++, "melanoma", 9, "Melanoma", "Dark skin tone, bright lighting",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(id++, "melanoma", 10, "Melanoma", "Bright lighting, far in distance",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(id++, "melanoma", 11, "Melanoma", "Normal lighting, far in distance",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(id, "melanoma", 12, "Melanoma",
                "Medium skin tone, low lighting, far in distance",
                MELANOMA_CONFIG, ExpectedResultType.DISEASE));
        return cases;
    }

    private static List<TestData> generateEczemaCases() {
        List<TestData> cases = new ArrayList<>();
        int baseId = 12;
        cases.add(buildCase(baseId++, "eczema", 1, "Eczema", "Normal lighting, moderate skin tone",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "eczema", 2, "Eczema", "Low lighting, extremities",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "eczema", 3, "Eczema", "Cropped, moderate skin tone",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "eczema", 4, "Eczema", "Distance, moderate skin tone",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "eczema", 5, "Eczema", "Cropped, light skin tone",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "eczema", 6, "Eczema", "Normal lighting, body",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "eczema", 7, "Eczema", "Angled, dark skin tone",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "eczema", 8, "Eczema", "Bright, light skin tone",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "eczema", 9, "Eczema", "Clear, extremities",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "eczema", 10, "Eczema", "Low lighting, dark skin tone",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "eczema", 11, "Eczema", "Bright, light skin tone (variant)",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId, "eczema", 12, "Eczema", "Cropped, extremities",
                ECZEMA_CONFIG, ExpectedResultType.DISEASE));
        return cases;
    }

    private static List<TestData> generatePsoriasisCases() {
        List<TestData> cases = new ArrayList<>();
        int baseId = 24;
        cases.add(buildCase(baseId++, "psoriasis", 1, "Nothing", "Clear skin",
                PSORIASIS_CONFIG, ExpectedResultType.NONE));
        cases.add(buildCase(baseId++, "psoriasis", 2, "Psoriasis",
                "Healthy skin with psoriasis", PSORIASIS_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "psoriasis", 3, "Psoriasis", "Low-light of psoriasis",
                PSORIASIS_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "psoriasis", 4, "Psoriasis", "Psoriasis in dark conditions",
                PSORIASIS_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "psoriasis", 5, "Psoriasis", "Psoriasis on dark skin tones",
                PSORIASIS_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "psoriasis", 6, "Psoriasis", "Psoriasis on low resolution",
                PSORIASIS_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "psoriasis", 7, "Psoriasis", "Psoriasis on blurry image",
                PSORIASIS_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "psoriasis", 8, "Psoriasis", "Psoriasis high resolution image",
                PSORIASIS_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "psoriasis", 9, "Psoriasis", "Psoriasis on light skin",
                PSORIASIS_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "psoriasis", 10, "Psoriasis", "Psoriasis on low resolution (2)",
                PSORIASIS_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "psoriasis", 11, "Psoriasis", "Large psoriasis coverage",
                PSORIASIS_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId, "psoriasis", 12, "Psoriasis", "Small psoriasis",
                PSORIASIS_CONFIG, ExpectedResultType.DISEASE));
        return cases;
    }

    private static List<TestData> generateFungalCases() {
        List<TestData> cases = new ArrayList<>();
        int baseId = 36;
        cases.add(buildCase(baseId++, "fungal_infection", 1, "Fungal Infection",
                "Fungal infection dark lighting", FUNGAL_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "fungal_infection", 2, "Fungal Infection",
                "Well lit spread fungal infection (Ringworm)", FUNGAL_CONFIG,
                ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "fungal_infection", 3, "Fungal Infection",
                "Multiple lesions fungal infection", FUNGAL_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "fungal_infection", 4, "Fungal Infection",
                "Healthy skin", FUNGAL_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "fungal_infection", 5, "Fungal Infection",
                "Fungal infection dark skin tone", FUNGAL_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "fungal_infection", 6, "Fungal Infection",
                "Fungal infection lighter skin tone", FUNGAL_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "fungal_infection", 7, "Fungal Infection",
                "Minor single spot fungal infection", FUNGAL_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "fungal_infection", 8, "Fungal Infection",
                "Extensive lesion / coverage", FUNGAL_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "fungal_infection", 9, "Fungal Infection",
                "High lighting conditions", FUNGAL_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "fungal_infection", 10, "Fungal Infection",
                "Lower lighting conditions", FUNGAL_CONFIG, ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId++, "fungal_infection", 11, "Fungal Infection",
                "Minimum image resolution fungal infection", FUNGAL_CONFIG,
                ExpectedResultType.DISEASE));
        cases.add(buildCase(baseId, "fungal_infection", 12, "Fungal Infection",
                "Maximum image resolution fungal infection", FUNGAL_CONFIG,
                ExpectedResultType.DISEASE));
        return cases;
    }

    private static TestData buildCase(int id, String folderName, int imageNumber,
            String expectedDisease, String description, QuestionnaireConfig config,
            ExpectedResultType expectedResultType) {
        TestData testData = new TestData(id, folderName, imageNumber + ".jpg", expectedDisease);
        testData.setDescription(description);
        testData.setExpectedResultType(expectedResultType);
        testData.withQuestionnaireAnswers(
                config.flakyBumpy,
                config.bodyCoverage,
                config.bodyLocation,
                config.duration,
                config.itches,
                config.fever);
        return testData;
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
