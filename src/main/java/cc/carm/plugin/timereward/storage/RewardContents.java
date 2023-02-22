package cc.carm.plugin.timereward.storage;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.plugin.timereward.Main;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RewardContents {

    public final @NotNull String id;

    private final long time;
    private final @Nullable String name;
    private final @Nullable String permission;
    private final @NotNull List<String> commands;


    public RewardContents(@NotNull String id, long time,
                          @Nullable String name, @Nullable String permission,
                          @NotNull List<String> commands) {
        this.id = id;
        this.time = time;
        this.name = name;
        this.permission = permission;
        this.commands = commands;
    }

    public String getRewardID() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public @Nullable String getName() {
        return name;
    }

    public @NotNull String getDisplayName() {
        return ColorParser.parse(getName() == null ? getRewardID() : getName());
    }

    public @Nullable String getPermission() {
        return permission;
    }

    public @NotNull List<String> getCommands() {
        return commands;
    }

    public boolean checkPermission(@NotNull Player player) {
        return permission == null || player.hasPermission(permission);
    }

    public boolean isTimeEnough(long requireSeconds) {
        return requireSeconds >= getTime();
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("time", getTime());
        if (getName() != null) map.put("name", getName());
        if (getPermission() != null) map.put("permission", getPermission());
        map.put("commands", getCommands());
        return map;
    }

    public static RewardContents parse(String id, @NotNull ConfigurationWrapper<?> section) {
        long time = section.getLong("time", -1L);
        if (time <= 0) {
            Main.severe("奖励 " + id + " 的时间配置错误，请检查配置文件。");
            return null;
        }

        return new RewardContents(
                id, time,
                section.getString("name"),
                section.getString("permission"),
                section.getStringList("commands")
        );
    }

    public static RewardContents defaults(String id) {
        return new RewardContents(
                id, 7200,
                "&f[初级奖励] &e总在线时长 2小时", "TimeReward.vip",
                Collections.singletonList("say &f恭喜 &b%player_name% &f领取了奖励 &r%(name) &f！")
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RewardContents that = (RewardContents) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static final class Group {
        final @NotNull Map<String, RewardContents> contents;

        public Group(@NotNull Map<String, RewardContents> contents) {
            this.contents = contents;
        }

        public @NotNull Map<String, RewardContents> getContents() {
            return contents;
        }

        public Map<String, Object> serialize() {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Map.Entry<String, RewardContents> entry : contents.entrySet()) {
                map.put(entry.getKey(), entry.getValue().serialize());
            }
            return map;
        }

        public static Group parse(@NotNull ConfigurationWrapper<?> section) {
            Map<String, RewardContents> rewards = new LinkedHashMap<>();
            for (String rewardID : section.getKeys(false)) {
                ConfigurationWrapper<?> rewardSection = section.getConfigurationSection(rewardID);
                if (rewardSection == null) continue;

                RewardContents c = RewardContents.parse(rewardID, rewardSection);
                if (c == null) continue;
                rewards.put(rewardID, c);
            }
            return new Group(rewards);
        }

        public static Group defaults() {
            Map<String, RewardContents> rewards = new LinkedHashMap<>();
            rewards.put("example", RewardContents.defaults("example"));
            return new Group(rewards);
        }

    }

}
