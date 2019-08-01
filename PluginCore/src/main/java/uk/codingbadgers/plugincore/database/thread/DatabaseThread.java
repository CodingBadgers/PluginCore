package uk.codingbadgers.plugincore.database.thread;

import java.util.ArrayList;
import java.util.logging.Logger;

public abstract class DatabaseThread extends Thread {

    protected final Logger m_logger;
    protected ArrayList<String> m_queries = new ArrayList<String>();
    protected boolean m_running = false;
    protected int m_updateTime = 20;


    public DatabaseThread(Logger logger) {
        m_logger = logger;
    }

    public abstract void login(String host, String dbname, String user, int port);
    protected abstract void executeQueries();

    public void kill() {
        m_running = false;
    }

    public void setup(int updateTime) {
        m_running = true;
        m_updateTime = updateTime;
    }

    public synchronized void query(String query) {
        m_queries.add(query);
    }

    public void run () {

        m_running = true;

        while (m_running) {
            synchronized(this) {
                executeQueries();
            }

            try {
                Thread.sleep(m_updateTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized(this) {
            executeQueries();
        }
    }
}
