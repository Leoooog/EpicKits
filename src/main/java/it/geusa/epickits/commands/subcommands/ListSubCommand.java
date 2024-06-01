package it.geusa.epickits.commands.subcommands;

import it.geusa.epickits.Utils;
import it.geusa.epickits.commands.EKSubCommand;
import org.bukkit.command.CommandSender;

public class ListSubCommand implements EKSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§aAvailable kits:");
        plugin.getKitManager().getKits().forEach(kit -> sender.sendMessage(Utils.getKitString(kit)));
    }

    @Override
    public String getPermission() {
        return "epickits.list";
    }
}
