package cc.carm.plugin.timereward.data;

import cc.carm.plugin.timereward.util.DateTimeUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDate;

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
        return !DateTimeUtils.sameWeek(LocalDate.now(), getDate());
    }

    public boolean isMonthUpdated() {
        if (!isDayUpdated()) return false; // Same day always same month
        return !DateTimeUtils.sameMonth(LocalDate.now(), getDate());
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
