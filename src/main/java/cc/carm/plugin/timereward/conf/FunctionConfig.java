package cc.carm.plugin.timereward.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class FunctionConfig extends ConfigurationRoot {

    @HeaderComment({
            "是否启用自动领取",
            "启用后，玩家将会在满足奖励领取条件时自动领取奖励。",
            "若关闭，则玩家需要手动输入指令领取奖励。"
    })
    public static final ConfigValue<Boolean> AUTO_CLAIM = ConfiguredValue.of(Boolean.class, true);

}
