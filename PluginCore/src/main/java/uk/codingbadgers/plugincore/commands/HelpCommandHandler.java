package uk.codingbadgers.plugincore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

public class HelpCommandHandler implements ICommandHandler {

    @Override
    public void Handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        messageSystem.SendMessage(sender, "Plugin Core Commands:");
        messageSystem.SendMessage(sender, ChatColor.GOLD + "module" + ChatColor.RESET + " - access module control commands");
        messageSystem.SendMessage(sender, ChatColor.GOLD + "list" + ChatColor.RESET + " - lists all installed modules");
    }
}
