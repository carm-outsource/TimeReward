package cc.carm.plugin.timereward.manager;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.user.UserDataManager;
import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.data.IntervalType;
import cc.carm.plugin.timereward.data.TimeRecord;
import cc.carm.plugin.timereward.storage.database.MySQLStorage;
import cc.carm.plugin.timereward.user.LockedRewardData;
import cc.carm.plugin.timereward.user.UserRewardData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.UUID;

public class UserManager extends UserDataManager<UUID, UserRewardData> {

    public UserManager(@NotNull EasyPlugin plugin) {
        super(plugin);
    }

    public @NotNull UserRewardData get(Player player) {
        return get(player.getUniqueId());
    }

    @Override
    protected @Nullable UserRewardData loadData(@NotNull UUID key) throws Exception {
        return Main.getStorage().loadData(key);
    }

    @Override
    public void saveData(@NotNull UserRewardData data) throws Exception {
        if (data instanceof LockedRewardData) return; // 不保存

        MySQLStorage storage = Main.getStorage();

        // 只需要保存游玩时间数据，领取数据已经实时保存了
        LocalDate date = LocalDate.now();
        Duration daily = data.getOnlineDuration(IntervalType.DAILY);
        Duration weekly = data.getOnlineDuration(IntervalType.WEEKLY);
        Duration monthly = data.getOnlineDuration(IntervalType.MONTHLY);
        Duration total = data.getOnlineDuration(IntervalType.TOTAL);
        TimeRecord newRecord = new TimeRecord(date, daily, weekly, monthly, total);

        storage.savePlayTime(data.getUserUUID(), newRecord);
    }

    @Override
    public @NotNull UserRewardData emptyUser(@NotNull UUID key) {
        return new UserRewardData(key, TimeRecord.empty(), new LinkedHashMap<>());
    }

    @Override
    public @NotNull UserRewardData errorUser(@NotNull UUID key) {
        return new LockedRewardData(key); // 避免影响数据存储
    }

}
