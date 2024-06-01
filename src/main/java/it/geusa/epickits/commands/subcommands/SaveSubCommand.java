package it.geusa.epickits.commands.subcommands;

import it.geusa.epickits.commands.EKSubCommand;
import it.geusa.epickits.managers.KitManager;
import org.bukkit.command.CommandSender;

public class SaveSubCommand implements EKSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (plugin.getConfigManager().autoUpdate()) {
            sender.sendMessage("§cThe plugin is set to auto-update the kits database. No need to save the kits");
            return;
        }
        KitManager kitManager = plugin.getKitManager();
        if (!kitManager.isDirty()) {
            sender.sendMessage("§cThere are no changes to save");
            return;
        }
        if (kitManager.save())
            sender.sendMessage("§aThe kits have been saved successfully");
        else
            sender.sendMessage("§cAn error occurred while saving the kits. Check the console for more information");
    }

    @Override
    public String getPermission() {
        return "epickits.save";
    }
}
