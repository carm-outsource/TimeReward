package cc.carm.plugin.timereward.util;

import cc.carm.plugin.timereward.storage.database.MySQLStorage;

@FunctionalInterface
public interface DataTaskRunner {
    void run(MySQLStorage mySQLStorage) throws Exception;
}
