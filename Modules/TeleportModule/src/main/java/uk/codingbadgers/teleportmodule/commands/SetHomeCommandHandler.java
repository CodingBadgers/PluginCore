package uk.codingbadgers.teleportmodule.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommand;
import uk.codingbadgers.teleportmodule.TeleportModule;

public class SetHomeCommandHandler extends ModuleCommand {

    private final TeleportModule m_module;

    public SetHomeCommandHandler(TeleportModule module) {
        super(module, "sethome");
        m_module = module;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!hasPermission(sender, "teleport.home.set")) {
            return true;
        }

        if (!(sender instanceof Player)) {
            sendMessage(sender, "Only online players can set a home.");
            return true;
        }

        Player player = (Player)sender;
        boolean forceHomeSetting = hasPermission(sender,"teleport.home.set.always", false);

        if (args.length == 0) {
            if (m_module.setHome(player.getUniqueId(), player.getLocation(), forceHomeSetting)) {
                sendMessage(sender, "Your home has been set to your current location.");
                sendMessage(sender, "Your home location can not be changed for 24 hours.");
            } else {
                sendMessage(sender, "You can not set your home at this time.");
                sendMessage(sender, "If you have already set your home today, please wait until tomorrow.");
            }
        } else {
            if (!hasPermission(sender, "teleport.home.set.other")) {
                return true;
            }

            String targetPlayerName = args[0];
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetPlayerName);
            if (!targetPlayer.hasPlayedBefore()) {
                sendMessage(player, "No player with the name '" + targetPlayerName + "' could be found.");
                return true;
            }

            if (targetPlayer.getUniqueId() == player.getUniqueId()) {
                sendMessage(player, "Use /sethome to set your own home location.");
                return true;
            }

            m_module.setHome(targetPlayer.getUniqueId(), player.getLocation(), true);
            sendMessage(sender, "The home of '" + targetPlayerName + "' has been set to your current location.");
        }

        return true;
    }
}
