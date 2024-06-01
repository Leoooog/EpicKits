package it.geusa.epickits.commands;

import it.geusa.epickits.EpicKits;
import org.bukkit.command.CommandSender;

public interface EKSubCommand {

    EpicKits plugin = EpicKits.getInstance();

    void execute(CommandSender sender, String[] args);

    String getPermission();
}
