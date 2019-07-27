package uk.codingbadgers.plugincore.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.*;

public class CommandManager extends SubCommandHandler implements TabExecutor {

    public CommandManager(PluginCore pluginCore, MessageSystem messageSystem) {
        super(messageSystem);
        registerSubCommand("module", new ModuleCommandHandler(pluginCore));
        registerSubCommand("modules", new ModulesCommandHandler(pluginCore));
        registerSubCommand("list", new ListCommandHandler(pluginCore));
    }

    @Override
    public String getHelpMessage() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        handle(m_messageSystem, sender, command, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 1) {
            return ImmutableList.sortedCopyOf(getSubCommands().keySet());
        }

        // TODO: Recurse to lower sub-command handlers
        String subCommand = args[0];
        if (getSubCommands().containsKey(subCommand)) {
            ICommandHandler commandHandler = getSubCommands().get(subCommand);
            if (commandHandler instanceof SubCommandHandler) {
                return ImmutableList.sortedCopyOf(((SubCommandHandler) commandHandler).getSubCommands().keySet());
            }
        }

        return ImmutableList.of();
    }
}
