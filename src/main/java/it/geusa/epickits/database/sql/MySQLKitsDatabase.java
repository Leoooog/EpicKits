package it.geusa.epickits.database.sql;

import it.geusa.epickits.database.IKitsDatabase;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLKitsDatabase extends SQLKitsDatabase {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    private final boolean useSSL;

    private final boolean verifyServerCertificate;

    private final boolean requireSSL;

    public MySQLKitsDatabase(String host, int port, String database, String username, String password, boolean useSSL, boolean verifyServerCertificate, boolean requireSSL) {
        super();
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.useSSL = useSSL;
        this.verifyServerCertificate = verifyServerCertificate;
        this.requireSSL = requireSSL;
    }

    @Override
    public IKitsDatabase.TYPE getType() {
        return IKitsDatabase.TYPE.MYSQL;
    }
    @Override
    protected void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            Properties properties = new Properties();
            properties.setProperty("user", username);
            properties.setProperty("password", password);
            if (useSSL) {
                properties.setProperty("useSSL", "true");
                properties.setProperty("verifyServerCertificate", Boolean.toString(verifyServerCertificate));
                properties.setProperty("requireSSL", Boolean.toString(requireSSL));
            }
            connection = DriverManager.getConnection(url, properties);
        }
    }

}
