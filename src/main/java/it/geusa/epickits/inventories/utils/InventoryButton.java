package it.geusa.epickits.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class InventoryButton {
    private Function<Player, ItemStack> iconCreator;
    private Consumer<InventoryClickEvent> eventConsumer;

    public static InventoryButton closeButton() {
        return new InventoryButton()
                .creator(p -> {
                    ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
                    item.getItemMeta().setDisplayName("§cClose");
                    return item;
                })
                .consumer(event -> {
                    event.getWhoClicked().closeInventory();
                });
    }

    public static InventoryButton saveButton() {
        return new InventoryButton()
                .creator(p -> {
                    ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                    item.getItemMeta().setDisplayName("§aSave");
                    return item;
                })
                .consumer(event -> {
                    event.getWhoClicked().closeInventory();
                });
    }

    public static InventoryButton infoButton(ItemStack item) {
        return new InventoryButton()
                .creator(p -> item)
                .consumer(event -> {
                });
    }

    public static InventoryButton booleanButton(String displayName, Supplier<Boolean> get,
            Consumer<Boolean> set) {
        return new InventoryButton()
                .creator(p -> booleanItemStack(displayName, get.get()))
                .consumer(event -> {
                    boolean value = get.get();
                    set.accept(!value);
                    event.getInventory().setItem(event.getSlot(), booleanItemStack(displayName, !value));
                });
    }

    private static ItemStack booleanItemStack(String displayName, boolean value) {
        ItemStack item = new ItemStack(
                value ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
        item.getItemMeta().setDisplayName(displayName);
        item.getItemMeta().setLore(value ? List.of("§aTrue") : List.of("§cFalse"));
        return item;
    }


    public InventoryButton creator(Function<Player, ItemStack> iconCreator) {
        this.iconCreator = iconCreator;
        return this;
    }

    public InventoryButton consumer(Consumer<InventoryClickEvent> eventConsumer) {
        this.eventConsumer = eventConsumer;
        return this;
    }

    public Function<Player, ItemStack> getIconCreator() {
        return iconCreator;
    }

    public Consumer<InventoryClickEvent> getEventConsumer() {
        return eventConsumer;
    }
}
