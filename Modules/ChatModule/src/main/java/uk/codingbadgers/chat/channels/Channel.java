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
import java.util.Arrays;
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

    class MessagePart {
        MessagePartType type;
        String value;
        ChatColor color;

        MessagePart(MessagePartType type, String value, ChatColor color) {
            this.type = type;
            this.value = value;
            this.color = color;
        }
    }

    @NotSerialized
    private List<CorePlayer> m_currentPlayers;
    @NotSerialized
    private File m_saveFile;

    @SerializedName("name")
    private String m_name;
    @SerializedName("format")
    private List<MessagePart> m_format = new ArrayList<>(Arrays.asList(
            new MessagePart(MessagePartType.PREFIX, "", ChatColor.WHITE),
            new MessagePart(MessagePartType.NAME, "", ChatColor.WHITE),
            new MessagePart(MessagePartType.SUFFIX, "", ChatColor.WHITE),
            new MessagePart(MessagePartType.TEXT, ": ", ChatColor.WHITE),
            new MessagePart(MessagePartType.MESSAGE, "", ChatColor.WHITE)
    ));
    @SerializedName("required")
    private boolean m_required;

    public Channel(String name, boolean required) {
        m_name = name;
        m_required = required;
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
                    if (chat == null) {
                        break;
                    }

                    String prefixStr = ChatColor.translateAlternateColorCodes('&', chat.getPlayerPrefix(player.getPlayer()));
                    BaseComponent[] prefix = TextComponent.fromLegacyText(prefixStr, part.color);
                    builder.append(prefix);
                    break;

                case NAME:
                    BaseComponent name = new TextComponent(player.getName());
                    name.setColor(part.color);
                    name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + player.getName() + " "));
                    builder.append(name);
                    break;

                case SUFFIX:
                    if (chat == null) {
                        break;
                    }

                    String suffixStr = ChatColor.translateAlternateColorCodes('&', chat.getPlayerSuffix(player.getPlayer()));
                    BaseComponent[] suffix = TextComponent.fromLegacyText(suffixStr, part.color);
                    builder.append(suffix);
                    break;

                case MESSAGE:
                    builder.append(TextComponent.fromLegacyText(msg, part.color));
                    break;

                case TEXT:
                    BaseComponent[] text = TextComponent.fromLegacyText(part.value, part.color);
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

    public boolean isRequired() {
        return m_required;
    }
}
