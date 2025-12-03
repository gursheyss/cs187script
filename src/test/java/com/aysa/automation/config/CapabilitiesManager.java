package com.aysa.automation.config;

import io.appium.java_client.android.options.UiAutomator2Options;

import java.time.Duration;

public class CapabilitiesManager {

    private final AppConfig config;

    public CapabilitiesManager() {
        this.config = AppConfig.getInstance();
    }

    public UiAutomator2Options getAndroidCapabilities() {
        UiAutomator2Options options = new UiAutomator2Options();

        // Platform capabilities
        options.setPlatformName(config.getPlatformName());
        options.setPlatformVersion(config.getPlatformVersion());
        options.setDeviceName(config.getDeviceName());
        options.setAutomationName(config.getAutomationName());

        // App capabilities
        options.setAppPackage(config.getAppPackage());
        if (config.getAppActivity() != null && !config.getAppActivity().isEmpty()) {
            options.setAppActivity(config.getAppActivity());
        }

        // Additional capabilities for stability
        options.setAutoGrantPermissions(true);
        options.setNoReset(false);
        options.setFullReset(false);

        // Timeout capabilities
        options.setNewCommandTimeout(Duration.ofSeconds(300));

        // UiAutomator2 specific settings
        options.setCapability("appium:uiautomator2ServerInstallTimeout", 60000);
        options.setCapability("appium:uiautomator2ServerLaunchTimeout", 60000);
        options.setCapability("appium:allowTestPackages", true);

        return options;
    }
}
