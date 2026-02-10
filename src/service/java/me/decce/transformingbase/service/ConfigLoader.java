package me.decce.transformingbase.service;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import me.decce.transformingbase.constants.Constants;
import me.decce.transformingbase.core.NetworkBlockerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {
    private static final Logger LOGGER = LogManager.getLogger(Constants.MOD_NAME);
    private static final Path CONFIG_PATH;
    private static final Path CONFIG_FILE;
    private static final Path WHITELIST_FILE;

    static {
        CONFIG_PATH = Paths.get("config").resolve("networkblocker");
        CONFIG_FILE = CONFIG_PATH.resolve("config.toml");
        WHITELIST_FILE = CONFIG_PATH.resolve("whitelist.txt");
        try {
            if (!Files.exists(CONFIG_PATH)) {
                Files.createDirectories(CONFIG_PATH);
            }
            if (!Files.exists(WHITELIST_FILE)) {
                Files.writeString(WHITELIST_FILE, NetworkBlockerConfig.DEFAULT_WHITELIST_FILE_CONTENT, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            }
        } catch (IOException ignored) {}
    }

    private static CommentedFileConfig makeNightConfig() {
        return CommentedFileConfig.builder(CONFIG_FILE, TomlFormat.instance())
                .preserveInsertionOrder()
                .sync()
                .build();
    }

    public static void save(NetworkBlockerConfig config) {
        try (var night = toNightConfig(config)) {
            night.save();
        } catch (Exception e) {
            LOGGER.error("Failed to save configuration!", e);
        }
    }

    public static NetworkBlockerConfig load() {
        var config = loadConfig();
        loadWhitelist(config);
        return config;
    }

    private static void loadWhitelist(NetworkBlockerConfig config) {
        try {
            List<String> whitelist = Files.exists(WHITELIST_FILE) ? Files.readAllLines(WHITELIST_FILE) : new ArrayList<>();
            if (config.useDefaultWhitelist) {
                whitelist.addAll(NetworkBlockerConfig.DEFAULT_WHITELIST.lines().toList());
            }
            config.whitelist.addAll(whitelist.stream()
                    .filter( line -> !line.isBlank())
                    .filter(line -> !line.startsWith("#"))
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static NetworkBlockerConfig loadConfig() {
        if (CONFIG_FILE.toFile().exists()) {
            try {
                return fromNightConfig();
            } catch (Exception e) {
                LOGGER.error("Failed to read configuration!", e);
            }
        }
        return new NetworkBlockerConfig();
    }

    private static CommentedFileConfig toNightConfig(NetworkBlockerConfig config) {
        var night = makeNightConfig();
        try {
            for (Field field : NetworkBlockerConfig.class.getDeclaredFields()) {
                var modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) || Modifier.isFinal(modifiers)) {
                    continue;
                }
                night.set(field.getName(), field.get(config));
                if (field.isAnnotationPresent(NetworkBlockerConfig.Comment.class)) {
                    night.setComment(field.getName(), field.getAnnotation(NetworkBlockerConfig.Comment.class).value());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return night;
    }

    private static NetworkBlockerConfig fromNightConfig() {
        var config = new NetworkBlockerConfig();
        try (var night = makeNightConfig()) {
            night.load();
            for (Field field : NetworkBlockerConfig.class.getDeclaredFields()) {
                var modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) || Modifier.isFinal(modifiers)) {
                    continue;
                }
                if (night.contains(field.getName())) {
                    field.set(config, night.get(field.getName()));
                }
            }
            if ("REDIRECT".equals(config.blockMethod)) config.currentBlockMethod = NetworkBlockerConfig.BlockMethod.REDIRECT;
            else if ("THROW".equals(config.blockMethod)) config.currentBlockMethod = NetworkBlockerConfig.BlockMethod.THROW;
            else {
                LOGGER.error("Found invalid value for blockMethod, changing to default.");
                config.blockMethod = "REDIRECT";
                config.currentBlockMethod = NetworkBlockerConfig.BlockMethod.REDIRECT;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return config;
    }
}
