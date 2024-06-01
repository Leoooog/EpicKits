package it.geusa.epickits.inventories;

import it.geusa.epickits.inventories.utils.InventoryButton;
import it.geusa.epickits.managers.ConfigManager;
import it.geusa.epickits.managers.InventoryManager;
import it.geusa.epickits.managers.KitPlayerManager;
import it.geusa.epickits.models.Kit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DisplayKitsInventory extends PaginatedInventory {

    private final KitPlayerManager kitPlayerManager = plugin.getKitPlayerManager();
    private final InventoryManager inventoryManager = plugin.getInventoryManager();

    private final ConfigManager configManager = plugin.getConfigManager();

    private final List<Kit> availableKits;

    public DisplayKitsInventory(Player player) {
        super(player, true);
        this.availableKits = kitPlayerManager.getAvailableKits(player);
    }

    @Override
    protected Inventory createInventory() {
        int size = configManager.getKitsInventorySize();

        if (size == 0) {
            int kitsSize = this.availableKits.size();
            if (kitsSize == 0) {
                size = 18;
            }
            else {
                size = Math.min((int) Math.ceil(kitsSize / 9.0) * 9 + 9, 54);
            }
        }

        return Bukkit.createInventory(null, size, "§8Your Kits");
    }


    @Override
    protected void populatePage(Player player, int page) {
        int startIndex = page * (inventorySize - 9);
        for (int i = startIndex; i < startIndex + (inventorySize - 9) && i < availableKits.size(); i++) {
            Kit kit = availableKits.get(i);
            addButton(i - startIndex, new InventoryButton()
                    .creator(p -> {
                        ItemStack icon = kit.getIcon();
                        icon.getLore().add("§7Left click to equip");
                        icon.getLore().add("§7Right click to preview");
                        return icon;
                    })
                    .consumer(event -> {
                        if (event.isLeftClick()) {
                            kitPlayerManager.equipKit(player, kit);
                            player.closeInventory();
                        }
                        else {
                            inventoryManager.openInventory(new KitPreviewInventory(player, kit), true);
                        }
                    }));
        }
        addButton(inventorySize - 5, InventoryButton.closeButton());
    }

    @Override
    protected boolean hasNextPage(Player player, int currentPage) {
        return (currentPage + 1) * (inventorySize - 9) < availableKits.size();
    }
}