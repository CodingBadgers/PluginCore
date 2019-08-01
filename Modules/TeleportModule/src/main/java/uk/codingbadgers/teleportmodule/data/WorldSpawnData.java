package uk.codingbadgers.teleportmodule.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import uk.codingbadgers.plugincore.database.table.IDatabaseTableData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorldSpawnData extends IDatabaseTableData {
    public static WorldSpawnData FromLocation(Location location) {
        WorldSpawnData data = new WorldSpawnData();
        data.worldName = location.getWorld().getName();
        data.x = location.getX();
        data.y = location.getY();
        data.z = location.getZ();
        data.pitch = location.getPitch();
        data.yaw = location.getYaw();
        return data;
    }

    public static Location ToLocation(ResultSet resultSet) {
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

                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");
                float pitch = resultSet.getFloat("pitch");
                float yaw = resultSet.getFloat("yaw");

                return new Location(world, x, y, z, yaw, pitch);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String worldName;
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;
}
