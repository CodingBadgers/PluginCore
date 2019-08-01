package uk.codingbadgers.plugincore.database.databases;

import uk.codingbadgers.plugincore.database.table.DatabaseTable;
import uk.codingbadgers.plugincore.database.table.SQLiteDatabaseTable;
import uk.codingbadgers.plugincore.database.thread.SQLiteThread;
import uk.codingbadgers.plugincore.modules.Module;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;

public class SQLiteDatabase extends CoreDatabase {

    private File m_databaseFile = null;

    public SQLiteDatabase(Logger logger, String name, Module owner, int updateTime) {
        super(logger, name, owner, updateTime);

        if (!createDatabase()) {
            logger.severe("Could not create database '" + m_databaseName + "'");
            return;
        }

        SQLiteThread thread = new SQLiteThread(logger);
        thread.setup(m_databaseFile, updateTime);
        thread.start();

        m_thread = thread;
    }

    private boolean createDatabase() {

        m_logger.info("Loading " + m_module.getDataFolder() + File.separator + m_databaseName + ".sqlite");

        m_databaseFile = new File(m_module.getDataFolder() + File.separator + m_databaseName + ".sqlite");

        if (m_databaseFile.exists()) {
            return true;
        }

        if (!m_module.getDataFolder().exists()) {
            m_module.getDataFolder().mkdir();
        }

        try {
            if (!m_databaseFile.createNewFile()) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private Connection getConnection() {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            m_logger.severe("Could not find sqlite drivers");
            return null;
        }

        try {
            return DriverManager.getConnection("jdbc:sqlite:" + m_databaseFile.getAbsolutePath());
        } catch (SQLException e) {
            return null;
        }

    }

    public void query(String query, boolean instant) {

        if (!instant) {
            m_thread.query(query);
        } else {

            final Connection connection = getConnection();
            try {
                if (connection == null) {
                    m_logger.severe("Could not connect to database '" + m_databaseName + "'");
                    return;
                }

                m_statement = connection.createStatement();
                m_statement.executeUpdate(query);

                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    m_statement.close();
                    connection.close();
                } catch (SQLException ce) {}
            }
        }
    }

    public ResultSet queryResult(String query) {

        final Connection connection = getConnection();
        try {

            if (connection == null) {
                m_logger.severe("Could not connect to database '" + m_databaseName + "'");
                return null;
            }

            if (m_statement != null) {
                m_statement.close();
                m_statement = null;
            }

            m_statement = connection.createStatement();
            ResultSet result = m_statement.executeQuery(query);

            if (result == null) {
                m_statement.close();
                m_statement = null;
                connection.close();
            }

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                m_statement.close();
                connection.close();
            } catch (SQLException ce) {}
        }

        return null;
    }

    public boolean tableExists(String name) {

        final Connection connection = getConnection();
        ResultSet tables = null;

        try {

            if (connection == null) {
                m_logger.severe("Could not connect to database '" + m_databaseName + "'");
                return false;
            }

            final DatabaseMetaData metaData = connection.getMetaData();
            tables = metaData.getTables(null, null, name, null);
            final boolean exists = tables.next();

            tables.close();
            connection.close();

            return exists;

        } catch (SQLException e) {
            try {
                tables.close();
                connection.close();
            } catch (SQLException ce) {
            }
            return false;
        }

    }

    @Override
    protected void finalize() throws Throwable {

    }

    @Override
    public boolean login(String host, String user, String password, int port) {
        return true;
    }

    @Override
    public DatabaseTable createTable(String name, Class<?> layout) {

        DatabaseTable table = new SQLiteDatabaseTable(m_logger,this, name);

        if (tableExists(name)) {
            return table;
        }

        if (!table.create(layout)) {
            return null;
        }

        return table;
    }

}
