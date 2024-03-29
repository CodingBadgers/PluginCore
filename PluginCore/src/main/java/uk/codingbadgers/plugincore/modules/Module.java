package uk.codingbadgers.plugincore.modules;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.database.DatabaseManager;
import uk.codingbadgers.plugincore.database.databases.CoreDatabase;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommand;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Module {

    private boolean m_enabled;
    private boolean m_debug;

    protected PluginCore m_plugin;
    private File m_dataFolder;
    private ModuleDescriptionFile m_mdf;
    private JarFile m_jar;
    private File m_file;
    private Logger m_logger;
    private final Set<Listener> m_listeners = new HashSet<>();

    public Module() {
        m_enabled = false;
    }

    public void init(PluginCore plugin, File file, JarFile jar, ModuleDescriptionFile mdf, File dataFolder) {
        m_plugin = plugin;
        m_file = file;
        m_jar = jar;
        m_mdf = mdf;
        m_dataFolder = dataFolder;
        m_dataFolder.mkdirs();

        m_logger = new ModuleLogger(plugin, this);
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

            // Unregister all resources created by this module
            m_plugin.getCommandSystem().unregisterCommands(this);

            for (Listener listener : m_listeners) {
                HandlerList.unregisterAll(listener);
            }
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

    public String getName() { return getDescription().getName(); }

    public String getVersion() { return getDescription().getVersion(); }

    public ModuleDescriptionFile getDescription() {
        return m_mdf;
    }

    public PluginCore getPlugin() {
        return m_plugin;
    }

    public Logger getLogger() {
        return m_logger;
    }

    public File getDataFolder() {
        return m_dataFolder;
    }

    public void sendMessage(CommandSender sender, String msg) {
        String message = String.format(
                "%s[%s]%s %s",
                ChatColor.DARK_PURPLE,
                getDescription().getName(),
                ChatColor.WHITE,
                msg);

        sender.sendMessage(message);
    }

    public void registerCommand(ModuleCommand command) {
        m_logger.log(Level.FINE, "Registered command '" + command.getName() + "'");
        m_plugin.getCommandSystem().registerCommand(this, command);
    }

    protected void registerListener(Listener listener) {
        m_logger.log(Level.FINE, "Registered listener '" + listener.getClass().getName() + "'");
        Bukkit.getServer().getPluginManager().registerEvents(listener, m_plugin);
        m_listeners.add(listener);
    }

    public CoreDatabase openDatabaseConnection(DatabaseManager.DatabaseType type) {
        return m_plugin.getDatabaseManager().createDatabase(getName(), this, type, 20);
    }

    public File getFile() {
        return m_file;
    }
}
