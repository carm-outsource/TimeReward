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

    @HeaderComments({
            "自动保存设定，用于设置自动保存的时间间隔，单位为秒（小于等于0则关闭）。",
            "一般来说，玩家会在退出游戏时进行保存。",
            "但如果您希望额外的定期保存数据以避免数据丢失，可以选择开启。",
    })
    ConfiguredValue<Long> AUTO_SAVE = ConfiguredValue.of(Long.class, 60L);

    @HeaderComments({
            "是否在保存时优先使用数据库中的数据。",
            "若启用，则会在保存数据时在数据库中重新读取数据计算时间，以确利用最新数据；",
            "若关闭，则保存时会直接采用进入服务器时所加载的数据进行计算。"
    })
    ConfiguredValue<Boolean> USE_STORAGE_DATA = ConfiguredValue.of(Boolean.class, true);

    @HeaderComments("周起始日，用于判断周度奖励的结算日期。")
    ConfiguredValue<DayOfWeek> WEEK_FIRST_DAY = ConfiguredValue.builderOf(DayOfWeek.class)
            .from(Integer.class)
            .parse((v, d) -> DayOfWeek.of(d))
            .serialize(DayOfWeek::getValue)
            .defaults(DayOfWeek.MONDAY).build();

}


