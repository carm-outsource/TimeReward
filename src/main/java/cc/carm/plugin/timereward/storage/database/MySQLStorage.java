package cc.carm.plugin.timereward.storage.database;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.storage.UserData;
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

public class MySQLStorage {
    protected static final Gson GSON = new Gson();
    protected static final JsonParser PARSER = new JsonParser();
    private SQLManager sqlManager;

    public void initialize() throws Exception {
        Main.info("加载数据库配置...");
        Main.getInstance().getConfigProvider().initialize(DatabaseConfig.class);

        try {
            Main.info("尝试连接到数据库...");
            this.sqlManager = EasySQL.createManager(
                    DatabaseConfig.DRIVER_NAME.getNotNull(), DatabaseConfig.buildJDBC(),
                    DatabaseConfig.USERNAME.getNotNull(), DatabaseConfig.PASSWORD.getNotNull()
            );
            this.sqlManager.setDebugMode(() -> Main.getInstance().isDebugging());
        } catch (Exception exception) {
            throw new Exception("无法连接到数据库，请检查配置文件。", exception);
        }

        try {
            Main.info("创建插件所需表...");
            for (DatabaseTables value : DatabaseTables.values()) {
                value.create(this.sqlManager);
            }
        } catch (SQLException exception) {
            throw new Exception("无法创建插件所需的表，请检查数据库权限。", exception);
        }
    }

    public void shutdown() {
        Main.info("关闭数据库连接...");
        EasySQL.shutdownManager(getSQLManager());
        this.sqlManager = null;
    }

    public SQLManager getSQLManager() {
        return sqlManager;
    }

    public @Nullable UserData loadData(@NotNull UUID uuid) throws Exception {
        Long playTime = loadPlayTime(uuid);
        Set<String> claimedData = loadClaimedData(uuid);
        return new UserData(uuid, playTime, claimedData);
    }

    @Nullable
    public Long loadPlayTime(@NotNull UUID uuid) throws Exception {
        return DatabaseTables.USER_TIMES.createQuery()
                .selectColumns("uuid", "time")
                .addCondition("uuid", uuid).setLimit(1).build()
                .executeFunction((query) -> {
                    ResultSet resultSet = query.getResultSet();
                    if (resultSet == null || !resultSet.next()) return 0L;
                    return resultSet.getLong("time");
                });
    }

    @NotNull
    public Set<String> loadClaimedData(@NotNull UUID uuid) throws Exception {
        return DatabaseTables.USER_CLAIMED.createQuery()
                .selectColumns("uuid", "value")
                .addCondition("uuid", uuid).setLimit(1).build()
                .executeFunction((query) -> {
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

    public void saveClaimedData(@NotNull UUID uuid, @Nullable Set<String> claimedRewards) throws Exception {
        if (claimedRewards != null) {
            DatabaseTables.USER_CLAIMED.createReplace()
                    .setColumnNames("uuid", "value")
                    .setParams(uuid.toString(), GSON.toJson(claimedRewards))
                    .execute();
        } else {
            DatabaseTables.USER_CLAIMED.createDelete()
                    .addCondition("uuid", uuid).setLimit(1)
                    .build()
                    .execute();
        }
    }

    public void savePlayTime(@NotNull UUID uuid, long time) throws Exception {
        DatabaseTables.USER_TIMES.createReplace()
                .setColumnNames("uuid", "time")
                .setParams(uuid.toString(), time)
                .execute();
    }

}

