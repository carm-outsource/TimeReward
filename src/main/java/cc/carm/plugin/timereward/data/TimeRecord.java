package cc.carm.plugin.timereward.data;

import cc.carm.plugin.timereward.conf.PluginConfig;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;

public class TimeRecord {

    public static TimeRecord empty() {
        return new TimeRecord(LocalDate.now(), 0, 0, 0, 0);
    }


    protected final @NotNull LocalDate date;

    protected final @NotNull Duration daily;
    protected final @NotNull Duration weekly;
    protected final @NotNull Duration monthly;

    protected final @NotNull Duration total;

    public TimeRecord(@NotNull LocalDate date, long daily, long weekly, long monthly, long total) {
        this(date,
                Duration.ofSeconds(daily), Duration.ofSeconds(weekly),
                Duration.ofSeconds(monthly), Duration.ofSeconds(total)
        );
    }

    public TimeRecord(@NotNull LocalDate date,
                      @NotNull Duration daily, @NotNull Duration weekly,
                      @NotNull Duration monthly, @NotNull Duration total) {
        this.date = date;
        this.daily = daily;
        this.weekly = weekly;
        this.monthly = monthly;
        this.total = total;
    }

    public @NotNull LocalDate getDate() {
        return date;
    }

    public @NotNull Duration getDailyTime() {
        return daily;
    }

    public @NotNull Duration getWeeklyTime() {
        return weekly;
    }

    public @NotNull Duration getMonthlyTime() {
        return monthly;
    }

    public @NotNull Duration getTotalTime() {
        return total;
    }

    public boolean isDayUpdated() {
        return !LocalDate.now().equals(getDate());
    }

    public boolean isWeekUpdated() {
        if (!isDayUpdated()) return false; // Same day always same week
        return !isSameWeek(LocalDate.now(), getDate());
    }

    public boolean isMonthUpdated() {
        if (!isDayUpdated()) return false; // Same day always same month

        // Predicate current month is after the month of the date
        return LocalDate.now().getMonth().compareTo(getDate().getMonth()) > 0;
    }

    public static boolean isSameWeek(Temporal a, Temporal b) {
        TemporalField woy = WeekFields.of(PluginConfig.WEEK_FIRST_DAY.getNotNull(), 4).weekOfWeekBasedYear();
        return a.get(woy) == b.get(woy);
    }

    public static boolean isSameMonth(LocalDate a, LocalDate b) {
        return a.getMonth().equals(b.getMonth());
    }

    public static LocalDateTime getTodayStart() {
        return LocalDate.now().atTime(0, 0);
    }

    public static LocalDateTime getThisWeekStart() {
        TemporalField woy = WeekFields.of(PluginConfig.WEEK_FIRST_DAY.getNotNull(), 4).weekOfWeekBasedYear();
        return LocalDate.now().with(woy, 1).atTime(0, 0);
    }

    public static LocalDateTime getThisMonthStart() {
        return LocalDate.now().withDayOfMonth(1).atTime(0, 0);
    }

    @Override
    public String toString() {
        return "TimeRecord{" +
                "date=" + date +
                ", daily=" + daily +
                ", weekly=" + weekly +
                ", monthly=" + monthly +
                ", total=" + total +
                '}';
    }
}
