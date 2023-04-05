package com.taggernation.simplepvp.utils;

import java.io.*;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {

    private File file;
    private final Logger logger;
    private FileConfiguration config;
    private final Plugin plugin;
    private final boolean isInFolder;
    private String configVersion;
    private boolean consoleLogger = true;

    /**
     * Initializes the Config.
     *
     * @param plugin   Instance of the plugin you want to initialize the config for
     * @param fileName String
     * @param force    boolean enable/disable force file update
     * @param copy     boolean either copy the file from the plugin or not
     */
    public ConfigManager(Plugin plugin, String fileName, boolean force, boolean copy) throws IOException {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        process(fileName,force,copy);
        isInFolder = false;
    }
    private void process(String fileName, boolean force, boolean copy) throws IOException {
        this.file = new File(plugin.getDataFolder(), fileName);
        if (copy) {
            try {
                copy(force);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        if (!file.exists()) {
            if (file.createNewFile()) {
                if (consoleLogger) logger.info("Created new file: " + file.getName());
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Initializes the Config. in given path
     *
     * @param plugin   Instance of the plugin you want to initialize the config for
     * @param fileName String
     * @param path     String path you want to initialize the config in
     * @param force    boolean enable/disable force file update
     * @param copy     boolean either copy the file from the plugin or not
     */
    public ConfigManager(Plugin plugin, String fileName, String path, boolean force, boolean copy) throws IOException {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        String filePath = plugin.getDataFolder() + File.separator + path;
        process(fileName, filePath, force, copy);
        isInFolder = true;
    }
    private void process(String fileName, String path, boolean force, boolean copy) throws IOException {
        this.file = new File(path, fileName);
        if (!file.exists()) {
            File file = new File(path);
            if (file.mkdirs()) {
                if (consoleLogger) logger.info("Created new directory: " + file.getName());
            }
            if (copy) try {
                copy(false, path + File.separator + fileName);
            } catch (IllegalArgumentException e) {
                if (consoleLogger) logger.warning("Failed to copy file: " + fileName + " to path: " + path);
                e.printStackTrace();
            }
            else {
                if (file.createNewFile()) if (consoleLogger) logger.info("Creating file: " + file.getName());
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Set logger of config load events. by default it's enabled
     * @param consoleLogger boolean
     * @return Manager
     */
    public ConfigManager setConsoleLogger(boolean consoleLogger) {
        this.consoleLogger = consoleLogger;
        return this;
    }
    /**
     * Set the version of the config. useful for updating the config.
     * @param configVersion Version of the config
     * @return Manager
     */
    public ConfigManager setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
        return this;
    }
    /**
     * Get Initialized file
     * @return File object
     */
    public File getFile() {
        return file;
    }

    /**
     * Get Initialized config.
     * @return FileConfiguration
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Save the config.
     */
    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reload the config.
     * @throws IOException IOException
     */
    public void reload() throws IOException {
        if (isInFolder) {
            process(file.getName(), file.getParentFile().getName(), false, false);
            return;
        }
        process(file.getName(), false, false);
    }

    /**
     * Copy the config.
     * @param force boolean enable/disable force copy
     */
    public void copy( boolean force) {
        if (!file.exists()) {
            this.saveResource(file.getName(), force);
        }
    }
    /**
     * Copy the config to the given path.
     * @param force boolean enable/disable force copy
     * @param path String path to save the resource
     */
    public void copy( boolean force, String path) {
        this.saveResource(path, force);
    }

    /**
     * Update the Config with the newer version of the file
     * @param versionPath String
     */
/*    public void updateConfig( @NotNull String versionPath) {
        String version = null;
        try {
            version = this.getString(versionPath);
        }catch (NullPointerException e) {
            logger.info("No version found in"+ file.getName() + "... Creating new version of the config");
        }
        if (version == null || !version.equals(this.configVersion)) {
            File newFile = new File(file.getParentFile() + File.separator + "backup", "old_" + version + "_" + getFile().getName());
            plugin.getLogger().info("Updating " + getFile().getName() + " to version " + this.configVersion);
            File oldFile = getFile();
            if (oldFile.renameTo(newFile)) {
                this.file = new File(plugin.getDataFolder(), getFile().getName());
                this.saveResource(getFile().getName(), true);
                this.config = YamlConfiguration.loadConfiguration(this.file);
            }
        }
    }*/
    public void updateConfig(String versionPath) {
        String version = this.getString(versionPath);
        if (version == null) {
            logger.info("No version found in " + file.getName() + ". Creating new version of the config");
        } else if (!version.equals(this.configVersion)) {
            Path backupPath = file.getParentFile().toPath().resolve("backup");
            Path newFilePath = backupPath.resolve(String.format("old_%s_%s", version, file.getName()));

            backupConfigFile(newFilePath);
            updateConfigFile();
        }
    }

    private void backupConfigFile(Path backupFilePath) {
        try {
            Files.createDirectories(backupFilePath.getParent());
            Files.move(file.toPath(), backupFilePath);
            plugin.getLogger().info("Backed up " + file.getName() + " to " + backupFilePath.toString());
        } catch (IOException e) {
            logger.warning("Failed to create backup file " + backupFilePath.toString());
        }
    }

    private void updateConfigFile() {
        this.saveResource(getFile().getName(), true);
        this.config = YamlConfiguration.loadConfiguration(this.file);
        plugin.getLogger().info("Updated " + getFile().getName() + " to version " + this.configVersion);
    }


    /**
     * update the config's existing path with given value.
     * @param path String
     * @param value Object
     * @throws IllegalArgumentException IOException
     */
    public void update(String path, Object value) {
        if (config.contains(path)) {
            config.set(path, value);
            save();
        }else {
            throw new IllegalArgumentException(path + " does not exist" + " in " + file.getName());
        }
    }

    /**
     * Set the given path with given value. And save the config.
     * @param path String
     * @param value Object
     */
    public ConfigManager set(String path, Object value) {
        config.set(path, value);
        return this;
    }

    /**
     * Get the given path.
     * @param path String
     * @return Object
     */
    public @Nullable Object get(String path) {
        if (config.contains(path)) {
            return config.get(path);
        }
        return null;
    }

    /**
     * Get the given path as a list.
     * @param path String
     * @return List
     */
    public List<String> getStringList(String path) {
        if (config.contains(path)) {
            return config.getStringList(path);
        }
        return new ArrayList<>();
    }

    /**
     * Add value to list of strings
     * @param path Path to add string with node
     * @param value String to add to list
     */
    public void addToStringList(String path, String value) {
        if (config.contains(path) && config.isList(path)) {
            List<String> list = config.getStringList(path);
            list.add(value);
            config.set(path, list);
            save();
        }else {
            throw new IllegalArgumentException(path + " does not exist" + " in " + file.getName() + " or is not a list");
        }
    }
    /**
     * Get the given path as a boolean.
     * @param path String
     * @return boolean
     */
    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    /**
     * Get the given path as an int.
     * @param path String
     * @return int
     */
    public int getInt(String path) {
        return config.getInt(path);
    }

    /**
     * Get the given path as a double.
     * @param path String
     * @return double
     */
    public double getDouble(String path) {
        return config.getDouble(path);
    }

    /**
     * Get the given path as a long.
     * @param path String
     * @return long
     */
    public String getString(String path) {
        return config.getString(path);
    }

    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + file);
        }

        File outFile = new File(plugin.getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(plugin.getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            if (outDir.mkdirs()) {
                logger.log(Level.INFO, "Created directory " + outDir);
            } else {
                logger.log(Level.WARNING, "Could not create directory " + outDir);
            }
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = Files.newOutputStream(outFile.toPath());
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                logger.log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

    public InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = this.getClass().getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }
}
