package it.geusa.epickits.database;

import it.geusa.epickits.models.Kit;

import java.util.List;

public interface IKitsDatabase {

    IKitsDatabase.TYPE getType();

    void load() throws Exception;

    void save(boolean async) throws Exception;

    boolean isLoaded();

    void reload() throws Exception;

    void close() throws Exception;

    boolean createKit(Kit kit) throws Exception;

    boolean deleteKit(String id) throws Exception;

    Kit getKit(String id) throws Exception;

    List<Kit> getKits() throws Exception;

    boolean editKit(Kit kit) throws Exception;

    enum TYPE {
        JSON, MYSQL, SQLITE, YAML;
    }
}
