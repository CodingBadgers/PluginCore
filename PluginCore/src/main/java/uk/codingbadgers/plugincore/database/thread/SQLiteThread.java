package uk.codingbadgers.plugincore.database.thread;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Logger;

public class SQLiteThread extends DatabaseThread {

    private File m_databaseFile = null;

    public SQLiteThread(Logger logger) {
        super(logger);
    }

    public void setup(File database, int updateTime) {
        m_databaseFile = database;
        super.setup(updateTime);
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

    protected synchronized void executeQueries() {

        if (!m_queries.isEmpty())
        {
            String query = null;
            Statement statement = null;

            final Connection connection = getConnection();
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

    @Override
    public void login(String host, String dbname, String user, int port) {}

}