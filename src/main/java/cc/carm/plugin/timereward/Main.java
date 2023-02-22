package cc.carm.plugin.timereward;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.updatechecker.GHUpdateChecker;
import cc.carm.lib.easyplugin.utils.MessageUtils;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cc.carm.plugin.timereward.command.TimeRewardCommand;
import cc.carm.plugin.timereward.conf.PluginConfig;
import cc.carm.plugin.timereward.conf.PluginMessages;
import cc.carm.plugin.timereward.storage.database.MySQLStorage;
import cc.carm.plugin.timereward.hooker.PAPIExpansion;
import cc.carm.plugin.timereward.listener.UserListener;
import cc.carm.plugin.timereward.manager.RewardManager;
import cc.carm.plugin.timereward.manager.UserManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;

public class Main extends EasyPlugin {
    private static Main instance;

    protected ConfigurationProvider<?> configProvider;
    protected ConfigurationProvider<?> messageProvider;

    protected MySQLStorage mySQLStorage;
    protected UserManager userManager;
    protected RewardManager rewardManager;

    public Main() {
        instance = this;
    }

    @Override
    protected void load() {
        log("加载插件配置文件...");
        this.configProvider = MineConfiguration.from(this, "config.yml");
        this.configProvider.initialize(PluginConfig.class);

        this.messageProvider = MineConfiguration.from(this, "messages.yml");
        this.messageProvider.initialize(PluginMessages.class);
    }

    @Override
    protected boolean initialize() {
        log("初始化数据管理器...");
        this.mySQLStorage = new MySQLStorage();
        try {
            mySQLStorage.initialize();
        } catch (Exception e) {
            severe("初始化存储失败，请检查配置文件。");
            setEnabled(false);
            return false; // 初始化失败，不再继续加载
        }

        log("加载用户管理器...");
        this.userManager = new UserManager();
        if (Bukkit.getOnlinePlayers().size() > 0) {
            log("加载现有用户数据...");
            this.userManager.loadAll();
        }

        log("加载奖励管理器...");
        this.rewardManager = new RewardManager(this);
        log("加载了 " + this.rewardManager.listRewards().size() + " 个奖励配置。");

        log("注册监听器...");
        registerListener(new UserListener());

        log("注册指令...");
        registerCommand("TimeReward", new TimeRewardCommand());

        if (MessageUtils.hasPlaceholderAPI()) {
            log("注册变量...");
            new PAPIExpansion(this).register();
        } else {
            log("未安装PlaceholderAPI，跳过变量注册...");
        }

        if (PluginConfig.METRICS.getNotNull()) {
            info("启用统计数据...");
            new Metrics(this, 14505);
        }

        if (PluginConfig.CHECK_UPDATE.getNotNull()) {
            info("开始检查更新...");
            getScheduler().runAsync(GHUpdateChecker.runner(this));
        } else {
            info("已禁用检查更新，跳过。");
        }

        return true;
    }

    @Override
    protected void shutdown() {
        info("终止奖励发放进程...");
        this.rewardManager.shutdown();

        info("保存用户数据...");
        this.userManager.unloadAll(true);

        info("终止数据库进程...");
        getStorage().shutdown();

        info("卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);
    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.getNotNull();
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

    public static Main getInstance() {
        return instance;
    }

    public static MySQLStorage getStorage() {
        return getInstance().mySQLStorage;
    }

    public ConfigurationProvider<?> getConfigProvider() {
        return configProvider;
    }

    public ConfigurationProvider<?> getMessageProvider() {
        return messageProvider;
    }

}