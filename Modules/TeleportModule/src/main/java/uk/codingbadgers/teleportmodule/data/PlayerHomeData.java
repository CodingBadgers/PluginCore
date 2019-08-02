package uk.codingbadgers.teleportmodule.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import uk.codingbadgers.plugincore.database.table.IDatabaseTableData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerHomeData extends IDatabaseTableData {

    public static PlayerHomeData create(UUID playerUuid, Location location) {
        PlayerHomeData data = new PlayerHomeData();
        data.playerUuid = playerUuid.toString();
        data.worldName = location.getWorld().getName();
        data.x = location.getX();
        data.y = location.getY();
        data.z = location.getZ();
        data.pitch = location.getPitch();
        data.yaw = location.getYaw();
        data.lastUpdateMs = System.currentTimeMillis();
        return data;
    }

    public static PlayerHomeData fromResult(ResultSet resultSet) {
        try {

            if (resultSet == null) {
                return null;
            }

            while (resultSet.next()) {

                String worldName = resultSet.getString("worldName");
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    return null;
                }

                PlayerHomeData data = new PlayerHomeData();

                data.playerUuid = resultSet.getString("playerUuid");
                data.worldName = worldName;
                data.x = resultSet.getDouble("x");
                data.y = resultSet.getDouble("y");
                data.z = resultSet.getDouble("z");
                data.pitch = resultSet.getFloat("pitch");
                data.yaw = resultSet.getFloat("yaw");
                data.lastUpdateMs = resultSet.getLong("lastUpdateMs");

                return data;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    public String playerUuid;
    public String worldName;
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;
    public long lastUpdateMs;
}
