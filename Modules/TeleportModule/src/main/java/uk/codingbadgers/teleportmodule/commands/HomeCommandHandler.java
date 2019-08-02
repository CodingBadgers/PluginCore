package uk.codingbadgers.teleportmodule.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommand;
import uk.codingbadgers.teleportmodule.TeleportModule;

import java.util.UUID;

public class HomeCommandHandler extends ModuleCommand {

    private final TeleportModule m_module;

    public HomeCommandHandler(TeleportModule module) {
        super(module, "home");
        m_module = module;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            TeleportToHome(sender);
            return true;
        }

        if (args.length == 1) {
            TeleportToHome(sender, args[0]);
            return true;
        }

        sendMessage(sender, getUsage());
        return true;
    }

    private void TeleportToHome(CommandSender sender) {

        if (!hasPermission(sender, "teleport.home")) {
            return;
        }

        if (!(sender instanceof Player)) {
            sendMessage(sender, "You must be an online player to teleport to your home.");
            return;
        }

        Player player = (Player)sender;
        TeleportToHome(player, player.getUniqueId());
    }

    private void TeleportToHome(CommandSender sender, String targetPlayerName) {

        if (!hasPermission(sender, "teleport.home.other")) {
            return;
        }

        if (!(sender instanceof Player)) {
            sendMessage(sender, "You must be an online player to teleport to a players home.");
            return;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetPlayerName);
        if (!targetPlayer.hasPlayedBefore()) {
            sendMessage(sender, "No player with the name '" + targetPlayerName + "' could be found.");
            return;
        }

        Player player = (Player)sender;
        TeleportToHome(player, targetPlayer.getUniqueId());
    }

    private void TeleportToHome(Player player, UUID targetHomePlayerUuid) {
        Location homeLocation = m_module.getHome(targetHomePlayerUuid);
        if (homeLocation == null) {
            if (player.getUniqueId() == targetHomePlayerUuid) {
                sendMessage(player, "You don't have a home yet.");
                sendMessage(player, "Use /sethome to set your home location.");
            } else {
                sendMessage(player, "No home for that player could be found.");
            }
            return;
        }
        player.teleport(homeLocation);
    }
}
