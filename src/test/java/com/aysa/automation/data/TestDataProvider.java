package com.aysa.automation.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestDataProvider {

    private static final String TEST_DATA_FILE = "src/test/resources/testdata/image-disease-mapping.json";

    @DataProvider(name = "diseaseTestData")
    public static Object[][] getDiseaseTestData() {
        List<TestData> testDataList = loadTestData();
        Object[][] data = new Object[testDataList.size()][1];

        for (int i = 0; i < testDataList.size(); i++) {
            data[i][0] = testDataList.get(i);
        }

        return data;
    }

    @DataProvider(name = "diseaseTestDataExpanded")
    public static Object[][] getDiseaseTestDataExpanded() {
        List<TestData> testDataList = loadTestData();
        Object[][] data = new Object[testDataList.size()][3];

        for (int i = 0; i < testDataList.size(); i++) {
            TestData td = testDataList.get(i);
            data[i][0] = td.getId();
            data[i][1] = td.getImageName();
            data[i][2] = td.getExpectedDisease();
        }

        return data;
    }

    private static List<TestData> loadTestData() {
        List<TestData> testDataList = new ArrayList<>();
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(TEST_DATA_FILE)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray testCases = jsonObject.getAsJsonArray("testCases");

            for (int i = 0; i < testCases.size(); i++) {
                TestData testData = gson.fromJson(testCases.get(i), TestData.class);
                testDataList.add(testData);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data from: " + TEST_DATA_FILE, e);
        }

        return testDataList;
    }

    public static TestData getTestDataById(int id) {
        List<TestData> allData = loadTestData();
        return allData.stream()
                .filter(td -> td.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
