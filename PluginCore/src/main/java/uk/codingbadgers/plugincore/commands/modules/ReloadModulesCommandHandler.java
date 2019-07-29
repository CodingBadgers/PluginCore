package uk.codingbadgers.plugincore.commands.modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.commands.ICommandHandler;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

public class ReloadModulesCommandHandler implements ICommandHandler {

    private final ModuleLoader m_moduleLoader;

    public ReloadModulesCommandHandler(ModuleLoader moduleLoader) {
        m_moduleLoader = moduleLoader;
    }

    @Override
    public String getHelpMessage() {
        return "Reloads all modules.";
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        m_moduleLoader.disableAllModules();
        m_moduleLoader.unloadAllModules();
        m_moduleLoader.loadAllModules();
        m_moduleLoader.enableAllModules();
        messageSystem.SendMessage(sender, "All modules have been reloaded.");
    }
}
