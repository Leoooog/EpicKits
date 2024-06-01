package it.geusa.epickits.commands;

import it.geusa.epickits.EpicKits;
import it.geusa.epickits.Utils;
import it.geusa.epickits.inventories.KitListInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KitsCommand implements CommandExecutor {

    private final EpicKits plugin = EpicKits.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            plugin.getKitManager().getKits().forEach(kit -> sender.sendMessage(Utils.getKitString(kit)));
            return true;
        }
        else if (sender instanceof Player player) {
            plugin.getInventoryManager().openInventory(new KitListInventory(player), true);
            return true;
        }
        sender.sendMessage("§cYou can't use this command.");
        return true;
    }
}
