package cc.carm.plugin.timereward.user;

import cc.carm.lib.easyplugin.user.UserData;
import cc.carm.plugin.timereward.data.IntervalType;
import cc.carm.plugin.timereward.data.RewardContents;
import cc.carm.plugin.timereward.data.TimeRecord;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 用户奖励数据，用于存储用户的奖励的领取情况。
 */
public class UserRewardData extends UserData<UUID> {

    private final @NotNull Map<String, LocalDateTime> claimedRewards; // 记录已领取的奖励ID

    private final @NotNull TimeRecord recordTime;
    private final @NotNull LocalDateTime joinTime;

    public UserRewardData(@NotNull UUID userUUID, @NotNull TimeRecord timeRecord, @NotNull Map<String, LocalDateTime> claimedRewards) {
        this(userUUID, claimedRewards, timeRecord, LocalDateTime.now());
    }

    public UserRewardData(@NotNull UUID userUUID, @NotNull Map<String, LocalDateTime> claimedRewards,
                          @NotNull TimeRecord recordTime, @NotNull LocalDateTime joinTime) {
        super(userUUID);
        this.claimedRewards = claimedRewards;
        this.recordTime = recordTime;
        this.joinTime = joinTime;
    }

    public @NotNull UUID getUserUUID() {
        return getKey();
    }

    /**
     * @return 数据库中的时间记录
     */
    public @NotNull TimeRecord getTimeRecord() {
        return recordTime;
    }

    /**
     * 得到本次加入游戏的时间
     *
     * @return 本次加入游戏时间
     */
    public @NotNull LocalDateTime getJoinTime() {
        return joinTime;
    }

    /**
     * @return 玩家的在线时间周期，需指定周期类型{@link IntervalType}
     */
    public Duration getOnlineDuration(@NotNull IntervalType type) {
        return type.calculate(getTimeRecord(), getJoinTime());
    }

    public boolean isTimeEnough(RewardContents reward) {
        return getOnlineDuration(reward.getType()).getSeconds() >= reward.getTime();
    }


    public @NotNull Map<String, LocalDateTime> getClaimedRewards() {
        return claimedRewards;
    }

    public boolean isClaimed(@NotNull RewardContents reward) {
        LocalDateTime claimedDate = claimedRewards.get(reward.getRewardID());
        if (claimedDate == null) return false;
        return !reward.getType().isReclaimable(claimedDate);
    }

    public boolean addClaimedReward(@NotNull RewardContents reward) {
        if (isClaimed(reward)) return false; // 已经领取过了
        this.claimedRewards.put(reward.getRewardID(), LocalDateTime.now());
        return true;
    }

}
