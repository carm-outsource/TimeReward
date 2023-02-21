package cc.carm.plugin.timereward.storage;

import cc.carm.lib.easyplugin.utils.ColorParser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

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
