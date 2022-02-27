package cc.carm.plugin.timereward.database;

import cc.carm.lib.easyplugin.configuration.values.ConfigValue;

public class DBConfiguration {

    protected static final ConfigValue<String> DRIVER_NAME = new ConfigValue<>(
            "storage.mysql.driver", String.class,
            "com.mysql.cj.jdbc.Driver"
    );

    protected static final ConfigValue<String> HOST = new ConfigValue<>(
            "storage.mysql.host", String.class,
            "127.0.0.1"
    );

    protected static final ConfigValue<Integer> PORT = new ConfigValue<>(
            "storage.mysql.port", Integer.class,
            3306
    );

    protected static final ConfigValue<String> DATABASE = new ConfigValue<>(
            "storage.mysql.database", String.class,
            "minecraft"
    );

    protected static final ConfigValue<String> USERNAME = new ConfigValue<>(
            "storage.mysql.username", String.class,
            "root"
    );

    protected static final ConfigValue<String> PASSWORD = new ConfigValue<>(
            "storage.mysql.password", String.class,
            "password"
    );

    protected static final ConfigValue<String> ADDITIONAL = new ConfigValue<>(
            "storage.mysql.additional", String.class,
            "?useSSL=false"
    );

    protected static String buildJDBC() {
        return String.format("jdbc:mysql://%s:%s/%s%s",
                HOST.get(), PORT.get(), DATABASE.get(), ADDITIONAL.get()
        );
    }


}
