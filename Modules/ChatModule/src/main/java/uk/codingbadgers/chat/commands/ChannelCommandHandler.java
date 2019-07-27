package uk.codingbadgers.chat.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.chat.ChatModule;
import uk.codingbadgers.chat.channels.Channel;
import uk.codingbadgers.chat.channels.ChannelManager;
import uk.codingbadgers.plugincore.modules.commands.ModuleChildCommand;
import uk.codingbadgers.plugincore.player.CorePlayer;

import java.util.List;

public class ChannelCommandHandler extends ModuleChildCommand {

    private ChannelManager m_channelManager;

    protected ChannelCommandHandler(ChatModule module) {
        super(module, "channel");

        m_channelManager = module.getChannelManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendChannelList(sender);
            return true;
        }

        Channel channel = m_channelManager.getChannel(args[0]);

        if (args.length == 1) {
            sendChannelInfo(sender, channel);
            return true;
        }

        switch (args[1]) {
            case "info":
                sendChannelInfo(sender, channel);
                break;
            case "delete":
                break;
        }

        return true;
    }

    private void sendChannelList(CommandSender sender) {
        sendMessage(sender, "Channels:");

        for (Channel channel : m_channelManager.getChannels()) {
            sendMessage(sender, " - " + channel.getName());
        }
    }

    private void sendChannelInfo(CommandSender sender, Channel channel) {
        sendMessage(sender, "Channel: ");
        sendMessage(sender, "  Name: " + channel.getName());
        sendMessage(sender, "  Active Players (" + channel.getActivePlayerCount() + "): ");

        for (CorePlayer p : channel.getActivePlayers()) {
            sendMessage(sender, "   - " + p.getName());
        }
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
        } else if (args.length == 2) {
            for (String s : new String[] { "info", "delete" }) {
                if (args[1].equalsIgnoreCase(s)) {
                    builder.add(s);
                }
            }
        }

        return builder.build();
    }
}
