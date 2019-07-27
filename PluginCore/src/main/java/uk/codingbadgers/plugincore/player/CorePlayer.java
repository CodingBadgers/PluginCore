package uk.codingbadgers.plugincore.player;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import uk.codingbadgers.plugincore.PluginCore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class CorePlayer {

    protected final PluginCore m_plugin;
    protected final Player m_player;
    protected final File m_dataFolder;
    protected HashMap<Class<? extends CorePlayerData>, CorePlayerData> m_playerdata = new HashMap<>();

    public CorePlayer(PluginCore plugin, Player player, File playerDataFolder) {
        m_plugin = plugin;
        m_player = player;
        m_dataFolder = new File(playerDataFolder, player.getUniqueId().toString());
        m_dataFolder.mkdirs();
    }

    public void sendMessage(String msg) {
        m_plugin.getMessageSystem().SendMessage(m_player, msg);
    }

    public void sendRawMessage(String formattedMessage) {
        m_player.sendMessage(formattedMessage);
    }

    public void sendRawMessage(BaseComponent[] components) {
        m_player.spigot().sendMessage(components);
    }

    public Player getPlayer() {
        return m_player;
    }

    public String getName() {
        return m_player.getName();
    }

    public UUID getUUID() {
        return m_player.getUniqueId();
    }

    public void addPlayerData(CorePlayerData data) {
        m_playerdata.put(data.getClass(), data);

        try {
            data.load(new File(m_dataFolder, String.format("%s.json", data.getClass().getSimpleName())));
        } catch (IOException e) {
            m_plugin.getLogger().log(Level.SEVERE, "Error loading player data '" + data.getClass().getSimpleName()
                    + "' for player '" + m_player.getName() + "'", e);
        }
    }

    public void removePlayerData(Class<? extends CorePlayerData> clazz) {
        CorePlayerData data = m_playerdata.get(clazz);

        try {
            data.save(new File(m_dataFolder, String.format("%s.json", data.getClass().getSimpleName())));
        } catch (IOException e) {
            m_plugin.getLogger().log(Level.SEVERE, "Error saving player data '" + data.getClass().getSimpleName()
                    + "' for player '" + m_player.getName() + "'", e);
        }

        m_playerdata.remove(clazz);
    }

    public void save() {
        for (CorePlayerData data : m_playerdata.values()) {
            try {
                data.save(new File(m_dataFolder, String.format("%s.json", data.getClass().getSimpleName())));
            } catch (IOException e) {
                m_plugin.getLogger().log(Level.SEVERE, "Error saving player data '" + data.getClass().getSimpleName()
                        + "' for player '" + m_player.getName() + "'", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends CorePlayerData> T getPlayerData(Class<T> clazz) {
        return (T) m_playerdata.get(clazz);
    }
}
