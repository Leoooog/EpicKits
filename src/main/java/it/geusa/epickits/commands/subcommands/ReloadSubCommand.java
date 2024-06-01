package it.geusa.epickits.commands.subcommands;

import it.geusa.epickits.commands.EKSubCommand;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand implements EKSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!plugin.reload(false)) {
            sender.sendMessage("§cThere are pending changes not saved to the kits database. Save the kits with §f/ek "
                    + "save §cor run §f/ek force-reload §cto discard the changes and force the reload");
        }
        else {
            sender.sendMessage("§aThe plugin configuration and database have been reloaded successfully");
        }
    }

    @Override
    public String getPermission() {
        return "epickits.reload";
    }
}
