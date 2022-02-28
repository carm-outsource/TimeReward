package cc.carm.plugin.timereward.listener;

import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.TimeRewardAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Main.getInstance().getScheduler().runAsync(() -> TimeRewardAPI.getUserManager().load(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Main.getInstance().getScheduler().runAsync(() -> TimeRewardAPI.getUserManager().unload(event.getPlayer().getUniqueId()));
    }

}
