package cc.carm.plugin.timereward.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.plugin.timereward.data.RewardContents;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

@HeaderComment({"奖励相关设定，包含以下设定：",
        " [id] 配置键名即奖励ID，支持英文、数字与下划线。",
        "  | 确定后请不要更改，因为该键值用于存储玩家是否领取的数据",
        "  | 如果更改，原先领取过该奖励的玩家将会自动再领取一次！",
        " [name] 奖励的显示名称，可以是任意字符串",
        "  | 可以在 commands 中使用 %(name) 来获取该奖励的名称",
        "  | 也可以使用变量 %TimeReward_reward_<奖励ID>% 来获取对应奖励的名称",
        " [permission] 领取奖励时后台执行的指令",
        "  | 支持PlaceholderAPI变量，指令中可以使用 %(name) 来获取该奖励的名称。",
        " [commands] 该奖励领取权限，可以不设置。",
        "  | 若为空则所有人都可以领取；若不为空，则需要拥有该权限的玩家才能领取。",
        " [auto] 该奖励是否自动领取，可以不设置，默认为true。",
        "  | 若关闭自动领取，则需要玩家手动输入/tr claim 领取奖励。",
})
public class RewardsConfig extends ConfigurationRoot {

    @HeaderComment("每日在线时长的奖励")
    public static final ConfigValue<RewardGroup> DAILY = create();

    @HeaderComment("每周在线时长的奖励")
    public static final ConfigValue<RewardGroup> WEEKLY = create();

    @HeaderComment("每月在线时长的奖励")
    public static final ConfigValue<RewardGroup> MONTHLY = create();

    @HeaderComment("总在线时长的奖励")
    public static final ConfigValue<RewardGroup> TOTAL = create();

    private static ConfigValue<RewardGroup> create() {
        return ConfigValue.builder()
                .asValue(RewardGroup.class).fromSection()
                .parseValue((v, d) -> RewardGroup.parse(v))
                .serializeValue(RewardGroup::serialize)
                .defaults(RewardGroup.defaults())
                .build();
    }

    public static final class RewardGroup {
        final @NotNull Map<String, RewardContents> contents;

        public RewardGroup(@NotNull Map<String, RewardContents> contents) {
            this.contents = contents;
        }

        public @NotNull Map<String, RewardContents> getContents() {
            return contents;
        }

        public Map<String, Object> serialize() {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Map.Entry<String, RewardContents> entry : contents.entrySet()) {
                map.put(entry.getKey(), entry.getValue().serialize());
            }
            return map;
        }

        public static RewardGroup parse(@NotNull ConfigurationWrapper<?> section) {
            Map<String, RewardContents> rewards = new LinkedHashMap<>();
            for (String rewardID : section.getKeys(false)) {
                ConfigurationWrapper<?> rewardSection = section.getConfigurationSection(rewardID);
                if (rewardSection == null) continue;

                RewardContents c = RewardContents.parse(rewardID, rewardSection);
                if (c == null) continue;
                rewards.put(rewardID, c);
            }
            return new RewardGroup(rewards);
        }

        public static RewardGroup defaults() {
            Map<String, RewardContents> rewards = new LinkedHashMap<>();
            rewards.put("example", RewardContents.defaults("example"));
            return new RewardGroup(rewards);
        }

    }

}
