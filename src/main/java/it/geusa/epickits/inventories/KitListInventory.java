package it.geusa.epickits.inventories;

import it.geusa.epickits.inventories.utils.InventoryButton;
import it.geusa.epickits.models.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KitListInventory extends PaginatedInventory {
    private final List<Kit> availableKits;

    public KitListInventory(Player player) {
        super(player, true);
        this.availableKits = kitPlayerManager.getAvailableKits(player);
    }

    @Override
    protected Inventory createInventory() {
        int size = configManager.getKitsInventorySize();
        if (size == 0) {
            int kitsSize = availableKits.size();
            if (kitsSize == 0) {
                size = 18;
            }
            else {
                size = Math.min((int) Math.ceil(kitsSize / 9.0) * 9 + 9, 54);
            }
        }
        if (size == 9 || size % 9 != 0) size = 18;
        inventorySize = size;
        return Bukkit.createInventory(null, inventorySize, "§8Your Kits");
    }


    @Override
    protected void populatePage(Player player, int page) {
        if (!availableKits.isEmpty()) {
            int startIndex = page * (inventorySize - 9);
            for (int i = startIndex; i < startIndex + (inventorySize - 9) && i < availableKits.size(); i++) {
                Kit kit = availableKits.get(i);
                addButton(i - startIndex, new InventoryButton().creator(p -> {
                    ItemStack icon = kit.getIcon().clone();
                    ItemMeta meta = icon.getItemMeta();
                    if (meta == null) {
                        meta = Bukkit.getItemFactory().getItemMeta(icon.getType());
                    }
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', kit.getDisplayName()));
                    ArrayList<String> lore = new ArrayList<>();
                    if (meta.hasLore()) lore.addAll(meta.getLore());
                    lore.add("§7Left click to equip");
                    lore.add("§7Right click to preview");
                    meta.setLore(lore);
                    icon.setItemMeta(meta);
                    return icon;
                }).consumer(event -> {
                    if (event.isLeftClick()) {
                        kitPlayerManager.equipKit(player, kit);
                        player.closeInventory();
                    }
                    else {
                        inventoryManager.openInventory(new KitPreviewInventory(player, kit), true);
                    }
                }));
            }
        }
        addButton(inventorySize - 5, InventoryButton.closeButton());
    }

    @Override
    protected boolean hasNextPage(Player player, int currentPage) {
        return (currentPage + 1) * (inventorySize - 9) < availableKits.size();
    }
}