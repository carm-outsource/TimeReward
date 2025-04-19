package cc.carm.plugin.timereward.storage.database;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;

@HeaderComments("数据库相关设定")
@ConfigPath(value = "storage.database", root = true)
public class DatabaseConfig implements Configuration {

    @ConfigPath("driver")
    protected static final ConfiguredValue<String> DRIVER_NAME = ConfiguredValue.of(
            String.class, "com.mysql.jdbc.Driver"
    );

    protected static final ConfiguredValue<String> HOST = ConfiguredValue.of(String.class, "127.0.0.1");
    protected static final ConfiguredValue<Integer> PORT = ConfiguredValue.of(Integer.class, 3306);
    protected static final ConfiguredValue<String> DATABASE = ConfiguredValue.of(String.class, "minecraft");
    protected static final ConfiguredValue<String> USERNAME = ConfiguredValue.of(String.class, "root");
    protected static final ConfiguredValue<String> PASSWORD = ConfiguredValue.of(String.class, "password");
    protected static final ConfiguredValue<String> EXTRA = ConfiguredValue.of(String.class, "?useSSL=false");

    @HeaderComments("插件相关表的名称")
    public static final class TABLES implements Configuration {
        public static final ConfiguredValue<String> USER_TIMES = ConfiguredValue.of(String.class, "tr_user_times");
        public static final ConfiguredValue<String> USER_CLAIMED = ConfiguredValue.of(String.class, "tr_user_claimed");
    }

    protected static String buildJDBC() {
        return String.format("jdbc:mysql://%s:%s/%s%s",
                HOST.getNotNull(), PORT.getNotNull(), DATABASE.getNotNull(), EXTRA.getNotNull()
        );
    }

}
