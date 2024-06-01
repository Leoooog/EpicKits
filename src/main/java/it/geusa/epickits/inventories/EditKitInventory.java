package it.geusa.epickits.inventories;

import it.geusa.epickits.inventories.utils.EKInventory;
import it.geusa.epickits.inventories.utils.InventoryButton;
import it.geusa.epickits.models.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class EditKitInventory extends EKInventory {
    protected final Kit kit;
    protected final Kit originalKit;


    public EditKitInventory(Player player, Kit kit) {
        super(player, false);
        this.originalKit = kit;
        this.kit = kit.copy();
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 54, "§8Edit Kit '" + kit.getId() + "'");
    }

    @Override
    public void decorate() {
        // Leave first three rows empty
        // Add buttons to edit boolean values
        addButton(36, createIconButton());
        addButton(37, InventoryButton.booleanButton("One Time", kit::isOneTime, kit::setOneTime));
        addButton(38, InventoryButton.booleanButton("Enabled", kit::isEnabled, kit::setEnabled));
        addButton(39, InventoryButton.booleanButton("Auto Armor", kit::isAutoArmor, kit::setAutoArmor));
        addButton(40, InventoryButton.booleanButton("Auto Shield", kit::isAutoShield, kit::setAutoShield));
        addButton(41, InventoryButton.booleanButton("Auto Elytra", kit::isAutoElytra, kit::setAutoElytra));
        addButton(42, InventoryButton.booleanButton("Clear Inventory", kit::isClearInventory, kit::setClearInventory));
        addButton(43, InventoryButton.booleanButton("Clear Armor", kit::isClearArmor, kit::setClearArmor));
        addButton(44, createLastModifiedButton());
        addButton(49, createSaveButton());
        addButton(53, InventoryButton.closeButton());
        addKitItemsToInventory();
        super.decorate();
    }

    private InventoryButton createIconButton() {
        return new InventoryButton()
                .creator(p -> {
                    ItemStack icon = kit.getIcon();
                    ItemMeta meta = icon.getItemMeta();
                    if (meta == null) {
                        meta = Bukkit.getItemFactory().getItemMeta(icon.getType());
                    }
                    if (plugin.getConfigManager().overrideIconName()) {
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', kit.getDisplayName()));
                    }
                    if (plugin.getConfigManager().overrideIconLore()) {
                        meta.setLore(List.of(ChatColor.translateAlternateColorCodes('&', kit.getDescription())));
                    }
                    icon.setItemMeta(meta);
                    return icon;
                })
                .consumer(event -> {
                    ItemStack hand = event.getCursor();
                    if (hand.getType() != Material.AIR) {
                        kit.setIcon(hand.clone());
                        player.getInventory().addItem(hand);
                        player.setItemOnCursor(null);
                        player.updateInventory();
                        super.decorateSlot(36);
                    }
                });
    }

    private InventoryButton createLastModifiedButton() {
        ItemStack lastModified = new ItemStack(Material.CLOCK);
        ItemMeta meta = lastModified.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Last Modified");
            meta.setLore(List.of(DateFormat.getDateTimeInstance().format(kit.getLastModified())));
            lastModified.setItemMeta(meta);
        }
        return InventoryButton.emptyButton(lastModified);
    }

    private InventoryButton createSaveButton() {
        return new InventoryButton()
                .creator(p -> {
                    ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
                    ItemMeta iconMeta = item.getItemMeta();
                    if (iconMeta == null) {
                        iconMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
                    }
                    iconMeta.setDisplayName("§aSave");
                    item.setItemMeta(iconMeta);

                    return item;
                })
                .consumer(event -> {
                    save();
                    event.getWhoClicked().closeInventory();
                });
    }

    private void addKitItemsToInventory() {
        for (int i = 0; i < kit.getItems().size(); i++) {
            if (i == 36) break;
            ItemStack item = kit.getItems().get(i);
            addButton(i, kitItemButton(item));
        }
    }


    private InventoryButton kitItemButton(ItemStack item) {
        return new InventoryButton()
                .creator(p -> {
                    ItemStack clone = item.clone();
                    ItemMeta meta = clone.getItemMeta();
                    if (meta == null) {
                        meta = Bukkit.getItemFactory().getItemMeta(clone.getType());
                    }
                    ArrayList<String> lore = new ArrayList<>();
                    if (meta.hasLore())
                        lore.addAll(meta.getLore());
                    lore.add("§7Right click to remove");
                    meta.setLore(lore);
                    clone.setItemMeta(meta);
                    return clone;
                })
                .consumer(event -> {
                    ItemStack cursorItem = event.getCursor();
                    if (cursorItem.getType() != Material.AIR && cursorItem.isSimilar(item)) {
                        int combinedAmount = item.getAmount() + cursorItem.getAmount();
                        int maxStackSize = item.getMaxStackSize();
                        item.setAmount(Math.min(combinedAmount, maxStackSize));
                        decorateSlot(event.getSlot());
                        player.setItemOnCursor(null);
                        player.getInventory().addItem(cursorItem);
                        player.updateInventory();
                    }
                    else if (event.isRightClick()) {
                        removeButton(event.getSlot());
                        decorateSlot(event.getSlot());
                        kit.removeItem(item);
                    }
                    event.setCancelled(true);
                });

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        super.onClick(event);
        int slot = event.getRawSlot();
        if (slot < 36) {
            if (getButton(slot) != null) {
                return;
            }
            if (event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.PLACE_ONE
                    || event.getAction() == InventoryAction.PLACE_SOME) {
                ItemStack item = event.getCursor();
                if (item.getType() != Material.AIR) {
                    player.getInventory().addItem(item);
                    player.setItemOnCursor(null);
                    ItemStack clone = item.clone();
                    addButton(slot, kitItemButton(clone));
                    decorateSlot(slot);
                    kit.addItem(clone);
                    player.updateInventory(); // Ensure the inventory is updated
                }
                event.setCancelled(true);
            }
        }
    }

    protected void save() {
        originalKit.setAutoArmor(kit.isAutoArmor());
        originalKit.setAutoElytra(kit.isAutoElytra());
        originalKit.setAutoShield(kit.isAutoShield());
        originalKit.setClearArmor(kit.isClearArmor());
        originalKit.setClearInventory(kit.isClearInventory());
        originalKit.setEnabled(kit.isEnabled());
        originalKit.setIcon(kit.getIcon());
        originalKit.setOneTime(kit.isOneTime());
        originalKit.setItems(kit.getItems());
        kitManager.editKit(originalKit);
        player.sendMessage("§aKit saved successfully.");
    }

}
