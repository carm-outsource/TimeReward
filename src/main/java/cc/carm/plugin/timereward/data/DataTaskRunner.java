package cc.carm.plugin.timereward.data;

import cc.carm.plugin.timereward.database.DataManager;

@FunctionalInterface
public interface DataTaskRunner {
    void run(TimeRewardUser user, DataManager dataManager) throws Exception;
}
