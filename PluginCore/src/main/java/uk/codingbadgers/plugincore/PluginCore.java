package uk.codingbadgers.plugincore;

import org.bukkit.plugin.java.JavaPlugin;
import uk.codingbadgers.plugincore.commands.CommandManager;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommandSystem;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.logging.Level;

public class PluginCore extends JavaPlugin {

    private final MessageSystem m_messageSystem;
    private ModuleLoader m_moduleLoader;
    private ModuleCommandSystem m_commandSystem;

    public PluginCore() {
        m_messageSystem = new MessageSystem(this);
    }

    @Override
    public void onLoad() {
        m_moduleLoader = new ModuleLoader(this, "modules");
        m_commandSystem = new ModuleCommandSystem(this);

        m_moduleLoader.load();
    }

    @Override
    public void onEnable() {
        getCommand("PluginCore").setExecutor(new CommandManager(this, m_messageSystem));

        m_moduleLoader.enable();

        // Done
        getLogger().log(Level.INFO, "Enabled " + getDescription().getName() + " version: " + getDescription().getVersion() );
    }

    @Override
    public void onDisable() {
        m_moduleLoader.disable();

        getLogger().log(Level.INFO, "Disabled " + getDescription().getName());
    }

    public ModuleLoader getModuleLoader() {
        return m_moduleLoader;
    }

    public ModuleCommandSystem getCommandSystem() {
        return m_commandSystem;
    }
}
