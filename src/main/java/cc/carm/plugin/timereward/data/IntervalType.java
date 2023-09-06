package cc.carm.plugin.timereward.data;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public enum IntervalType {

    TOTAL(
            0, time -> false,
            (timeRecord, join) -> Duration.between(join, LocalDateTime.now()).plus(timeRecord.getTotalTime())
    ),

    DAILY(
            1, time -> time.isBefore(TimeRecord.getTodayStart()),
            (timeRecord, join) -> {
                LocalDateTime now = LocalDateTime.now();
                if (now.toLocalDate().isEqual(timeRecord.getDate())) {
                    // 和记录还在同一天
                    return Duration.between(join, now).plus(timeRecord.getDailyTime());
                } else if (now.toLocalDate().isEqual(join.toLocalDate())) {
                    // 加入的时间和现在的时间在同一天
                    return Duration.between(join, now);
                } else {
                    // 加入的时间和现在的时间不在同一天
                    return Duration.between(TimeRecord.getTodayStart(), now);
                }
            }
    ),
    WEEKLY(
            2, time -> time.isBefore(TimeRecord.getThisWeekStart()),
            (timeRecord, join) -> {
                LocalDateTime now = LocalDateTime.now();
                if (TimeRecord.isSameWeek(timeRecord.getDate(), now)) {
                    return Duration.between(join, now).plus(timeRecord.getWeeklyTime());
                } else if (TimeRecord.isSameWeek(join, now)) {
                    return Duration.between(join, now);
                } else {
                    return Duration.between(TimeRecord.getThisWeekStart(), now);
                }
            }
    ),

    MONTHLY(
            3, time -> time.isBefore(TimeRecord.getThisMonthStart()),
            (timeRecord, join) -> {
                LocalDateTime now = LocalDateTime.now();
                if (TimeRecord.isSameMonth(timeRecord.getDate(), now.toLocalDate())) {
                    return Duration.between(join, now).plus(timeRecord.getMonthlyTime());
                } else if (TimeRecord.isSameMonth(join.toLocalDate(), now.toLocalDate())) {
                    return Duration.between(join, now);
                } else {
                    return Duration.between(TimeRecord.getThisMonthStart(), now);
                }
            }
    );

    private final int id;
    private final @NotNull Predicate<LocalDateTime> periodChangePredicate;
    private final @NotNull BiFunction<TimeRecord, LocalDateTime, Duration> calculator;

    IntervalType(int id,
                 @NotNull Predicate<LocalDateTime> periodChangePredicate,
                 @NotNull BiFunction<TimeRecord, LocalDateTime, Duration> calculator) {
        this.id = id;
        this.periodChangePredicate = periodChangePredicate;
        this.calculator = calculator;
    }

    public int getID() {
        return id;
    }

    public @NotNull BiFunction<TimeRecord, LocalDateTime, Duration> getCalculator() {
        return calculator;
    }

    public @NotNull Predicate<LocalDateTime> getPreiodChangePredicate() {
        return periodChangePredicate;
    }

    public Duration calculate(@NotNull TimeRecord timeRecord, @NotNull LocalDateTime joinTime) {
        return calculator.apply(timeRecord, joinTime);
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
