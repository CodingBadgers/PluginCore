package uk.codingbadgers.chat.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import uk.codingbadgers.chat.ChatModule;
import uk.codingbadgers.chat.channels.Channel;
import uk.codingbadgers.chat.channels.ChannelManager;
import uk.codingbadgers.plugincore.player.CorePlayer;
import uk.codingbadgers.plugincore.player.CorePlayerData;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatPlayerData implements CorePlayerData {

    private final ChannelManager m_channelManager;

    private Channel m_activeChannel;
    private List<Channel> m_listeningChannels;
    private CorePlayer m_player;

    public ChatPlayerData(ChatModule module, CorePlayer player) {
        m_channelManager = module.getChannelManager();
        m_player = player;
        m_listeningChannels = new ArrayList<>();
    }

    public Channel getActiveChannel() {
        return m_activeChannel;
    }

    public void setActiveChannel(Channel channel) {
        m_activeChannel = channel;

        if (!m_listeningChannels.contains(channel)) {
            m_listeningChannels.add(channel);
        }
    }

    public List<Channel> getChannels() {
        return m_listeningChannels;
    }

    public void joinChannel(Channel channel) {
        m_listeningChannels.add(channel);
    }

    public void leaveChannel(Channel channel) {
        m_listeningChannels.remove(channel);
    }

    @Override
    public boolean save(File dataFile) throws IOException {
        if (!dataFile.exists() && !dataFile.createNewFile()) {
            return false;
        }

        JsonObject data = new JsonObject();
        data.addProperty("active_channel", m_activeChannel.getName());

        JsonArray listeningChannels = new JsonArray();
        for (Channel channel : m_listeningChannels) {
            listeningChannels.add(channel.getName());
        }
        data.add("listening_channels", listeningChannels);

        try (FileWriter writer = new FileWriter(dataFile)) {
            ChatModule.GSON.toJson(data, writer);
        }

        return true;
    }

    @Override
    public boolean load(File dataFile) throws IOException {
        if (!dataFile.exists()) {
            Channel channel = m_channelManager.getDefaultChannel();
            m_activeChannel = channel;
            m_listeningChannels.add(channel);
            channel.playerJoin(m_player);
            save(dataFile);

            return true;
        }

        try (FileReader reader = new FileReader(dataFile)) {
            JsonElement json = new JsonParser().parse(reader);
            JsonObject data = json.getAsJsonObject();

            m_activeChannel = m_channelManager.getChannel(data.get("active_channel").getAsString());

            for (JsonElement e : data.getAsJsonArray("listening_channels")) {
                Channel channel = m_channelManager.getChannel(e.getAsString());
                m_listeningChannels.add(channel);
                channel.playerJoin(m_player);
            }
        }

        return true;
    }
}
