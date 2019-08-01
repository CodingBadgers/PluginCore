package uk.codingbadgers.plugincore.database.databases;

import uk.codingbadgers.plugincore.database.table.DatabaseTable;
import uk.codingbadgers.plugincore.database.table.SQLDatabaseTable;
import uk.codingbadgers.plugincore.database.thread.SQLThread;
import uk.codingbadgers.plugincore.modules.Module;

import java.sql.*;
import java.util.logging.Logger;

public class SQLDatabase extends CoreDatabase {

    public class SQLOptions {
        public String	host;
        public String 	databaseName;
        public String 	username;
        public String 	password;
        public int 		port;
    }

    private SQLOptions m_options = new SQLOptions();

    public SQLDatabase(Logger logger, String name, Module owner, int updateTime) {
        super(logger, name, owner, updateTime);
        m_options.databaseName = name;

        SQLThread thread = new SQLThread(logger);
        thread.setup(name, m_options, updateTime);
        m_thread = thread;
        m_thread.start();
    }

    public boolean login(String host, String user, String password, int port) {
        m_options.host = host;
        m_options.username = user;
        m_options.password = password;
        m_options.port = port;

        m_thread.login(host, user, password, port);

        return getConnection() != null;
    }

    private Connection getConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            m_logger.severe("Could not find sql drivers");
            return null;
        }

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + m_options.host + ":" + m_options.port + "/" +
                            m_options.databaseName + "?" +
                            "user=" + m_options.username +
                            "&password=" + m_options.password
            );

            if (connection == null) {
                m_logger.severe("Could not connect to database '" + m_databaseName + "' using '" + m_options.username + "'@'" + m_options.host + ":" + m_options.port + "' using password '" + (m_options.password.length() == 0 ? "NO" : "YES") + "'");
            }

            return connection;

        } catch (SQLException e) {
            return null;
        }

    }

    public void query(String query, boolean instant) {

        if (!instant) {
            m_thread.query(query);
        } else {
            try {

                final Connection connection = getConnection();
                if (connection == null) {
                    return;
                }

                m_statement = connection.createStatement();
                m_statement.executeUpdate(query);

                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet queryResult(String query) {

        try {
            final Connection connection = getConnection();
            if (connection == null) {
                m_logger.severe("Could not connect to database '" + m_databaseName + "' using '" + m_options.username + "'@'" + m_options.host + ":" + m_options.port + "' using password '" + (m_options.password.length() == 0 ? "NO" : "YES") + "'");
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
            return null;
        }
    }

    public boolean tableExists(String name) {

        try {
            final Connection connection = getConnection();
            if (connection == null) {
                m_logger.severe("Could not connect to database '" + m_databaseName + "' using '" + m_options.username + "'@'" + m_options.host + ":" + m_options.port + "' using password '" + (m_options.password.length() == 0 ? "NO" : "YES") + "'");
                return false;
            }

            final DatabaseMetaData metaData = connection.getMetaData();
            final ResultSet tables = metaData.getTables(null, null, name, null);
            final boolean exists = tables.next();

            tables.close();
            connection.close();

            return exists;

        } catch (SQLException e) {
            return false;
        }

    }

    @Override
    protected void finalize() throws Throwable {

    }

    @Override
    public DatabaseTable createTable(String name, Class<?> layout) {

        DatabaseTable table = new SQLDatabaseTable(m_logger, this, name);

        if (tableExists(name)) {
            return null;
        }

        if (!table.create(layout)) {
            return null;
        }

        return table;
    }

    @Override
    public DatabaseTable openTable(String name) {

        DatabaseTable table = new SQLDatabaseTable(m_logger, this, name);

        if (tableExists(name)) {
            return table;
        }

        return null;
    }

}
