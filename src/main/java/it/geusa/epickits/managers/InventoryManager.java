package it.geusa.epickits.managers;

import it.geusa.epickits.inventories.utils.EKInventory;
import it.geusa.epickits.inventories.utils.InventoryHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Stack;

public class InventoryManager {
    private final HashMap<Inventory, InventoryHandler> activeInventories = new HashMap<>();
    private final HashMap<Player, Stack<EKInventory>> inventoryStacks = new HashMap<>();

    public void openInventory(EKInventory inventory, boolean stack) {
        Player player = inventory.getPlayer();
        if (stack) {
            inventoryStacks.computeIfAbsent(player, k -> new Stack<>()).push(inventory);
            System.out.println("Pushed inventory to stack");
        } //TODO: aggiusta gli stack
        registerInventory(inventory.getInventory(), inventory);
        player.openInventory(inventory.getInventory());
    }

    public void registerInventory(Inventory inventory, InventoryHandler handler) {
        activeInventories.put(inventory, handler);
    }

    public void unregisterInventory(Inventory inventory) {
        activeInventories.remove(inventory);
    }

    public void handleClick(InventoryClickEvent event) {
        InventoryHandler handler = activeInventories.get(event.getInventory());
        if (handler != null) {
            handler.onClick(event);
        }
    }

    public void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHandler handler = activeInventories.get(inventory);
        System.out.println("InventoryClosed = " + handler.getClass());
        if (handler != null) {
            Player player = (Player) event.getPlayer();
            Stack<EKInventory> stack = inventoryStacks.get(player);
            EKInventory peek = stack.peek();
            System.out.println("Peek = " + peek.getClass());
            if (!stack.isEmpty() && peek == handler) {
                stack.pop();
                if (!stack.isEmpty()) {
                    EKInventory previousInventory = stack.peek();
                    registerInventory(previousInventory.getInventory(), previousInventory);
                    player.openInventory(previousInventory.getInventory());
                }
            }
            handler.onClose(event);
            unregisterInventory(inventory);
        }
    }


    public void handleOpen(InventoryOpenEvent event) {
        InventoryHandler handler = activeInventories.get(event.getInventory());
        if (handler != null) {
            handler.onOpen(event);
        }
    }

    public void dispose() {
        activeInventories.clear();
    }
}