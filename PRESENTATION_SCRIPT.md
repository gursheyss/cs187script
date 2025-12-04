# Aysa Disease Detection Test Automation - Presentation Script

## Opening (30 seconds)

"So what we built here is an automated testing framework for the Aysa app - it's a skin disease detection app that uses AI to analyze photos of skin conditions. Our goal was to test how accurate the app is at detecting four different diseases: eczema, melanoma, psoriasis, and fungal infections."

---

## Project Overview (1 minute)

"Let me walk you through the project structure real quick."

**Open the project folder and show:**

```
cs187script/
├── src/test/java/com/aysa/automation/
│   ├── base/           → Base test setup
│   ├── config/         → App configuration
│   ├── data/           → Test data providers
│   ├── listeners/      → Result reporting
│   ├── pages/          → Page objects
│   └── tests/          → Actual test cases
├── images/             → 48 test images (12 per disease)
└── test-results.txt    → Output file with results
```

"We're using Java with TestNG as our test framework, and Appium for mobile automation. The whole thing follows the Page Object Model pattern - which basically means we separate our UI interactions from our test logic."

---

## File 1: BaseTest.java (2 minutes)

**Open:** `src/test/java/com/aysa/automation/base/BaseTest.java`

"This is our BaseTest class - think of it as the foundation everything else builds on."

**Point to key sections:**

```java
@BeforeMethod(alwaysRun = true)
public void setUp() {
    logger.info("Setting up Appium driver for test");
    initializeDriver();
    configureTimeouts();
}
```

"Before every single test runs, this setUp method fires. It initializes our Appium driver - that's what lets us actually control the Android emulator."

**Scroll to initializeDriver:**

```java
private void initializeDriver() {
    int maxRetries = 3;
    // ... retry logic
}
```

"We added retry logic here because sometimes the emulator connection drops. Instead of failing immediately, we try up to 3 times with a 2-second wait between attempts. Makes the tests way more stable."

**Scroll to tearDown:**

```java
@AfterMethod(alwaysRun = true)
public void tearDown() {
    if (driver != null) {
        driver.quit();
    }
}
```

"And after each test, we clean up by quitting the driver. This is important because we want each test to start fresh."

---

## File 2: DiseaseDetectionTest.java (3 minutes)

**Open:** `src/test/java/com/aysa/automation/tests/DiseaseDetectionTest.java`

"This is where the actual test logic lives."

**Point to the main test method:**

```java
@Test(dataProvider = "eczemaTestData", dataProviderClass = TestDataProvider.class)
public void testEczemaDetection(TestData testData) {
    testDiseaseDetection(testData);
}
```

"See how we have separate test methods for each disease type? They all call the same core method, but with different data. That's the data provider pattern - we'll look at that in a sec."

**Scroll to testDiseaseDetection:**

"Here's the main flow - let me walk through it step by step:"

```java
// Step 1: Handle first-launch onboarding
clickOnboardingButtonIfPresent();

// Step 2: Start a new case
clickStartNewCase();

// Step 3: Open the image picker
clickSelectImageButton();

// Step 4: Select our test image
selectFolder(testData.getFolderName());
selectImage(testData.getImageName());
```

"So first we handle the app's onboarding screen if it shows up - that only happens on first launch. Then we tap 'Start new case', open the image picker, navigate to the right folder, and select our test image."

**Continue scrolling:**

```java
// Step 5: Complete the questionnaire
QuestionnairePage questionnairePage = new QuestionnairePage(driver);
ResultsPage resultsPage = questionnairePage.completeQuestionnaire(...);

// Step 6: Check the results
boolean foundExpectedDisease = resultsPage.containsDisease(testData.getExpectedDisease());
```

"After selecting the image, the app asks a bunch of questions - is it flaky, where on the body, how long have you had it, etc. We automated all of that. Then we check if the disease we expected actually shows up in the results."

**Point to assertion:**

```java
Assert.assertTrue(foundExpectedDisease,
    "Expected disease '" + testData.getExpectedDisease() + "' not found in results");
```

"This is what determines pass or fail. If the expected disease isn't in the results, the test fails."

---

## File 3: QuestionnairePage.java (2 minutes)

**Open:** `src/test/java/com/aysa/automation/pages/QuestionnairePage.java`

"This is one of our Page Object classes. It encapsulates all the interactions with the questionnaire screen."

**Point to completeQuestionnaire:**

```java
public ResultsPage completeQuestionnaire(String flakyBumpy, String profileName,
        String bodyCoverage, String bodyLocation, String duration,
        String itches, String fever) {

    clickUseThisPhoto();
    answerFlakyBumpyQuestion(flakyBumpy);
    selectProfile(profileName);
    selectBodyCoverage(bodyCoverage);
    selectBodyLocation(bodyLocation);
    // ...
}
```

