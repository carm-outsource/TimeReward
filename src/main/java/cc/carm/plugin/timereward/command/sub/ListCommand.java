package cc.carm.plugin.timereward.command.sub;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.command.MainCommand;
import cc.carm.plugin.timereward.conf.PluginMessages;
import cc.carm.plugin.timereward.storage.RewardContents;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ListCommand extends SubCommand<MainCommand> {

    public ListCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        Collection<RewardContents> awards = TimeRewardAPI.getRewardManager().listRewards().values();
        PluginMessages.LIST.HEADER.send(sender, awards.size());

        for (RewardContents reward : awards) {
            if (reward.getPermission() != null) {
                PluginMessages.LIST.OBJECT_PERM.send(sender,
                        reward.getRewardID(), reward.getDisplayName(),
                        reward.getTime(), reward.getPermission()
                );
            } else {
                PluginMessages.LIST.OBJECT.send(sender,
                        reward.getRewardID(), reward.getDisplayName(), reward.getTime()
                );
            }
        }
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("TimeReward.admin");
    }

}
