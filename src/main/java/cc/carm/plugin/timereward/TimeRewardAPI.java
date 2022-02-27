package cc.carm.plugin.timereward;

import cc.carm.plugin.timereward.manager.ConfigManager;
import cc.carm.plugin.timereward.manager.RewardManager;
import cc.carm.plugin.timereward.manager.UserManager;

public class TimeRewardAPI {

    public static UserManager getUserManager() {
        return Main.getInstance().userManager;
    }

    public static ConfigManager getConfigManager() {
        return Main.getInstance().configManager;
    }

    public static RewardManager getRewardManager(){
        return Main.getInstance().rewardManager;
    }


}
