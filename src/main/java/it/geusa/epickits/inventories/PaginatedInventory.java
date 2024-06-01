package it.geusa.epickits.inventories;

import it.geusa.epickits.inventories.utils.EKInventory;
import it.geusa.epickits.inventories.utils.InventoryButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class PaginatedInventory extends EKInventory {

    protected int inventorySize;
    private final Map<Player, Integer> pageMap = new HashMap<>();

    public PaginatedInventory(Player player, boolean cancelClicks) {
        super(player, cancelClicks);
    }

    @Override
    public void decorate() {
        int page = pageMap.getOrDefault(player, 0);

        // Add previous page button
        if (page > 0) {
            addButton(inventorySize - 9, new InventoryButton()
                    .creator(p -> new ItemStack(Material.ARROW))
                    .consumer(event -> {
                        pageMap.put(player, page - 1);
                        decorate();
                    }));
        }

        // Add next page button
        if (hasNextPage(player, page)) {
            addButton(inventorySize - 1, new InventoryButton()
                    .creator(p -> new ItemStack(Material.ARROW))
                    .consumer(event -> {
                        pageMap.put(player, page + 1);
                        decorate();
                    }));
        }

        populatePage(player, page);
        super.decorate();
    }

    protected abstract void populatePage(Player player, int page);

    protected abstract boolean hasNextPage(Player player, int currentPage);
}