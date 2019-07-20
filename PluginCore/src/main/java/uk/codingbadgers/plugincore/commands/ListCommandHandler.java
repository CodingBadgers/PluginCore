package uk.codingbadgers.plugincore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

public class ListCommandHandler implements ICommandHandler {
    @Override
    public void Handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        messageSystem.SendMessage(sender, "Todo: List command");
    }
}
