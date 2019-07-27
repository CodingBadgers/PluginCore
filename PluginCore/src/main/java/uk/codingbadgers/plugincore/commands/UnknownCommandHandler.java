package uk.codingbadgers.plugincore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

public class UnknownCommandHandler implements ICommandHandler {

    @Override
    public String getHelpMessage() {
        return null;
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        messageSystem.SendMessage(sender, "Unknown command " + ChatColor.RED + args[0] + ChatColor.RESET + ".");
        messageSystem.SendMessage(sender, "Enter '/plugincore' for help.");
    }
}
