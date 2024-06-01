package it.geusa.epickits.commands.subcommands;

import it.geusa.epickits.commands.EKSubCommand;
import org.bukkit.command.CommandSender;

public class HelpCommand implements EKSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§6List of commands:");
        sender.sendMessage("§f/ek help §6- Displays this message");
        sender.sendMessage("§f/ek reload §6- Reloads the plugin configuration");
        sender.sendMessage("§f/ek save §6- Saves the kits to the database");
        sender.sendMessage("§f/ek list §6- Lists all the available kits");
        sender.sendMessage("§f/ek give <player> <kit> §6- Gives a kit to a player");
        sender.sendMessage("§f/ek create <kit> §6- Creates a new kit");
        sender.sendMessage("§f/ek delete <kit> §6- Deletes a kit");
        sender.sendMessage("§f/ek edit <kit> §6- Edits a kit");
        sender.sendMessage("§f/ek preview <kit> §6- Previews a kit");
        sender.sendMessage("§f/ek seticon <kit> §6- Sets the icon of a kit");
        sender.sendMessage("§f/ek displayname <kit> <display name> §6- Sets the display name of a kit");
        sender.sendMessage("§f/ek cooldown <kit> <cooldown> §6- Sets the cooldown of a kit");
        sender.sendMessage("§f/ek permission <kit> <permission> §6- Sets the permission of a kit");
        sender.sendMessage("§f/ek description <kit> <description> §6- Sets the description of a kit");
        sender.sendMessage("§f/ek onetime <kit> <true|false> §6- Sets the one-time status of a kit");
        sender.sendMessage("§f/ek cost <kit> <cost> §6- Sets the cost of a kit (0 to make it free)");
        sender.sendMessage("§f/ek enabled <kit> <true|false> §6- Sets the enabled status of a kit");
        sender.sendMessage(
                "§f/ek clearinventory <kit> <true|false> §6- Sets the clear inventory status of a kit");
        sender.sendMessage("§f/ek cleararmor <kit> <true|false> §6- Sets the clear armor status of a kit");
        sender.sendMessage("§f/ek autoarmor <kit> <true|false> §6- Sets the auto armor status of a kit");
        sender.sendMessage("§f/ek autoshield <kit> <true|false> §6- Sets the auto shield status of a kit");
        sender.sendMessage("§f/ek autoelytra <kit> <true|false> §6- Sets the auto elytra status of a kit");
        sender.sendMessage("§f/ek additem <kit> §6- Adds the item in hand to a kit");
        sender.sendMessage("§f/ek info <kit> §6- Displays information about a kit");
    }

    @Override
    public String getPermission() {
        return "epickits.help";
    }
}
