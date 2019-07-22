package uk.codingbadgers.plugincore.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.codingbadgers.plugincore.PluginCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CorePlayerManager implements Listener {

    private final PluginCore m_plugin;
    private Map<UUID, CorePlayer> m_players = new HashMap<>();

    public CorePlayerManager(PluginCore plugin) {
        m_plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        m_players.put(player.getUniqueId(), new CorePlayer(m_plugin, player));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onPlayerQuit(PlayerQuitEvent e) {
        final Player player = e.getPlayer();

        m_players.remove(player.getUniqueId());
    }

    public CorePlayer getPlayer(Player player) {
        return m_players.get(player.getUniqueId());
    }
}
