```text
  _______                ____                              __
 /_  __(_)___ ___  ___  / __ \___ _      ______ __________/ /
  / / / / __ `__ \/ _ \/ /_/ / _ \ | /| / / __ `/ ___/ __  / 
 / / / / / / / / /  __/ _, _/  __/ |/ |/ / /_/ / /  / /_/ /  
/_/ /_/_/ /_/ /_/\___/_/ |_|\___/|__/|__/\__,_/_/   \__,_/   
```

# TimeReward

![CodeSize](https://img.shields.io/github/languages/code-size/carm-outsource/TimeReward)
[![Download](https://img.shields.io/github/downloads/carm-outsource/TimeReward/total)](https://github.com/carm-outsource/TimeReward/releases)
[![Java CI with Maven](https://github.com/CarmJos/TimeReward/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/TimeReward/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.8--Latest-yellow)
![](https://visitor-badge.glitch.me/badge?page_id=TimeReward.readme)

使用数据库存储的在线时长自动领奖插件，通过指令发放奖励，基于EasyPlugin实现。

## 功能

- 基于数据库存储的用户在线时长统计。
- **高效算法。** 在线时间不受服务器卡顿(如果有)影响。
- **异步存取。** 数据读取与存储均为异步操作，不影响服务器性能。
- **接口全面。** 提供插件用户数据访问接口与变量，便于其他插件进行读取判断。
- **轻量插件。** 适合小型服务器使用，配置简单方便。
- **规范开发。** 插件架构符合开发规范，适合新手开发者学习。

## [依赖](https://github.com/CarmJos/TimeReward/network/dependencies)

- **[必须]** 插件本体基于 [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT) 、[BukkitAPI](http://bukkit.org/) 实现。
- **[自带]** 插件功能基于 [EasyPlugin](https://github.com/CarmJos/EasyPlugin) 实现。
- **[自带]** 数据功能基于 [EasySQL](https://github.com/CarmJos/EasySQL) 实现。
- **[自带]** 消息格式基于 [MineDown](https://github.com/Phoenix616/MineDown) 实现。
    - 所有 messages.yml 均支持 MineDown 语法。
- **[推荐]** 变量部分基于 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 实现。

详细依赖列表可见 [Dependencies](https://github.com/CarmJos/TimeReward/network/dependencies) 。

## [指令](src/main/resources/plugin.yml)

以下指令的主指令为 `/TimeReward` 或 `/tr`。

- 必须参数 `<参数>`

```text
# claim [奖励ID]
@ 玩家指令
- 为自己手动领取对应奖励。
- 若不填写奖励ID，则自动领取全部可领取的奖励。

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

## 变量 (PlaceholderAPI)

安装 PlaceholderAPI 后，可以输入 /papi info TimeReward 查看相关变量。

变量如下:

```text
# %TimeReward_time%
- 得到玩家总共的在线时长(秒)。

# %TimeReward_reward_<奖励ID>%
- 得到某个奖励配置的名称。

# %TimeReward_claimed_<奖励ID>%
- 得到玩家是否已经领取了某个奖励。

# %TimeReward_claimable_<奖励ID>%
- 得到玩家是否可以领取某个奖励 
```

## 配置文件

### 插件配置文件 ([config.yml](src/main/java/cc/carm/plugin/timereward/conf/PluginConfig.java))

详见代码源文件，将在首次启动时生成配置。

### 消息配置文件 ([messages.yml](src/main/java/cc/carm/plugin/timereward/conf/PluginMessages.java))

支持 [MineDown 语法](https://wiki.phoenix616.dev/library:minedown:syntax)，详见代码源文件，将在首次启动时生成配置。

## 使用统计

[![bStats](https://bstats.org/signatures/bukkit/TimeReward.svg)](https://bstats.org/plugin/bukkit/TimeReward/14505)

## 支持与捐赠

若您觉得本插件做的不错，您可以捐赠支持我，感谢您成为开源项目的支持者！

Many thanks to Jetbrains for kindly providing a license for me to work on this and other open-source projects.  
[![](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)](https://www.jetbrains.com/?from=https://github.com/CarmJos/UserPrefix)

## 开源协议
本项目源码采用 [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0) 开源协议。

<details>
<summary>关于 GPL 协议</summary>

> GNU General Public Licence (GPL) 有可能是开源界最常用的许可模式。GPL 保证了所有开发者的权利，同时为使用者提供了足够的复制，分发，修改的权利：
>
> #### 可自由复制
> 你可以将软件复制到你的电脑，你客户的电脑，或者任何地方。复制份数没有任何限制。
> #### 可自由分发
> 在你的网站提供下载，拷贝到U盘送人，或者将源代码打印出来从窗户扔出去（环保起见，请别这样做）。
> #### 可以用来盈利
> 你可以在分发软件的时候收费，但你必须在收费前向你的客户提供该软件的 GNU GPL 许可协议，以便让他们知道，他们可以从别的渠道免费得到这份软件，以及你收费的理由。
> #### 可自由修改
> 如果你想添加或删除某个功能，没问题，如果你想在别的项目中使用部分代码，也没问题，唯一的要求是，使用了这段代码的项目也必须使用 GPL 协议。
>
> 需要注意的是，分发的时候，需要明确提供源代码和二进制文件，另外，用于某些程序的某些协议有一些问题和限制，你可以看一下 @PierreJoye 写的 Practical Guide to GPL Compliance 一文。使用 GPL 协议，你必须在源代码代码中包含相应信息，以及协议本身。
>
> *以上文字来自 [五种开源协议GPL,LGPL,BSD,MIT,Apache](https://www.oschina.net/question/54100_9455) 。*
</details>


