package uk.codingbadgers.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.codingbadgers.chat.channels.ChannelManager;
import uk.codingbadgers.chat.commands.ChatCommandHandler;
import uk.codingbadgers.chat.commands.PrivateMessageCommandHandler;
import uk.codingbadgers.chat.data.ChatPlayerData;
import uk.codingbadgers.chat.listeners.PlayerListener;
import uk.codingbadgers.plugincore.json.JsonExclusionStrategy;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.player.CorePlayer;

import java.util.logging.Level;

public class ChatModule extends Module {

    public static final Gson GSON = new GsonBuilder()
            .setExclusionStrategies(new JsonExclusionStrategy())
            .setPrettyPrinting()
            .create();

    private ChannelManager m_channelManager;

    @Override
    public void onLoad() {
        m_channelManager = new ChannelManager(this);
    }

    @Override
    public void onEnable() {
        this.registerListener(new PlayerListener(this));

        this.registerCommand(new ChatCommandHandler(this));
        this.registerCommand(new PrivateMessageCommandHandler(this));

        m_channelManager.loadChannels();

        for (CorePlayer p : m_plugin.getPlayerManager().getPlayers()) {
            p.addPlayerData(new ChatPlayerData(this, p));
        }

        getLogger().log(Level.INFO, "Enabled " + getDescription().getName() +
                " v" + getDescription().getVersion() + " successfully");
    }

    @Override
    public void onDisable() {
        for (CorePlayer p : m_plugin.getPlayerManager().getPlayers()) {
            p.removePlayerData(ChatPlayerData.class);
        }

        getLogger().log(Level.INFO, "Disabled " + getDescription().getName() +
                " v" + getDescription().getVersion() + " successfully");
    }

    public ChannelManager getChannelManager() {
        return m_channelManager;
    }
}
