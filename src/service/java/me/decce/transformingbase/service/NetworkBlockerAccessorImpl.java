package me.decce.transformingbase.service;

import me.decce.transformingbase.core.NetworkBlockerAccessor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class NetworkBlockerAccessorImpl implements NetworkBlockerAccessor {
    private static final Set<String> SKIPPED_CLASS_PREFIXES = Set.of("sun.", "java.", "jdk.");
    private static final Map<String, String> prefixToModId = Map.of("com.mojang.", "minecraft");
    //? if fabric {
    private static final Map<Path, String> pathToModId = new HashMap<>();

    private static List<Path> determineLocation(String modid) {
        var container = net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer(modid).orElse(null);
        if (container == null) {
            return List.of();
        }
        var origin = container.getOrigin();
        if (origin.getKind() == net.fabricmc.loader.api.metadata.ModOrigin.Kind.PATH) {
            return origin.getPaths();
        }
        else if (origin.getKind() == net.fabricmc.loader.api.metadata.ModOrigin.Kind.NESTED) {
            return determineLocation(origin.getParentModId());
        }
        return List.of();
    }
    //?}

    public NetworkBlockerAccessorImpl() {
        //? if fabric {
        for (var mod : net.fabricmc.loader.api.FabricLoader.getInstance().getAllMods()) {
            var modid = mod.getMetadata().getId();
            var paths = determineLocation(modid);
            for (var path : paths) {
                pathToModId.put(path, modid);
            }
        }
        //?}
    }

    // CallerSensitive
    @Override
    public Optional<String> getCallerMod() {
        var walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        //? if fabric {
        return walker.walk(s ->
                        s.skip(3) // Skip this class and the caller classes (us)
                                .dropWhile(f -> SKIPPED_CLASS_PREFIXES.stream().anyMatch(prefix -> f.getClassName().startsWith(prefix)))
                                .map(f -> getModId(f.getDeclaringClass()))
                                .filter(Objects::nonNull)
                                .findFirst());

        //?} else {
        /*return walker.walk(s ->
                        s.skip(3) // Skip this class and the caller classes (us)
                                .dropWhile(f -> SKIPPED_CLASS_PREFIXES.stream().anyMatch(prefix -> f.getClassName().startsWith(prefix)))
                                .map(f -> getModId(f.getDeclaringClass()))
                                .filter(Objects::nonNull)
                                .findFirst());
        *///?}
    }

    private String getModId(Class<?> clazz) {
        String className = clazz.getName();
        String modid = null;
        for (var entry : prefixToModId.entrySet()) {
            if (className.startsWith(entry.getKey())) {
                modid = entry.getValue();
                break;
            }
        }
        if (modid == null) {
            //? fabric {
            var cs = clazz.getProtectionDomain().getCodeSource();
            try {
                modid = pathToModId.get(Paths.get(cs.getLocation().toURI()));
            } catch (Throwable ignored) {}
            //? } else {
            /*modid = clazz.getModule().getName();
            *///? }
        }
        return modid;
    }
}
