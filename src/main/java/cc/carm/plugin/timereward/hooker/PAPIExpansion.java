package cc.carm.plugin.timereward.hooker;

import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.data.RewardContents;
import cc.carm.plugin.timereward.data.TimeRewardUser;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PAPIExpansion extends PlaceholderExpansion {

    private static final List<String> PLACEHOLDERS = Arrays.asList(
            "%TimeReward_time%",
            "%TimeReward_reward_<奖励ID>%",
            "%TimeReward_claimed_<奖励ID>%"
    );

    private final JavaPlugin plugin;

    public PAPIExpansion(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return PLACEHOLDERS;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getDescription().getName();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "加载中...";
        String[] args = identifier.split("_");

        if (args.length < 1) {
            return "Error Params";
        }

        TimeRewardUser user = TimeRewardAPI.getUserManager().get(player);

        switch (args[0].toLowerCase()) {
            case "time": {
                return Long.toString(user.getAllSeconds());
            }
            case "reward": {
                if (args.length < 2) return "请填写奖励ID";
                String rewardName = args[1];
                RewardContents contents = TimeRewardAPI.getRewardManager().getReward(rewardName);
                if (contents == null) return "奖励不存在";
                return contents.getDisplayName();
            }
            case "claimed": {
                if (args.length < 2) return "请填写奖励ID";
                return Boolean.toString(user.isClaimed(args[1]));
            }
            case "version": {
                return getVersion();
            }
            default: {
                return "参数错误";
            }
        }
    }

}
