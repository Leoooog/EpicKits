package it.geusa.epickits.inventories;

import it.geusa.epickits.models.Kit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CreateKitInventory extends EditKitInventory {

    public CreateKitInventory(Player player, Kit kit) {
        super(player, kit);
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 54, "§8Create Kit '" + kit.getId() + "'");
    }

    @Override
    protected void save() {
        kitManager.createKit(kit);
        player.sendMessage("§aKit created successfully.");
    }

}
