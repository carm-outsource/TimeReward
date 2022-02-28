package cc.carm.plugin.timereward.manager;

import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.data.DataTaskRunner;
import cc.carm.plugin.timereward.data.TimeRewardUser;
import cc.carm.plugin.timereward.database.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UserManager {

    private final HashMap<UUID, TimeRewardUser> userDataMap;

    public UserManager() {
        this.userDataMap = new HashMap<>();
    }

    @NotNull
    public TimeRewardUser read(UUID userUUID) {
        try {
            long start = System.currentTimeMillis();
            DataManager storage = Main.getDataManager();

            Long playTime = storage.getPlayTime(userUUID);
            Set<String> claimedRewards = storage.getClaimedData(userUUID);

            if (playTime == null && claimedRewards.isEmpty()) {
                Main.debugging("当前还不存在玩家 " + userUUID + " 的数据，视作新档。");
                return new TimeRewardUser(userUUID);
            }

            Main.debugging("读取 " + userUUID + " 的用户数据完成， 耗时 " + (System.currentTimeMillis() - start) + "ms。");
            return new TimeRewardUser(userUUID, claimedRewards, playTime == null ? 0L : playTime);
        } catch (Exception e) {
            Main.severe("无法正常读取玩家数据，玩家操作将不会被保存，请检查数据配置！");
            Main.severe("Could not load user's data, please check the data configuration!");
            e.printStackTrace();
            return new TimeRewardUser(userUUID);
        }
    }

    public void save(TimeRewardUser user) {
        try {
            long start = System.currentTimeMillis();
            DataManager dataManager = Main.getDataManager();

            Main.debugging("正在保存 " + user.getUserUUID() + " 的用户数据...");
            dataManager.saveClaimedData(user.getUserUUID(), user.getClaimedRewards());
            dataManager.savePlayTime(user.getUserUUID(), user.getAllSeconds());
            Main.debugging("保存 " + user.getUserUUID() + " 的用户数据完成，耗时 " + (System.currentTimeMillis() - start) + "ms。");

        } catch (Exception e) {
            Main.severe("无法正常保存玩家数据，请检查数据配置！");
            Main.severe("Could not save user's data, please check the data configuration!");
            e.printStackTrace();
        }
    }

    public void load(UUID userUUID) {
        getUsersMap().put(userUUID, read(userUUID));
    }

    public void unload(UUID userUUID) {
        unload(userUUID, true);
    }

    public void unload(UUID userUUID, boolean save) {
        TimeRewardUser data = get(userUUID);
        if (data == null) return;
        if (save) save(data);
        getUsersMap().remove(userUUID);
    }

    public void loadAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getUsersMap().containsKey(player.getUniqueId())) continue;
            load(player.getUniqueId());
        }
    }

    public void saveAll() {
        getUsersMap().values().forEach(this::save);
    }

    public void unloadAll(boolean save) {
        if (save) saveAll();
        getUsersMap().clear();
    }

    @Nullable
    public TimeRewardUser get(UUID userUUID) {
        return getUsersMap().get(userUUID);
    }

    @NotNull
    public TimeRewardUser get(Player player) {
        return getUsersMap().get(player.getUniqueId());
    }

    public void editData(@NotNull UUID uuid, @NotNull DataTaskRunner task) {
        TimeRewardUser user = get(uuid);
        if (user == null) user = read(uuid); // 不在线则加载数据
        try {
            task.run(user, Main.getDataManager());
        } catch (Exception exception) {
            Main.severe("无法正常更改玩家数据，请检查数据配置！");
            Main.severe("Could not edit user's data, please check the data configuration!");
            exception.printStackTrace();
        }
    }

    public void editDataAsync(@NotNull UUID uuid, @NotNull DataTaskRunner task) {
        Main.getInstance().getScheduler().runAsync(() -> editData(uuid, task));
    }

    @NotNull
    @Unmodifiable
    public Map<UUID, TimeRewardUser> list() {
        return new HashMap<>(getUsersMap());
    }

    protected @NotNull HashMap<UUID, TimeRewardUser> getUsersMap() {
        return userDataMap;
    }

}
