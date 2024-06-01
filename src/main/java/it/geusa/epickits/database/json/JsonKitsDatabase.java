package it.geusa.epickits.database;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.geusa.epickits.EpicKits;
import it.geusa.epickits.models.Kit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class JsonKitsDatabase implements IKitsDatabase {

    private final File kitsFile;

    private final String fileName;
    private final Logger logger = EpicKits.getInstance().getLogger();

    private final Gson gson = EpicKits.getGson();
    private JsonReader reader;
    private JsonWriter writer;

    public JsonKitsDatabase(File kitsFile) {
        this.kitsFile = kitsFile;
        this.fileName = kitsFile.getName();
        this.reader = null;
        this.writer = null;
    }

    @Override
    public TYPE getType() {
        return TYPE.JSON;
    }

    @Override
    public void load() throws IOException {
        if (!kitsFile.exists()) {
            try {
                kitsFile.createNewFile();
            } catch (Exception e) {
                logger.severe("An error occurred while creating the " + fileName + " file");
                throw new IllegalArgumentException("An error occurred while creating the " + fileName + " file");
            } finally {
                logger.info("The " + fileName + " file has been created successfully");
            }
        }
        reader = new JsonReader(new FileReader(kitsFile));
        writer = new JsonWriter(new FileWriter(kitsFile));
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
                        writer.flush();
                        logger.info("The " + fileName + " file has been saved successfully");
                    } catch (Exception e) {
                        logger.severe("An error occurred while saving the " + fileName + " file");
                    }
                }
            }.runTaskAsynchronously(EpicKits.getInstance());
        }
        else {
            writer.flush();
            logger.info("The " + fileName + " file has been saved successfully");
        }
    }

    @Override
    public boolean isLoaded() {
        return reader != null && writer != null;
    }

    @Override
    public void reload() throws IOException {
        logger.info("Reloading the " + fileName + " file...");
        close();
        load();
        logger.info("The " + fileName + " file has been reloaded successfully");
    }

    @Override
    public void close() throws IOException {
        logger.info("Closing the " + fileName + " file...");
        reader.close();
        writer.close();
        logger.info("The " + fileName + " file has been closed");
    }

    @Override
    public boolean createKit(Kit kit) throws Exception {
        if (getKit(kit.getId()) == null) {
            JsonElement jsonElement = gson.toJsonTree(kit, ConfigurationSerializable.class);
            JsonArray kits = gson.fromJson(reader, JsonArray.class);
            kits.add(jsonElement);
            gson.toJson(kits, writer);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteKit(String id) throws Exception {
        JsonArray kits = gson.fromJson(reader, JsonArray.class);
        for (JsonElement kit : kits) {
            if (kit.getAsJsonObject().get("id").getAsString().equals(id)) {
                kits.remove(kit);
                gson.toJson(kits, writer);
                return true;
            }
        }
        return false;
    }

    @Override
    public Kit getKit(String id) throws Exception {
        JsonArray kits = gson.fromJson(reader, JsonArray.class);
        for (JsonElement kit : kits) {
            if (kit.getAsJsonObject().get("id").getAsString().equals(id)) {
                return (Kit) gson.fromJson(kit, ConfigurationSerializable.class);
            }
        }
        return null;
    }

    @Override
    public List<Kit> getKits() throws Exception {
        JsonArray kits = gson.fromJson(reader, JsonArray.class);
        if (kits != null) {
            Kit[] kitArray = (Kit[]) gson.fromJson(kits, ConfigurationSerializable[].class);
            return List.of(kitArray);
        }
        return List.of();
    }

    @Override
    public boolean editKit(Kit kit) throws Exception {
        if (deleteKit(kit.getId())) {
            createKit(kit);
            return true;
        }
        return false;
    }
}
