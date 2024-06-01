package it.geusa.epickits.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class EKInventory implements InventoryHandler {

    private final Inventory inventory;

    private final Map<Integer, InventoryButton> buttons = new HashMap<>();

    private final boolean cancelClicks;

    public EKInventory(boolean cancelClicks) {
        this.cancelClicks = cancelClicks;
        this.inventory = createInventory();
    }

    public Inventory getInventory() {
        return inventory;
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

    public void decorate(Player player) {
        buttons.forEach((slot, button) -> inventory.setItem(slot, button.getIconCreator().apply(player)));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(cancelClicks);
        int slot = event.getSlot();
        InventoryButton button = buttons.get(slot);
        if (button != null) {
            event.setCancelled(true);
            button.getEventConsumer().accept(event);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        decorate((Player) event.getPlayer());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    protected abstract Inventory createInventory();
}
