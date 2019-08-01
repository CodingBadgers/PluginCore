package uk.codingbadgers.teleportmodule;

import org.bukkit.Location;
import org.bukkit.World;
import uk.codingbadgers.plugincore.database.DatabaseManager;
import uk.codingbadgers.plugincore.database.databases.CoreDatabase;
import uk.codingbadgers.plugincore.database.table.DatabaseTable;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.teleportmodule.commands.SetSpawnCommandHandler;
import uk.codingbadgers.teleportmodule.commands.SpawnCommandHandler;
import uk.codingbadgers.teleportmodule.data.WorldSpawnData;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class TeleportModule extends Module {

    private CoreDatabase m_database;
    private DatabaseTable m_spawnsTable;

    @Override
    public void onEnable() {
        this.registerCommand(new SpawnCommandHandler(this));
        this.registerCommand(new SetSpawnCommandHandler(this));

        m_database = OpenDatabaseConnection(DatabaseManager.DatabaseType.SQLite);
        setupSpawnsTable();

        getLogger().log(Level.INFO, "Enabled " + getDescription().getName());
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Disabled " + getDescription().getName());
    }

    private void setupSpawnsTable() {
        if (!m_database.tableExists("spawns")) {
            m_spawnsTable = m_database.createTable("spawns", WorldSpawnData.class);
            return;
        }
        m_spawnsTable = m_database.openTable("spawns");
    }

    public void setSpawn(Location spawnLocation) {
        WorldSpawnData data = WorldSpawnData.FromLocation(spawnLocation);
        m_spawnsTable.update(data, WorldSpawnData.class, "worldName='" + data.worldName + "'",false);
    }

    public Location getSpawn(World world) {

        ResultSet result = m_spawnsTable.selectAll("worldName='" + world.getName() + "'");
        Location location = WorldSpawnData.ToLocation(result);
        if (location != null)
        {
            return location;
        }

        return world.getSpawnLocation();
    }
}
