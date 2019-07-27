package uk.codingbadgers.chat.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.codingbadgers.chat.ChatModule;
import uk.codingbadgers.chat.channels.Channel;
import uk.codingbadgers.chat.channels.ChannelManager;
import uk.codingbadgers.chat.data.ChatPlayerData;
import uk.codingbadgers.plugincore.player.CorePlayer;
import uk.codingbadgers.plugincore.player.CorePlayerManager;

public class PlayerListener implements Listener {

    private final ChatModule m_module;
    private final CorePlayerManager m_playerManager;
    private final ChannelManager m_channelManager;

    public PlayerListener(ChatModule module) {
        m_module = module;
        m_playerManager = module.getPlugin().getPlayerManager();
        m_channelManager = module.getChannelManager();
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        CorePlayer player = m_playerManager.getPlayer(e.getPlayer());
        ChatPlayerData data = player.getPlayerData(ChatPlayerData.class);

        e.setCancelled(true);

        data.getActiveChannel().sendMessage(m_module, player, e.getMessage());
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerJoin(PlayerJoinEvent e) {
        CorePlayer player = m_playerManager.getPlayer(e.getPlayer());

        player.addPlayerData(new ChatPlayerData(m_module, player));
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerLeave(PlayerQuitEvent e) {
        CorePlayer player = m_playerManager.getPlayer(e.getPlayer());
        ChatPlayerData data = player.getPlayerData(ChatPlayerData.class);

        player.removePlayerData(ChatPlayerData.class);

        for (Channel channel : data.getChannels()) {
            channel.playerLeave(player);
        }
    }

}
