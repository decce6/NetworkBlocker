package me.decce.transformingbase.service;

import me.decce.transformingbase.constants.Constants;
import me.decce.transformingbase.core.LibraryAccessor;
import me.decce.transformingbase.core.NetworkBlocker;
import me.decce.transformingbase.instrumentation.AgentLoader;
import me.decce.transformingbase.transform.TransformationHelper;
import me.decce.transformingbase.transform.TransformerDefinition;
import net.lenni0451.reflect.Agents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.Objects;

import static me.decce.transformingbase.core.util.ReflectionHelper.unreflect;

public class Bootstrapper {
    public static final Logger LOGGER = LogManager.getLogger(Constants.MOD_NAME);
    private static boolean bootstrapped;

    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        var classLoaderHandler = new ClassLoaderHandlerImpl(null, Bootstrapper.class.getClassLoader());
        classLoaderHandler.loadCoreClasses(Bootstrapper.class);
        classLoaderHandler.removeModClassesFromServiceLayer();

        var helper = new TransformationHelper(classLoaderHandler.targetClassLoader, classLoaderHandler.modClassLoader);

        helper.expandModuleReads();

        helper.setup(getInstrumentation(), true, false
        //? if fabric {
                , new TransformerDefinition("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate", me.decce.transformingbase.service.fabric.KnotClassDelegateTransformer.class)
        //?}
        );

        classLoaderHandler.close();

        PostBootstrapper.postBootstrap();
    }

    private static Instrumentation getInstrumentation() {
        try {
            var codeSource = Objects.requireNonNull(Bootstrapper.class.getProtectionDomain().getCodeSource());
            var path = getAgentPath(codeSource);
            if (path != null) {
                return AgentLoader.load(path);
            }
            else {
                LOGGER.debug("Could not find mod jar, using fallback method for instrumentation");
                return Agents.getInstrumentation();
            }
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static String getAgentPath(CodeSource codeSource) {
        var location = codeSource.getLocation();
        try {
            return Paths.get(location.toURI()).toFile().getAbsolutePath();
        } catch (Throwable throwable) {
            return getParsedAgentPath(codeSource);
        }
    }

    private static String getParsedAgentPath(CodeSource codeSource) {
        var location = codeSource.getLocation();
        if ("union".equals(location.getProtocol())) {
            try {
                var path = location.getPath();
                if (path.contains("%")) {
                    String parsed = path.substring(0, path.indexOf('%'));
                    if (Files.exists(Paths.get(parsed))) {
                        return parsed;
                    }
                }
            }
            catch (Throwable ignored) {}
        }
        return null;
    }
}
