package it.geusa.epickits.config;

import it.geusa.epickits.EpicKits;
import it.geusa.epickits.database.IKitsDatabase;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public class ConfigManager {
    private final EpicKits plugin = EpicKits.getInstance();
    private final Logger logger = plugin.getLogger();

    public ConfigManager() {
        // Load the configuration files
        loadConfig();
    }

    public void loadConfig() {
        // Load the config.yml file
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        logger.info("The configuration files have been loaded successfully");
        logger.info("The storage type is " + databaseType());
    }

    public void saveConfig() {
        if (saveAsync())
            new BukkitRunnable() {
                @Override
                public void run() {
                    saveConfig();
                    logger.info("The configuration files have been saved successfully");
                }
            }.runTaskAsynchronously(plugin);
        else {
            plugin.saveConfig();
            logger.info("The configuration files have been saved successfully");
        }
    }

    public IKitsDatabase.TYPE databaseType() {
        return IKitsDatabase.TYPE.valueOf(plugin.getConfig().getString("storage-type").toUpperCase());
    }

    public boolean saveAsync() {
        return plugin.getConfig().getBoolean("storage.save-async");
    }

    public String yamlFileName() {
        return plugin.getConfig().getString("storage.yaml.file-name");
    }

    public String sqliteFileName() {
        return plugin.getConfig().getString("storage.sqlite.file-name");
    }

    public String jsonFileName() {
        return plugin.getConfig().getString("storage.json.file-name");
    }

    public String mysqlHost() {
        return plugin.getConfig().getString("storage.mysql.host");
    }

    public int mysqlPort() {
        return plugin.getConfig().getInt("storage.mysql.port");
    }

    public String mysqlDatabase() {
        return plugin.getConfig().getString("storage.mysql.database");
    }

    public String mysqlUsername() {
        return plugin.getConfig().getString("storage.mysql.username");
    }

    public String mysqlPassword() {
        return plugin.getConfig().getString("storage.mysql.password");
    }

    public boolean mysqlUseSSL() {
        return plugin.getConfig().getBoolean("storage.mysql.use-ssl");
    }

    public boolean mysqlVerifyServerCertificate() {
        return plugin.getConfig().getBoolean("storage.mysql.verify-server-certificate");
    }

    public boolean mysqlRequireSSL() {
        return plugin.getConfig().getBoolean("storage.mysql.require-ssl");
    }

    public boolean autoUpdate() {
        return plugin.getConfig().getBoolean("auto-update");
    }

    public boolean saveOnDisable() {
        return plugin.getConfig().getBoolean("save-on-disable");
    }

    public int secondsToConfirmCommand() {
        return plugin.getConfig().getInt("seconds-to-confirm-command");
    }
}
