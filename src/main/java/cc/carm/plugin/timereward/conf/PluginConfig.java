package cc.carm.plugin.timereward.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.util.MapFactory;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredMap;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.storage.RewardContents;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PluginConfig extends ConfigurationRoot {

    public static final ConfigValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComment({
            "统计数据设定",
            "该选项用于帮助开发者统计插件版本与使用情况，且绝不会影响性能与使用体验。",
            "当然，您也可以选择在这里关闭，或在plugins/bStats下的配置文件中关闭。"
    })
    public static final ConfigValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComment({
            "检查更新设定",
            "该选项用于插件判断是否要检查更新，若您不希望插件检查更新并提示您，可以选择关闭。",
            "检查更新为异步操作，绝不会影响性能与使用体验。"
    })
    public static final ConfigValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);

    @HeaderComment("数据存储相关设定")
    public static final class STORAGE extends ConfigurationRoot {
    }

    @HeaderComment("奖励相关设定")
    public static final class REWARDS extends ConfigurationRoot {

        @HeaderComment({
                "配置键名即奖励ID，支持英文、数字与下划线。",
                "确定后请不要更改，因为该键值用于存储玩家是否领取的数据",
                "如果更改，原先领取过该奖励的玩家将会自动再领取一次！"
        })
        public static final class EXAMPLE extends ConfigurationRoot {

            @HeaderComment({
                    "奖励的显示名称，可以是任意字符串",
                    "可以在 commands 中使用 %(name) 来获取该奖励的名称",
                    "也可以使用变量 %TimeReward_reward_<奖励ID>% 来获取对应奖励的名称"
            })
            public static final ConfigValue<String> NAME = ConfiguredValue.of(String.class, "&f[初级奖励] &e总在线时长 2小时");

            @HeaderComment("该奖励自动领取需要的在线时长，单位为秒")
            public static final ConfigValue<Integer> TIME = ConfiguredValue.of(Integer.class, 7200);

            @HeaderComment({
                    "该奖励领取权限，可以不设置。",
                    "若为空则所有人都可以领取；若不为空，则需要拥有该权限的玩家才能领取。"
            })
            public static final ConfigValue<String> PERMISSION = ConfiguredValue.of(String.class, "TimeReward.vip");

            @HeaderComment({
                    "领取奖励时后台执行的指令",
                    "支持PlaceholderAPI变量，指令中可以使用 %(name) 来获取该奖励的名称。"
            })
            public static final ConfiguredList<String> COMMANDS = ConfiguredList.builder(String.class)
                    .fromString()
                    .defaults("say &f恭喜 &b%player_name% &f领取了奖励 &r%(name) &f！")
                    .build();

        }

    }

}
