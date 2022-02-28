package cc.carm.plugin.timereward.data;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户奖励数据，用于存储用户的奖励的领取情况。
 */
public class TimeRewardUser {

    UUID userUUID;

    Set<@NotNull String> claimedRewards; //记录已领取的奖励ID

    long storedSeconds; //记录已经游玩的时间
    long joinMillis; // 记录本次加入的时间

    public TimeRewardUser(UUID userUUID) {
        this(userUUID, new LinkedHashSet<>(), 0);
    }

    public TimeRewardUser(UUID userUUID, Set<@NotNull String> claimedRewards,
                          long storedSeconds) {
        this(userUUID, claimedRewards, storedSeconds, System.currentTimeMillis());
    }

    public TimeRewardUser(UUID userUUID, Set<@NotNull String> claimedRewards,
                          long storedSeconds, long joinMillis) {
        this.userUUID = userUUID;
        this.claimedRewards = claimedRewards;
        this.storedSeconds = storedSeconds;
        this.joinMillis = joinMillis;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public long getStoredSeconds() {
        return storedSeconds;
    }

    /**
     * 得到本次加入游戏的时间
     *
     * @return 本次加入游戏时间
     */
    public long getJoinMillis() {
        return joinMillis;
    }


    /**
     * 获取玩家本次加入服务器的时间 ，即为 当前时间-加入时间。
     *
     * @return 玩家本次加入服务器的时间
     */
    public long getCurrentSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - getJoinMillis());
    }

    /**
     * 获取 玩家在数据库中已经游玩的时间 + (当前时间-加入时间)
     *
     * @return 之前游玩的世界与本次游玩时间之和
     */
    public long getAllSeconds() {
        return getStoredSeconds() + getCurrentSeconds();
    }

    public Set<String> getClaimedRewards() {
        return claimedRewards;
    }

    public boolean isClaimed(@NotNull String rewardId) {
        return this.claimedRewards.contains(rewardId);
    }

    public boolean addClaimedReward(@NotNull String rewardId) {
        if (isClaimed(rewardId)) return false; // 已经领取过了
        this.claimedRewards.add(rewardId);
        return true;
    }

}
