package uk.codingbadgers.chat.channels;

import uk.codingbadgers.chat.ChatModule;
import uk.codingbadgers.plugincore.utilities.FileExtensionFilter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ChannelManager {

    private ChatModule m_module;

    private Map<String, Channel> m_channels = new HashMap<>();
    private File m_channelsDirectory;

    public ChannelManager(ChatModule m_module) {
        this.m_module = m_module;

        m_channelsDirectory = new File(m_module.getDataFolder(), "channels");
        m_channelsDirectory.mkdirs();
    }

    public void loadChannels() {
        File[] files = m_channelsDirectory.listFiles(new FileExtensionFilter("json"));

        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                Channel channel = ChatModule.GSON.fromJson(reader, Channel.class);
                addChannel(channel);
                m_module.getLogger().log(Level.INFO, "Loaded channel \"{0}\"", channel.getName());
            } catch (IOException e) {
                m_module.getLogger().log(Level.SEVERE, "Error loading channel from '" + file.getName() + "'", e);
            }
        }

        /*
         * If no channels are loaded, add a default channel so there always exists at least one.
         */
        if (m_channels.size() == 0) {
            addChannel(new Channel("default"));
            m_module.getLogger().log(Level.INFO, "Added default channel \"default\"");
        }
    }

    public void saveChannel(Channel channel) {
        channel.saveChannel(m_module, m_channelsDirectory);
    }

    public boolean addChannel(Channel channel) {
        if (m_channels.containsKey(channel.getName())) {
            return false;
        }

        channel.setupChannel();
        saveChannel(channel);
        m_channels.put(channel.getName(), channel);
        return true;
    }

    public void removeChannel(Channel channel) {
        saveChannel(channel);
        m_channels.remove(channel.getName());
    }

    public Channel getChannel(String name) {
        return m_channels.get(name);
    }

    public Iterable<Channel> getChannels() {
        return m_channels.values();
    }

    public Channel getDefaultChannel() {
        return m_channels.get("default"); // TODO make configurable
    }
}
