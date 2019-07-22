package uk.codingbadgers.plugincore.player;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import uk.codingbadgers.plugincore.PluginCore;

import java.util.HashMap;
import java.util.UUID;

public class CorePlayer {

    protected PluginCore m_plugin;
    protected Player m_player;
    protected HashMap<Class<? extends CorePlayerData>, CorePlayerData> m_playerdata = new HashMap<>();

    public CorePlayer(PluginCore plugin, Player player) {
        m_plugin = plugin;
        m_player = player;
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
    }

    public void removePlayerData(Class<? extends CorePlayerData> clazz) {
        m_playerdata.remove(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T extends CorePlayerData> T getPlayerData(Class<T> clazz) {
        return (T) m_playerdata.get(clazz);
    }
}
