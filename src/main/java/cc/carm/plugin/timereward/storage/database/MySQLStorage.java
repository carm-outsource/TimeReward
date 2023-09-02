package cc.carm.plugin.timereward.storage.database;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.data.TimeRecord;
import cc.carm.plugin.timereward.user.UserRewardData;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public @Nullable UserRewardData loadData(@NotNull UUID uuid) throws Exception {
        TimeRecord recordDate = loadTimeRecord(uuid);
        System.out.println(recordDate.toString());
        Map<String, LocalDateTime> claimedData = loadClaimedData(uuid);
        return new UserRewardData(uuid, recordDate, claimedData);
    }

    public TimeRecord loadTimeRecord(@NotNull UUID uuid) throws Exception {
        return DatabaseTables.USER_TIMES.createQuery()
                .addCondition("uuid", uuid).setLimit(1).build()
                .executeFunction(query -> {
                    ResultSet rs = query.getResultSet();
                    if (rs == null || !rs.next()) return TimeRecord.empty();

                    LocalDate date = rs.getDate("date").toLocalDate();
                    long daily = rs.getLong("daily_time");
                    long weekly = rs.getLong("weekly_time");
                    long monthly = rs.getLong("monthly_time");
                    long total = rs.getLong("total_time");

                    return new TimeRecord(date, daily, weekly, monthly, total);
                }, TimeRecord.empty());
    }

    @NotNull
    public Map<String, LocalDateTime> loadClaimedData(@NotNull UUID uuid) throws Exception {
        return DatabaseTables.USER_CLAIMED.createQuery()
                .addCondition("uuid", uuid).build()
                .executeFunction((query) -> {
                    ResultSet rs = query.getResultSet();
                    Map<String, LocalDateTime> map = new LinkedHashMap<>();

                    while (rs.next()) {
                        String rewardID = rs.getString("reward");
                        LocalDateTime time = rs.getTimestamp("time").toLocalDateTime();
                        map.put(rewardID, time);
                    }

                    return map;
                }, new LinkedHashMap<>());
    }

    public void addClaimedData(@NotNull UUID uuid, String reward) throws Exception {
        HashMap<String, LocalDateTime> time = new HashMap<>();
        time.put(reward, LocalDateTime.now());
        addClaimedData(uuid, time);
    }

    public void addClaimedData(@NotNull UUID uuid, @NotNull Map<String, LocalDateTime> data) throws Exception {
        if (!data.isEmpty()) {
            List<Object[]> values = data.entrySet().stream()
                    .map(entry -> new Object[]{uuid.toString(), entry.getKey(), entry.getValue()})
                    .collect(Collectors.toList());
            DatabaseTables.USER_CLAIMED.createReplaceBatch()
                    .setColumnNames("uuid", "reward", "time")
                    .setAllParams(values).execute();
        } else {
            DatabaseTables.USER_CLAIMED.createDelete()
                    .addCondition("uuid", uuid).setLimit(1)
                    .build().execute();
        }
    }

    public void savePlayTime(@NotNull UUID uuid, @Nullable TimeRecord newRecord) throws Exception {
        if (newRecord != null) {
            DatabaseTables.USER_TIMES.createReplace()
                    .setColumnNames("uuid", "date", "daily_time", "weekly_time", "monthly_time", "total_time")
                    .setParams(
                            uuid.toString(), newRecord.getDate(),
                            newRecord.getDailyTime().getSeconds(),
                            newRecord.getWeeklyTime().getSeconds(),
                            newRecord.getMonthlyTime().getSeconds(),
                            newRecord.getTotalTime().getSeconds()
                    ).execute();
        } else {
            DatabaseTables.USER_TIMES.createDelete()
                    .addCondition("uuid", uuid).setLimit(1)
                    .build().execute();
        }
    }

}

