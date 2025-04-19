package cc.carm.plugin.timereward.listener;

import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.TimeRewardAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;

public class UserListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        @Nullable Player player = event.getPlayer();
        // Delayed load to avoid async error
        Main.getInstance().getScheduler().runLater(20L, () -> {
            if (player == null || !player.isOnline()) return;
            TimeRewardAPI.getUserManager()
                    .load(event.getPlayer().getUniqueId(), true)
                    .thenAccept(data -> {
                        if (!player.isOnline()) {  // 不在线自动卸载
                            TimeRewardAPI.getUserManager().unload(data.getUserUUID(), false);
                        }
                    }).exceptionally(ex -> {
                        Main.severe("加载用户在线时长数据失败: " + event.getPlayer().getName());
                        ex.printStackTrace();
                        return null;
                    });
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        TimeRewardAPI.getUserManager().unload(event.getPlayer().getUniqueId());
    }

}
