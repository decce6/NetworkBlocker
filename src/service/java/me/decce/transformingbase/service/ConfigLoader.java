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
            for (var line : whitelist) {
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("=")) {
                    config.whitelistedMods.add(line.substring(1));
                }
                else if (!line.isBlank()) {
                    config.whitelistedDomains.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static NetworkBlockerConfig loadConfig() {
        NetworkBlockerConfig config = null;
        if (CONFIG_FILE.toFile().exists()) {
            try {
                config = fromNightConfig();
            } catch (Exception e) {
                LOGGER.error("Failed to read configuration!", e);
            }
        }
        if (config == null) {
            config = new NetworkBlockerConfig();
        }
        config.validate();
        return config;
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return config;
    }
}
