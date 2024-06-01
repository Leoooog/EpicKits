package it.geusa.epickits.commands.subcommands;

import it.geusa.epickits.commands.EKSubCommand;
import it.geusa.epickits.models.Kit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class InfoSubCommand implements EKSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§cUsage: /ek info <kit>");
            return;
        }
        String kitId = args[0];
        Kit kit = plugin.getKitManager().getKit(kitId);
        if (kit == null) {
            sender.sendMessage("§cKit §6" + kitId + "§c not found.");
            return;
        }
        sender.sendMessage("§aKit §6" + kit + "§a info:");
        sender.sendMessage("§a- Id: §6" + kit.getId());
        sender.sendMessage("§a- Display name: §f§r" + ChatColor.translateAlternateColorCodes('&',
                kit.getDisplayName()));
        sender.sendMessage("§a- Permission: §6" + kit.getPermission());
        sender.sendMessage("§a- Description: §f§r" + ChatColor.translateAlternateColorCodes('&', kit.getDescription()));
        sender.sendMessage("§a- One time: §6" + kit.isOneTime());
        sender.sendMessage("§a- Cooldown: §6" + kit.getCooldown());
        sender.sendMessage("§a- Enabled: §6" + kit.isEnabled());
        sender.sendMessage("§a- Cost: §6" + (kit.getCost() == 0 ? "Free" : kit.getCost()));
        sender.sendMessage("§a- Auto armor: §6" + kit.isAutoArmor());
        sender.sendMessage("§a- Auto shield: §6" + kit.isAutoShield());
        sender.sendMessage("§a- Auto elytra: §6" + kit.isAutoElytra());
        sender.sendMessage("§a- Clear inventory: §6" + kit.isClearInventory());
        sender.sendMessage("§a- Clear armor: §6" + kit.isClearArmor());
        sender.sendMessage("§a- Last modified: §6" + kit.getLastModified());
    }

    @Override
    public String getPermission() {
        return "epickits.info";
    }
}
