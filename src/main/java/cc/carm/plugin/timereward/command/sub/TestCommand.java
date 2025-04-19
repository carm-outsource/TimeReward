package cc.carm.plugin.timereward.command.sub;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.command.MainCommand;
import cc.carm.plugin.timereward.conf.PluginMessages;
import cc.carm.plugin.timereward.data.RewardContents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestCommand extends SubCommand<MainCommand> {

    public TestCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (args.length < 1) return getParent().noArgs(sender);
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.sendTo(sender);
            return null;
        }

        RewardContents contents = TimeRewardAPI.getRewardManager().getReward(args[0]);
        if (contents == null) {
            PluginMessages.NOT_EXISTS.sendTo(sender, args[0]);
            return null;
        }

        PluginMessages.COMMAND_LIST.sendTo(sender, contents.getRewardID());

        TimeRewardAPI.getRewardManager().executeCommand((Player) sender, contents);
        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return SimpleCompleter.text(args[args.length - 1], TimeRewardAPI.getRewardManager().listRewards().keySet());
        } else return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("TimeReward.admin");
    }


}
