package cc.carm.plugin.timereward.storage.database;

import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLTable;
import cc.carm.lib.easysql.api.builder.TableCreateBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.function.Consumer;

public enum DatabaseTables implements SQLTable {

    /**
     * 用于记录用户在线时间的表
     */
    USER_TIMES(DatabaseConfig.TABLES.USER_TIMES, table -> {
        table.addColumn("uuid", "CHAR(36) NOT NULL PRIMARY KEY"); // 用户的UUID
        table.addColumn("date", "DATE NOT NULL"); // 日期

        table.addColumn("day_time", "MEDIUMINT UNSIGNED NOT NULL DEFAULT 0"); // 用户日在线时间(秒)
        table.addColumn("week_time", "MEDIUMINT UNSIGNED NOT NULL DEFAULT 0"); // 用户周在线时间(秒)
        table.addColumn("month_time", "INT UNSIGNED NOT NULL DEFAULT 0"); // 用户月在线时间(秒)
        table.addColumn("total_time", "INT UNSIGNED NOT NULL DEFAULT 0"); // 用户总在线时间(秒)

        table.addColumn("update",
                "DATETIME NOT NULL " +
                        "DEFAULT CURRENT_TIMESTAMP " +
                        "ON UPDATE CURRENT_TIMESTAMP"
        );

    }),

    /**
     * 用于记录用户奖励领取情况的表
     */
    USER_CLAIMED(DatabaseConfig.TABLES.USER_CLAIMED, (table) -> {
        table.addAutoIncrementColumn("id", false, true); // 排序键
        table.addColumn("uuid", "CHAR(36) NOT NULL PRIMARY KEY"); // 用户的UUID 主键

        table.addColumn("value", "MEDIUMTEXT"); // 已领取的奖励ID
        table.addColumn("time", "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP"); // 领取时间
    });

    private final Consumer<TableCreateBuilder> builder;
    private final ConfigValue<String> name;
    private @Nullable SQLManager manager;

    DatabaseTables(ConfigValue<String> name,
                   Consumer<TableCreateBuilder> builder) {
        this.name = name;
        this.builder = builder;
    }

    @Override
    public @Nullable SQLManager getSQLManager() {
        return this.manager;
    }

    @Override
    public @NotNull String getTableName() {
        return this.name.getNotNull();
    }

    @Override
    public boolean create(SQLManager sqlManager) throws SQLException {
        if (this.manager == null) this.manager = sqlManager;

        TableCreateBuilder tableBuilder = sqlManager.createTable(getTableName());
        if (builder != null) builder.accept(tableBuilder);
        return tableBuilder.build().executeFunction(l -> l > 0, false);
    }
}
