package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;

public class UtilityTest {

    @Test
    public void configReaderLoadsConfiguredValues() {
        Assert.assertEquals(ConfigReader.getBaseUrl(), "http://localhost:5500");
        Assert.assertEquals(ConfigReader.getBrowser(), "chrome");
        Assert.assertEquals(ConfigReader.getExplicitWaitSeconds(), 10);
        Assert.assertFalse(ConfigReader.isHeadless());
    }

    @Test
    public void quitDriverAcceptsNull() {
        DriverFactory.quitDriver(null);
    }

    @Test
    public void screenshotCaptureCopiesScreenshotToScreenshotsDirectory() throws IOException {
        Path source = Files.createTempFile("employee-benefits-screenshot", ".png");
        Files.write(source, new byte[] { 1, 2, 3 });

        WebDriver driver = (WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(),
                new Class<?>[] { WebDriver.class, TakesScreenshot.class },
                (proxy, method, args) -> {
                    if (method.getName().equals("getScreenshotAs")) {
                        return source.toFile();
                    }
                    if (method.getReturnType() == boolean.class) {
                        return false;
                    }
                    if (method.getReturnType() == byte[].class) {
                        return new byte[0];
                    }
                    return null;
                });

        String screenshotPath = ScreenshotUtil.capture(driver, "utilityTest");
        File screenshot = new File(screenshotPath);

        Assert.assertTrue(screenshot.exists());
        Assert.assertEquals(Files.readAllBytes(screenshot.toPath()), new byte[] { 1, 2, 3 });

        Files.deleteIfExists(screenshot.toPath());
        Files.deleteIfExists(source);
    }
}