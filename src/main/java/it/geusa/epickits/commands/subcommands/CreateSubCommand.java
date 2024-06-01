package it.geusa.epickits.commands.subcommands;

import it.geusa.epickits.commands.EKSubCommand;
import it.geusa.epickits.inventories.CreateKitInventory;
import it.geusa.epickits.managers.KitManager;
import it.geusa.epickits.models.Kit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateSubCommand implements EKSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        KitManager kitManager = plugin.getKitManager();

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /ek create <kitName>");
            return;
        }
        String kitName = args[0];
        if (kitManager.getKit(kitName) != null) {
            sender.sendMessage("§cA kit with that name already exists.");
            return;
        }
        Kit kit = Kit.create(kitName);
        if (sender instanceof Player player) {
            plugin.getInventoryManager().openInventory(new CreateKitInventory(player, kit), false);
        }
        else {
            if (kitManager.createKit(kit))
                sender.sendMessage("§aKit created successfully.");
            else
                sender.sendMessage("§cAn error occurred while creating the kit.");
        }
    }

    @Override
    public String getPermission() {
        return "epickits.create";
    }
}

