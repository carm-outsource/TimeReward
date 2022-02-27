```text
 _____ _               ______                           _ 
|_   _(_)              | ___ \                         | |
  | |  _ _ __ ___   ___| |_/ /_____      ____ _ _ __ __| |
  | | | | '_ ` _ \ / _ \    // _ \ \ /\ / / _` | '__/ _` |
  | | | | | | | | |  __/ |\ \  __/\ V  V / (_| | | | (_| |
  \_/ |_|_| |_| |_|\___\_| \_\___| \_/\_/ \__,_|_|  \__,_|
```

# TimeReward

[![workflow](https://github.com/CarmJos/TimeReward/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/TimeReward/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.16--Latest-yellow)
![](https://visitor-badge.glitch.me/badge?page_id=TimeReward.readme)

在线时长自动领奖插件，通过指令发放奖励，基于EasyPlugin实现。

## 插件依赖

- **[必须]** 插件本体基于 [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT) 、 [BukkitAPI](http://bukkit.org/) 实现。
- **[自带]** 插件功能基于 [EasyPlugin](https://github.com/CarmJos/EasyPlugin) 实现。
- **[自带]** 数据功能基于 [EasySQL](https://github.com/CarmJos/EasySQL) 实现。
 
详细依赖列表可见 [Dependencies](https://github.com/CarmJos/timereward/network/dependencies) 。

## 插件指令

指令主指令为 `/TimeReward`

```text
```

## 插件权限

```text

# TimeReward.admin
- 建筑魔杖的管理权限。

```

## 配置文件

### 插件配置文件 ([config.yml](src/main/resources/config.yml))

详见源文件。

### 消息配置文件 ([messages.yml](src/main/java/cc/carm/plugin/timereward/configuration/PluginMessages.java))

详见代码源文件，将在首次启动时生成配置。