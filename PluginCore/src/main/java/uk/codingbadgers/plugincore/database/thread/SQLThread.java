package uk.codingbadgers.plugincore.database.thread;

import uk.codingbadgers.plugincore.database.databases.SQLDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Logger;

public class SQLThread extends DatabaseThread {

    SQLDatabase.SQLOptions m_options = null;;

    public SQLThread(Logger logger) {
        super(logger);
    }

    public void setup(String database, SQLDatabase.SQLOptions options, int updateTime) {
        super.setup(updateTime);
        m_options = options;
        m_options.databaseName = database;
    }

    public void login(String host, String user, String password, int port) {
        m_options.host = host;
        m_options.username = user;
        m_options.password = password;
        m_options.port = port;
    }

    private Connection getConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            m_logger.severe("Could not find sql drivers");
            return null;
        }

        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://" + m_options.host + ":" + m_options.port + "/" +
                            m_options.databaseName + "?" +
                            "user=" + m_options.username +
                            "&password=" + m_options.password
            );
        } catch (SQLException e) {
            return null;
        }

    }

    protected synchronized void executeQueries() {

        if (!m_queries.isEmpty())
        {
            String query = null;
            Statement statement = null;

            Connection connection = getConnection();
            try {
                if (connection == null) {
                    m_logger.severe("Could not connect to database.");
                    return;
                }

                statement = connection.createStatement();

                Iterator<String> queryItr = m_queries.iterator();
                while (queryItr.hasNext()) {
                    query = queryItr.next();
                    statement.addBatch(query);
                }

                statement.executeBatch();

                statement.close();
                connection.close();
                m_queries.clear();
            } catch (SQLException e) {
                final String errorMessage = e.getMessage().toLowerCase();
                if (!(errorMessage.contains("locking") || errorMessage.contains("locked"))) {
                    m_logger.severe("Batch Exception: Exception whilst executing");
                    m_logger.severe(query);
                    e.printStackTrace();
                    m_queries.remove(query);
                }
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException ce) {}
            }
        }
    }

}
