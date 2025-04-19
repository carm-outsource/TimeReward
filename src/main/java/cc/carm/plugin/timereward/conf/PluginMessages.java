package cc.carm.plugin.timereward.conf;


import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import de.themoep.minedown.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;


@HeaderComments({
        "TimeReward 在线奖励插件的消息配置文件",
        "如特定的消息不需要任何提示，可直接留下单行空内容消息。",
        "支持 &+颜色代码(原版颜色)、&(#XXXXXX)(RGB颜色) 与 &<#XXXXXX>(前后标注RGB颜色渐变)。",
        " "
})
@ConfigPath(root = true)
public interface PluginMessages extends Configuration {

    static ConfiguredMessage.@NotNull Builder<BaseComponent[]> list() {
        return ConfiguredMessage.create(getParser())
                .dispatcher((sender, message) -> {
                    if (sender instanceof ConsoleCommandSender) {
                        message.forEach(m -> sender.sendMessage(BaseComponent.toLegacyText(m)));
                        return;
                    }
                    Player player = (Player) sender;
                    message.forEach(m -> player.spigot().sendMessage(m));
                });
    }

    static @NotNull BiFunction<CommandSender, String, BaseComponent[]> getParser() {
        return (sender, message) -> {
            if (sender instanceof Player) message = PlaceholderAPI.setPlaceholders((Player) sender, message);
            return MineDown.parse(ColorParser.parse(message));
        };
    }

    interface USAGE extends Configuration {

        ConfiguredMessage<BaseComponent[]> ADMIN = list().defaults(
                "&6&l在线奖励 &f管理员指令帮助",
                "&8#&f claim &6[奖励ID]",
                "&8-&7 为自己手动领取对应奖励。",
                "&8-&7 若不填写奖励ID，则自动领取全部可领取的奖励。",
                "&8#&f reload",
                "&8-&7 重载插件配置文件。",
                "&8#&f user &6<玩家>",
                "&8-&7 查看用户的在线时长信息与奖励领取情况。",
                "&8#&f list",
                "&8-&7 列出所有奖励与条件。",
                "&8#&f test &6<奖励ID>",
                "&8-&7 测试执行奖励配置的指令。"
        ).build();

        ConfiguredMessage<BaseComponent[]> USER = list().defaults(
                "&6&l在线奖励 &f您可以输入 &e/tr claim &6[奖励ID] &f领取对应奖励。"
        ).build();
    }

    ConfiguredMessage<BaseComponent[]> NO_PERMISSION = list().defaults(
            "&c&l抱歉！&f但您没有足够的权限使用该指令。"
    ).build();

    ConfiguredMessage<BaseComponent[]> NOT_PLAYER = list().defaults(
            "&f该指令请以玩家身份执行。"
    ).build();

    ConfiguredMessage<BaseComponent[]> NOT_ONLINE = list().defaults(
            "&f玩家 &e%(player) &f并不在线。"
    ).params("player").build();

    ConfiguredMessage<BaseComponent[]> NOT_EXISTS = list().defaults(
            "&f奖励 &e%(award) &f并不存在。"
    ).params("award").build();

    ConfiguredMessage<BaseComponent[]> NOT_CLAIMABLE = list().defaults(
            "&c&l抱歉！&f但您暂时未满足领取奖励 &e%(award) &f的条件。"
    ).params("award").build();

    ConfiguredMessage<BaseComponent[]> NO_UNCLAIMED_REWARD = list().defaults(
            "&f您暂时没有未领取的奖励。"
    ).build();

    ConfiguredMessage<BaseComponent[]> USER_INFO = list().defaults(
            "&f玩家 &6%(player) &f的在线时间数据：",
            "&f  本日在线 &e%(daily)&f秒，本周在线 &e%(weekly)&f秒，",
            "&f  本月在线 &e%(monthly)&f秒，累积在线 &e%(total)&f秒。",
            "&7已领取了 &f%(amount) &7种奖励，如下所示："
    ).params("player", "daily", "weekly", "monthly", "total", "amount").build();

    ConfiguredMessage<BaseComponent[]> USER_RECEIVED = list().defaults(
            "&7- &e%(reward) &7于 &f%(time) &7领取"
    ).params("reward", "time").build();

    ConfiguredMessage<BaseComponent[]> COMMAND_LIST = list().defaults(
            "&f正在执行奖励 %(award) 的指令列表..."
    ).params("award").build();

    interface LIST extends Configuration {

        ConfiguredMessage<BaseComponent[]> HEADER = list().defaults(
                "&6&l在线奖励 &f奖励列表 &7(共%(amount)个)"
        ).params("amount").build();

        ConfiguredMessage<BaseComponent[]> OBJECT = list().defaults(
                "&8# &f%(id)",
                "&8- &7奖励名称 &f%(name)",
                "&8- &7奖励类型 &f%(type)",
                "&8- &7领取时间 &f&e%(time)&f秒"
        ).params("id", "name", "type", "time").build();

        ConfiguredMessage<BaseComponent[]> OBJECT_PERM = list().defaults(
                "&8# &f%(id)",
                "&8- &7奖励名称 &f%(name)",
                "&8- &7奖励类型 &f%(type)",
                "&8- &7领取时间 &f&e%(time)&f秒",
                "&8- &7需要权限 &f%(permission)"
        ).params("id", "name", "type", "time", "permission").build();

    }

    interface RELOAD extends Configuration {

        ConfiguredMessage<BaseComponent[]> START = list().defaults(
                "&f正在重载配置文件..."
        ).build();

        ConfiguredMessage<BaseComponent[]> ERROR = list().defaults(
                "&f配置文件&c重载失败！&f详细原因详见后台输出。"
        ).build();

        ConfiguredMessage<BaseComponent[]> COMPLETE = list().defaults(
                "&f配置文件重载完成！耗时 &d%(time)&fms，共加载了 &d%(count) &f个奖励配置。"
        ).params("time", "count").build();

    }

}
