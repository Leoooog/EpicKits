package it.geusa.epickits.commands.subcommands;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import it.geusa.epickits.commands.EKSubCommand;
import org.bukkit.command.CommandSender;

public class ForceReloadSubCommand implements EKSubCommand {

    private final Cache<CommandSender, Boolean> confirmSenders;

    public ForceReloadSubCommand() {
        confirmSenders = CacheBuilder.newBuilder()
                .expireAfterWrite(plugin.getConfigManager().secondsToConfirmCommand(),
                        java.util.concurrent.TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (confirmSenders.getIfPresent(sender) != null) {
            confirmSenders.invalidate(sender);
            plugin.reload(true);
            sender.sendMessage("§aThe plugin configuration and database have been reloaded successfully");
        }
        else {
            confirmSenders.put(sender, true);
            sender.sendMessage(
                    "§cAre you sure you want to force the reload? Type §f/ek force-reload §cagain in the next "
                            + plugin.getConfigManager().secondsToConfirmCommand()
                            + "seconds to confirm");
        }
    }

    @Override
    public String getPermission() {
        return "epickits.force-reload";
    }
}
