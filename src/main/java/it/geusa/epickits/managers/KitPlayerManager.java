package it.geusa.epickits.managers;

import it.geusa.epickits.EpicKits;
import it.geusa.epickits.models.Kit;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class KitPlayerManager {
    private final EpicKits plugin = EpicKits.getInstance();

    private final CooldownManager cooldownManager = new CooldownManager();

    private final ConfigManager configManager = plugin.getConfigManager();

    public void equipKit(Player player, Kit kit) {
        if (kit == null) {
            player.sendMessage("§cKit not found.");
            return;
        }
        if (!kit.isEnabled()) {
            player.sendMessage("§cThis kit is currently disabled.");
            return;
        }
        if (kit.isOneTime() && hasUsedKit(player, kit)) {
            player.sendMessage("§cYou have already used this kit.");
            return;
        }
        long cooldown = cooldownManager.getCooldown(player, kit);
        if (cooldown > 0) {
            player.sendMessage("§cYou must wait " + DurationFormatUtils.formatDurationWords(cooldown, true, true) +
                    " before using this kit again.");
            return;
        }
        if (kit.getCost() != 0) {
            //TODO: Implement economy
        }
        cooldownManager.setCooldown(player, kit);
        equip(player, kit);
        player.sendMessage("§aYou have received the kit " + kit.getDisplayName() + ".");
    }

    public List<Kit> getAvailableKits(Player player) {
        return plugin.getKitManager().getKits().stream()
                .filter(kit -> kit.isEnabled() && (!kit.isOneTime() || !hasUsedKit(player, kit))
                        && player.hasPermission(kit.getPermission()))
                .toList();
    }

    public void resetKit(Player player, Kit kit) {
        player.getPersistentDataContainer().remove(kit.getNamespacedKey());
    }

    public void giveKit(Player player, Kit kit) {
        equip(player, kit);
    }

    private void equip(Player player, Kit kit) {
        if (kit.isClearArmor()) player.getInventory().setArmorContents(null);
        if (kit.isClearInventory()) player.getInventory().clear();
        kit.getItems().forEach(item -> equipItem(player, kit, item));
    }

    private void equipItem(Player player, Kit kit, ItemStack item) {
        String itemType = item.getType().name();
        if (kit.isAutoArmor() && itemType.contains("HELMET")) player.getInventory().setHelmet(item);
        else if (kit.isAutoArmor() && itemType.contains("CHESTPLATE")) player.getInventory().setChestplate(item);
        else if (kit.isAutoArmor() && itemType.contains("LEGGINGS")) player.getInventory().setLeggings(item);
        else if (kit.isAutoArmor() && itemType.contains("BOOTS")) player.getInventory().setBoots(item);
        else if (kit.isAutoElytra() && itemType.contains("ELYTRA")) player.getInventory().setChestplate(item);
        else if (kit.isAutoShield() && itemType.contains("SHIELD")) player.getInventory().setItemInOffHand(item);
        else {
            if (configManager.stackItems()) {
                player.getInventory().addItem(item);
            }
            else {
                int firstEmpty = player.getInventory().firstEmpty();
                if (firstEmpty != -1)
                    player.getInventory().setItem(firstEmpty, item);
            }
        }
    }

    private boolean hasUsedKit(Player player, Kit kit) {
        return player.getPersistentDataContainer().get(kit.getNamespacedKey(), PersistentDataType.LONG) != null;
    }
}
