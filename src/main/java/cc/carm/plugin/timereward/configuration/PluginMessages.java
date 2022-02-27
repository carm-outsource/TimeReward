package cc.carm.plugin.timereward.configuration;

import cc.carm.lib.easyplugin.configuration.language.EasyMessageList;
import cc.carm.lib.easyplugin.configuration.language.MessagesRoot;


public class PluginMessages extends MessagesRoot {

    public static final EasyMessageList NOT_ONLINE = EasyMessageList.builder()
            .contents("&7玩家 &c%(player) &7并不在线。")
            .params("player")
            .build();


}
