package com.aysa.automation.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Custom TestNG listener that writes test results to an output file.
 */
public class TestResultReporter implements ITestListener {

    private static final String OUTPUT_FILE = "test-results.txt";
    private List<TestResultEntry> results = new ArrayList<>();
    private long suiteStartTime;
    private int passCount = 0;
    private int failCount = 0;
    private int skipCount = 0;

    @Override
    public void onStart(ITestContext context) {
        suiteStartTime = System.currentTimeMillis();
        results.clear();
        passCount = 0;
        failCount = 0;
        skipCount = 0;
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Test starting
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passCount++;
        results.add(new TestResultEntry(
            getTestName(result),
            "PASS",
            result.getEndMillis() - result.getStartMillis(),
            null
        ));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failCount++;
        String errorMessage = result.getThrowable() != null
            ? result.getThrowable().getMessage()
            : "Unknown error";
        results.add(new TestResultEntry(
            getTestName(result),
            "FAIL",
            result.getEndMillis() - result.getStartMillis(),
            errorMessage
        ));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skipCount++;
        String reason = result.getThrowable() != null
            ? result.getThrowable().getMessage()
            : "Skipped";
        results.add(new TestResultEntry(
            getTestName(result),
            "SKIP",
            0,
            reason
        ));
    }

    @Override
    public void onFinish(ITestContext context) {
        writeResultsToFile();
    }

    private String getTestName(ITestResult result) {
        Object[] params = result.getParameters();
        if (params != null && params.length > 0) {
            return result.getMethod().getMethodName() + " - " + params[0].toString();
        }
        return result.getMethod().getMethodName();
    }

    private void writeResultsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            long totalDuration = System.currentTimeMillis() - suiteStartTime;

            // Header
            writer.println("================================================================================");
            writer.println("                    AYSA DISEASE DETECTION TEST RESULTS");
            writer.println("================================================================================");
            writer.println();
            writer.println("Run Date: " + timestamp);
            writer.println("Total Duration: " + formatDuration(totalDuration));
            writer.println();

            // Summary
            writer.println("--------------------------------------------------------------------------------");
            writer.println("                              SUMMARY");
            writer.println("--------------------------------------------------------------------------------");
            writer.println(String.format("  Total Tests: %d", passCount + failCount + skipCount));
            writer.println(String.format("  Passed:      %d", passCount));
            writer.println(String.format("  Failed:      %d", failCount));
            writer.println(String.format("  Skipped:     %d", skipCount));
            writer.println(String.format("  Pass Rate:   %.1f%%",
                (passCount + failCount) > 0 ? (passCount * 100.0 / (passCount + failCount)) : 0));
            writer.println();

            // Detailed Results
            writer.println("--------------------------------------------------------------------------------");
            writer.println("                           DETAILED RESULTS");
            writer.println("--------------------------------------------------------------------------------");
            writer.println();

            for (TestResultEntry entry : results) {
                String statusIcon = getStatusIcon(entry.status);
                writer.println(String.format("%s [%s] %s", statusIcon, entry.status, entry.testName));
                if (entry.duration > 0) {
                    writer.println(String.format("       Duration: %s", formatDuration(entry.duration)));
                }
                if (entry.errorMessage != null && !entry.errorMessage.isEmpty()) {
                    writer.println(String.format("       Error: %s", entry.errorMessage));
                }
                writer.println();
            }

            // Footer
            writer.println("================================================================================");
            writer.println("                              END OF REPORT");
            writer.println("================================================================================");

            System.out.println("\n>>> Test results saved to: " + OUTPUT_FILE);

        } catch (IOException e) {
            System.err.println("Failed to write test results: " + e.getMessage());
        }
    }

    private String getStatusIcon(String status) {
        switch (status) {
            case "PASS": return "[PASS]";
            case "FAIL": return "[FAIL]";
            case "SKIP": return "[SKIP]";
            default: return "[????]";
        }
    }

    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        }
        return String.format("%d.%ds", seconds, (millis % 1000) / 100);
    }

    private static class TestResultEntry {
        String testName;
        String status;
        long duration;
        String errorMessage;

        TestResultEntry(String testName, String status, long duration, String errorMessage) {
            this.testName = testName;
            this.status = status;
            this.duration = duration;
            this.errorMessage = errorMessage;
        }
    }
}
