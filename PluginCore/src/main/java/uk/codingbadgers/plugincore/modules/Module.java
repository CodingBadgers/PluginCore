package uk.codingbadgers.plugincore.modules;

import java.io.File;
import java.util.jar.JarFile;

public abstract class Module {

    private boolean m_enabled;
    private boolean m_debug;

    private File m_dataFolder;
    private ModuleDescriptionFile m_mdf;
    private JarFile m_jar;
    private File m_file;

    public Module() {
        m_enabled = false;
    }

    public void init(File file, JarFile jar, ModuleDescriptionFile mdf, File dataFolder) {
        m_file = file;
        m_jar = jar;
        m_mdf = mdf;
        m_dataFolder = dataFolder;
    }

    public void onLoad() {}

    public abstract void onEnable();

    public abstract void onDisable();

    public void setEnabled(boolean enabled) {
        if (enabled == m_enabled) {
            return;
        }

        if (enabled) {
            onEnable();
            m_enabled = true;
        } else {
            onDisable();
            m_enabled = false;
        }
    }

    public boolean isEnabled() {
        return m_enabled;
    }

    public void setDebug(boolean debug) {
        m_debug = debug;
    }

    public boolean isDebug() {
        return m_debug;
    }

    public ModuleDescriptionFile getDescription() {
        return m_mdf;
    }
}
