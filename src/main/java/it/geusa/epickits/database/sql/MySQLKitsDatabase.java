package it.geusa.epickits.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLKitsDatabase extends SQLKitsDatabase {
    private final String url;
    private final String username;
    private final String password;

    public MySQLKitsDatabase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public TYPE getType() {
        return TYPE.MYSQL;
    }

    @Override
    public void save(boolean async) {
        // There is no need to save the data to the MySQL database
    }

    @Override
    protected void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
        }
    }

}
