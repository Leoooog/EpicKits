package it.geusa.epickits.database.yaml;

import it.geusa.epickits.EpicKits;
import it.geusa.epickits.database.IKitsDatabase;
import it.geusa.epickits.models.Kit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class YamlKitsDatabase implements IKitsDatabase {
    private final File kitsFile;

    private final String fileName;

    private final Logger logger = EpicKits.getInstance().getLogger();
    private FileConfiguration kitsConfig;

    public YamlKitsDatabase(File kitsFile) {
        this.kitsFile = kitsFile;
        this.fileName = kitsFile.getName();
        this.kitsConfig = null;
    }

    @Override
    public TYPE getType() {
        return TYPE.YAML;
    }

    @Override
    public void load() throws IllegalArgumentException {
        kitsConfig = YamlConfiguration.loadConfiguration(kitsFile);
        if (!kitsFile.exists()) {
            try {
                kitsFile.createNewFile();
            } catch (IOException e) {
                logger.severe("An error occurred while creating the " + fileName + " file");
                throw new IllegalArgumentException("An error occurred while creating the " + fileName + " file");
            } finally {
                logger.info("The " + fileName + " file has been created successfully");
            }
        }
        logger.info("The " + fileName + " file has been loaded successfully");
    }

    @Override
    public void save(boolean async) throws IOException {
        logger.info("Saving the " + fileName + " file...");
        if (async) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        kitsConfig.save(kitsFile);
                        logger.info("The " + fileName + " file has been saved successfully");
                    } catch (IOException e) {
                        logger.severe("An error occurred while saving the " + fileName + " file");
                    }
                }
            }.runTaskAsynchronously(EpicKits.getInstance());
        }
        else {
            kitsConfig.save(kitsFile);
            logger.info("The " + fileName + " file has been saved successfully");
        }
    }

    @Override
    public boolean isLoaded() {
        return kitsConfig != null;
    }

    @Override
    public void reload() throws IOException, InvalidConfigurationException {
        logger.info("Reloading the " + fileName + " file...");
        kitsConfig.load(kitsFile);
        logger.info("The " + fileName + " file has been reloaded successfully");
    }

    @Override
    public void close() {
        kitsConfig = null;
        logger.info("The " + fileName + " file has been closed successfully");
    }

    @Override
    public boolean createKit(Kit kit) {
        if (getKit(kit.getId()) == null) {
            String sectionKey = "kits." + kit.getId();
            kitsConfig.set(sectionKey, kit);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteKit(String id) {
        if (getKit(id) != null) {
            kitsConfig.set("kits." + id, null);
            return true;
        }
        return false;
    }

    @Override
    public Kit getKit(String id) {
        return (Kit) kitsConfig.get("kits." + id);
    }

    @Override
    public List<Kit> getKits() {
        ConfigurationSection kitsSection = kitsConfig.getConfigurationSection("kits");
        if (kitsSection != null) {
            List<Kit> kits = new ArrayList<>();
            for (String key : kitsSection.getKeys(false)) {
                kits.add((Kit) kitsSection.get(key));
            }
            return kits;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean editKit(Kit kit) {
        if (getKit(kit.getId()) != null) {
            kit.setLastModified(new Date());
            String sectionKey = "kits." + kit.getId();
            kitsConfig.set(sectionKey, kit);
            return true;
        }
        return false;
    }
}
