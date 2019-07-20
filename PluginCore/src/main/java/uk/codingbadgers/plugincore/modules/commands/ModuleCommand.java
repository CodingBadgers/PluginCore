package uk.codingbadgers.plugincore.modules.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.codingbadgers.plugincore.modules.Module;

public abstract class ModuleCommand extends Command implements TabCompleter {
    private final Module m_module;

    private List<ModuleChildCommand> m_childCommands = new ArrayList<>();

    protected ModuleCommand(Module module, String name) {
        this(module, name, "", "", new ArrayList<>());
    }

    protected ModuleCommand(Module module, String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);

        m_module = module;
    }

    protected void registerChildCommand(ModuleChildCommand childCommand) {
        m_childCommands.add(childCommand);
    }

    @Override
    public final boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            for (ModuleChildCommand child : m_childCommands) {
                if (child.getLabel().equalsIgnoreCase(args[0])) {
                    return child.execute(sender, label, Arrays.copyOfRange(args, 1, args.length));
                }
            }
        }

        if (onCommand(sender, this, label, args)) {
            return true;
        }

        sendMessage(sender, getUsage());
        return false;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    protected void sendMessage(CommandSender sender, String msg) {
        String message = String.format(
            "%s[%s]%s %s",
            ChatColor.DARK_PURPLE,
            m_module.getDescription().getName(),
            ChatColor.WHITE,
            msg);

        sender.sendMessage(message);
    }
}
