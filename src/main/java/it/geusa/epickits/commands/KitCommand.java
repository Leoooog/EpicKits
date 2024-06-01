package it.geusa.epickits.commands;

import it.geusa.epickits.EpicKits;
import it.geusa.epickits.inventories.KitListInventory;
import it.geusa.epickits.models.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {

    private final EpicKits plugin = EpicKits.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }
        if (args.length == 0) {
            plugin.getInventoryManager().openInventory(new KitListInventory(player), true);
            return true;
        }
        if (args.length == 1) {
            String kitName = args[0];
            Kit kit = plugin.getKitManager().getKit(kitName);
            plugin.getKitPlayerManager().equipKit(player, kit);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return plugin.getKitPlayerManager().getAvailableKits((Player) sender).stream()
                    .map(Kit::getId).filter(id -> id.startsWith(args[0])).toList();
        }
        return Collections.emptyList();
    }
}
