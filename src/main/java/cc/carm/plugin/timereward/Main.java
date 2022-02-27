package cc.carm.plugin.timereward;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.i18n.EasyPluginMessageProvider;
import cc.carm.plugin.timereward.configuration.PluginConfig;
import cc.carm.plugin.timereward.database.DataManager;
import cc.carm.plugin.timereward.listener.UserListener;
import cc.carm.plugin.timereward.manager.ConfigManager;
import cc.carm.plugin.timereward.manager.RewardManager;
import cc.carm.plugin.timereward.manager.UserManager;
import org.bukkit.Bukkit;

public class Main extends EasyPlugin {
    private static Main instance;

    public Main() {
        super(new EasyPluginMessageProvider.zh_CN());
        instance = this;
    }

    protected DataManager dataManager;
    protected ConfigManager configManager;
    protected UserManager userManager;
    protected RewardManager rewardManager;

    @Override
    public boolean initialize() {
        log("加载插件配置文件...");
        this.configManager = new ConfigManager();
        if (!this.configManager.initConfig()) {
            error("插件配置文件初始化失败，请检查文件权限。");
            return false;
        }

        info("初始化数据管理器...");
        this.dataManager = new DataManager();
        if (!dataManager.initialize()) {
            severe("初始化数据库失败，请检查配置文件。");
            dataManager.shutdown();
            return false; // 初始化失败，不再继续加载
        }

        log("加载用户管理器...");
        this.userManager = new UserManager();
        if (Bukkit.getOnlinePlayers().size() > 0) {
            log("   加载当前在线用户数据...");
            this.userManager.loadAll();
        }

        log("加载奖励管理器...");
        this.rewardManager = new RewardManager();

        log("注册监听器...");
        regListener(new UserListener());

        log("注册指令...");

        return true;
    }

    @Override
    protected void shutdown() {

        info("卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);

        info("保存用户数据...");
        this.userManager.unloadAll(true);

        info("结束数据库进程...");
        getDataManager().shutdown();

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

    public static DataManager getDataManager() {
        return getInstance().dataManager;
    }

}