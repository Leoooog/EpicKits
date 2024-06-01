package it.geusa.epickits.commands.subcommands.editing;

import it.geusa.epickits.commands.EKSubCommand;
import it.geusa.epickits.models.Kit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditSubCommand implements EKSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 2 || args.length > 3) {
            sender.sendMessage("§cUsage: /ek edit <kit> [property] [value]");
            return;
        }
        String kitId = args[0];
        Kit kit = plugin.getKitManager().getKit(kitId);
        if (kit == null) {
            sender.sendMessage("§cKit §6" + kitId + "§c not found.");
            return;
        }
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cYou must be a player to edit items.");
                return;
            }
            //TODO show kit items in gui
            return;
        }
        String property = args[1].toLowerCase();
        String value = args[2];
        Object propertyValue = kit.setProperty(property, value);
        if (propertyValue == null) {
            sender.sendMessage(
                    "§cCould not set property §6" + property + "§c to §6" + value + "§c for kit §6" + kitId + "§c.");
            return;
        }
        sender.sendMessage(
                "§aProperty §6" + property + "§a set to §6" + propertyValue + "§a for kit §6" + kitId + "§a.");
    }

    @Override
    public String getPermission() {
        return "epickits.edit";
    }
}
