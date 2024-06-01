package it.geusa.epickits.inventories;

import it.geusa.epickits.inventories.utils.EKInventory;
import it.geusa.epickits.inventories.utils.InventoryButton;
import it.geusa.epickits.models.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitPreviewInventory extends EKInventory {

    private final Kit kit;

    public KitPreviewInventory(Player player, Kit kit) {
        super(player, true);
        this.kit = kit;
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 45, ChatColor.translateAlternateColorCodes('&', kit.getDisplayName())
                + " §r§8Kit Preview");
    }

    @Override
    public void decorate() {
        // Add the kit items to the inventory
        for (int i = 0; i < kit.getItems().size(); i++) {
            ItemStack item = kit.getItems().get(i);
            addButton(i, InventoryButton.emptyButton(item));
        }
        // Populate last row with glass panes
        for (int i = 36; i < 45; i++) {
            getInventory().setItem(i, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
        }
        // Add a close button
        addButton(40, InventoryButton.closeButton());
        super.decorate();
    }
}
