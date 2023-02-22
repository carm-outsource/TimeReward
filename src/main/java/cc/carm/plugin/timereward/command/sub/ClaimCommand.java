package cc.carm.plugin.timereward.command.sub;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.command.MainCommand;
import cc.carm.plugin.timereward.conf.PluginMessages;
import cc.carm.plugin.timereward.manager.RewardManager;
import cc.carm.plugin.timereward.storage.RewardContents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClaimCommand extends SubCommand<MainCommand> {

    public ClaimCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        Player player = (Player) sender;
        RewardManager manager = TimeRewardAPI.getRewardManager();

        @Nullable String rewardID = args.length > 0 ? args[0] : null;
        if (rewardID == null) {

            List<RewardContents> unclaimedRewards = manager.getUnclaimedRewards(player);
            if (unclaimedRewards.isEmpty()) {
                PluginMessages.NO_UNCLAIMED_REWARD.send(sender);
                return null;
            }

            Main.getInstance().getScheduler().run(() -> unclaimedRewards.forEach(
                    // 在同步进程中为玩家发放奖励
                    unclaimedReward -> manager.claimReward(player, unclaimedReward, false)
            ));

        } else {

            RewardContents reward = manager.getReward(rewardID);
            if (reward == null) {
                PluginMessages.NOT_EXISTS.send(sender, rewardID);
                return null;
            }

            if (!manager.isClaimable(player, reward)) {
                PluginMessages.NOT_CLAIMABLE.send(sender, reward.getDisplayName());
                return null;
            }
            Main.getInstance().getScheduler().run(() -> manager.claimReward(player, reward, false));
        }
        return null;
    }
}
