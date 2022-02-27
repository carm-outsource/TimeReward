package cc.carm.plugin.timereward.configuration;

import cc.carm.lib.easyplugin.configuration.impl.ConfigSound;
import cc.carm.lib.easyplugin.configuration.values.ConfigValue;
import org.bukkit.Sound;

public class PluginConfig {

    public static final ConfigValue<Boolean> DEBUG = new ConfigValue<>(
            "debug", Boolean.class, false
    );
}
