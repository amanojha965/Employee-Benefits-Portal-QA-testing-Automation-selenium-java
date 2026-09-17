package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads values from src/test/resources/config.properties so the rest
 * of the framework never hard-codes the base URL, browser, or timeouts.
 */
public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found on the classpath.");
            }
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("baseUrl", "http://localhost:5500");
    }

    public static String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }

    public static int getExplicitWaitSeconds() {
        return Integer.parseInt(properties.getProperty("explicitWaitSeconds", "10"));
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }
}
