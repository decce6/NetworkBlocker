package me.decce.transformingbase.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal", "unused"})
public class NetworkBlockerConfig {
    public static final String DEFAULT_WHITELIST_FILE_CONTENT = """
            # Add additional domains/mods to the whitelist in this file, one per line
            # Lines starting with # are ignored. (use them as comments!)
            # Lines starting with = are treated as modid's.
            # Other lines are treated as domain names.
            # The following default allows connection by any mod to *.minecraft.net, as well as any connection made by vanilla game.
            
            minecraft.net
            =minecraft
            """;
    public transient Set<String> whitelistedDomains = new HashSet<>();
    public transient Set<String> whitelistedMods = new HashSet<>();
    @Comment("When enabled, connections that are permitted will be logged")
    public boolean logPermitted = false;
    @Comment("When enabled, connections that are blocked will be logged")
    public boolean logBlocked = true;
    @Comment("When enabled, stacktrace will be logged")
    public boolean logStacktrace = false;
    @Comment("When enabled, direct IP connections will be allowed even if they are not in the whitelist")
    public boolean allowIP = true;
    @Comment("Specifies the method used to block network connection. Valid values:\n" +
            "DEFAULT: use the default blocking method (THROW)\n" +
            "REDIRECT: redirect addresses to 0.0.0.0\n" +
            "THROW: throw an exception")
    public String blockingMethod = "THROW";
    public transient BlockMethod currentBlockMethod = BlockMethod.THROW;
    public enum BlockMethod {
        REDIRECT,
        THROW
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Comment {
        String value();
    }
}
