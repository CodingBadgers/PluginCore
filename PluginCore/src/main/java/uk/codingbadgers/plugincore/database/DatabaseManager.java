package uk.codingbadgers.plugincore.database;

import uk.codingbadgers.plugincore.database.databases.CoreDatabase;
import uk.codingbadgers.plugincore.database.databases.SQLDatabase;
import uk.codingbadgers.plugincore.database.databases.SQLiteDatabase;
import uk.codingbadgers.plugincore.modules.Module;

import java.util.ArrayList;
import java.util.logging.Logger;

public class DatabaseManager {

    public enum DatabaseType {
        SQLite,
        SQL
    }

    private Logger m_logger;
    private final ArrayList<CoreDatabase> m_databases = new ArrayList<CoreDatabase>();

    public DatabaseManager(Logger logger) {
        m_logger = logger;
    }

    public CoreDatabase createDatabase(String name, Module owner, DatabaseType type) {
        return createDatabase(name, owner, type, 20);
    }

    public CoreDatabase createDatabase(String name, Module owner, DatabaseType type, int updateTime) {

        CoreDatabase database = null;

        switch (type) {
            case SQLite:
                database = new SQLiteDatabase(m_logger, name, owner, updateTime);
                m_logger.info("The plugin '" + owner.getName() + "' registered an SQLite database called '" + name + "'");
                break;

            case SQL:
                database = new SQLDatabase(m_logger, name, owner, updateTime);
                m_logger.info("The plugin '" + owner.getName() + "' registered an SQL database called '" + name + "'");
                break;

            default:
                m_logger.severe("The plugin '" + owner.getName() + "' tried to create a database of type '" + type.name() + "' which is not yet supported.");
                return null;
        }

        m_databases.add(database);
        return database;
    }

    public CoreDatabase findDatabase(String name, String pluginName) {

        for (CoreDatabase database : m_databases) {
            if (name.equalsIgnoreCase(database.getName())) {
                if (pluginName.equalsIgnoreCase(database.getOwnerName())) {
                    return database;
                }
            }
        }

        return null;
    }
}
