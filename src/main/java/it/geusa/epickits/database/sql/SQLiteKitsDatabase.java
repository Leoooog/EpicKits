package it.geusa.epickits.database.sql;

import it.geusa.epickits.database.IKitsDatabase;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteKitsDatabase extends SQLKitsDatabase {

    private final File kitsFile;

    public SQLiteKitsDatabase(File kitsFile) {
        super();
        this.kitsFile = kitsFile;
    }


    @Override
    public IKitsDatabase.TYPE getType() {
        return IKitsDatabase.TYPE.SQLITE;
    }

    @Override
    protected void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:sqlite:" + kitsFile.getAbsolutePath());
        }
    }
}
