package uk.codingbadgers.plugincore.modules;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModuleLoader {

    private static final FileExtensionFilter c_jarFilter = new FileExtensionFilter("jar");

    private final Logger m_logger;
    private PluginCore m_plugin;
    private File m_modulesDir;

    private File[] m_files;
    private Set<File> m_moduleFiles = new HashSet<>();
    private List<Module> m_modules = new ArrayList<>();
    private ModuleClassLoader m_loader;

    public ModuleLoader(PluginCore plugin, String path) {
        m_plugin = plugin;
        m_logger = m_plugin.getLogger();
        m_modulesDir = new File(path);

        findModules();
        generateClassLoader();
    }

    public List<Module> getModules() {
        return ImmutableList.copyOf(m_modules);
    }

    private void findModules() {
        if (!m_modulesDir.exists()) {
            m_modulesDir.mkdirs();
        }

        m_files = m_modulesDir.listFiles(c_jarFilter);

        m_logger.log(Level.INFO, "Found " + m_files.length + " modules");
    }

    private void generateClassLoader() {
        if (m_loader != null) {
            try {
                m_loader.close();
            } catch (IOException e) {
                m_logger.log(Level.SEVERE, "Failed to close old module class loader", e);
            }
        }

        List<URL> urls = new ArrayList<>();

        for (File file : m_files) {
            try {
                urls.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        m_loader = new ModuleClassLoader(urls.toArray(new URL[0]), m_plugin.getClass().getClassLoader());
    }

    public boolean loadModules() {
        boolean success = true;

        m_logger.log(Level.INFO, "Loading " + m_files.length+ " modules");

        for (File file : m_files) {
            if (load(file) == null) {
                m_logger.log(Level.SEVERE, "Failed to load module '" + file.getPath() + "'");
                success = false;
            }
        }

        return success;
    }

    public boolean enableModules() {
        boolean success = true;

        m_logger.log(Level.INFO, "Enabling " + m_modules.size() + " modules");

        for (Module module : m_modules) {
            if (!enableModule(module)) {
                success = false;
            }
        }

        return success;
    }

    public boolean disableModules() {
        boolean success = true;

        m_logger.log(Level.INFO, "Disabling " + m_modules.size() + " modules");

        for (Module module : m_modules) {
            if (!disableModule(module)) {
                success = false;
            }
        }

        return success;
    }

    public boolean enableModule(Module module) {
        try {
            module.getLogger().log(Level.INFO, "Enabling " + module.getDescription().getName() + " v" + module.getDescription().getVersion());
            module.setEnabled(true);

            Bukkit.getServer().getPluginManager().callEvent(new ModuleEnableEvent(module));
        } catch (Exception e) {
            module.getLogger().log(Level.SEVERE, "Error enabling module '" + module.getDescription().getName() + "'", e);
            return false;
        }

        return true;
    }

    public boolean disableModule(Module module) {
        try {
            module.getLogger().log(Level.INFO, "Disabling " + module.getDescription().getName() + " v" + module.getDescription().getVersion());
            module.setEnabled(false);

            Bukkit.getServer().getPluginManager().callEvent(new ModuleDisableEvent(module));
        } catch (Exception e) {
            module.getLogger().log(Level.SEVERE, "Error disabling module '" + module.getDescription().getName() + "'", e);
            return false;
        }

        return true;
    }

    public void unloadModules() {
        m_moduleFiles.clear();
        m_modules.clear();

        findModules();
        generateClassLoader();
    }

    public Module loadModule(String file) {
        return loadModule(new File(m_modulesDir, file));
    }

    public Module loadModule(File file) {
        if (m_moduleFiles.contains(file)) {
            m_plugin.getLogger().log(Level.SEVERE, "Attempting to load already loaded module '" + file.getName() + "'");
            return null;
        }

        try {
            m_loader.addURL(file.toURI().toURL());
            return load(file);
        } catch (Exception e) {
            m_logger.log(Level.SEVERE, "Failed to load module '" + file.getPath() + "'");
        }

        return null;
    }

    public void unloadModule(Module module) {
        m_modules.remove(module);
        m_moduleFiles.remove(module.getFile());
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

    private Module load(File file) {
        JarFile jar = null;

        try {
            jar = new JarFile(file);
            ModuleDescriptionFile mdf = loadDescription(jar);

            if (mdf == null) {
                return null;
            }

            String mainClass = mdf.getMainClass();
            Class<?> clazz = Class.forName(mainClass, true, m_loader);

            if (clazz == null) {
                return null;
            }

            Class<? extends Module> moduleClass = clazz.asSubclass(Module.class);
            Constructor<? extends Module> constructor = moduleClass.getConstructor();
            Module module = constructor.newInstance();

            module.init(m_plugin, file, jar, mdf, new File(m_modulesDir, mdf.getName()));

            m_moduleFiles.add(file);
            m_modules.add(module);
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

    public Module getModule(String name) {
        for (Module m: m_modules) {
            if (m.getDescription().getName().equalsIgnoreCase(name)) {
                return m;
            }
        }

        return null;
    }
}
