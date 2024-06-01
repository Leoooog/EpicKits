package it.geusa.epickits.database.sql;

import it.geusa.epickits.Utils;
import it.geusa.epickits.database.IKitsDatabase;
import it.geusa.epickits.models.Kit;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.logging.Logger;

public abstract class SQLKitsDatabase implements IKitsDatabase {
    protected final Logger logger = Logger.getLogger(getClass().getName());
    protected Connection connection;

    public SQLKitsDatabase() {
        this.connection = null;
    }

    @NotNull
    private static Map<String, String> getSchema() {
        Map<String, String> columns = new HashMap<>();
        columns.put("id", "varchar(255)");
        columns.put("displayName", "varchar(255)");
        columns.put("permission", "varchar(255)");
        columns.put("description", "text");
        columns.put("cooldown", "int");
        columns.put("oneTime", "tinyint(1)");
        columns.put("cost", "double");
        columns.put("enabled", "tinyint(1)");
        columns.put("clearInventory", "tinyint(1)");
        columns.put("clearArmor", "tinyint(1)");
        columns.put("autoArmor", "tinyint(1)");
        columns.put("autoShield", "tinyint(1)");
        columns.put("autoElytra", "tinyint(1)");
        columns.put("items", "text");
        columns.put("icon", "text");
        columns.put("lastModified", "timestamp");
        return columns;
    }

    protected abstract void openConnection() throws SQLException;

    @Override
    public void save(boolean async) {
        // There is no need to save the data to the SQL database
    }

    @Override
    public void load() throws SQLException {
        openConnection();
        if (!checkSchema()) {
            logger.warning("The kits table is not correctly configured, creating a new one...");
            createSchema();
        }
        logger.info("The connection to the " + getType().toString() + " database has been established");
    }

    private void createSchema() throws SQLException {
        Map<String, String> schema = getSchema();
        StringBuilder query = new StringBuilder("CREATE TABLE kits (");
        for (Map.Entry<String, String> entry : schema.entrySet()) {
            query.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }
        query.append("PRIMARY KEY (id))");
        PreparedStatement statement = connection.prepareStatement(query.toString());
        if (statement.executeUpdate() == 0) {
            logger.info("The kits table has been created successfully");
        }
        else {
            logger.warning("An error occurred while creating the kits table");
        }

    }

    private boolean checkSchema() throws SQLException {
        String query = "SHOW COLUMNS FROM kits";
        try (PreparedStatement statement = connection.prepareStatement(
                query); ResultSet resultSet = statement.executeQuery()) {
            Map<String, String> columns = getSchema();
            while (resultSet.next()) {
                String columnName = resultSet.getString("Field");
                String columnType = resultSet.getString("Type");
                if (columns.containsKey(columnName)) {
                    String expectedType = columns.get(columnName);
                    if (!columnType.toLowerCase().startsWith(expectedType)) return false;
                    columns.remove(columnName);
                }
            }

            return columns.isEmpty();
        }
    }


    @Override
    public boolean isLoaded() {
        return connection != null;
    }

    @Override
    public void reload() throws SQLException {
        logger.info("Reloading the " + getType() + " database connection...");
        close();
        load();
        logger.info("The " + getType() + " database connection has been reloaded successfully");
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        connection = null;
        logger.info("The connection to the " + getType() + " database has been closed");
    }

    @Override
    public boolean createKit(Kit kit) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO kits (id, displayName, permission, description, cooldown, oneTime, cost, enabled, "
                        + "clearInventory, clearArmor, autoArmor, autoShield, autoElytra, items, icon, lastModified) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        statement.setString(1, kit.getId());
        statement.setString(2, kit.getDisplayName());
        statement.setString(3, kit.getPermission());
        statement.setString(4, kit.getDescription());
        statement.setInt(5, kit.getCooldown());
        statement.setBoolean(6, kit.isOneTime());
        statement.setDouble(7, kit.getCost());
        statement.setBoolean(8, kit.isEnabled());
        statement.setBoolean(9, kit.isClearInventory());
        statement.setBoolean(10, kit.isClearArmor());
        statement.setBoolean(11, kit.isAutoArmor());
        statement.setBoolean(12, kit.isAutoShield());
        statement.setBoolean(13, kit.isAutoElytra());
        statement.setString(14, kit.getSerializedItems());
        statement.setString(15, kit.getSerializedIcon());
        statement.setTimestamp(16, new Timestamp(System.currentTimeMillis()));
        return statement.executeUpdate() > 0;
    }

    @Override
    public boolean deleteKit(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM kits WHERE id = ?");
        statement.setString(1, id);
        return statement.executeUpdate() > 0;
    }

    @Override
    public Kit getKit(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM kits WHERE id = ?");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return getKitFromResultSet(resultSet);
        }
        return null;
    }

    @Override
    public List<Kit> getKits() throws Exception {
        List<Kit> kits = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM kits");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            kits.add(getKitFromResultSet(resultSet));
        }
        return kits;
    }

    @Override
    public boolean editKit(Kit kit) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE kits SET displayName = ?, permission = ?, description = ?, cooldown = ?, oneTime = ?, free = ?, "
                        + "cost = ?, enabled = ?, clearInventory = ?, clearArmor = ?, autoArmor = ?, autoShield = ?, "
                        + "autoElytra = ?, items = ?, icon = ?, lastModified = ? WHERE id = ?");
        statement.setString(1, kit.getDisplayName());
        statement.setString(2, kit.getPermission());
        statement.setString(3, kit.getDescription());
        statement.setInt(4, kit.getCooldown());
        statement.setBoolean(5, kit.isOneTime());
        statement.setDouble(6, kit.getCost());
        statement.setBoolean(7, kit.isEnabled());
        statement.setBoolean(8, kit.isClearInventory());
        statement.setBoolean(9, kit.isClearArmor());
        statement.setBoolean(10, kit.isAutoArmor());
        statement.setBoolean(11, kit.isAutoShield());
        statement.setBoolean(12, kit.isAutoElytra());
        statement.setString(13, kit.getSerializedItems());
        statement.setString(14, kit.getSerializedIcon());
        Timestamp lastModified = new Timestamp(System.currentTimeMillis());
        statement.setTimestamp(15, lastModified);
        statement.setString(15, kit.getId());
        if (statement.executeUpdate() > 0) {
            kit.setLastModified(lastModified);
            return true;
        }
        return false;
    }

    private Kit getKitFromResultSet(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String displayName = resultSet.getString("displayName");
        String permission = resultSet.getString("permission");
        String description = resultSet.getString("description");
        int cooldown = resultSet.getInt("cooldown");
        boolean oneTime = resultSet.getBoolean("oneTime");
        double cost = resultSet.getDouble("cost");
        boolean enabled = resultSet.getBoolean("enabled");
        boolean clearInventory = resultSet.getBoolean("clearInventory");
        boolean clearArmor = resultSet.getBoolean("clearArmor");
        boolean autoArmor = resultSet.getBoolean("autoArmor");
        boolean autoShield = resultSet.getBoolean("autoShield");
        boolean autoElytra = resultSet.getBoolean("autoElytra");
        String serializedItems = resultSet.getString("items");
        String serializedIcon = resultSet.getString("icon");
        Date lastModified = resultSet.getTimestamp("lastModified");
        Kit kit = new Kit(id, displayName, permission, description, cooldown, oneTime, cost, enabled, clearInventory,
                clearArmor, autoArmor, autoShield, autoElytra, Utils.deserializeItems(serializedItems),
                Objects.requireNonNull(Utils.deserializeItemStack(serializedIcon)), lastModified);
        if (kit.isValid()) return kit;
        return null;
    }
}
