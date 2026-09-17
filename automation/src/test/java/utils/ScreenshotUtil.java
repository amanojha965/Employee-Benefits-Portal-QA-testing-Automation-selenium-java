package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Captures a screenshot on test failure and saves it under /screenshots
 * with a timestamped, test-name-based file name.
 */
public class ScreenshotUtil {

    private static final String SCREENSHOT_DIR = "screenshots";

    public static String capture(WebDriver driver, String testName) {
        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            File destination = new File(SCREENSHOT_DIR, fileName);

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), destination.toPath());

            return destination.getAbsolutePath();

        } catch (IOException e) {
            System.out.println("Could not save screenshot for " + testName + ": " + e.getMessage());
            return null;
        }
    }
}
