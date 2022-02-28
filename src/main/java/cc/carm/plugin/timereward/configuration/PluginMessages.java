package cc.carm.plugin.timereward.configuration;

import cc.carm.lib.easyplugin.configuration.language.EasyMessageList;
import cc.carm.lib.easyplugin.configuration.language.MessagesRoot;


public class PluginMessages extends MessagesRoot {

    public static final EasyMessageList COMMAND_USAGE = new EasyMessageList(
            "&6&l在线奖励 &f后台指令帮助",
            "&8#&f reload",
            "&8-&7 重载插件配置文件。",
            "&8#&f user &6<玩家>",
            "&8-&7 查看用户的在线时长信息与奖励领取情况。",
            "&8#&f list",
            "&8-&7 列出所有奖励与条件。",
            "&8#&f info &6<奖励ID>",
            "&8-&7 查看奖励详情。",
            "&8#&f test &6<奖励ID>",
            "&8-&7 测试执行奖励配置的指令。"
    );


    public static class List {

        public static final EasyMessageList HEADER = EasyMessageList.builder()
                .contents("&6&l在线奖励 &f奖励列表 &7(共%(amount)个)")
                .params("amount").build();

        public static final EasyMessageList OBJECT = EasyMessageList.builder()
                .contents(
                        "&8# &f%(id)",
                        "&8- &7奖励名称 &f%(name)",
                        "&8- &7领取时间 &f&e%(time)&f秒"
                ).params("id", "name", "time").build();

        public static final EasyMessageList OBJECT_PERM = EasyMessageList.builder()
                .contents(
                        "&8# &f%(id)",
                        "&8- &7奖励名称 &f%(name)",
                        "&8- &7领取时间 &f&e%(time)&f秒",
                        "&8- &7需要权限 &f%(permission)"
                ).params("id", "name", "time", "permission").build();
    }

    public static final EasyMessageList USER_INFO = EasyMessageList.builder()
            .contents(
                    "&f玩家 &6%(player) &f已在线&e%(time)&f秒，共领取了 &e%(amount)&f 次奖励。",
                    "&7已领取的奖励列表如下：&r%(rewards) &7。"
            ).params("player", "time", "amount", "rewards").build();

    public static final EasyMessageList COMMAND_LIST = EasyMessageList.builder()
            .contents("&f正在执行奖励 %(award) 的指令列表...")
            .params("award")
            .build();

    public static final EasyMessageList NOT_ONLINE = EasyMessageList.builder()
            .contents("&f玩家 &e%(player) &f并不在线。")
            .params("player")
            .build();
    public static final EasyMessageList NOT_EXISTS = EasyMessageList.builder()
            .contents("&f奖励 &e%(award) &f并不存在。")
            .params("award")
            .build();

}
