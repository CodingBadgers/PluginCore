package uk.codingbadgers.plugincore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.Map;

public class HelpCommandHandler implements ICommandHandler {

    private final CommandManager m_commandManager;

    HelpCommandHandler(CommandManager commandManager) {
        m_commandManager = commandManager;
    }

    @Override
    public String getHelpMessage() { return null; }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        messageSystem.SendMessage(sender, "PluginCore Commands:");
        for (Map.Entry<String, ICommandHandler> entry : m_commandManager.getCommandHandlers())
        {
            String subCommand = entry.getKey();
            String helpMessage = entry.getValue().getHelpMessage();
            if (helpMessage != null) {
                messageSystem.SendMessage(sender, ChatColor.GOLD + subCommand + ChatColor.RESET
                                                    + " " + helpMessage);
            }
        }
    }
}
