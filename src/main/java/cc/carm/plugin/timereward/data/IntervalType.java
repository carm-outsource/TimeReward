package cc.carm.plugin.timereward.data;

import cc.carm.plugin.timereward.Main;
import cc.carm.plugin.timereward.util.DateTimeUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDate;
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
            1, Duration.ofDays(1), time -> !DateTimeUtils.sameDay(time.toLocalDate()),
            (record, join) -> {
                LocalDateTime now = LocalDateTime.now();
                LocalDate today = LocalDate.now();
                if (DateTimeUtils.sameDay(join.toLocalDate(), today)) {
                    // 如果加入的时间和今天的日期相同
                    if (DateTimeUtils.sameDay(record.getDate(), today)) {
                        // 如果上一次记录的日期和今天的日期相同
                        // 则从加入的时间开始计算到现在的时间，并加上当天的游玩时间
                        return Duration.between(join, now).plus(record.getDailyTime());
                    } else {
                        // 如果上一次记录的日期和今天的日期不同
                        // 则从加入的时间开始计算到现在的时间
                        return Duration.between(join, now);
                    }
                } else {
                    // 如果加入的时间和今天的日期不同
                    // 则从今天的0点开始计算到现在的时间
                    return Duration.between(today.atTime(0, 0, 0), now);
                }
            }
    ),
    WEEKLY(
            2, Duration.ofDays(7),
            time -> !DateTimeUtils.sameWeek(time),
            (r, join) -> {
                LocalDateTime now = LocalDateTime.now();
                if (DateTimeUtils.sameWeek(join.toLocalDate(), now)) {
                    if (DateTimeUtils.sameWeek(r.getDate(), now.toLocalDate())) {
                        return Duration.between(join, now).plus(r.getWeeklyTime());
                    } else {
                        return Duration.between(join, now);
                    }
                } else {
                    return Duration.between(DateTimeUtils.currentWeekStartTime(), now);
                }
            }
    ),

    MONTHLY(
            3, Duration.ofDays(31),
            time -> !DateTimeUtils.sameMonth(time.toLocalDate()),
            (r, join) -> {
                LocalDateTime now = LocalDateTime.now();
                if (DateTimeUtils.sameMonth(join.toLocalDate(), now.toLocalDate())) {
                    if (DateTimeUtils.sameMonth(r.getDate(), now.toLocalDate())) {
                        return Duration.between(join, now).plus(r.getMonthlyTime());
                    } else {
                        return Duration.between(join, now);
                    }
                } else {
                    return Duration.between(DateTimeUtils.currentMonthStartTime(), now);
                }
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


    public Duration calculate(@NotNull TimeRecord timeRecord, @NotNull LocalDateTime joinTime) {
        Duration result = calculator.apply(timeRecord, joinTime);
        // 如果超过最大值，则返回最大值
        if (result.compareTo(maxDuration) > 0) {
            Main.debugging("在线时间超过最大值，类型: " + this.name() + ", 计算结果: " + result + " 加入时间 " + joinTime + " 时间记录" + timeRecord);
            return maxDuration;
        }
        return result;
    }

    public boolean isPeriodChanged(@NotNull LocalDateTime claimedDate) {
        return periodChangePredicate.test(claimedDate);
    }

    public static IntervalType parse(String input) {
        if (input == null) return null;
        else return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(input) || Integer.toString(t.id).equals(input))
                .findFirst().orElse(null);
    }

}
