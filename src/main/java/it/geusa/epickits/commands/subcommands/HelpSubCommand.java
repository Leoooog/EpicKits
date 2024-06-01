package it.geusa.epickits.commands.subcommands;

import it.geusa.epickits.commands.EKSubCommand;
import org.bukkit.command.CommandSender;

public class HelpSubCommand implements EKSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§6List of commands:");
        sender.sendMessage("§f/ek help §6- Displays this message");
        sender.sendMessage("§f/ek reload §6- Reloads the plugin configuration and the kits from the database");
        sender.sendMessage(
                "§f/ek force-reload §6- Forces the reload of the plugin configuration and the kits from the database");
        sender.sendMessage("§f/ek save §6- Saves the kits to the database");
        sender.sendMessage("§f/ek list §6- Lists all the available kits");
        sender.sendMessage("§f/ek give <player> <kit> §6- Gives a kit to a player");
        sender.sendMessage("§f/ek create <kit> [gui/inv] §6- Creates a new kit");
        sender.sendMessage("§f/ek delete <kit> §6- Deletes a kit");
        sender.sendMessage("§f/ek edit <kit> §6- Edits a kit");
        sender.sendMessage("§f/ek preview <kit> §6- Previews a kit");
        sender.sendMessage("§f/ek additem <kit> §6- Adds the item in hand to a kit");
        sender.sendMessage("§f/ek info <kit> §6- Displays information about a kit");
    }

    @Override
    public String getPermission() {
        return "epickits.help";
    }
}
