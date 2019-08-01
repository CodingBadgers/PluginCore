package uk.codingbadgers.plugincore.database.table;

import uk.codingbadgers.plugincore.database.databases.SQLDatabase;

import java.util.logging.Logger;

public class SQLDatabaseTable extends DatabaseTable {

    public SQLDatabaseTable(Logger logger, SQLDatabase database, String name) {
        super(logger);
        m_name = name;
        m_database = database;
    }

}