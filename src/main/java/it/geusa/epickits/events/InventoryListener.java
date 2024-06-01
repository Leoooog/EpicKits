package it.geusa.epickits.events;


import it.geusa.epickits.EpicKits;
import it.geusa.epickits.managers.InventoryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryListener implements Listener {

    private final InventoryManager inventoryManager = EpicKits.getInstance().getInventoryManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        event.getWhoClicked().sendMessage(event.getAction().name() + " " + event.isLeftClick());
        inventoryManager.handleClick(event);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        inventoryManager.handleOpen(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        inventoryManager.handleClose(event);
    }
}
