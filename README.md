```text

 _____            _               _____           _            _   _             
|  __ \          (_)             |  __ \         | |          | | (_)            
| |__) |___  __ _ _  ___  _ __   | |__) | __ ___ | |_ ___  ___| |_ _  ___  _ __  
|  _  // _ \/ _` | |/ _ \| '_ \  |  ___/ '__/ _ \| __/ _ \/ __| __| |/ _ \| '_ \ 
| | \ \  __/ (_| | | (_) | | | | | |   | | | (_) | ||  __/ (__| |_| | (_) | | | |
|_|  \_\___|\__, |_|\___/|_| |_| |_|   |_|  \___/ \__\___|\___|\__|_|\___/|_| |_|
             __/ |                                                               
            |___/                                                                
```

# timereward

[![workflow](https://github.com/CarmJos/timereward/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/timereward/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.16--Latest-yellow)
![](https://visitor-badge.glitch.me/badge?page_id=timereward.readme)

区域保护插件，将不符合条件的玩家弹出区域，基于EasyPlugin实现。

本插件由 [重庆溢鹏多赛科技有限公司](https://ypchongqing.com) 请求本人开发，经过授权后开源。

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