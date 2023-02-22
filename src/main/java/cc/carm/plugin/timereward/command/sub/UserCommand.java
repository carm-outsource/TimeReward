package cc.carm.plugin.timereward.command.sub;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.command.MainCommand;
import cc.carm.plugin.timereward.conf.PluginMessages;
import cc.carm.plugin.timereward.storage.UserData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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

        UserData user = TimeRewardAPI.getUserManager().getData(player);
        PluginMessages.USER_INFO.send(sender,
                player.getName(), user.getAllSeconds(),
                user.getClaimedRewards().size(), String.join("&8, &f", user.getClaimedRewards())
        );
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
