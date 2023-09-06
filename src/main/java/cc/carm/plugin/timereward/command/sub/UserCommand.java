package cc.carm.plugin.timereward.command.sub;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.command.MainCommand;
import cc.carm.plugin.timereward.conf.PluginMessages;
import cc.carm.plugin.timereward.data.IntervalType;
import cc.carm.plugin.timereward.user.UserRewardData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserCommand extends SubCommand<MainCommand> {

    public UserCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (args.length < 1) return getParent().noArgs(sender);

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            PluginMessages.NOT_ONLINE.send(sender, args[0]);
            return null;
        }

        UserRewardData user = TimeRewardAPI.getUserManager().get(player);
        PluginMessages.USER_INFO.prepare(
                player.getName(),
                user.getOnlineDuration(IntervalType.DAILY).getSeconds(),
                user.getOnlineDuration(IntervalType.WEEKLY).getSeconds(),
                user.getOnlineDuration(IntervalType.MONTHLY).getSeconds(),
                user.getOnlineDuration(IntervalType.TOTAL).getSeconds(),
                user.getClaimedRewards().size()
        ).to(sender);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        user.getClaimedRewards().forEach((id, time) -> {
            PluginMessages.USER_RECEIVED.send(sender, id, time.format(formatter));
        });

        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) return SimpleCompleter.onlinePlayers(args[0]);
        else return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("TimeReward.admin");
    }

}
