package cc.carm.plugin.timereward.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;

import java.time.DayOfWeek;

@ConfigPath(root = true)
public interface PluginConfig extends Configuration {

    ConfiguredValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComments({
            "统计数据设定",
            "该选项用于帮助开发者统计插件版本与使用情况，且绝不会影响性能与使用体验。",
            "当然，您也可以选择在这里关闭，或在plugins/bStats下的配置文件中关闭。"
    })
    ConfiguredValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComments({
            "检查更新设定",
            "该选项用于插件判断是否要检查更新，若您不希望插件检查更新并提示您，可以选择关闭。",
            "检查更新为异步操作，绝不会影响性能与使用体验。"
    })
    ConfiguredValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);

    @HeaderComments("周起始日，用于判断周度奖励的结算日期。")
    ConfiguredValue<DayOfWeek> WEEK_FIRST_DAY = ConfiguredValue.builderOf(DayOfWeek.class)
            .from(Integer.class)
            .parse((v, d) -> DayOfWeek.of(d))
            .serialize(DayOfWeek::getValue)
            .defaults(DayOfWeek.MONDAY).build();

}


