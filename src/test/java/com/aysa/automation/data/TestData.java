package com.aysa.automation.data;

/**
 * Test data for Aysa skin condition tests.
 * Contains image info and questionnaire answers for each test case.
 */
public class TestData {

    private int id;
    private String folderName;       // eczema, melanoma, psoriasis, fungal_infection
    private String imageName;        // 1.jpg through 12.jpg
    private String expectedDisease;
    private String description;

    // Questionnaire answers
    private String flakyBumpy;       // "YES" or "NO"
    private String bodyCoverage;     // "Single Lesion", "Limited Area", "Widespread"
    private String bodyLocation;     // e.g., "arm-lower-right"
    private String duration;         // e.g., "Weeks to Months"
    private String itches;           // "Yes" or "No"
    private String fever;            // "Yes" or "No"
    private String profileName;      // Profile to use (default: "test")

    public TestData() {
        this.profileName = "test";
    }

    public TestData(int id, String folderName, String imageName, String expectedDisease) {
        this.id = id;
        this.folderName = folderName;
        this.imageName = imageName;
        this.expectedDisease = expectedDisease;
        this.description = folderName + "/" + imageName;
        this.profileName = "test";
    }

    // Builder-style setters for fluent configuration
    public TestData withQuestionnaireAnswers(
            String flakyBumpy,
            String bodyCoverage,
            String bodyLocation,
            String duration,
            String itches,
            String fever) {
        this.flakyBumpy = flakyBumpy;
        this.bodyCoverage = bodyCoverage;
        this.bodyLocation = bodyLocation;
        this.duration = duration;
        this.itches = itches;
        this.fever = fever;
        return this;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getExpectedDisease() {
        return expectedDisease;
    }

    public void setExpectedDisease(String expectedDisease) {
        this.expectedDisease = expectedDisease;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlakyBumpy() {
        return flakyBumpy;
    }

    public void setFlakyBumpy(String flakyBumpy) {
        this.flakyBumpy = flakyBumpy;
    }

    public String getBodyCoverage() {
        return bodyCoverage;
    }

    public void setBodyCoverage(String bodyCoverage) {
        this.bodyCoverage = bodyCoverage;
    }

    public String getBodyLocation() {
        return bodyLocation;
    }

    public void setBodyLocation(String bodyLocation) {
        this.bodyLocation = bodyLocation;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getItches() {
        return itches;
    }

    public void setItches(String itches) {
        this.itches = itches;
    }

    public String getFever() {
        return fever;
    }

    public void setFever(String fever) {
        this.fever = fever;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    @Override
    public String toString() {
        return String.format("TestCase[id=%d, folder=%s, image=%s, disease=%s]",
                id, folderName, imageName, expectedDisease);
    }
}
