package uk.codingbadgers.chat.commands.chat;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.codingbadgers.chat.ChatModule;
import uk.codingbadgers.chat.channels.Channel;
import uk.codingbadgers.chat.channels.ChannelManager;
import uk.codingbadgers.chat.data.ChatPlayerData;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.commands.ModuleChildCommand;
import uk.codingbadgers.plugincore.player.CorePlayer;
import uk.codingbadgers.plugincore.player.CorePlayerManager;

import java.util.List;

public class FocusCommandHandler extends ModuleChildCommand {
    private final ChannelManager m_channelManager;
    private final CorePlayerManager m_playerManager;

    public FocusCommandHandler(ChatModule module) {
        super(module, "focus");

        m_channelManager = module.getChannelManager();
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

        String channelName = args[0];
        Channel channel = m_channelManager.getChannel(channelName);

        if (channel == null) {
            sendMessage(sender, "Unknown channel \"" + channelName + "\"");
            return true;
        }

        if (!hasPermission(sender, "chat.channel." + channel.getName().toLowerCase() + ".speak")) {
            return true;
        }

        if (!data.getChannels().contains(channel)) {
            sendMessage(sender, "You are not part of the channel \"" + channelName + "\"");
            return true;
        }

        data.setActiveChannel(channel);
        sendMessage(sender, "Your active channel has been set to \"" + channelName + "\"");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();

        if (args.length == 1) {
            for (Channel channel : m_channelManager.getChannels()) {
                if (channel.getName().startsWith(args[0])) {
                    builder.add(channel.getName());
                }
            }
        }

        return builder.build();
    }
}
