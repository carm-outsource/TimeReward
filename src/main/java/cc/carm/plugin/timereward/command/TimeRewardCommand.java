package cc.carm.plugin.timereward.command;

import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.configuration.PluginMessages;
import cc.carm.plugin.timereward.data.RewardContents;
import cc.carm.plugin.timereward.data.TimeRewardUser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TimeRewardCommand implements CommandExecutor, TabCompleter {

    private boolean help(CommandSender sender) {
        PluginMessages.COMMAND_USAGE.send(sender);
        return true;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length < 1) return help(sender);
        String aim = args[0];

        if (aim.equalsIgnoreCase("reload")) {
            sender.sendMessage("§7正在重载配置文件...");
            try {
                TimeRewardAPI.getConfigManager().reload();
                sender.sendMessage("§a配置文件重载完成！");
            } catch (Exception e) {
                sender.sendMessage("§c配置文件重载失败，错误信息：" + e.getMessage() + " (详见后台");
                e.printStackTrace();
            }
            return true;
        } else if (aim.equalsIgnoreCase("list")) {
            Collection<RewardContents> awards = TimeRewardAPI.getRewardManager().listRewards().values();
            PluginMessages.List.HEADER.send(sender, awards.size());
            for (RewardContents reward : awards) {
                if (reward.getPermission() != null) {
                    PluginMessages.List.OBJECT_PERM.send(sender,
                            reward.getRewardID(), reward.getDisplayName(),
                            reward.getTime(), reward.getPermission()
                    );
                } else {
                    PluginMessages.List.OBJECT.send(sender,
                            reward.getRewardID(), reward.getDisplayName(), reward.getTime()
                    );
                }
            }
            return true;
        } else if (aim.equalsIgnoreCase("test")) {
            if (args.length < 2) return help(sender);
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c您不是玩家，无法使用此命令！");
                return true;
            }

            RewardContents contents = TimeRewardAPI.getRewardManager().getReward(args[1]);
            if (contents == null) {
                PluginMessages.NOT_EXISTS.send(sender, args[1]);
                return true;
            }

            PluginMessages.COMMAND_LIST.send(sender, contents.getRewardID());

            TimeRewardAPI.getRewardManager().executeCommand((Player) sender, contents);

            return true;
        } else if (aim.equalsIgnoreCase("user")) {
            if (args.length < 2) return help(sender);
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                PluginMessages.NOT_ONLINE.send(sender, args[1]);
                return true;
            }

            TimeRewardUser user = TimeRewardAPI.getUserManager().get(player);
            PluginMessages.USER_INFO.send(sender,
                    player.getName(), user.getAllSeconds(),
                    user.getClaimedRewards().size(), String.join("&8, &f", user.getClaimedRewards())
            );

            return true;
        }

        return help(sender);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        List<String> allCompletes = new ArrayList<>();
        // 玩家指令部分
        if (sender.hasPermission("TimeReward.admin")) {
            switch (args.length) {
                case 1: {
                    allCompletes.add("reload");
                    allCompletes.add("user");
                    allCompletes.add("list");
                    if (sender instanceof Player) allCompletes.add("test");

                    break;
                }
                case 2: {
                    String aim = args[0];
                    if (aim.equalsIgnoreCase("test") && sender instanceof Player) {
                        allCompletes = new ArrayList<>(TimeRewardAPI.getRewardManager().listRewards().keySet());
                    } else if (aim.equalsIgnoreCase("user")) {
                        allCompletes = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
                    }
                    break;
                }
            }
        }

        return allCompletes.stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .limit(10).collect(Collectors.toList());
    }

}
