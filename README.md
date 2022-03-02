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

使用数据库存储的在线时长自动领奖插件，通过指令发放奖励，基于EasyPlugin实现。

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

## 支持与捐赠

若您觉得本插件做的不错，您可以捐赠支持我！

感谢您成为开源项目的支持者！

<img height=25% width=25% src="https://raw.githubusercontent.com/CarmJos/CarmJos/main/img/donate-code.jpg"  alt=""/>

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


