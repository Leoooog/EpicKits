package it.geusa.epickits.managers;

import it.geusa.epickits.models.Kit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class CooldownManager {

    public long getCooldown(Player player, Kit kit) {
        if (player.getPersistentDataContainer().has(kit.getNamespacedKey(), PersistentDataType.LONG)) {
            // not using primitive type to avoid NPE
            Long lastUsed = player.getPersistentDataContainer().get(kit.getNamespacedKey(), PersistentDataType.LONG);
            if (lastUsed != null) {
                long remaining = lastUsed + kit.getCooldown() * 1000L - System.currentTimeMillis();
                return remaining > 0 ? remaining : 0;
            }
        }
        return 0;
    }

    public void setCooldown(Player player, Kit kit) {
        // save last used time
        player.getPersistentDataContainer().set(kit.getNamespacedKey(), PersistentDataType.LONG,
                System.currentTimeMillis());
    }
}
