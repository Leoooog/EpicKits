package it.geusa.epickits.inventories.utils;

import org.bukkit.inventory.ItemStack;

public class KitItemButton extends InventoryButton {

    private final ItemStack item;

    public KitItemButton(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}
