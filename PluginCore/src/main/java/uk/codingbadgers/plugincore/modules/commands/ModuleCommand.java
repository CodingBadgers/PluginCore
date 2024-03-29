package uk.codingbadgers.plugincore.modules.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import uk.codingbadgers.plugincore.modules.Module;

public abstract class ModuleCommand extends Command implements TabCompleter {
    protected final Module m_module;

    private final List<ModuleChildCommand> m_childCommands = new ArrayList<>();

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

        sendUsage(sender);
        return false;
    }

    private void sendUsage(CommandSender sender) {
        if (m_childCommands.size() == 0) {
            sendMessage(sender, getUsage());
            return;
        }

        sendMessage(sender, "Command: " + getLabel());
        sendMessage(sender, "Description: " + getDescription());
        sendMessage(sender, "Child commands:");
        for (ModuleChildCommand child : m_childCommands) {
            sendMessage(sender, " - " + child.getLabel() + ": " + child.getDescription());
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        // TODO this code needs cleaning up and optimising
        ImmutableList.Builder<String> builder = ImmutableList.builder();

        if (m_childCommands.size() >= 1) {
            if (args.length == 1) {
                for (ModuleChildCommand child : m_childCommands) {
                    if (child.getLabel().startsWith(args[0])) {
                        builder.add(child.getLabel());
                    }
                }
            } else {
                for (ModuleChildCommand child : m_childCommands) {
                    if (child.getLabel().equalsIgnoreCase(args[0])) {
                        builder.addAll(child.tabComplete(sender, alias, args));
                    }
                }
            }
        } else {
            try {
                List<String> completions = onTabComplete(sender, this, alias, Arrays.copyOfRange(args, 1, args.length));

                if (completions != null) {
                    builder.addAll(completions);
                }
            } catch (Throwable ex) {
                StringBuilder message = new StringBuilder();
                message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');
                for (String arg : args) {
                    message.append(arg).append(' ');
                }
                message.deleteCharAt(message.length() - 1).append("' in plugin ").append(m_module.getDescription().getName());
                throw new CommandException(message.toString(), ex);
            }
        }

        List<String> sortable = new ArrayList<>(builder.build());
        sortable.sort(String.CASE_INSENSITIVE_ORDER);
        return ImmutableList.copyOf(sortable);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }

    protected void sendMessage(CommandSender sender, String message) {
        m_module.sendMessage(sender, message);
    }

    protected boolean hasPermission(CommandSender sender, String permission) {
        return hasPermission(sender, permission, true);
    }

    protected boolean hasPermission(CommandSender sender, String permission, boolean verbose) {

        if (m_module.getPlugin().getVaultPermissions().has(sender, permission)) {
            return true;
        }

        if (verbose) {
            sendMessage(sender, "You do not have the required permissions '" + permission + "'");
        }
        return false;
    }

    protected List<String> tabCompletePlayer(String name) {
        List<Player> players = Bukkit.matchPlayer(name);
        ImmutableList.Builder<String> builder = ImmutableList.builder();

        for (Player player : players) {
            builder.add(player.getName());
        }

        return builder.build();
    }
}
