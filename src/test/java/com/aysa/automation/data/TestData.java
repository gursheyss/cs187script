package com.aysa.automation.data;

public class TestData {

    private int id;
    private String imageName;
    private String expectedDisease;
    private String description;

    public TestData() {
    }

    public TestData(int id, String imageName, String expectedDisease, String description) {
        this.id = id;
        this.imageName = imageName;
        this.expectedDisease = expectedDisease;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return String.format("TestCase[id=%d, image=%s, disease=%s]", id, imageName, expectedDisease);
    }
}
