package uk.codingbadgers.plugincore.modules;

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
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
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
    private List<Module> m_modules = new ArrayList<>();
    private ClassLoader m_loader;

    public ModuleLoader(PluginCore plugin, String path) {
        m_plugin = plugin;
        m_logger = m_plugin.getLogger();
        m_modulesDir = new File(m_plugin.getDataFolder(), path);

        findModules();
        generateClassLoader();
    }

    private void findModules() {
        m_files = m_modulesDir.listFiles(c_jarFilter);

        m_logger.log(Level.INFO, "Found " + m_files.length + " modules");
    }

    private void generateClassLoader() {
        List<URL> urls = new ArrayList<>();

        for (File file : m_files) {
            try {
                urls.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        m_loader = URLClassLoader.newInstance(urls.toArray(new URL[0]), m_plugin.getClass().getClassLoader());
    }

    public boolean load() {
        boolean success = true;

        m_logger.log(Level.INFO, "Loading " + m_files.length+ " modules");

        for (File file : m_files) {
            if (!load(file)) {
                m_logger.log(Level.SEVERE, "Failed to load module '" + file.getPath() + "'");
                success = false;
            }
        }

        return success;
    }

    public boolean enable() {
        boolean success = true;

        m_logger.log(Level.INFO, "Enabling " + m_modules.size() + " modules");

        for (Module module : m_modules) {
            try {
                module.getLogger().log(Level.INFO, "Enabling " + module.getDescription().getName() + " v" + module.getDescription().getVersion());
                module.setEnabled(true);

                Bukkit.getServer().getPluginManager().callEvent(new ModuleEnableEvent(module));
            } catch (Exception e) {
                module.getLogger().log(Level.SEVERE, "Error enabling module '" + module.getDescription().getName() + "'", e);
                success = false;
            }
        }

        return success;
    }

    public boolean disable() {
        boolean success = true;

        m_logger.log(Level.INFO, "Disabling " + m_modules.size() + " modules");

        for (Module module : m_modules) {
            try {
                module.getLogger().log(Level.INFO, "Disabling " + module.getDescription().getName() + " v" + module.getDescription().getVersion());
                module.setEnabled(false);

                Bukkit.getServer().getPluginManager().callEvent(new ModuleDisableEvent(module));
            } catch (Exception e) {
                module.getLogger().log(Level.SEVERE, "Error disabling module '" + module.getDescription().getName() + "'", e);
                success = false;
            }
        }

        return success;
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

    public boolean load(File file) {
        JarFile jar = null;

        try {
            jar = new JarFile(file);
            ModuleDescriptionFile mdf = loadDescription(jar);

            if (mdf == null) {
                return false;
            }

            String mainClass = mdf.getMainClass();
            Class<?> clazz = Class.forName(mainClass, true, m_loader);

            if (clazz == null) {
                return false;
            }

            Class<? extends Module> moduleClass = clazz.asSubclass(Module.class);
            Constructor<? extends Module> constructor = moduleClass.getConstructor();
            Module module = constructor.newInstance();

            module.init(m_plugin, file, jar, mdf, new File(m_modulesDir, mdf.getName()));

            m_modules.add(module);
            Bukkit.getServer().getPluginManager().callEvent(new ModuleLoadEvent(module));
            return true;
        } catch (ClassNotFoundException e) {
            m_logger.log(Level.SEVERE, "Invalid module.yml");
        } catch (Throwable t) {
            m_logger.log(Level.SEVERE, "An unknown error has occurred whilst loading a module.", t);
        } finally {
            try {
                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
