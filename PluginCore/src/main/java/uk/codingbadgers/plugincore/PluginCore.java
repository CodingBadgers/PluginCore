package uk.codingbadgers.plugincore;

import org.bukkit.plugin.java.JavaPlugin;
import uk.codingbadgers.plugincore.commands.CommandManager;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.logging.Level;

public class PluginCore extends JavaPlugin {

    private final MessageSystem m_messageSystem;
    private ModuleLoader m_moduleLoader;

    public PluginCore() {
        m_messageSystem = new MessageSystem(this);
    }

    public void onLoad() {
        m_moduleLoader = new ModuleLoader(this, "modules");

        m_moduleLoader.load();
    }

    @Override
    public void onEnable()
    {
        getCommand("PluginCore").setExecutor(new CommandManager(m_messageSystem));

        m_moduleLoader.enable();

        // Done
        getLogger().log(Level.INFO, "Enabled " + getDescription().getName() + " version: " + getDescription().getVersion() );
    }

    @Override
    public void onDisable()
    {
        m_moduleLoader.disable();

        getLogger().log(Level.INFO, "Disabled " + getDescription().getName());
    }
}
