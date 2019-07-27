package uk.codingbadgers.plugincore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleDescriptionFile;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

public class ListCommandHandler implements ICommandHandler {

    private final ModuleLoader m_moduleLoader;

    ListCommandHandler(PluginCore pluginCore) {
        m_moduleLoader = pluginCore.getModuleLoader();
    }

    @Override
    public String getHelpMessage() {
        return "Lists all installed modules, their state and version.";
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        messageSystem.SendMessage(sender, "PluginCore Modules:");
        for (Module module : m_moduleLoader.getModules()) {
            ModuleDescriptionFile desc = module.getDescription();
            messageSystem.SendMessage(sender, String.format("%s- %s by %s", ChatColor.GOLD, desc.getName(),
                    String.join(", ", desc.getAuthors())));
            messageSystem.SendMessage(sender, desc.getDescription());
            messageSystem.SendMessage(sender, String.format("%sVersion: %s%s", ChatColor.DARK_GREEN, ChatColor.WHITE,
                    desc.getVersion()));
            messageSystem.SendMessage(sender, String.format("%sEnabled: %s%s", ChatColor.DARK_GREEN,
                    module.isEnabled() ? ChatColor.GREEN : ChatColor.RED, module.isEnabled()));
        }
    }
}
