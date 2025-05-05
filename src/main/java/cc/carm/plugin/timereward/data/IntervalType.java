package cc.carm.plugin.timereward.data;

import cc.carm.plugin.timereward.util.DateTimeUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public enum IntervalType {

    TOTAL(
            0, Duration.ofSeconds(4294967295L), time -> false,
            (timeRecord, join) -> Duration.between(join, LocalDateTime.now()).plus(timeRecord.getTotalTime())
    ),

    DAILY(
            1, Duration.ofDays(1), time -> DateTimeUtils.sameDay(time.toLocalDate()),
            (timeRecord, join) -> {
                LocalDateTime now = LocalDateTime.now();
                if (!now.toLocalDate().isEqual(join.toLocalDate())) {
                    // 加入的时间和现在的时间不在同一天
                    return Duration.between(DateTimeUtils.todayStartTime(), now);
                }
                if (now.toLocalDate().isEqual(timeRecord.getDate())) {  // 和记录还在同一天
                    return Duration.between(join, now).plus(timeRecord.getDailyTime());
                }
                return Duration.between(join, now);   // 加入的时间和现在的时间在同一天
            }
    ),
    WEEKLY(
            2, Duration.ofDays(7),
            time -> !DateTimeUtils.sameWeek(time),
            (r, join) -> {
                LocalDateTime now = LocalDateTime.now();
                if (!DateTimeUtils.sameWeek(join, now)) {
                    return Duration.between(DateTimeUtils.currentWeekStartTime(), now);
                }
                if (DateTimeUtils.sameWeek(r.getDate(), now)) {
                    return Duration.between(join, now).plus(r.getWeeklyTime());
                }
                return Duration.between(join, now);
            }
    ),

    MONTHLY(
            3, Duration.ofDays(31),
            time -> !DateTimeUtils.sameMonth(time.toLocalDate()),
            (r, join) -> {
                LocalDateTime now = LocalDateTime.now();
                if (!DateTimeUtils.sameMonth(join.toLocalDate(), now.toLocalDate())) {
                    return Duration.between(DateTimeUtils.currentMonthStartTime(), now);
                }
                if (DateTimeUtils.sameMonth(r.getDate(), now.toLocalDate())) {
                    return Duration.between(join, now).plus(r.getMonthlyTime());
                }
                return Duration.between(join, now);
            }
    );

    private final int id;
    private final @NotNull Duration maxDuration;
    private final @NotNull Predicate<LocalDateTime> periodChangePredicate;
    private final @NotNull BiFunction<TimeRecord, LocalDateTime, Duration> calculator;

    IntervalType(int id, @NotNull Duration maxDuration,
                 @NotNull Predicate<LocalDateTime> periodChangePredicate,
                 @NotNull BiFunction<TimeRecord, LocalDateTime, Duration> calculator) {
        this.id = id;
        this.maxDuration = maxDuration;
        this.periodChangePredicate = periodChangePredicate;
        this.calculator = calculator;
    }

    public int getID() {
        return id;
    }

    public @NotNull Duration getMaxDuration() {
        return maxDuration;
    }

    public @NotNull BiFunction<TimeRecord, LocalDateTime, Duration> calculator() {
        return calculator;
    }

    public @NotNull Predicate<LocalDateTime> changePredicator() {
        return periodChangePredicate;
    }

    public Duration calculate(@NotNull TimeRecord timeRecord, @NotNull LocalDateTime joinTime) {
        Duration result = calculator.apply(timeRecord, joinTime);
        // 如果超过最大值，则返回最大值
        return result.compareTo(maxDuration) > 0 ? maxDuration : result;
    }

    public boolean isPeriodChanged(@NotNull LocalDateTime claimedDate) {
        return changePredicator().test(claimedDate);
    }

    public static IntervalType parse(String input) {
        if (input == null) return null;
        else return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(input) || Integer.toString(t.id).equals(input))
                .findFirst().orElse(null);
    }

}
