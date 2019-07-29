package uk.codingbadgers.plugincore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public abstract class SubCommandHandler implements ICommandHandler {

    protected final MessageSystem m_messageSystem;
    private final Map<String, ICommandHandler> m_subCommands;

    protected SubCommandHandler(MessageSystem messageSystem) {
        m_messageSystem = messageSystem;
        m_subCommands = new TreeMap<>();
    }

    protected void registerSubCommand(String command, ICommandHandler handler) {
        m_subCommands.put(command, handler);
    }

    protected Map<String, ICommandHandler> getSubCommands() {
        return m_subCommands;
    }

    protected void showHelp(CommandSender sender) {
        m_messageSystem.SendMessage(sender, "PluginCore Commands:");
        for (Map.Entry<String, ICommandHandler> entry : m_subCommands.entrySet())
        {
            String subCommand = entry.getKey();
            String helpMessage = entry.getValue().getHelpMessage();
            if (helpMessage != null) {
                m_messageSystem.SendMessage(sender, ChatColor.GOLD + subCommand + ChatColor.RESET
                        + " " + helpMessage);
            }
        }
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return;
        }

        String subCommand = args[0];
        if (getSubCommands().containsKey(subCommand)) {
            ICommandHandler commandHandler = getSubCommands().get(subCommand);

            String[] subArgs = new String[0];
            if (args.length >= 2) {
                subArgs = Arrays.copyOfRange(args, 1, args.length);
            }

            commandHandler.handle(m_messageSystem, sender, command, subCommand, subArgs);
            return;
        }

        showHelp(sender);
    }
}
