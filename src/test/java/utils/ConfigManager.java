package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath.");
            }

            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getConfig(String key) {
        return properties.getProperty(key);
    }

    public static Properties getProperties() {
        return properties;
    }
}
