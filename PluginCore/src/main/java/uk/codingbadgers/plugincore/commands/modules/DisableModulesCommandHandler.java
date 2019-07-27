package uk.codingbadgers.plugincore.commands.modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.commands.ICommandHandler;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

public class DisableModulesCommandHandler implements ICommandHandler {

    private final ModuleLoader m_moduleLoader;

    public DisableModulesCommandHandler(ModuleLoader moduleLoader) {
        m_moduleLoader = moduleLoader;
    }

    @Override
    public String getHelpMessage() {
        return "Disable all enabled modules.";
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        m_moduleLoader.disableModules();
        messageSystem.SendMessage(sender, "All modules have been disabled.");
    }
}
