package uk.codingbadgers.chat.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.codingbadgers.chat.ChatModule;
import uk.codingbadgers.chat.channels.Channel;
import uk.codingbadgers.chat.data.ChatPlayerData;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommand;
import uk.codingbadgers.plugincore.player.CorePlayer;
import uk.codingbadgers.plugincore.player.CorePlayerManager;

public class QuickMessageCommandHandler extends ModuleCommand {
    private final Channel m_channel;
    private final CorePlayerManager m_playerManager;

    public QuickMessageCommandHandler(ChatModule module, Channel channel) {
        super(module, channel.getName());

        m_channel = channel;
        m_playerManager = module.getPlugin().getPlayerManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sendMessage(sender, "Join command can only be used by players");
            return true;
        }

        CorePlayer player = m_playerManager.getPlayer((Player) sender);
        ChatPlayerData data = player.getPlayerData(ChatPlayerData.class);

        if (!hasPermission(sender, "chat.channel." + m_channel.getName().toLowerCase() + ".speak")) {
            return true;
        }

        if (!data.getChannels().contains(m_channel)) {
            sendMessage(sender, "You are not part of the channel \"" + m_channel.getName() + "\"");
            return true;
        }

        String message = StringUtils.join(args, ' ');
        m_channel.sendMessage((ChatModule) m_module, player, message);

        return true;
    }
}
