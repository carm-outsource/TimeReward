package cc.carm.plugin.timereward;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.easyplugin.utils.MessageUtils;
import cc.carm.plugin.timereward.manager.RewardManager;
import cc.carm.plugin.timereward.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class TimeRewardAPI {

    private TimeRewardAPI() {
    }

    public static UserManager getUserManager() {
        return Main.getInstance().userManager;
    }

    public static RewardManager getRewardManager() {
        return Main.getInstance().rewardManager;
    }

    public static void executeCommands(Player player, List<String> commands) {
        if (commands == null || commands.isEmpty()) return;
        for (String command : commands) {
            try {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parseCommand(player, command));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private static String parseCommand(Player player, String command) {
        return MessageUtils.setPlaceholders(player, ColorParser.parse(command));
    }

}
