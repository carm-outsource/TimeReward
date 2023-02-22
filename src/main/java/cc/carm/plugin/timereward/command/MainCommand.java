package cc.carm.plugin.timereward.command;

import cc.carm.lib.easyplugin.command.CommandHandler;
import cc.carm.plugin.timereward.command.sub.*;
import cc.carm.plugin.timereward.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends CommandHandler {

    public MainCommand(@NotNull JavaPlugin plugin) {
        super(plugin);

        registerSubCommand(new ClaimCommand(this, "claim"));
        registerSubCommand(new ListCommand(this, "list"));
        registerSubCommand(new UserCommand(this, "user"));
        registerSubCommand(new TestCommand(this, "test"));
        registerSubCommand(new ReloadCommand(this, "reload"));
    }

    @Override
    public Void noArgs(CommandSender sender) {
        if (sender.hasPermission("TimeReward.admin")) {
            PluginMessages.USAGE.ADMIN.send(sender);
        } else {
            PluginMessages.USAGE.USER.send(sender);
        }
        return null;
    }

    @Override
    public Void noPermission(CommandSender sender) {
        PluginMessages.NO_PERMISSION.send(sender);
        return null;
    }
}
