package uk.codingbadgers.teleportmodule;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import uk.codingbadgers.plugincore.database.DatabaseManager;
import uk.codingbadgers.plugincore.database.databases.CoreDatabase;
import uk.codingbadgers.plugincore.database.table.DatabaseTable;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.teleportmodule.commands.HomeCommandHandler;
import uk.codingbadgers.teleportmodule.commands.SetHomeCommandHandler;
import uk.codingbadgers.teleportmodule.commands.SetSpawnCommandHandler;
import uk.codingbadgers.teleportmodule.commands.SpawnCommandHandler;
import uk.codingbadgers.teleportmodule.data.PlayerHomeData;
import uk.codingbadgers.teleportmodule.data.WorldSpawnData;

import java.sql.ResultSet;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class TeleportModule extends Module {

    private static final String SpawnsTableName = "spawns";
    private static final String HomesTableName = "homes";

    private CoreDatabase m_database;
    private DatabaseTable m_spawnsTable;
    private DatabaseTable m_homesTable;

    @Override
    public void onEnable() {
        m_database = openDatabaseConnection(DatabaseManager.DatabaseType.SQLite);

        setupSpawnsTable();
        setupHomesTable();
        registerAllCommands();

        getLogger().log(Level.INFO, "Enabled " + getDescription().getName());
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Disabled " + getDescription().getName());
    }

    private void registerAllCommands() {
        registerCommand(new SpawnCommandHandler(this));
        registerCommand(new SetSpawnCommandHandler(this));
        registerCommand(new HomeCommandHandler(this));
        registerCommand(new SetHomeCommandHandler(this));
    }

    private void setupSpawnsTable() {
        if (!m_database.tableExists(SpawnsTableName)) {
            m_spawnsTable = m_database.createTable(SpawnsTableName, WorldSpawnData.class);
            return;
        }
        m_spawnsTable = m_database.openTable(SpawnsTableName);
    }

    private void setupHomesTable() {
        if (!m_database.tableExists(HomesTableName)) {
            m_homesTable = m_database.createTable(HomesTableName, PlayerHomeData.class);
            return;
        }
        m_homesTable = m_database.openTable(HomesTableName);
    }

    private int hoursBetween(Date a, Date b) {
        final int MilliSecondsInAnHour = 1000 * 60 * 60;
        return (int) (a.getTime() - b.getTime()) / MilliSecondsInAnHour;
    }

    public void setSpawn(Location spawnLocation) {
        WorldSpawnData data = WorldSpawnData.create(spawnLocation);
        m_spawnsTable.update(data, WorldSpawnData.class, "worldName='" + data.worldName + "'",false);
    }

    public Location getSpawn(World world) {
        ResultSet result = m_spawnsTable.selectAll("worldName='" + world.getName() + "'");
        Location location = WorldSpawnData.toLocation(result);
        if (location != null)
        {
            return location;
        }

        return world.getSpawnLocation();
    }

    public boolean setHome(UUID playerUuid, Location spawnLocation, boolean force) {

        if (!force) {
            // Try get an existing home
            ResultSet result = m_homesTable.selectAll("playerUuid='" + playerUuid.toString() + "'");
            PlayerHomeData existing = PlayerHomeData.fromResult(result);
            if (existing != null) {
                Date now = new Date();
                Date lastUpdated = new Date(existing.lastUpdateMs);
                if (hoursBetween(now, lastUpdated) < 24) {
                    return false;
                }
            }
        }

        PlayerHomeData data = PlayerHomeData.create(playerUuid, spawnLocation);
        m_homesTable.update(data, PlayerHomeData.class, "playerUuid='" + playerUuid.toString() + "'",false);
        return true;
    }

    public Location getHome(UUID playerUuid) {
        ResultSet result = m_homesTable.selectAll("playerUuid='" + playerUuid.toString() + "'");
        PlayerHomeData data = PlayerHomeData.fromResult(result);
        if (data != null)
        {
            return data.toLocation();
        }

        return null;
    }
}
