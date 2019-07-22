package uk.codingbadgers.chat.channels;

import uk.codingbadgers.chat.ChatModule;
import uk.codingbadgers.plugincore.PluginCore;

import java.util.HashMap;
import java.util.Map;

public class ChannelManager {

    private ChatModule m_module;

    private Map<String, Channel> m_channels = new HashMap<>();

    public ChannelManager(ChatModule m_module) {
        this.m_module = m_module;
    }

    public boolean addChannel(Channel channel) {
        if (m_channels.containsKey(channel.getName())) {
            return false;
        }

        m_channels.put(channel.getName(), channel);
        return true;
    }

    public void removeChannel(Channel channel) {
        m_channels.remove(channel.getName());
    }

    public Channel getChannel(String name) {
        return m_channels.get(name);
    }
}
