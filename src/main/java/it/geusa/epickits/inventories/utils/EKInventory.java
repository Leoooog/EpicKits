package it.geusa.epickits.inventories.utils;

import it.geusa.epickits.EpicKits;
import it.geusa.epickits.managers.ConfigManager;
import it.geusa.epickits.managers.InventoryManager;
import it.geusa.epickits.managers.KitManager;
import it.geusa.epickits.managers.KitPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class EKInventory implements InventoryHandler {

    protected final EpicKits plugin = EpicKits.getInstance();

    protected final ConfigManager configManager = plugin.getConfigManager();
    protected final InventoryManager inventoryManager = plugin.getInventoryManager();

    protected final KitManager kitManager = plugin.getKitManager();

    protected final KitPlayerManager kitPlayerManager = plugin.getKitPlayerManager();

    private Inventory inventory;
    private final Map<Integer, InventoryButton> buttons = new HashMap<>();
    private final boolean cancelClicks;

    protected final Player player;

    public EKInventory(Player player, boolean cancelClicks) {
        this.cancelClicks = cancelClicks;
        this.player = player;
    }

    public Inventory getInventory() {
        if (inventory == null)
            inventory = createInventory();
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public void addButton(int slot, InventoryButton button) {
        buttons.put(slot, button);
    }

    public void removeButton(int slot) {
        buttons.remove(slot);
    }

    public InventoryButton getButton(int slot) {
        return buttons.get(slot);
    }

    public void decorate() {
        buttons.forEach((slot, button) -> inventory.setItem(slot, button.getIconCreator().apply(player)));
        player.updateInventory();
    }

    public void decorateSlot(int slot) {
        InventoryButton button = buttons.get(slot);
        if (button != null) {
            inventory.setItem(slot, button.getIconCreator().apply(player));
        }
        else {
            inventory.setItem(slot, null);
        }
        player.updateInventory();
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(cancelClicks);
        int slot = event.getRawSlot();
        InventoryButton button = buttons.get(slot);
        if (button != null) {
            event.setCancelled(true);
            button.getEventConsumer().accept(event);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        decorate();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    protected abstract Inventory createInventory();

}
