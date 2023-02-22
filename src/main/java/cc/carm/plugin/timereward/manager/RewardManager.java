package cc.carm.plugin.timereward.manager;

import cc.carm.lib.easyplugin.utils.MessageUtils;
import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.conf.FunctionConfig;
import cc.carm.plugin.timereward.conf.PluginConfig;
import cc.carm.plugin.timereward.storage.RewardContents;
import cc.carm.plugin.timereward.storage.UserData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RewardManager {

    protected BukkitRunnable runnable;

    public RewardManager(Main main) {
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!FunctionConfig.AUTO_CLAIM.getNotNull()) return;
                if (Bukkit.getOnlinePlayers().isEmpty()) return;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    List<RewardContents> unclaimedRewards = getUnclaimedRewards(player);
                    if (unclaimedRewards.isEmpty()) continue;

                    main.getScheduler().run(() -> unclaimedRewards.forEach(
                            // 在同步进程中为玩家发放奖励
                            unclaimedReward -> claimReward(player, unclaimedReward, false)
                    ));
                }
            }
        };
        this.runnable.runTaskTimerAsynchronously(main, 100L, 20L);
    }

    public void shutdown() {
        this.runnable.cancel();
        this.runnable = null;
    }

    protected Map<String, RewardContents> getRewards() {
        return PluginConfig.REWARDS.getNotNull().getContents();
    }

    public @Nullable RewardContents getReward(String rewardID) {
        return getRewards().get(rewardID);
    }

    public Map<String, RewardContents> listRewards() {
        return Collections.unmodifiableMap(PluginConfig.REWARDS.getNotNull().getContents());
    }

    @Unmodifiable
    public @NotNull List<RewardContents> getUnclaimedRewards(@NotNull Player player) {
        return listRewards().values().stream()
                .filter(reward -> isClaimable(player, reward))
                .collect(Collectors.toList());
    }


    public boolean isClaimable(Player player, RewardContents reward) {
        UserData user = TimeRewardAPI.getUserManager().getData(player);
        return !user.isClaimed(reward.getRewardID()) // 未曾领取
                && reward.isTimeEnough(user.getAllSeconds()) // 时间足够
                && reward.checkPermission(player); // 满足权限
    }

    public boolean claimReward(Player player, RewardContents reward, boolean check) {
        if (check && !isClaimable(player, reward)) return false;

        TimeRewardAPI.getUserManager().getData(player).addClaimedReward(reward.getRewardID());
        executeCommand(player, reward);
        return true;
    }

    public void executeCommand(Player player, RewardContents reward) {
        Main.debugging("正在为玩家 " + player.getName() + " 执行奖励 " + reward.getRewardID() + " 的相关指令。");
        List<String> finalCommands = MessageUtils.setCustomParams(
                reward.getCommands(), "%(name)", reward.getDisplayName()
        );
        TimeRewardAPI.executeCommands(player, finalCommands); // 执行命令
    }

    public boolean claimReward(Player player, RewardContents reward) {
        return claimReward(player, reward, true);
    }

}