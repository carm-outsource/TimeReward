package cc.carm.plugin.timereward.manager;

import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.util.DataTaskRunner;
import cc.carm.plugin.timereward.storage.UserData;
import cc.carm.plugin.timereward.storage.database.MySQLStorage;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private final HashMap<UUID, UserData> userDataMap;

    public UserManager() {
        this.userDataMap = new HashMap<>();
    }

    @NotNull
    public UserData readData(UUID userUUID) {
        try {
            long start = System.currentTimeMillis();
            MySQLStorage storage = Main.getStorage();

            UserData data = storage.loadData(userUUID);

            if (data == null) {
                Main.debugging("当前还不存在玩家 " + userUUID + " 的数据，视作新档。");
                return new UserData(userUUID);
            }

            Main.debugging("读取 " + userUUID + " 的用户数据完成， 耗时 " + (System.currentTimeMillis() - start) + "ms。");

            return data;
        } catch (Exception e) {
            Main.severe("无法正常读取玩家数据，玩家操作将不会被保存，请检查数据配置！");
            Main.severe("Could not loadData user's data, please check the data conf!");
            e.printStackTrace();
            return new UserData(userUUID);
        }
    }

    public void saveData(UserData data) {
        try {
            long start = System.currentTimeMillis();
            MySQLStorage storage = Main.getStorage();

            Main.debugging("正在保存 " + data.getUserUUID() + " 的用户数据...");
            storage.saveClaimedData(data.getUserUUID(), data.getClaimedRewards());
            storage.savePlayTime(data.getUserUUID(), data.getAllSeconds());
            Main.debugging("保存 " + data.getUserUUID() + " 的用户数据完成，耗时 " + (System.currentTimeMillis() - start) + "ms。");

        } catch (Exception e) {
            Main.severe("无法正常保存玩家数据，请检查数据配置！");
            Main.severe("Could not saveData user's data, please check the data conf!");
            e.printStackTrace();
        }
    }

    public void loadData(UUID userUUID) {
        getUserDataMap().put(userUUID, readData(userUUID));
    }

    public void unloadData(UUID userUUID) {
        unloadData(userUUID, true);
    }

    public void unloadData(UUID userUUID, boolean save) {
        UserData data = getData(userUUID);
        if (data == null) return;
        if (save) saveData(data);
        getUserDataMap().remove(userUUID);
    }

    public void loadAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getUserDataMap().containsKey(player.getUniqueId())) continue;
            loadData(player.getUniqueId());
        }
    }

    public void saveAll() {
        getUserDataMap().values().forEach(this::saveData);
    }

    public void unloadAll(boolean save) {
        if (save) saveAll();
        getUserDataMap().clear();
    }

    @Nullable
    public UserData getData(UUID userUUID) {
        return getUserDataMap().get(userUUID);
    }

    @NotNull
    public UserData getData(Player player) {
        return getUserDataMap().get(player.getUniqueId());
    }

    public void editData(@NotNull DataTaskRunner task) {
        try {
            task.run(Main.getStorage());
        } catch (Exception exception) {
            Main.severe("无法正常更改玩家数据，请检查数据配置！");
            Main.severe("Could not edit user's data, please check the data conf!");
            exception.printStackTrace();
        }
    }

    public void editDataAsync(@NotNull DataTaskRunner task) {
        Main.getInstance().getScheduler().runAsync(() -> editData(task));
    }

    @NotNull
    @Unmodifiable
    public Map<UUID, UserData> listUserData() {
        return ImmutableMap.copyOf(getUserDataMap());
    }

    protected @NotNull HashMap<UUID, UserData> getUserDataMap() {
        return userDataMap;
    }

}
