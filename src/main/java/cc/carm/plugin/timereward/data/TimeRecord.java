package cc.carm.plugin.timereward.data;

import cc.carm.plugin.timereward.conf.PluginConfig;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;

public class TimeRecord {

    protected final @NotNull LocalDate date;

    protected final int daily;
    protected final int weekly;
    protected final int monthly;

    protected final int total;

    public TimeRecord(@NotNull LocalDate date, int daily, int weekly, int monthly, int total) {
        this.date = date;
        this.daily = daily;
        this.weekly = weekly;
        this.monthly = monthly;
        this.total = total;
    }

    public @NotNull LocalDate getDate() {
        return date;
    }

    public int getDailyTime() {
        return isDayUpdated() ? 0 : daily;
    }

    public int getWeeklyTime() {
        return isWeekUpdated() ? 0 : weekly;
    }

    public int getMonthlyTime() {
        return isMonthUpdated() ? 0 : monthly;
    }

    public boolean isDayUpdated() {
        return !LocalDate.now().equals(getDate());
    }

    public boolean isWeekUpdated() {
        if (!isDayUpdated()) return false; // Same day always same week

        TemporalField woy = WeekFields.of(PluginConfig.WEEK_FIRST_DAY.getNotNull(), 4).weekOfWeekBasedYear();
        return getDate().get(woy) != LocalDate.now().get(woy);
    }

    public boolean isMonthUpdated() {
        if (!isDayUpdated()) return false; // Same day always same month

        // Predicate current month is after the month of the date
        return LocalDate.now().getMonth().compareTo(getDate().getMonth()) > 0;
    }


}
