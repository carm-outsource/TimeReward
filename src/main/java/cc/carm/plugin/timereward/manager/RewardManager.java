package cc.carm.plugin.timereward.manager;

import cc.carm.lib.easyplugin.utils.MessageUtils;
import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.conf.RewardsConfig;
import cc.carm.plugin.timereward.data.RewardContents;
import cc.carm.plugin.timereward.user.UserRewardData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RewardManager {

    protected BukkitRunnable runnable;

    public RewardManager(Main main) {
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().isEmpty()) return;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    List<RewardContents> rewards = getUnclaimedRewards(player).stream()
                            .filter(RewardContents::isAutoClaimed)
                            .collect(Collectors.toList());
                    if (rewards.isEmpty()) continue;

                    //为玩家发放奖励
                    claimRewards(player, rewards, true); // 二次检查避免重复发奖
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
        return RewardsConfig.getContents();
    }

    public @Nullable RewardContents getReward(String rewardID) {
        return getRewards().get(rewardID);
    }

    public Map<String, RewardContents> listRewards() {
        return Collections.unmodifiableMap(RewardsConfig.getContents());
    }

    @Unmodifiable
    public @NotNull List<RewardContents> getUnclaimedRewards(@NotNull Player player) {
        return listRewards().values().stream()
                .filter(reward -> isClaimable(player, reward))
                .collect(Collectors.toList());
    }

    public boolean isClaimable(Player player, RewardContents reward) {
        UserRewardData user = TimeRewardAPI.getUserManager().get(player);

        return !user.isClaimed(reward) // 未曾领取
                && user.isTimeEnough(reward)// 时间足够
                && reward.checkPermission(player); // 满足权限
    }

    public CompletableFuture<Boolean> claimReward(Player player, RewardContents reward, boolean check) {
        return claimRewards(player, Collections.singletonList(reward), check);
    }

    public CompletableFuture<Boolean> claimRewards(Player player, Collection<RewardContents> rewards, boolean check) {
        Set<RewardContents> contents = rewards.stream()
                .filter(r -> !check || isClaimable(player, r))
                .collect(Collectors.toSet());

        Map<String, LocalDateTime> map = new LinkedHashMap<>();
        contents.forEach(reward -> map.put(reward.getRewardID(), LocalDateTime.now()));

        return Main.getInstance().supplyAsync(() -> {
            try {

                UserRewardData user = TimeRewardAPI.getUserManager().get(player);
                Main.getStorage().addClaimedData(player.getUniqueId(), map);
                contents.forEach(user::addClaimedReward);

                return true;
            } catch (Exception ex) {
                Main.severe("为玩家 " + player.getName() + " 领取奖励时发生错误。");
                ex.printStackTrace();
                return false;
            }
        }).thenCompose(result -> {
            if (result) {
                return Main.getInstance().supplySync(() -> {
                    rewards.forEach(reward -> executeCommand(player, reward));
                    return true;
                });
            } else {
                return CompletableFuture.completedFuture(false);
            }
        });
    }

    public void executeCommand(Player player, RewardContents reward) {
        Main.debugging("正在为玩家 " + player.getName() + " 执行奖励 " + reward.getRewardID() + " 的相关指令。");
        List<String> finalCommands = MessageUtils.setCustomParams(
                reward.getCommands(), "%(name)", reward.getDisplayName()
        );
        TimeRewardAPI.executeCommands(player, finalCommands); // 执行命令
    }

}