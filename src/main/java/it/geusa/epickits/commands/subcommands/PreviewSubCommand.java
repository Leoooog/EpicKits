package it.geusa.epickits.commands.subcommands;

import it.geusa.epickits.commands.EKSubCommand;
import org.bukkit.command.CommandSender;

public class PreviewSubCommand implements EKSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
    
    }

    @Override
    public String getPermission() {
        return "epickits.preview";
    }
}
