package cc.carm.plugin.timereward;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.i18n.EasyPluginMessageProvider;
import cc.carm.lib.easyplugin.utils.MessageUtils;
import cc.carm.plugin.timereward.command.TimeRewardCommand;
import cc.carm.plugin.timereward.configuration.PluginConfig;
import cc.carm.plugin.timereward.database.DataManager;
import cc.carm.plugin.timereward.hooker.GHUpdateChecker;
import cc.carm.plugin.timereward.hooker.PAPIExpansion;
import cc.carm.plugin.timereward.listener.UserListener;
import cc.carm.plugin.timereward.manager.ConfigManager;
import cc.carm.plugin.timereward.manager.RewardManager;
import cc.carm.plugin.timereward.manager.UserManager;
import cc.carm.plugin.timereward.util.JarResourceUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;

public class Main extends EasyPlugin {
    private static Main instance;

    public Main() {
        super(new EasyPluginMessageProvider.zh_CN());
        instance = this;
    }

    private GHUpdateChecker checker;

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
        log("   加载了 " + this.rewardManager.listRewards().size() + " 个奖励配置");
        this.rewardManager.initialize();

        log("注册监听器...");
        regListener(new UserListener());

        log("注册指令...");
        registerCommand("TimeReward", new TimeRewardCommand());


        if (MessageUtils.hasPlaceholderAPI()) {
            log("注册变量...");
            new PAPIExpansion(this).register();
        } else {
            log("检测到未安装PlaceholderAPI，跳过变量注册。");
        }

        if (PluginConfig.METRICS.get()) {
            info("启用统计数据...");
            Metrics metrics = new Metrics(this, 14505);
        }

        if (PluginConfig.CHECK_UPDATE.get()) {
            info("开始检查更新...");
            this.checker = new GHUpdateChecker("CarmJos", "TimeReward");
            getScheduler().runAsync(() -> this.checker.checkUpdate(Main.this.getDescription().getVersion()));
        } else {
            info("已禁用检查更新，跳过。");
        }

        return true;
    }

    @Override
    protected void shutdown() {

        info("终止发奖励进程...");
        this.rewardManager.shutdown();

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

    @Override
    public void outputInfo() {
        String[] pluginInfo = JarResourceUtils.readResource(this.getResource("PLUGIN_INFO"));
        if (pluginInfo != null) Main.info(pluginInfo);
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