package uk.codingbadgers.chat.channels;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import uk.codingbadgers.chat.ChatModule;
import uk.codingbadgers.plugincore.player.CorePlayer;

import java.util.ArrayList;
import java.util.List;

public class Channel {

    private final ChatModule m_module;

    private String m_name;
    private List<CorePlayer> m_currentPlayers = new ArrayList<>();

    public Channel(ChatModule module, String name) {
        m_module = module;
        m_name = name;
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

    public void sendMessage(CorePlayer player, String msg) {
        Chat chat = m_module.getPlugin().getVaultChat();

        // Build message
        ComponentBuilder builder = new ComponentBuilder("");

        String prefixStr = ChatColor.translateAlternateColorCodes('&', chat.getPlayerPrefix(player.getPlayer()));
        BaseComponent[] prefix = TextComponent.fromLegacyText(prefixStr);
        builder.append(prefix);

        builder.append("", ComponentBuilder.FormatRetention.NONE);

        BaseComponent name = new TextComponent(player.getName());
        name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + player.getName() + " "));
        builder.append(name);

        String suffixStr = ChatColor.translateAlternateColorCodes('&', chat.getPlayerSuffix(player.getPlayer()));
        BaseComponent[] suffix = TextComponent.fromLegacyText(suffixStr);
        builder.append(suffix);

        builder.append(": ", ComponentBuilder.FormatRetention.NONE);

        msg = ChatColor.translateAlternateColorCodes('&', msg);
        builder.append(TextComponent.fromLegacyText(msg));

        // Send final message to all players currently in this channel
        BaseComponent[] components = builder.create();

        for (CorePlayer p : m_currentPlayers) {
            p.sendRawMessage(components);
        }
    }
}
