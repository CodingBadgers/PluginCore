package uk.codingbadgers.teleportmodule;

import org.bukkit.Location;
import org.bukkit.World;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.teleportmodule.commands.SetSpawnCommandHandler;
import uk.codingbadgers.teleportmodule.commands.SpawnCommandHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class TeleportModule extends Module {

    private final Map<String, Location> m_worldSpawns = new HashMap<String, Location>();

    @Override
    public void onEnable() {
        this.registerCommand(new SpawnCommandHandler(this));
        this.registerCommand(new SetSpawnCommandHandler(this));

        getLogger().log(Level.INFO, "Enabled " + getDescription().getName());
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Disabled " + getDescription().getName());
    }


    public void setSpawn(Location spawnLocation) {

        String worldName = spawnLocation.getWorld().getName();
        if (m_worldSpawns.containsKey(worldName)) {
            m_worldSpawns.replace(worldName, spawnLocation);
        } else {
            m_worldSpawns.put(worldName, spawnLocation);
        }

    }

    public Location getSpawn(World world) {

        String worldName = world.getName();
        if (m_worldSpawns.containsKey(worldName)) {
            return m_worldSpawns.get(worldName);
        }

        return world.getSpawnLocation();
    }
}
