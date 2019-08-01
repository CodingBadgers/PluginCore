package uk.codingbadgers.plugincore.database.databases;

import uk.codingbadgers.plugincore.database.DatabaseManager;
import uk.codingbadgers.plugincore.database.table.DatabaseTable;
import uk.codingbadgers.plugincore.database.thread.DatabaseThread;
import uk.codingbadgers.plugincore.modules.Module;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public abstract class CoreDatabase {

    protected final Logger m_logger;
    protected DatabaseThread m_thread = null;
    protected Module m_module = null;
    protected Statement m_statement = null;
    protected String m_databaseName = null;
    protected int m_updateTime = 20;

    public CoreDatabase(Logger logger, String name, Module owner, int updateTime) {
        m_logger = logger;
        m_module = owner;
        m_databaseName = name;
        m_updateTime = updateTime;
    }

    protected abstract void finalize() throws Throwable;
    public abstract boolean login(String host, String user, String password, int port);
    public abstract void query(String query, boolean instant);
    public abstract ResultSet queryResult(String query);
    public abstract boolean tableExists(String name);
    public abstract DatabaseTable createTable(String name, Class<?> layout);
    public abstract DatabaseTable openTable(String name);

    public void query(String query) {
        query(query, false);
    }

    public void freeResult(ResultSet result) {

        try {

            if (result != null) {
                result.close();
                result = null;
            }

            if (m_statement != null) {
                Connection connection = m_statement.getConnection();
                m_statement.close();
                connection.close();
                m_statement = null;
                connection = null;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

    }

    public void freeDatabase() {

        try {
            if (m_thread != null) {
                m_thread.kill();
                m_thread = null;
            }

            if (m_statement != null) {
                m_statement.close();
                m_statement = null;
            }
        } catch (SQLException e) {}

    }

    public String getName() {
        return m_databaseName;
    }

    public String getOwnerName() {
        return m_module.getName();
    }
}
