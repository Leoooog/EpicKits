package it.geusa.epickits.managers;

import it.geusa.epickits.EpicKits;
import it.geusa.epickits.database.IKitsDatabase;
import it.geusa.epickits.database.json.JsonKitsDatabase;
import it.geusa.epickits.database.sql.MySQLKitsDatabase;
import it.geusa.epickits.database.sql.SQLiteKitsDatabase;
import it.geusa.epickits.database.yaml.YamlKitsDatabase;
import it.geusa.epickits.models.Kit;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class KitManager {
    private static final EpicKits plugin = EpicKits.getInstance();
    private static final Logger logger = plugin.getLogger();
    private static final ConfigManager configManager = plugin.getConfigManager();
    private final IKitsDatabase database;
    private final boolean asyncSaving;
    private boolean dirty;
    private List<Kit> kits;

    public KitManager(IKitsDatabase database) {
        this.database = database;
        this.asyncSaving = configManager.saveAsync();
        this.kits = null;
        this.dirty = false;
    }

    private static KitManager YAML() {
        return new KitManager(new YamlKitsDatabase(new File(plugin.getDataFolder(), configManager.yamlFileName())));
    }

    private static KitManager JSON() {
        return new KitManager(new JsonKitsDatabase(new File(plugin.getDataFolder(), configManager.jsonFileName())));
    }

    private static KitManager SQLITE() {
        return new KitManager(new SQLiteKitsDatabase(new File(plugin.getDataFolder(), configManager.sqliteFileName())));
    }

    private static KitManager MYSQL() {
        return new KitManager(new MySQLKitsDatabase(configManager.mysqlHost(), configManager.mysqlPort(),
                configManager.mysqlDatabase(), configManager.mysqlUsername(), configManager.mysqlPassword(),
                configManager.mysqlUseSSL(), configManager.mysqlVerifyServerCertificate(),
                configManager.mysqlRequireSSL()));
    }

    public static KitManager fromType(IKitsDatabase.TYPE type) {
        switch (type) {
            case YAML:
                return YAML();
            case JSON:
                return JSON();
            case SQLITE:
                return SQLITE();
            case MYSQL:
                return MYSQL();
            default:
                return null;
        }
    }

    public void load() {
        try {
            database.load();
            kits = database.getKits();
            int size = kits.size();
            logger.info(size + " kit" + (size == 1 ? "" : "s") + " loaded successfully");
        } catch (Exception e) {
            logger.severe("An error occurred while loading the kits");
            e.printStackTrace();
        }
    }

    public boolean save() {
        try {
            if (!Bukkit.getPluginManager().isPluginEnabled(plugin)) {
                logger.warning("The plugin is being disabled, the kits will be saved synchronously");
                database.save(false);
            }
            else {
                database.save(asyncSaving);
            }
        } catch (Exception e) {
            logger.severe("An error occurred while saving the kits");
            e.printStackTrace();
            return false;
        } finally {
            dirty = false;
        }
        return true;
    }

    public boolean createKit(Kit kit) {
        if (!validateKitForCreation(kit)) {
            logger.severe("The kit is not valid for creation");
            return false;
        }
        try {
            kits.add(kit);
            database.createKit(kit);
        } catch (Exception e) {
            logger.severe("An error occurred while creating the kit");
            e.printStackTrace();
            return false;
        } finally {
            if (configManager.autoUpdate()) {
                save();
            }
            else {
                dirty = true;
            }
        }
        return true;
    }

    public boolean deleteKit(String id) {
        try {
            kits.removeIf(kit -> kit.getId().equals(id));
            return database.deleteKit(id);
        } catch (Exception e) {
            logger.severe("An error occurred while deleting the kit");
            e.printStackTrace();
            return false;
        } finally {
            if (configManager.autoUpdate()) {
                save();
            }
            else {
                dirty = true;
            }
        }
    }

    public boolean editKit(Kit kit) {
        try {
            if (!validateKitForEdit(kit)) {
                logger.severe("The kit is not valid for editing");
                return false;
            }
            return database.editKit(kit);
        } catch (Exception e) {
            logger.severe("An error occurred while editing the kit");
            e.printStackTrace();
            return false;
        } finally {
            if (configManager.autoUpdate()) {
                save();
            }
            else {
                dirty = true;
            }
        }
    }

    public Kit getKit(String id) {
        try {
            return kits.stream().filter(kit -> kit.getId().equals(id)).findFirst().orElse(null);
        } catch (Exception e) {
            logger.severe("An error occurred while getting the kit");
            e.printStackTrace();
            return null;
        }
    }

    public List<Kit> getKits() {
        return kits;
    }

    private boolean validateKitForCreation(Kit kit) {
        // Check for unique values: id, permission
        return kits.stream()
                .noneMatch(k -> k.getId().equals(kit.getId()) || k.getPermission().equals(kit.getPermission()))
                && kit.isValid();
    }

    private boolean validateKitForEdit(Kit kit) {
        // Check for unique value: permission
        return kits.stream().filter(k -> k != kit)
                .noneMatch(k -> k.getPermission().equals(kit.getPermission()))
                && kit.isValid();
    }

    public boolean isDirty() {
        return database.getType() != IKitsDatabase.TYPE.SQLITE && database.getType() != IKitsDatabase.TYPE.MYSQL
                && !configManager.autoUpdate() && dirty;
    }

}
