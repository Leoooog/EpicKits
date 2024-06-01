package it.geusa.epickits.models;

import it.geusa.epickits.EpicKits;
import it.geusa.epickits.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class Kit implements ConfigurationSerializable {
    private final String id;
    private final NamespacedKey namespacedKey;
    private final ArrayList<ItemStack> items;
    private String displayName;
    private String permission;
    private String description;

    private ItemStack icon;
    private int cooldown;
    private boolean oneTime;
    private double cost;
    private boolean enabled;
    private boolean clearInventory;
    private boolean clearArmor;
    private boolean autoArmor;
    private boolean autoShield;
    private boolean autoElytra;
    private Date lastModified;

    @ParametersAreNonnullByDefault
    public Kit(String id, String displayName, String permission, String description, int cooldown, boolean oneTime,
            double cost, boolean enabled, boolean clearInventory, boolean clearArmor, boolean autoArmor,
            boolean autoShield, boolean autoElytra, ArrayList<ItemStack> items, ItemStack icon, Date lastModified) {
        this.id = id;
        this.displayName = displayName;
        this.permission = permission;
        this.description = description;
        this.cooldown = cooldown;
        this.oneTime = oneTime;
        this.cost = cost;
        this.enabled = enabled;
        this.clearInventory = clearInventory;
        this.clearArmor = clearArmor;
        this.autoArmor = autoArmor;
        this.autoShield = autoShield;
        this.autoElytra = autoElytra;
        this.items = items;
        this.icon = icon;
        this.lastModified = lastModified;
        this.namespacedKey = new NamespacedKey(EpicKits.getInstance(), "kit-" + id);
    }

    public Kit copy() {
        return new Kit(id, displayName, permission, description, cooldown, oneTime, cost, enabled, clearInventory,
                clearArmor, autoArmor, autoShield, autoElytra, new ArrayList<>(items), icon, lastModified);
    }

    public Kit(Map<String, Object> map) {
        this.id = (String) map.get("id");
        this.displayName = (String) map.get("displayName");
        this.permission = (String) map.get("permission");
        this.description = (String) map.get("description");
        this.cooldown = (int) map.get("cooldown");
        this.oneTime = (boolean) map.get("oneTime");
        this.cost = (double) map.get("cost");
        this.enabled = (boolean) map.get("enabled");
        this.clearInventory = (boolean) map.get("clearInventory");
        this.clearArmor = (boolean) map.get("clearArmor");
        this.autoArmor = (boolean) map.get("autoArmor");
        this.autoShield = (boolean) map.get("autoShield");
        this.autoElytra = (boolean) map.get("autoElytra");
        this.items = new ArrayList<>((List<ItemStack>) map.get("items"));
        this.icon = (ItemStack) map.get("icon");
        this.lastModified = (Date) map.get("lastModified");
        this.namespacedKey = new NamespacedKey(EpicKits.getInstance(), "kit-" + id);
    }

    public static Kit create(String id) {
        return new Kit(id, "§6" + id,
                EpicKits.getInstance().getConfigManager().kitPermission().toLowerCase().replace("<kit-name>", id),
                "§7" + id + " kit description.",
                0, false, 0, true, false, false, false, false, false, new ArrayList<>(),
                new ItemStack(Material.STONE), new Date());
    }


    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isClearInventory() {
        return clearInventory;
    }

    public void setClearInventory(boolean clearInventory) {
        this.clearInventory = clearInventory;
    }

    public boolean isClearArmor() {
        return clearArmor;
    }

    public void setClearArmor(boolean clearArmor) {
        this.clearArmor = clearArmor;
    }

    public boolean isAutoArmor() {
        return autoArmor;
    }

    public void setAutoArmor(boolean autoArmor) {
        this.autoArmor = autoArmor;
    }

    public boolean isAutoShield() {
        return autoShield;
    }

    public void setAutoShield(boolean autoShield) {
        this.autoShield = autoShield;
    }

    public boolean isAutoElytra() {
        return autoElytra;
    }

    public void setAutoElytra(boolean autoElytra) {
        this.autoElytra = autoElytra;
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getSerializedItems() {
        return Utils.getSerializedItems(items);
    }

    public String getSerializedIcon() {
        return Utils.getSerializedItemStack(icon);
    }

    public void addItem(ItemStack item) {
        items.add(item);
    }

    public void removeItem(ItemStack item) {
        items.remove(item);
    }

    public void setItems(List<ItemStack> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Kit kit = (Kit) o;
        return id.equals(kit.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("displayName", displayName);
        map.put("permission", permission);
        map.put("description", description);
        map.put("cooldown", cooldown);
        map.put("oneTime", oneTime);
        map.put("cost", cost);
        map.put("enabled", enabled);
        map.put("clearInventory", clearInventory);
        map.put("clearArmor", clearArmor);
        map.put("autoArmor", autoArmor);
        map.put("autoShield", autoShield);
        map.put("autoElytra", autoElytra);
        map.put("icon", icon);
        map.put("items", items);
        map.put("lastModified", lastModified);
        return map;
    }

    public Object setProperty(String property, String value) {
        switch (property) {
            case "displayname":
                setDisplayName(value);
                return getDisplayName();
            case "description":
                setDescription(value);
                return getDescription();
            case "permission":
                setPermission(value);
                return getPermission();
            case "cooldown":
                try {
                    setCooldown(Integer.parseInt(value));
                    return getCooldown();
                } catch (NumberFormatException e) {
                    return null;
                }
            case "onetime":
                setOneTime(Boolean.parseBoolean(value));
                return isOneTime();
            case "cost":
                try {
                    setCost(Double.parseDouble(value));
                    return getCost();
                } catch (NumberFormatException e) {
                    return null;
                }
            case "enabled":
                setEnabled(Boolean.parseBoolean(value));
                return isEnabled();
            case "clearinventory":
                setClearInventory(Boolean.parseBoolean(value));
                return isClearInventory();
            case "cleararmor":
                setClearArmor(Boolean.parseBoolean(value));
                return isClearArmor();
            case "autoarmor":
                setAutoArmor(Boolean.parseBoolean(value));
                return isAutoArmor();
            case "autoshield":
                setAutoShield(Boolean.parseBoolean(value));
                return isAutoShield();
            case "autoelytra":
                setAutoElytra(Boolean.parseBoolean(value));
                return isAutoElytra();
            default:
                return null;
        }
    }

    public boolean isValid() {
        return !id.isEmpty() && !permission.isEmpty() && !displayName.isEmpty()
                && !description.isEmpty() && cooldown >= 0 && cost >= 0 && lastModified != null && items.size() < 36;
    }
}
