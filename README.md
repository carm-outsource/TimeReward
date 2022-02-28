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
![Support](https://img.shields.io/badge/Minecraft-Java%201.12--Latest-yellow)
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
# reload
@ 管理指令 (TimeReward.admin)
- 重载插件配置文件。

# user <玩家名>
@ 管理指令 (TimeReward.admin)
- 查看用户的在线时长信息与奖励领取情况。

# list 
@ 管理指令 (TimeReward.admin)
- 列出所有奖励与条件。

# test <奖励ID>
@ 管理指令 (TimeReward.admin)
- 测试执行奖励配置的指令。
```

## 插件权限

```text

# TimeReward.admin
- 建筑魔杖的管理权限。

```

## 插件变量

```text
# %TimeReward_time%
- 得到玩家总共的在线时长(秒)。

# %TimeReward_reward_<奖励ID>%
- 得到某个奖励配置的名称。

# %TimeReward_claimed_<奖励ID>%
- 得到玩家是否已经领取了某个奖励。
- * 也可以代表玩家是否可以领取某个奖励 
- * 因为一旦可以领取就会自动领取，变为已领取状态

```

## 配置文件

### 插件配置文件 ([config.yml](src/main/resources/config.yml))

详见源文件。

### 消息配置文件 ([messages.yml](src/main/java/cc/carm/plugin/timereward/configuration/PluginMessages.java))

详见代码源文件，将在首次启动时生成配置。