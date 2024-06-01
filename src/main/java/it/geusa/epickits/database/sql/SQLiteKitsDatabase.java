package it.geusa.epickits.database;

import it.geusa.epickits.EpicKits;
import it.geusa.epickits.models.Kit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class SQLiteKitsDatabase extends SQLKitsDatabase {

    private final File kitsFile;

    private final String fileName;

    public SQLiteKitsDatabase(File kitsFile) {
        super();
        this.kitsFile = kitsFile;
        this.fileName = kitsFile.getName();
    }


    @Override
    public TYPE getType() {
        return TYPE.SQLITE;
    }

    @Override
    public void save(boolean async) throws Exception {
        
    }

    @Override
    protected void openConnection() throws SQLException {

    }
}
