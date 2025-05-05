package cc.carm.plugin.timereward.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.function.Supplier;

public class DateTimeUtils {

    public static Supplier<DayOfWeek> WEEK_START_DAY = () -> DayOfWeek.MONDAY;

    public static boolean sameDay(LocalDate a) {
        return sameDay(a, LocalDate.now());
    }

    public static boolean sameDay(LocalDate a, LocalDate b) {
        return a.getDayOfYear() == b.getDayOfYear() && a.getYear() == b.getYear();
    }

    public static boolean sameWeek(Temporal a) {
        return sameWeek(WEEK_START_DAY.get(), a, LocalDate.now());
    }

    public static boolean sameWeek(DayOfWeek weekStart, Temporal a) {
        return sameWeek(weekStart, a, LocalDate.now());
    }

    public static boolean sameWeek(Temporal a, Temporal b) {
        return sameWeek(WEEK_START_DAY.get(), a, b);
    }

    public static boolean sameWeek(DayOfWeek weekStart, Temporal a, Temporal b) {
        TemporalField woy = WeekFields.of(weekStart, 4).weekOfWeekBasedYear();
        return a.get(woy) == b.get(woy);
    }

    public static boolean sameMonth(LocalDate a) {
        return sameMonth(a, LocalDate.now());
    }

    public static boolean sameMonth(LocalDate a, LocalDate b) {
        return a.getMonth().equals(b.getMonth());
    }

    public static LocalDateTime todayStartTime() {
        return LocalDate.now().atTime(0, 0);
    }

    public static LocalDate currentWeekStartDay(DayOfWeek weekStart) {
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(weekStart));
    }

    public static LocalDate currentWeekStartDay() {
        return currentWeekStartDay(WEEK_START_DAY.get());
    }

    public static LocalDateTime currentWeekStartTime(DayOfWeek weekStart) {
        return currentWeekStartDay(weekStart).atTime(0, 0);
    }

    public static LocalDateTime currentWeekStartTime() {
        return currentWeekStartTime(WEEK_START_DAY.get());
    }

    public static LocalDateTime currentMonthStartTime() {
        return LocalDate.now().withDayOfMonth(1).atTime(0, 0);
    }

}
