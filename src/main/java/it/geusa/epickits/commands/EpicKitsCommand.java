package it.geusa.epickits.commands;

import it.geusa.epickits.EpicKits;
import it.geusa.epickits.commands.subcommands.*;
import it.geusa.epickits.models.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class EpicKitsCommand implements CommandExecutor, TabExecutor {
    private final EpicKits plugin = EpicKits.getInstance();

    private final HashMap<String, EKSubCommand> subCommands = new HashMap<>();

    public EpicKitsCommand() {
        subCommands.put("help", new HelpSubCommand());
        subCommands.put("reload", new ReloadSubCommand());
        subCommands.put("force-reload", new ForceReloadSubCommand());
        subCommands.put("save", new SaveSubCommand());
        subCommands.put("list", new ListSubCommand());
        subCommands.put("create", new CreateSubCommand());
        subCommands.put("delete", new DeleteSubCommand());
        subCommands.put("edit", new EditSubCommand());
        subCommands.put("give", new GiveSubCommand());
        subCommands.put("preview", new PreviewSubCommand());
        subCommands.put("info", new InfoSubCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            String version = plugin.getDescription().getVersion();
            sender.sendMessage("§6EpicKits v" + version + " by Leog04");
            sender.sendMessage("§6Type §f/ek help §6for a list of commands");
            return true;
        }
        String subCommand = args[0].toLowerCase();
        if (subCommands.containsKey(subCommand)) {
            EKSubCommand subCommandClass = subCommands.get(subCommand);
            if (sender.hasPermission(subCommandClass.getPermission())) {
                subCommandClass.execute(sender, Arrays.copyOfRange(args, 1, args.length));
            }
            else {
                sender.sendMessage("§cYou do not have permission to use this command");
            }
        }
        else {
            sender.sendMessage("§cUnknown command. Type §f/ek help §cfor a list of commands");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return subCommands.keySet().stream().toList();
        }
        String subCommand = args[0].toLowerCase();
        if (args.length == 1) {
            return subCommands.keySet().stream().filter(s -> s.startsWith(subCommand)).toList();
        }
        String arg1 = args[1].toLowerCase();
        if (args.length == 2) {
            switch (subCommand) {
                case "delete", "edit", "preview", "info" -> {
                    return plugin.getKitManager().getKits().stream().map(Kit::getId).filter(id -> id.startsWith(arg1))
                            .toList();
                }
                case "give" -> {
                    return plugin.getServer().getOnlinePlayers().stream().map(Player::getName)
                            .filter(name -> name.startsWith(arg1)).toList();
                }
            }
        }

        if (args.length == 3) {
            String arg2 = args[2].toLowerCase();
            switch (subCommand) {
                case "give" -> {
                    return plugin.getKitManager().getKits().stream().map(Kit::getId).filter(id -> id.startsWith(arg2))
                            .toList();
                }
                case "edit" -> {
                    return EditSubCommand.properties.stream().filter(prop -> prop.startsWith(arg2)).toList();
                }
                case "create" -> {
                    return Stream.of("gui", "inv").filter(mode -> mode.startsWith(arg2)).toList();
                }
            }
        }
        if (args.length == 4) {
            String arg3 = args[3].toLowerCase();
            if (subCommand.equals("edit") && EditSubCommand.booleanProperties.contains(arg1)) {
                return Stream.of("true", "false").filter(id -> id.startsWith(arg3)).toList();
            }
        }
        return Collections.emptyList();
    }
}
