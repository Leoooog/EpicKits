package it.geusa.epickits.inventories;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class PreviewKitInventory extends EKInventory {

    
    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 36, "Preview Kit");
    }

}
