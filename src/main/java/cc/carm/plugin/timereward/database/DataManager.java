package cc.carm.plugin.timereward.database;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.plugin.timereward.Main;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class DataManager {
    protected static final Gson GSON = new Gson();
    protected static final JsonParser PARSER = new JsonParser();
    private SQLManager sqlManager;

    public boolean initialize() {
        try {
            Main.info("	尝试连接到数据库...");
            this.sqlManager = EasySQL.createManager(
                    DBConfiguration.DRIVER_NAME.get(), DBConfiguration.buildJDBC(),
                    DBConfiguration.USERNAME.get(), DBConfiguration.PASSWORD.get()
            );
            this.sqlManager.setDebugMode(() -> Main.getInstance().isDebugging());
        } catch (Exception exception) {
            Main.severe("无法连接到数据库，请检查配置文件。");
            exception.printStackTrace();
            return false;
        }

        try {
            Main.info("	创建插件所需表...");
            getSQLManager().createTable(DBTables.UserOnlineTime.TABLE_NAME.get())
                    .setColumns(DBTables.UserOnlineTime.TABLE_COLUMNS)
                    .build().execute();

            getSQLManager().createTable(DBTables.UserClaimedReward.TABLE_NAME.get())
                    .setColumns(DBTables.UserClaimedReward.TABLE_COLUMNS)
                    .build().execute();

        } catch (SQLException exception) {
            Main.severe("无法创建插件所需的表，请检查数据库权限。");
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    public void shutdown() {
        Main.info("	关闭数据库连接...");
        EasySQL.shutdownManager(getSQLManager());
        this.sqlManager = null;
    }

    public SQLManager getSQLManager() {
        return sqlManager;
    }

    public @Nullable Long getPlayTime(@NotNull UUID userUUID) throws SQLException {
        return getSQLManager().createQuery().inTable(DBTables.UserOnlineTime.TABLE_NAME.get())
                .selectColumns("uuid", "time")
                .addCondition("uuid", userUUID.toString())
                .setLimit(1).build().executeFunction(query -> {
                    ResultSet resultSet = query.getResultSet();
                    return resultSet.next() ? resultSet.getLong("time") : 0L;
                });
    }

    public @NotNull Set<String> getClaimedData(@NotNull UUID userUUID) throws SQLException {
        return getSQLManager().createQuery().inTable(DBTables.UserClaimedReward.TABLE_NAME.get())
                .selectColumns("uuid", "value")
                .addCondition("uuid", userUUID.toString())
                .setLimit(1).build().executeFunction(query -> {
                    ResultSet resultSet = query.getResultSet();
                    if (!resultSet.next()) return new LinkedHashSet<>();
                    String json = resultSet.getString("value");
                    if (json == null) return new LinkedHashSet<>();
                    JsonElement element = PARSER.parse(json);
                    if (!element.isJsonArray()) return new LinkedHashSet<>();
                    Set<String> ids = new LinkedHashSet<>();
                    for (JsonElement e : element.getAsJsonArray()) {
                        ids.add(e.getAsString());
                    }
                    return ids;
                }, new LinkedHashSet<>());
    }


    public void saveClaimedData(@NotNull UUID userUUID, @Nullable Set<String> claimedRewards) throws SQLException {
        if (claimedRewards == null) {
            getSQLManager().createDelete(DBTables.UserClaimedReward.TABLE_NAME.get())
                    .addCondition("uuid", userUUID.toString())
                    .setLimit(1).build().execute();
        } else {
            getSQLManager().createReplace(DBTables.UserClaimedReward.TABLE_NAME.get())
                    .setColumnNames("uuid", "value")
                    .setParams(userUUID.toString(), GSON.toJson(claimedRewards))
                    .execute();
        }
    }

    public void savePlayTime(@NotNull UUID userUUID, long time) throws SQLException {
        getSQLManager().createReplace(DBTables.UserOnlineTime.TABLE_NAME.get())
                .setColumnNames("uuid", "time").setParams(userUUID.toString(), time)
                .execute();
    }


}

