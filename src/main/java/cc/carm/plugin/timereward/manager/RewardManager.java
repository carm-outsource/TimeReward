package cc.carm.plugin.timereward.manager;

import cc.carm.lib.easyplugin.utils.MessageUtils;
import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.configuration.PluginConfig;
import cc.carm.plugin.timereward.data.RewardContents;
import cc.carm.plugin.timereward.data.TimeRewardUser;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RewardManager {

    private BukkitRunnable rewardChecker;

    public void initialize() {
        this.rewardChecker = new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().isEmpty()) return;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    List<RewardContents> unclaimedRewards = getUnclaimedRewards(player);
                    if (unclaimedRewards.isEmpty()) continue;

                    Main.getInstance().getScheduler().run(() -> unclaimedRewards.forEach(
                            // 在同步进程中为玩家发放奖励
                            unclaimedReward -> claimReward(player, unclaimedReward, false)
                    ));
                }

            }
        };

        this.rewardChecker.runTaskTimerAsynchronously(Main.getInstance(), 100L, 20L);
    }

    public void shutdown() {
        this.rewardChecker.cancel();
        this.rewardChecker = null;
    }

    public HashMap<String, RewardContents> readRewards(@NotNull ConfigurationSection section) {
        HashMap<String, RewardContents> rewards = new HashMap<>();
        for (String rewardID : section.getKeys(false)) {
            ConfigurationSection rewardSection = section.getConfigurationSection(rewardID);
            if (rewardSection == null) continue;
            long time = rewardSection.getLong("time");
            if (time <= 0) {
                Main.severe("奖励 " + rewardID + " 的时间配置错误，请检查配置文件。");
                continue;
            }

            rewards.put(rewardID, new RewardContents(
                    rewardID, time,
                    rewardSection.getString("name"),
                    rewardSection.getString("permission"),
                    rewardSection.getStringList("commands")
            ));
        }
        return rewards;
    }

    public Map<String, RewardContents> listRewards() {
        return new HashMap<>(getRewardsMap());
    }

    public RewardContents getReward(String rewardID) {
        return getRewardsMap().get(rewardID);
    }

    protected HashMap<String, RewardContents> getRewardsMap() {
        return PluginConfig.REWARDS.getOptional().orElse(new HashMap<>());
    }

    public List<RewardContents> getUnclaimedRewards(Player player) {
        TimeRewardUser user = TimeRewardAPI.getUserManager().get(player);
        return listRewards().values().stream()
                .filter(reward -> reward.isTimeEnough(user.getAllSeconds())) // 时间足够
                .filter(reward -> !user.isClaimed(reward.getRewardID())) // 未曾领取
                .filter(reward -> reward.checkPermission(player)) // 满足权限
                .collect(Collectors.toList());
    }

    public boolean claimReward(Player player, RewardContents reward, boolean check) {
        TimeRewardUser user = TimeRewardAPI.getUserManager().get(player);
        if (check && user.isClaimed(reward.getRewardID())) return false;
        if (check && !reward.isTimeEnough(user.getAllSeconds())) return false;
        if (check && !reward.checkPermission(player)) return false;

        user.addClaimedReward(reward.getRewardID());
        List<String> finalCommands = MessageUtils.setCustomParams(
                reward.getCommands(), "%(reward_name)", reward.getDisplayName()
        );
        TimeRewardAPI.executeCommands(player, finalCommands); // 执行命令
        return true;
    }

    public boolean claimReward(Player player, RewardContents reward) {
        return claimReward(player, reward, true);
    }


}