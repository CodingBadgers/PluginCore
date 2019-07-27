package uk.codingbadgers.chat.channels;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.milkbowl.vault.chat.Chat;
import uk.codingbadgers.chat.ChatModule;
import uk.codingbadgers.plugincore.json.NotSerialized;
import uk.codingbadgers.plugincore.player.CorePlayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Channel {

    public enum MessagePartType {
        TEXT,
        PREFIX,
        NAME,
        SUFFIX,
        MESSAGE,
    }

    public class MessagePart {
        public MessagePartType type;
        public String value;
        public ChatColor color;
    }

    @NotSerialized
    private List<CorePlayer> m_currentPlayers;
    @NotSerialized
    private File m_saveFile;

    @SerializedName("name")
    private String m_name;
    @SerializedName("format")
    private List<MessagePart> m_format = new ArrayList<>();

    public Channel(String name) {
        m_name = name;
    }

    public void setupChannel() {
        m_currentPlayers = new ArrayList<>();
    }

    public void saveChannel(ChatModule module, File channelsDir) {
        try (FileWriter writer = new FileWriter(getSaveFile(channelsDir))) {
            ChatModule.GSON.toJson(this, writer);
            writer.flush();
        } catch (IOException e) {
            module.getLogger().log(Level.SEVERE, "Error saving channel '" + m_name + "'", e);
        }
    }

    private File getSaveFile(File channelsDir) {
        if (m_saveFile == null) {
            m_saveFile = new File(channelsDir, String.format("%s.json", m_name));
        }
        return m_saveFile;
    }

    public String getName() {
        return m_name;
    }

    public void playerJoin(CorePlayer player) {
        m_currentPlayers.add(player);
    }

    public void playerLeave(CorePlayer player) {
        m_currentPlayers.remove(player);
    }

    public void sendMessage(ChatModule module, CorePlayer player, String msg) {
        Chat chat = module.getPlugin().getVaultChat();

        module.getLogger().log(Level.INFO, "[{0}->{1}] {2}", new String[] { player.getName(), m_name, msg });

        // Build message
        ComponentBuilder builder = new ComponentBuilder("");

        // TODO check permission for colored text
        msg = ChatColor.translateAlternateColorCodes('&', msg);

        for (MessagePart part : m_format) {
            switch (part.type) {
                case PREFIX:
                    String prefixStr = ChatColor.translateAlternateColorCodes('&', chat.getPlayerPrefix(player.getPlayer()));
                    BaseComponent[] prefix = TextComponent.fromLegacyText(prefixStr);
                    builder.append(prefix);
                    break;

                case NAME:
                    BaseComponent name = new TextComponent(player.getName());
                    name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + player.getName() + " "));
                    builder.append(name);
                    break;

                case SUFFIX:
                    String suffixStr = ChatColor.translateAlternateColorCodes('&', chat.getPlayerSuffix(player.getPlayer()));
                    BaseComponent[] suffix = TextComponent.fromLegacyText(suffixStr);
                    builder.append(suffix);
                    break;

                case MESSAGE:
                    builder.append(TextComponent.fromLegacyText(msg));
                    break;

                case TEXT:
                    BaseComponent[] text = TextComponent.fromLegacyText(part.value);
                    builder.append(text);
                    break;
            }

            builder.append("", ComponentBuilder.FormatRetention.NONE);
        }

        // Send final message to all players currently in this channel
        BaseComponent[] components = builder.create();

        for (CorePlayer p : m_currentPlayers) {
            p.sendRawMessage(components);
        }
    }

    public Iterable<CorePlayer> getActivePlayers() {
        return ImmutableList.copyOf(m_currentPlayers);
    }

    public int getActivePlayerCount() {
        return m_currentPlayers.size();
    }
}
