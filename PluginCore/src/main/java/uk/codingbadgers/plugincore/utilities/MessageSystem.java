package uk.codingbadgers.plugincore.utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.PluginCore;

public class MessageSystem {

    private final String m_pluginName;

    public MessageSystem(PluginCore plugin) {
        m_pluginName = plugin.getDescription().getName();
    }

    public void SendMessage(CommandSender target, String message) {
        String formattedMethod = String.format(
                "%s[%s]%s %s",
                ChatColor.DARK_PURPLE,
                m_pluginName,
                ChatColor.WHITE,
                message);
        target.sendMessage(formattedMethod);
    }

}
