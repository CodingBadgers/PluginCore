package uk.codingbadgers.plugincore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

public interface ICommandHandler {
    String getHelpMessage();
    void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args);
}
