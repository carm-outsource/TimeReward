package cc.carm.plugin.timereward.listener;

import cc.carm.plugin.timereward.TimeRewardAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TimeRewardAPI.getUserManager().load(event.getPlayer().getUniqueId(), true).thenAccept(data -> {
            Player player = Bukkit.getPlayer(data.getUserUUID());
            if (player == null || !player.isOnline()) {
                TimeRewardAPI.getUserManager().unload(data.getUserUUID());
            } // 不在线自动卸载
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        TimeRewardAPI.getUserManager().unload(event.getPlayer().getUniqueId());
    }

}
