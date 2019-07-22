package uk.codingbadgers.chat;

import uk.codingbadgers.chat.channels.Channel;
import uk.codingbadgers.chat.channels.ChannelManager;
import uk.codingbadgers.chat.listeners.PlayerListener;
import uk.codingbadgers.plugincore.modules.Module;

public class ChatModule extends Module {

    private ChannelManager m_channelManager;

    @Override
    public void onLoad() {
        m_channelManager = new ChannelManager(this);
    }

    @Override
    public void onEnable() {
        this.registerListener(new PlayerListener(this));

        m_channelManager.addChannel(new Channel(this, "default"));
    }

    @Override
    public void onDisable() {

    }

    public ChannelManager getChannelManager() {
        return m_channelManager;
    }
}
