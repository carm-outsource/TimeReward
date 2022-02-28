package cc.carm.plugin.timereward.configuration;

import cc.carm.lib.easyplugin.configuration.cast.ConfigSectionCast;
import cc.carm.lib.easyplugin.configuration.values.ConfigValue;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.data.RewardContents;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class PluginConfig {

    public static final ConfigValue<Boolean> DEBUG = new ConfigValue<>(
            "debug", Boolean.class, false
    );

    public static final ConfigSectionCast<HashMap<String, RewardContents>> REWARDS = new ConfigSectionCast<>(
            "rewards", TimeRewardAPI.getRewardManager()::readRewards, new LinkedHashMap<>()
    );


}
