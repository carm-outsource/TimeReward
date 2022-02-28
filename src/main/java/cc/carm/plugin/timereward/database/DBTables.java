package cc.carm.plugin.timereward.database;

import cc.carm.lib.easyplugin.configuration.values.ConfigValue;

public class DBTables {

    protected static class UserOnlineTime {

        protected static final ConfigValue<String> TABLE_NAME = new ConfigValue<>(
                "database.tables.time", String.class,
                "tr_user_times"
        );

        protected static final String[] TABLE_COLUMNS = new String[]{
                "`uuid` VARCHAR(36) NOT NULL PRIMARY KEY COMMENT '用户UUID'", // 用户的UUID
                "`time` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户在线秒数'",// 用户在线时间(秒)
                "`update` DATETIME NOT NULL " +
                        "DEFAULT CURRENT_TIMESTAMP " +
                        "ON UPDATE CURRENT_TIMESTAMP " +
                        " COMMENT '最后更新时间'"
        };

    }

    protected static class UserClaimedReward {

        protected static final ConfigValue<String> TABLE_NAME = new ConfigValue<>(
                "database.tables.claimed", String.class,
                "tr_user_claimed"
        );

        protected static final String[] TABLE_COLUMNS = new String[]{
                "`id` INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE KEY", // 排序键
                "`uuid` VARCHAR(36) NOT NULL PRIMARY KEY COMMENT '用户UUID'", // 用户的UUID 主键
                "`value` MEDIUMTEXT", // 已领取的奖励ID
                "`update` DATETIME NOT NULL " +
                        "DEFAULT CURRENT_TIMESTAMP " +
                        "ON UPDATE CURRENT_TIMESTAMP " +
                        " COMMENT '最后更新时间'"
        };

    }


}
