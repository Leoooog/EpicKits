package it.geusa.epickits.commands.subcommands;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import it.geusa.epickits.commands.EKSubCommand;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class DeleteSubCommand implements EKSubCommand {

    private final Cache<CommandSender, String> deleteCache;

    public DeleteSubCommand() {
        deleteCache = CacheBuilder.newBuilder()
                .expireAfterAccess(plugin.getConfigManager().secondsToConfirmCommand(), TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§cUsage: /ek delete <kit>");
            return;
        }
        String kit = deleteCache.getIfPresent(sender);
        if (kit == null) {
            kit = args[0];
            deleteCache.put(sender, kit);
            sender.sendMessage(
                    "§aAre you sure you want to delete the kit §6" + kit + "§a? Type the command again in the next "
                            + plugin.getConfigManager().secondsToConfirmCommand()
                            + "seconds to confirm.");
        }
        else {
            deleteCache.invalidate(sender);
            if (plugin.getKitManager().deleteKit(kit))
                sender.sendMessage("§aKit §6" + kit + "§a has been deleted successfully.");
            else
                sender.sendMessage("§cAn error occurred while deleting the kit §6" + kit
                        + "§c. Please check the console for more information.");
        }
    }

    @Override
    public String getPermission() {
        return "epickits.delete";
    }
}
