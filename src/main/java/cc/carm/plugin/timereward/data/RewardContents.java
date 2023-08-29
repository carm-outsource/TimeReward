package cc.carm.plugin.timereward.data;

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

    private final boolean auto;


    public RewardContents(@NotNull String id, long time,
                          @Nullable String name, @Nullable String permission,
                          @NotNull List<String> commands, boolean auto) {
        this.id = id;
        this.time = time;
        this.name = name;
        this.permission = permission;
        this.commands = commands;
        this.auto = auto;
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

    public boolean isAutoClaimed() {
        return auto;
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
        map.put("auto", auto);
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
                section.getStringList("commands"),
                section.getBoolean("auto", false)
        );
    }

    public static RewardContents defaults(String id) {
        return new RewardContents(
                id, 7200,
                "&f[初级奖励] &e总在线时长 2小时", "TimeReward.vip",
                Collections.singletonList("say &f恭喜 &b%player_name% &f领取了奖励 &r%(name) &f！"),
                true
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



}
