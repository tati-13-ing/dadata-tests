package ru.example.dadata.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public final class ConfigReader {

    private static final String CONFIG_FILE =
            "config.properties";

    private static final Properties PROPERTIES =
            new Properties();

    static {
        loadProperties();
    }

    private ConfigReader() {
    }

    private static void loadProperties() {
        try (
                InputStream inputStream =
                        ConfigReader.class
                                .getClassLoader()
                                .getResourceAsStream(
                                        CONFIG_FILE
                                )
        ) {
            if (inputStream == null) {
                throw new IllegalStateException(
                        "Файл "
                                + CONFIG_FILE
                                + " не найден в src/test/resources"
                );
            }

            PROPERTIES.load(
                    new java.io.InputStreamReader(
                            inputStream,
                            StandardCharsets.UTF_8
                    )
            );
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Не удалось прочитать файл "
                            + CONFIG_FILE,
                    exception
            );
        }
    }

    public static String get(String key) {
        String value =
                PROPERTIES.getProperty(key);

        if (value == null) {
            throw new IllegalStateException(
                    "В файле "
                            + CONFIG_FILE
                            + " отсутствует настройка: "
                            + key
            );
        }

        return value;
    }

    public static int getInt(String key) {
        String value = get(key);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalStateException(
                    "Настройка "
                            + key
                            + " должна содержать целое число, но содержит: "
                            + value,
                    exception
            );
        }
    }
}