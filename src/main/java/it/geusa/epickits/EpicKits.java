package it.geusa.epickits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.geusa.epickits.commands.EpicKitsCommand;
import it.geusa.epickits.commands.KitCommand;
import it.geusa.epickits.commands.KitsCommand;
import it.geusa.epickits.database.json.ConfigurationSerializableAdapter;
import it.geusa.epickits.database.json.ItemStackAdapter;
import it.geusa.epickits.events.InventoryListener;
import it.geusa.epickits.managers.ConfigManager;
import it.geusa.epickits.managers.InventoryManager;
import it.geusa.epickits.managers.KitManager;
import it.geusa.epickits.managers.KitPlayerManager;
import it.geusa.epickits.models.Kit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class EpicKits extends JavaPlugin {
    private static EpicKits instance;
    private Gson gson;
    private ConfigManager configManager;
    private KitManager kitManager;

    private InventoryManager inventoryManager;

    private KitPlayerManager kitPlayerManager;

    public static EpicKits getInstance() {
        return instance;
    }

    public Gson getGson() {
        return gson;
    }

    @Override
    public void onEnable() {
        instance = this;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .setObjectToNumberStrategy(JsonReader::nextInt)
                .registerTypeHierarchyAdapter(ConfigurationSerializable.class, new ConfigurationSerializableAdapter())
                .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
                .create();
        ConfigurationSerialization.registerClass(Kit.class);
        loadManagers();
        registerCommands();
        registerEvents();
    }

    private void registerCommands() {
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("kit").setTabCompleter(new KitCommand());
        getCommand("epickits").setExecutor(new EpicKitsCommand());
        getCommand("epickits").setTabCompleter(new EpicKitsCommand());
        getCommand("kits").setExecutor(new KitsCommand());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
    }

    private boolean loadManagers() {
        configManager = new ConfigManager();
        if (inventoryManager != null) {
            inventoryManager.dispose();
        }
        else {
            inventoryManager = new InventoryManager();
        }
        kitManager = KitManager.fromType(configManager.databaseType());
        if (kitManager == null) {
            getLogger().severe("An error occurred while loading the database");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        kitManager.load();
        kitPlayerManager = new KitPlayerManager();
        return true;
    }

    @Override
    public void onDisable() {
        if (configManager.saveOnDisable()) {
            kitManager.save();
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public boolean reload(boolean force) {
        if (force) {
            return loadManagers();
        }
        if (kitManager.isDirty()) return false;
        return loadManagers();
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public KitPlayerManager getKitPlayerManager() {
        return kitPlayerManager;
    }
}
