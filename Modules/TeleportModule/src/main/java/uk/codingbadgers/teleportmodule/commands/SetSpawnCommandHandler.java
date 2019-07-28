package uk.codingbadgers.teleportmodule.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommand;
import uk.codingbadgers.teleportmodule.TeleportModule;

public class SetSpawnCommandHandler extends ModuleCommand {

    private final TeleportModule m_module;

    public SetSpawnCommandHandler(TeleportModule module) {
        super(module, "setspawn");
        m_module = module;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!hasPermission(sender, "teleport.spawn.set")) {
            return true;
        }

        if (!(sender instanceof Player)) {
            sendMessage(sender, "Only players can set a world spawn");
            return true;
        }

        m_module.setSpawn(((Player)sender).getLocation());
        return true;
    }
}
