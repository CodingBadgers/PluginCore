package uk.codingbadgers.teleportmodule;

import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.teleportmodule.commands.SpawnCommandHandler;

import java.util.logging.Level;

public class TeleportModule extends Module {
    @Override
    public void onEnable() {
        this.registerCommand(new SpawnCommandHandler(this));

        getLogger().log(Level.INFO, "Enabled " + getDescription().getName());
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Disabled " + getDescription().getName());
    }
}
