import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;

public class TimeTest {

    LocalDate date = LocalDate.of(2023, 8, 30);

    @Test
    public void test() {

        LocalDate date1 = LocalDate.of(2023, 9, 2);
        LocalDate date2 = LocalDate.of(2023, 8, 28);
        LocalDate date3 = LocalDate.of(2023, 8, 27);

        System.out.println(isDayUpdated(date1));
        System.out.println(isWeekUpdated(date1));
        System.out.println(isMonthUpdated(date1));

        System.out.println(isDayUpdated(date2));
        System.out.println(isWeekUpdated(date2));
        System.out.println(isMonthUpdated(date2));

        System.out.println(isDayUpdated(date3));
        System.out.println(isWeekUpdated(date3));
        System.out.println(isMonthUpdated(date3));

        System.out.println(isDayUpdated(date));

    }

    public @NotNull LocalDate getDate() {
        return date;
    }

    public boolean isDayUpdated(LocalDate d) {
        return !d.equals(getDate());
    }

    public boolean isWeekUpdated(LocalDate d) {
        if (!isDayUpdated(d)) return false; // Same day always same week

        TemporalField woy = WeekFields.of(DayOfWeek.MONDAY, 4).weekOfWeekBasedYear();
        return getDate().get(woy) != d.get(woy);
    }

    public boolean isMonthUpdated(LocalDate d) {
        if (!isDayUpdated(d)) return false; // Same day always same month

        // Predicate current month is after the month of the date
        return d.getMonth().compareTo(getDate().getMonth()) > 0;
    }
}
