package uk.codingbadgers.plugincore;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class PluginCore extends JavaPlugin {

    @Override
    public void onEnable()
    {
        getLogger().log(Level.INFO, "Enabled " + getDescription().getName() + " version: " + getDescription().getVersion() );
    }

    @Override
    public void onDisable()
    {
        getLogger().log(Level.INFO, "Disabled " + getDescription().getName());
    }
}
