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

    public final @NotNull IntervalType type;
    private final long time;
    private final boolean loop;

    private final @Nullable String name;
    private final @Nullable String permission;
    private final @NotNull List<String> commands;

    private final boolean auto;

    public RewardContents(@NotNull String id, @NotNull IntervalType type, long time, boolean loop,
                          @Nullable String name, @Nullable String permission,
                          @NotNull List<String> commands, boolean auto) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.loop = loop;
        this.name = name;
        this.permission = permission;
        this.commands = commands;
        this.auto = auto;
    }

    public @NotNull String getRewardID() {
        return id;
    }

    public @NotNull IntervalType getType() {
        return type;
    }

    public boolean isLoop() {
        return loop;
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

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("time", getTime());
        map.put("type", getType().getID());
        map.put("loop", isLoop());
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

        IntervalType intervalType = IntervalType.parse(Objects.toString(section.get("type")));
        if (intervalType == null) {
            Main.severe("奖励 " + id + " 的类型配置错误，请检查配置文件。");
            return null;
        }

        return new RewardContents(
                id, intervalType, time, section.getBoolean("loop", false),
                section.getString("name"), section.getString("permission"),
                section.getStringList("commands"), section.getBoolean("auto", false)
        );
    }

    public static RewardContents defaults(String id) {
        return new RewardContents(
                id, IntervalType.TOTAL, 7200, false,
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
