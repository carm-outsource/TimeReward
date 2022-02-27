package cc.carm.plugin.timereward;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.i18n.EasyPluginMessageProvider;
import cc.carm.plugin.timereward.configuration.PluginConfig;
import cc.carm.plugin.timereward.manager.ConfigManager;

public class Main extends EasyPlugin {
    private static Main instance;

    public Main() {
        super(new EasyPluginMessageProvider.zh_CN());
        instance = this;
    }

    protected static ConfigManager configManager;

    @Override
    public boolean initialize() {
        log("加载插件配置文件...");
        Main.configManager = new ConfigManager();
        if (!Main.configManager.initConfig()) {
            error("插件配置文件初始化失败，请检查文件权限。");
            return false;
        }

        log("加载玩家管理器...");

        log("注册监听器...");

        log("注册指令...");

        return true;
    }

    @Override
    protected void shutdown() {

    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.get();
    }

    public static Main getInstance() {
        return instance;
    }

    public static void info(String... messages) {
        getInstance().log(messages);
    }

    public static void severe(String... messages) {
        getInstance().error(messages);
    }

    public static void debugging(String... messages) {
        getInstance().debug(messages);
    }

}