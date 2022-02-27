package cc.carm.plugin.timereward.configuration;

import cc.carm.lib.easyplugin.configuration.values.ConfigValue;

public class PluginConfig {

    public static final ConfigValue<Boolean> DEBUG = new ConfigValue<>(
            "debug", Boolean.class, false
    );


}
