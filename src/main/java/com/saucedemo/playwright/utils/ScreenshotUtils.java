package com.saucedemo.playwright.utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ScreenshotType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    public static String takeScreenshot(Page page, String scenarioName) {
        if (page == null) {
            System.err.println("Page instance is null. Cannot take screenshot.");
            return null;
        }
        String screenshotDir = System.getProperty("user.dir") + "/reports/screenshots/";
        Path screenshotDirPath = Paths.get(screenshotDir);

        try {
            Files.createDirectories(screenshotDirPath); // Creates directories if they don't exist
        } catch (Exception e) {
            System.err.println("Failed to create screenshot directory: " + e.getMessage());
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = now.format(formatter);

        // Clean scenario name for filename safety
        String cleanScenarioName = scenarioName.replaceAll("[^a-zA-Z0-9.-]", "_");
        String filename = cleanScenarioName + "_" + timestamp + ".png";

        Path fullPath = screenshotDirPath.resolve(filename);

        try {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(fullPath)
                    .setType(ScreenshotType.PNG)
                    .setFullPage(true));

            String relativePathForReport = "./screenshots/" + filename; // Path relative to the HTML report

            System.out.println("Screenshot saved to: " + fullPath);
            System.out.println("Path for report: " + relativePathForReport);
            return relativePathForReport;
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }
}