"Each step of the questionnaire has its own method. This makes the code super readable - you can literally read it like English: click use this photo, answer the flaky bumpy question, select profile, and so on."

**Scroll to selectBodyLocation:**

```java
public QuestionnairePage selectBodyLocation(String location) {
    // First click zooms in
    bodyPart.click();
    sleep(2000);

    // Second click selects
    bodyPart.click();
    sleep(2500);
}
```

"This part was tricky - the app has this body diagram where you first tap to zoom in on a region, then tap again to actually select it. We had to add delays between clicks because the animation needs time to complete."

---

## File 4: ResultsPage.java (1.5 minutes)

**Open:** `src/test/java/com/aysa/automation/pages/ResultsPage.java`

"After the questionnaire, we land on the results page. This class handles reading what diseases the app detected."

**Point to getAllDetectedDiseases:**

```java
public List<String> getAllDetectedDiseases() {
    List<WebElement> titleElements = driver.findElements(
        AppiumBy.id("com.visualdx.aysa:id/title_text")
    );
    // Extract text from each element
}
```

"We find all the disease title elements on screen and grab their text. The app usually shows 2-3 possible conditions ranked by likelihood."

**Point to containsDisease:**

```java
public boolean containsDisease(String expectedDisease) {
    for (String disease : diseases) {
        if (disease.toLowerCase().contains(expectedDisease.toLowerCase())) {
            return true;
        }
    }
    return false;
}
```

"Then we do a case-insensitive check to see if our expected disease is anywhere in that list. We use 'contains' instead of exact match because the app might say 'Atopic dermatitis' when we're looking for 'eczema' - same condition, different name."

---

## File 5: TestDataProvider.java (1.5 minutes)

**Open:** `src/test/java/com/aysa/automation/data/TestDataProvider.java`

"This is where we define all our test data."

**Point to data provider:**

```java
@DataProvider(name = "eczemaTestData")
public static Object[][] getEczemaTestData() {
    return getTestDataForFolder("eczema", ECZEMA_CONFIG, "Atopic dermatitis");
}
```

"TestNG data providers let us run the same test multiple times with different inputs. For eczema, we have 12 images, so this generates 12 test cases automatically."

**Point to config:**

```java
private static final TestConfig ECZEMA_CONFIG = new TestConfig(
    "YES",              // flakyBumpy
    "test",             // profile name
    "Limited Area",     // body coverage
    "arm-lower-right",  // body location
    "Weeks to Months",  // duration
    "Yes",              // itches
    "No"                // fever
);
```

"Each disease type has its own config with appropriate questionnaire answers. Eczema is typically flaky, limited to one area, itchy - so that's what we tell the app."

---

## File 6: TestResultReporter.java (1 minute)

**Open:** `src/test/java/com/aysa/automation/listeners/TestResultReporter.java`

"Finally, this listener captures all the results and writes them to a file."

**Point to onTestSuccess/onTestFailure:**

```java
@Override
public void onTestSuccess(ITestResult result) {
    passCount++;
    results.add(new TestResultEntry(getTestName(result), "PASS", ...));
}

@Override
public void onTestFailure(ITestResult result) {
    failCount++;
    results.add(new TestResultEntry(getTestName(result), "FAIL", ...));
}
```

"Every time a test passes or fails, we log it. At the end of the suite, we write everything to test-results.txt with a nice summary."

---

## Show Results (1 minute)

**Open:** `test-results.txt`

"Here's what the output looks like after a full run."

**Scroll through:**

"We get a summary at the top - total tests, pass rate, how long it took. Then detailed results for each test case showing exactly which images passed and which failed, and why."

"In this run we got about 65% accuracy. Some failures are because the app rejected the image - too dark, too blurry. Others are because the AI just didn't detect the right condition."

---

## Closing (30 seconds)

"So that's the framework. 48 test images across 4 disease types, fully automated end-to-end testing. The whole thing runs in about 40 minutes and gives us a clear picture of how accurate the app's AI detection is."

"Any questions?"

---

## Quick Reference - Files to Have Open

1. `BaseTest.java` - Driver setup and teardown
2. `DiseaseDetectionTest.java` - Main test logic
3. `QuestionnairePage.java` - Questionnaire automation
4. `ResultsPage.java` - Reading detection results
5. `TestDataProvider.java` - Test data configuration
6. `TestResultReporter.java` - Results output
7. `test-results.txt` - Sample output
