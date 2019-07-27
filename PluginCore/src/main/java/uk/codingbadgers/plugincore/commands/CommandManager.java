package uk.codingbadgers.plugincore.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.*;

public class CommandManager implements TabExecutor {

    private final MessageSystem m_messageSystem;
    private final ICommandHandler m_helpCommand;
    private final ICommandHandler m_unknownCommand;
    private final Map<String, ICommandHandler> m_registeredCommands;

    public CommandManager(PluginCore pluginCore, MessageSystem messageSystem) {
        m_messageSystem = messageSystem;
        m_helpCommand = new HelpCommandHandler(this);
        m_unknownCommand = new UnknownCommandHandler();

        m_registeredCommands = new TreeMap<>();

        m_registeredCommands.put("module", new ModuleCommandHandler(pluginCore));
        m_registeredCommands.put("modules", new ModulesCommandHandler(pluginCore));
        m_registeredCommands.put("list", new ListCommandHandler(pluginCore));
    }

    Set<Map.Entry<String, ICommandHandler>> getCommandHandlers() {
        return m_registeredCommands.entrySet();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            m_helpCommand.handle(m_messageSystem, sender, command, label, args);
            return true;
        }

        String subCommand = args[0];
        if (m_registeredCommands.containsKey(subCommand)) {
            ICommandHandler commandHandler = m_registeredCommands.get(subCommand);

            String[] subArgs = new String[0];
            if (args.length >= 2) {
                subArgs = Arrays.copyOfRange(args, 1, args.length);
            }

            commandHandler.handle(m_messageSystem, sender, command, subCommand, subArgs);
            return true;
        }

        m_unknownCommand.handle(m_messageSystem, sender, command, label, args);
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
