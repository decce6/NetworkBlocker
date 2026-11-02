package me.decce.transformingbase.service;

import me.decce.transformingbase.core.LibraryAccessor;
import me.decce.transformingbase.core.NetworkBlocker;
import org.apache.logging.log4j.Logger;

import static me.decce.transformingbase.core.util.ReflectionHelper.unreflect;

public class PostBootstrapper {
    public static void postBootstrap() {
        initMethodHandles();
        initAccessor();
        initConfig();
    }

    private static void initConfig() {
        NetworkBlocker.config = ConfigLoader.load();
        ConfigLoader.save(NetworkBlocker.config);
    }

    private static void initMethodHandles() {
        LibraryAccessor.logger = Bootstrapper.LOGGER;
        LibraryAccessor.logInfoString = unreflect(() -> Logger.class.getMethod("info", String.class));
        LibraryAccessor.logInfoObject = unreflect(() -> Logger.class.getMethod("info", Object.class));
        LibraryAccessor.logInfoStringObject = unreflect(() -> Logger.class.getMethod("info", String.class, Object.class));
        LibraryAccessor.logWarnString = unreflect(() -> Logger.class.getMethod("warn", String.class));
        LibraryAccessor.logWarnObject = unreflect(() -> Logger.class.getMethod("warn", Object.class));
        LibraryAccessor.logWarnStringObject = unreflect(() -> Logger.class.getMethod("warn", String.class, Object.class));
        LibraryAccessor.logErrorString = unreflect(() -> Logger.class.getMethod("error", String.class));
        LibraryAccessor.logErrorObject = unreflect(() -> Logger.class.getMethod("error", Object.class));
        LibraryAccessor.logErrorStringObject = unreflect(() -> Logger.class.getMethod("error", String.class, Object.class));
        LibraryAccessor.logFatalString = unreflect(() -> Logger.class.getMethod("fatal", String.class));
        LibraryAccessor.logFatalObject = unreflect(() -> Logger.class.getMethod("fatal", Object.class));
        LibraryAccessor.logFatalStringObject = unreflect(() -> Logger.class.getMethod("fatal", String.class, Object.class));
        LibraryAccessor.logDebugString = unreflect(() -> Logger.class.getMethod("debug", String.class));
        LibraryAccessor.logDebugObject = unreflect(() -> Logger.class.getMethod("debug", Object.class));
        LibraryAccessor.logDebugStringObject = unreflect(() -> Logger.class.getMethod("debug", String.class, Object.class));
    }

    private static void initAccessor() {
        NetworkBlocker.accessor = new NetworkBlockerAccessorImpl();
    }
}
