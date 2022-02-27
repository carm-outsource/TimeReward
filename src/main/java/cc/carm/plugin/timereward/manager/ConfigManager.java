package cc.carm.plugin.timereward.manager;


import cc.carm.lib.easyplugin.configuration.file.FileConfig;
import cc.carm.lib.easyplugin.configuration.language.MessagesConfig;
import cc.carm.lib.easyplugin.configuration.language.MessagesInitializer;
import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.configuration.PluginMessages;

public class ConfigManager {

    private FileConfig pluginConfiguration;

    private MessagesConfig messageConfiguration;

    public boolean initConfig() {
        try {
            pluginConfiguration = new FileConfig(Main.getInstance(), "config.yml");
            messageConfiguration = new MessagesConfig(Main.getInstance(), "messages.yml");

            FileConfig.pluginConfiguration = () -> pluginConfiguration;
            FileConfig.messageConfiguration = () -> messageConfiguration;

            MessagesInitializer.initialize(messageConfiguration, PluginMessages.class);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public FileConfig getPluginConfig() {
        return FileConfig.pluginConfiguration.get();
    }

    public FileConfig getMessageConfig() {
        return FileConfig.messageConfiguration.get();
    }

    public void reload() throws Exception {
        getPluginConfig().reload();
        getMessageConfig().reload();
    }

    public void saveConfig() throws Exception {
        getPluginConfig().save();
        getMessageConfig().save();
    }

}
