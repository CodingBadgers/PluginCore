package uk.codingbadgers.plugincore;

import org.bukkit.plugin.java.JavaPlugin;
import uk.codingbadgers.plugincore.commands.CommandManager;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.logging.Level;

public class PluginCore extends JavaPlugin {

    private final MessageSystem m_messageSystem;

    public PluginCore() {
        m_messageSystem = new MessageSystem(this);
    }

    @Override
    public void onEnable()
    {
        getCommand("PluginCore").setExecutor(new CommandManager(m_messageSystem));

        // Done
        getLogger().log(Level.INFO, "Enabled " + getDescription().getName() + " version: " + getDescription().getVersion() );
    }

    @Override
    public void onDisable()
    {
        getLogger().log(Level.INFO, "Disabled " + getDescription().getName());
    }
}
