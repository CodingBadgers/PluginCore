package uk.codingbadgers.teleportmodule.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommand;

public class SpawnCommandHandler extends ModuleCommand {

    public SpawnCommandHandler(Module module) {
        super(module, "spawn");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            TeleportToSpawn(sender);
            return true;
        }

        if (args.length == 1) {
            TeleportToSpawn(sender, args[0]);
            return true;
        }

        if (args.length == 2) {
            TeleportToSpawn(sender, args[1], args[2]);
            return true;
        }

        sendMessage(sender, getUsage());
        return true;
    }

    private void TeleportToSpawn(CommandSender sender) {

        if (!hasPermission(sender, "teleport.spawn")) {
            return;
        }

        if (!(sender instanceof Player)) {
            sendMessage(sender, "You must be an online player to teleport to spawn.");
            return;
        }

        Player player = (Player)sender;
        TeleportToSpawn(player, player.getWorld());
    }

    private void TeleportToSpawn(CommandSender sender, String arg) {

        // Check to see if the arg is a world
        World world = Bukkit.getWorld(arg);
        if (world != null) {
            if (!(sender instanceof Player)) {
                sendMessage(sender, "You must be an online player to teleport to spawn.");
                return;
            }

            if (hasPermission(sender, "teleport.spawn.world")) {
                // Teleport to a specified world
                TeleportToSpawn((Player) sender, world);
                return;
            }
        }

        // See if there is an online player we can teleport to
        Player player = Bukkit.getPlayer(arg);
        if (player == null) {
            sendMessage(sender, "No online player or world named " + arg + " exists, teleport failed.");
            return;
        }

        if (hasPermission(sender, "teleport.spawn.other")) {
            TeleportToSpawn(player, player.getWorld());
        }
    }

    private void TeleportToSpawn(CommandSender sender, String playerName, String worldName) {

        if (!(hasPermission(sender, "teleport.spawn.other")
            && hasPermission(sender, "teleport.spawn.world"))) {
            return;
        }

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sendMessage(sender, "No player named " + playerName + " exists or they are offline, teleport failed.");
            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sendMessage(sender, "No world named " + playerName + " exists, teleport failed.");
            return;
        }

        TeleportToSpawn(player, world);
    }

    private void TeleportToSpawn(Player player, World world) {
        player.teleport(world.getSpawnLocation());
        sendMessage(player, "You have been teleported to the spawn of " + world.getName());
    }

}
