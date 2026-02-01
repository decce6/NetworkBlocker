package me.decce.transformingbase.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal", "unused"})
public class NetworkBlockerConfig {
    public static final String DEFAULT_WHITELIST = """
            minecraft.net
            minecraftservices.com
            minecraft-services.net
            mojang.com
            """;
    public static final String DEFAULT_WHITELIST_FILE_CONTENT = """
            # Add additional domains to whitelist in this file, one per line
            # Lines starting with # will be ignored.
            #
            # Example:
            minecraft.net
            """;
    public transient Set<String> whitelist = new HashSet<>();
    @Comment("Specifies whether to enable the mod")
    public boolean enabled = true;
    @Comment("When enabled, connections that are permitted will be logged")
    public boolean logPermitted = false;
    @Comment("When enabled, connections that are blocked will be logged")
    public boolean logBlocked = true;
    @Comment("When enabled, stacktrace will be logged")
    public boolean logStacktrace = false;
    @Comment("When enabled, direct IP connections will be allowed even if they are not in the whitelist")
    public boolean allowIP = true;
    @Comment("Set to true to add some useful domains to the whitelist. The current defaults are:\n\n" + DEFAULT_WHITELIST + "\nYou can add additional domains to the whitelist using the file in the config folder.")
    public boolean useDefaultWhitelist = true;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Comment {
        String value();
    }
}
