package uk.codingbadgers.plugincore.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.List;

public class CommandManager implements TabExecutor {

    private final MessageSystem m_messageSystem;
    private final ICommandHandler m_helpCommand;
    private final ICommandHandler m_unknownCommand;

    public CommandManager(MessageSystem messageSystem) {
        m_messageSystem = messageSystem;
        m_helpCommand = new HelpCommand();
        m_unknownCommand = new UnknownCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            m_helpCommand.Handle(m_messageSystem, sender, command, label, args);
            return true;
        }

        m_unknownCommand.Handle(m_messageSystem, sender, command, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return ImmutableList.of();
    }
}
