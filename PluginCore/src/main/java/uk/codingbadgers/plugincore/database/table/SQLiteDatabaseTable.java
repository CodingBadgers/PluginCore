package uk.codingbadgers.plugincore.database.table;

import uk.codingbadgers.plugincore.database.databases.SQLiteDatabase;

import java.util.logging.Logger;

public class SQLiteDatabaseTable extends DatabaseTable {

    public SQLiteDatabaseTable(Logger logger, SQLiteDatabase database, String name) {
        super(logger);
        m_name = name;
        m_database = database;
    }

}
