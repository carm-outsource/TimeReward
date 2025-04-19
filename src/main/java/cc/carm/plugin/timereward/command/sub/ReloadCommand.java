package cc.carm.plugin.timereward.command.sub;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.command.MainCommand;
import cc.carm.plugin.timereward.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand<MainCommand> {

    public ReloadCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {

        long s1 = System.currentTimeMillis();
        PluginMessages.RELOAD.START.sendTo(sender);

        try {
            Main.getInstance().getConfigProvider().reload();
            Main.getInstance().getMessageProvider().reload();
            Main.getInstance().getRewardProvider().reload();

            PluginMessages.RELOAD.COMPLETE.prepare(
                    System.currentTimeMillis() - s1,
                    TimeRewardAPI.getRewardManager().listRewards().size()
            ).to(sender);
        } catch (Exception e) {
            PluginMessages.RELOAD.ERROR.sendTo(sender);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("TimeReward.admin");
    }

}