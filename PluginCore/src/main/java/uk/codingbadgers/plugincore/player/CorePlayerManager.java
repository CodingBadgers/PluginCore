package uk.codingbadgers.plugincore.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.codingbadgers.plugincore.PluginCore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class CorePlayerManager implements Listener {

    private final PluginCore m_plugin;
    private final File m_dataFolder;
    private final Map<UUID, CorePlayer> m_players = new HashMap<>();

    public CorePlayerManager(PluginCore plugin) {
        m_plugin = plugin;
        m_dataFolder = new File(plugin.getDataFolder(), "player_data");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        m_players.put(player.getUniqueId(), new CorePlayer(m_plugin, player, m_dataFolder));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onPlayerQuit(PlayerQuitEvent e) {
        final Player p = e.getPlayer();

        CorePlayer player = m_players.get(p.getUniqueId());
        player.save();

        m_players.remove(p.getUniqueId());
    }

    public CorePlayer getPlayer(Player player) {
        CorePlayer corePlayer = m_players.get(player.getUniqueId());

        if (corePlayer == null) {
            m_plugin.getLogger().log(Level.SEVERE, "Could not find core player for player '" + player.getName() + "'");
        }

        return corePlayer;
    }
}
