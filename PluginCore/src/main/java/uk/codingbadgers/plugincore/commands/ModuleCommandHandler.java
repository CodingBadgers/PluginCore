package uk.codingbadgers.plugincore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

public class ModuleCommandHandler implements ICommandHandler {

    private final PluginCore m_plugin;
    private final ModuleLoader m_moduleLoader;

    public ModuleCommandHandler(PluginCore plugin) {
        m_plugin = plugin;
        m_moduleLoader = plugin.getModuleLoader();
    }

    @Override
    public void Handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {

    }
}
