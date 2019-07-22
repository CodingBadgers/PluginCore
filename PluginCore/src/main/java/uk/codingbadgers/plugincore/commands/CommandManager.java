package uk.codingbadgers.plugincore.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandManager implements TabExecutor {

    private final PluginCore m_pluginCore;
    private final MessageSystem m_messageSystem;
    private final ICommandHandler m_helpCommand;
    private final ICommandHandler m_unknownCommand;
    private final HashMap<String, ICommandHandler> m_registeredCommands;

    public CommandManager(PluginCore pluginCore, MessageSystem messageSystem) {
        m_pluginCore = pluginCore;
        m_messageSystem = messageSystem;
        m_helpCommand = new HelpCommandHandler();
        m_unknownCommand = new UnknownCommandHandler();

        m_registeredCommands = new HashMap<>();

        m_registeredCommands.put("module", new ModuleCommandHandler(m_pluginCore));
        m_registeredCommands.put("modules", new ModulesCommandHandler(m_pluginCore));
        m_registeredCommands.put("list", new ListCommandHandler());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            m_helpCommand.Handle(m_messageSystem, sender, command, label, args);
            return true;
        }

        String subCommand = args[0];
        if (m_registeredCommands.containsKey(subCommand)) {
            ICommandHandler commandHandler = m_registeredCommands.get(subCommand);

            String[] subArgs = new String[0];
            if (args.length >= 2) {
                subArgs = Arrays.copyOfRange(args, 1, args.length);
            }

            commandHandler.Handle(m_messageSystem, sender, command, subCommand, subArgs);
            return true;
        }

        m_unknownCommand.Handle(m_messageSystem, sender, command, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return ImmutableList.sortedCopyOf(m_registeredCommands.keySet());
        }

        return ImmutableList.of();
    }
}
