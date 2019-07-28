package uk.codingbadgers.chat.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PrivateMessageCommandHandler extends ModuleCommand {
    public PrivateMessageCommandHandler(Module module) {
        super(module, "pm");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sendMessage(sender, ChatColor.RED + "/" + label + " <target> <message>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sendMessage(sender, ChatColor.RED + "Unknown player \"" + args[0] + "\"");
            return true;
        }

        String message = StringUtils.join(args, " ", 1, args.length);

        sendPrivateMessage(sender, target, message);
        m_module.getLogger().log(Level.INFO, "[{0}->{1}] {2}", new Object[] { sender.getName(), target.getName(), message });
        return true;
    }

    private void sendPrivateMessage(CommandSender sender, Player target, String message) {
        ComponentBuilder builder = new ComponentBuilder("");

        builder.color(ChatColor.DARK_GRAY);
        builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pm " + target.getName() + " "));
        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Message " + target.getName())));
        builder.append("To [");
        builder.append(target.getName());
        builder.append("]: ");
        builder.append(message);
        builder.color(ChatColor.GRAY);

        sender.spigot().sendMessage(builder.create());

        builder = new ComponentBuilder("");

        builder.color(ChatColor.DARK_GRAY);
        builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pm " + sender.getName() + " "));
        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Reply to " + sender.getName())));
        builder.append("From [");
        builder.append(sender.getName());
        builder.append("]: ");
        builder.append(message);
        builder.color(ChatColor.GRAY);

        target.spigot().sendMessage(builder.create());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return tabCompletePlayer(args[0]);
        }

        return new ArrayList<>();
    }
}
