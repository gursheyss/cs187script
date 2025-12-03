package com.aysa.automation.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {

    private static final Properties properties = new Properties();
    private static AppConfig instance;

    private AppConfig() {
        loadProperties();
    }

    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    private void loadProperties() {
        String configFile = System.getProperty("config.file", "src/test/resources/config.properties");
        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + configFile, e);
        }
    }

    public String getAppiumServerUrl() {
        return properties.getProperty("appium.server.url", "http://127.0.0.1:4723");
    }

    public String getAppPackage() {
        return properties.getProperty("app.package", "com.visualdx.aysa");
    }

    public String getAppActivity() {
        return properties.getProperty("app.activity");
    }

    public String getPlatformName() {
        return properties.getProperty("platform.name", "Android");
    }

    public String getPlatformVersion() {
        return properties.getProperty("platform.version", "13");
    }

    public String getDeviceName() {
        return properties.getProperty("device.name", "emulator-5554");
    }

    public String getAutomationName() {
        return properties.getProperty("automation.name", "UiAutomator2");
    }

    public int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait", "10"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait", "30"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("page.load.timeout", "60"));
    }

    public String getGalleryImagePath() {
        return properties.getProperty("gallery.image.path", "/sdcard/Pictures/");
    }
}
