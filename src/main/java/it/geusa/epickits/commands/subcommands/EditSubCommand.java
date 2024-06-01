package it.geusa.epickits.commands.subcommands;

import it.geusa.epickits.commands.EKSubCommand;
import it.geusa.epickits.inventories.EditKitInventory;
import it.geusa.epickits.managers.KitManager;
import it.geusa.epickits.models.Kit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class EditSubCommand implements EKSubCommand {
    public static final List<String> properties = List.of("displayname", "permission", "description", "onetime",
            "cooldown", "enabled", "cost", "autoarmor", "autoshield", "autoelytra", "clearinventory", "cleararmor");

    public static final List<String> booleanProperties = List.of("onetime", "enabled", "autoarmor", "autoshield",
            "autoelytra", "clearinventory", "cleararmor");

    @Override
    public void execute(CommandSender sender, String[] args) {
        KitManager kitManager = plugin.getKitManager();
        if (args.length == 2) {
            sender.sendMessage("§cUsage: /ek edit <kit> [property] [value]");
            return;
        }
        String kitId = args[0];
        Kit kit = kitManager.getKit(kitId);
        if (kit == null) {
            sender.sendMessage("§cKit §6" + kitId + "§c not found.");
            return;
        }
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cYou must be a player to edit items.");
                return;
            }
            plugin.getInventoryManager().openInventory(new EditKitInventory(player, kit), false);
            return;
        }
        String property = args[1].toLowerCase();
        String[] values = Arrays.copyOfRange(args, 2, args.length);
        String value = String.join(" ", values);
        Object propertyValue = kit.setProperty(property, value);
        if (propertyValue == null) {
            sender.sendMessage(
                    "§cCould not set property §6" + property + "§c to §6" + value + "§c for kit §6" + kitId + "§c.");
            return;
        }
        sender.sendMessage(
                "§aProperty §6" + property + "§a set to §6" + propertyValue + "§a for kit §6" + kitId + "§a.");
        if (kitManager.editKit(kit)) {
            sender.sendMessage("§aKit §6" + kitId + "§a has been edited successfully.");
        }
        else {
            sender.sendMessage("§cAn error occurred while editing the kit §6" + kitId
                    + "§c. Please check the console for more information.");
        }
    }

    @Override
    public String getPermission() {
        return "epickits.edit";
    }
}
