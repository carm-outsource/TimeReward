package cc.carm.plugin.timereward.user;

import cc.carm.plugin.timereward.data.IntervalType;
import cc.carm.plugin.timereward.data.RewardContents;
import cc.carm.plugin.timereward.data.TimeRecord;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

public class LockedRewardData extends UserRewardData {
    public LockedRewardData(@NotNull UUID userUUID) {
        super(userUUID, TimeRecord.empty(), Collections.emptyMap());
    }

    @Override
    public Duration getOnlineDuration(@NotNull IntervalType type) {
        return Duration.ZERO;
    }

    @Override
    public boolean isClaimed(@NotNull RewardContents reward) {
        return false;
    }

    @Override
    public boolean isTimeEnough(RewardContents reward) {
        return false;
    }

    @Override
    public void updateClaimed(@NotNull RewardContents reward) {
    }

}
