package uk.codingbadgers.chat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.codingbadgers.chat.channels.Channel;
import uk.codingbadgers.chat.data.ChatPlayerData;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.commands.ModuleChildCommand;
import uk.codingbadgers.plugincore.player.CorePlayer;

import java.util.List;

public class UserCommandHandler extends ModuleChildCommand {
    public UserCommandHandler(Module module) {
        super(module, "user");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        Player p = Bukkit.getPlayer(args[0]);

        if (p == null) {
            sendMessage(sender, ChatColor.RED + "Unknown player '" + args[0] + "'");
            return true;
        }

        CorePlayer player = m_module.getPlugin().getPlayerManager().getPlayer(p);
        ChatPlayerData data = player.getPlayerData(ChatPlayerData.class);

        if (args.length == 1) {
            SendPlayerInfo(sender, player, data);
            return true;
        }

        return true;
    }

    private void SendPlayerInfo(CommandSender sender, CorePlayer player, ChatPlayerData data) {
        sendMessage(sender, "Player: " + player.getName());
        sendMessage(sender, "Active: " + data.getActiveChannel().getName());
        sendMessage(sender, "Channels: ");

        for (Channel channel : data.getChannels()) {
            sendMessage(sender, " - " + channel.getName());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return super.onTabComplete(sender, command, label, args);
    }
}
