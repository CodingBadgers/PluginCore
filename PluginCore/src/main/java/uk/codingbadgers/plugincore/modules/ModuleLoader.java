package uk.codingbadgers.plugincore.modules;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.modules.events.ModuleDisableEvent;
import uk.codingbadgers.plugincore.modules.events.ModuleEnableEvent;
import uk.codingbadgers.plugincore.modules.events.ModuleLoadEvent;
import uk.codingbadgers.plugincore.utilities.FileExtensionFilter;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModuleLoader {

    private static final FileExtensionFilter c_jarFilter = new FileExtensionFilter("jar");

    private final Logger m_logger;
    private final PluginCore m_plugin;
    private final PluginManager m_pluginManager;
    private final File m_modulesDir;

    private final Map<File, Module> m_loadedModuleFiles = new TreeMap<>();
    private final Map<Module, ModuleClassLoader> m_loaders = new HashMap<>();

    public ModuleLoader(PluginCore plugin, String path) {
        m_plugin = plugin;
        m_logger = m_plugin.getLogger();
        m_pluginManager = plugin.getServer().getPluginManager();
        m_modulesDir = new File(path);
    }

    public List<Module> getLoadedModules() {
        return ImmutableList.copyOf(m_loadedModuleFiles.values());
    }

    public boolean isModuleFileLoaded(File file) {
        return m_loadedModuleFiles.containsKey(file);
    }

    public File[] findAllModuleFiles() {
        if (!m_modulesDir.exists()) {
            if (!m_modulesDir.mkdirs()) {
                m_logger.log(Level.SEVERE,"Failed to create the directory '" + m_modulesDir + "'");
            }
        }

        File[] files = m_modulesDir.listFiles(c_jarFilter);
        if (files == null) {
            m_logger.log(Level.INFO, "File to list files within the directory '" + m_modulesDir + "'");
            return new File[0];
        }

        m_logger.log(Level.INFO, "Found " + files.length + " modules");
        return files;
    }

    public boolean loadAllModules() {
        boolean success = true;

        File[] files = findAllModuleFiles();
        m_logger.log(Level.INFO, "Loading " + files.length+ " modules");

        for (File file : files) {
            if (loadModule(file) == null) {
                m_logger.log(Level.SEVERE, "Failed to load module '" + file.getPath() + "'");
                success = false;
            }
        }

        return success;
    }

    public void unloadAllModules() {
        for (Module module : getLoadedModules()) {
            unloadModule(module);
        }
    }

    public boolean enableAllModules() {
        boolean success = true;

        List<Module> loadedModules = getLoadedModules();
        m_logger.log(Level.INFO, "Enabling " + loadedModules.size() + " modules");

        for (Module module : loadedModules) {
            if (!enableModule(module)) {
                success = false;
            }
        }

        return success;
    }

    public boolean disableAllModules() {
        boolean success = true;

        List<Module> loadedModules = getLoadedModules();
        m_logger.log(Level.INFO, "Disabling " + loadedModules.size() + " modules");

        for (Module module : loadedModules) {
            if (!disableModule(module)) {
                success = false;
            }
        }

        return success;
    }

    public boolean enableModule(Module module) {
        try {
            module.getLogger().log(Level.INFO, "Enabling " + module.getName() + " v" + module.getVersion());
            module.setEnabled(true);
            m_pluginManager.callEvent(new ModuleEnableEvent(module));
        } catch (Exception e) {
            module.getLogger().log(Level.SEVERE, "Error enabling module '" + module.getName() + "'", e);
            return false;
        }

        return true;
    }

    public boolean disableModule(Module module) {
        if (!module.isEnabled()) {
            return true;
        }

        try {
            module.getLogger().log(Level.INFO, "Disabling " + module.getName() + " v" + module.getVersion());
            module.setEnabled(false);
            m_pluginManager.callEvent(new ModuleDisableEvent(module));
        } catch (Exception e) {
            module.getLogger().log(Level.SEVERE, "Error disabling module '" + module.getName() + "'", e);
            return false;
        }

        return true;
    }

    public Module loadModule(File file) {
        if (m_loadedModuleFiles.containsKey(file)) {
            m_plugin.getLogger().log(Level.SEVERE, "Attempting to load already loaded module '" + file.getName() + "'");
            return null;
        }

        try {
            return load(file);
        } catch (Exception e) {
            m_logger.log(Level.SEVERE, "Failed to load module '" + file.getPath() + "'");
        }

        return null;
    }

    public void unloadModule(Module module) {
        String moduleName = module.getName();
        m_loadedModuleFiles.remove(module.getFile());
        m_loaders.remove(module);
        m_logger.log(Level.INFO, "Unloaded the module '" + moduleName + "'");
    }

    private Module load(File file) {
        try {
            JarFile jar = new JarFile(file);
            ModuleDescriptionFile mdf = loadDescription(jar);

            if (mdf == null) {
                return null;
            }

            ModuleClassLoader loader = new ModuleClassLoader(this, file.toURI().toURL(), getClass().getClassLoader());

            String mainClass = mdf.getMainClass();
            Class<?> clazz = Class.forName(mainClass, true, loader);

            if (clazz == null) {
                return null;
            }

            Class<? extends Module> moduleClass = clazz.asSubclass(Module.class);
            Constructor<? extends Module> constructor = moduleClass.getConstructor();
            Module module = constructor.newInstance();

            module.init(m_plugin, file, jar, mdf, new File(m_modulesDir, mdf.getName()));

            m_loadedModuleFiles.put(file, module);
            m_loaders.put(module, loader);
            module.onLoad();

            Bukkit.getServer().getPluginManager().callEvent(new ModuleLoadEvent(module));
            return module;
        } catch (ClassNotFoundException e) {
            m_logger.log(Level.SEVERE, "Invalid module.yml");
        } catch (Exception e) {
            m_logger.log(Level.SEVERE, "An unknown error has occurred whilst loading a module.", e);
        }

        return null;
    }

    private ModuleDescriptionFile loadDescription(JarFile file) throws IOException {
        JarEntry moduleDesc = file.getJarEntry("module.yml");

        if (moduleDesc == null) {
            return null;
        }

        try (Reader reader = new InputStreamReader(file.getInputStream(moduleDesc))) {
            return new ModuleDescriptionFile(reader);
        }
    }

    public Module getModule(File file) {
        if (!isModuleFileLoaded(file)) {
            return null;
        }

        return m_loadedModuleFiles.get(file);
    }

    public Module getModule(String name) {
        for (Module m : getLoadedModules()) {
            if (m.getDescription().getName().equalsIgnoreCase(name)) {
                return m;
            }
        }

        return null;
    }

    public Class<?> findClass(String name) throws ClassNotFoundException{
        Class<?> clazz;
        for (ModuleClassLoader loader : m_loaders.values()) {
            try {
                clazz = loader.findClass(name);
                return clazz;
            } catch (ClassNotFoundException e) {
                // ignored
            }
        }

        throw new ClassNotFoundException(name);
    }
}
