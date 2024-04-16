package cn.ussshenzhou.config;

import cn.ussshenzhou.log.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * @author USS_Shenzhou
 * <br> Cpoied from my cn.ussshenzhou.t88.config.ConfigHelper
 */
public class ConfigHelper {
    private static final File CONFIG_DIR = Paths.get("config").toFile();
    private static final HashMap<Class<? extends TConfig>, TConfig> CACHE = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static void checkDir(File dir) {
        if (!dir.isDirectory()) {
            dir.mkdir();
        }
    }

    private static File checkFile(TConfig config) {
        checkDir(CONFIG_DIR);
        String configFileName = config.getClass().getSimpleName();
        Path childDir = (CONFIG_DIR).toPath().resolve(config.getChildDirName());
        checkDir(childDir.toFile());
        return childDir.resolve(configFileName + ".json").toFile();
    }

    public static void loadConfig(TConfig newInstance) {
        File configFile = checkFile(newInstance);
        loadConfigInternal(newInstance, configFile);
    }

    @SuppressWarnings("unchecked")
    public static <T extends TConfig> T getConfigRead(Class<T> configClass) {
        return (T) CACHE.get(configClass);
    }

    @SuppressWarnings("unchecked")
    public static <T extends TConfig> void getConfigWrite(Class<T> configClass, Consumer<T> setter) {
        T config = (T) CACHE.get(configClass);
        setter.accept(config);
        saveConfig(config);
    }

    public static <T extends TConfig> void saveConfig(T config) {
        File configFile = checkFile(config);
        saveConfigInternal(config, configFile);
    }

    private static void loadConfigInternal(TConfig newInstance, File configFile) {
        try {
            if (configFile.isFile()) {
                newInstance = GSON.fromJson(FileUtils.readFileToString(configFile, StandardCharsets.UTF_8), newInstance.getClass());
            } else {
                FileUtils.write(configFile, GSON.toJson(newInstance), StandardCharsets.UTF_8);
            }
            CACHE.put(newInstance.getClass(), newInstance);
            saveConfigInternal(newInstance, configFile);
        } catch (IOException ignored) {
            LogUtils.getLogger().error("Failed to load config {}. Things may not work well.", newInstance.getClass());
        }
    }

    private static <T extends TConfig> void saveConfigInternal(T config, File configFile) {
        try {
            FileUtils.write(configFile, GSON.toJson(config), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            LogUtils.getLogger().error("Failed to save config {}. Things may not work well.", config.getClass());
        }
    }
}