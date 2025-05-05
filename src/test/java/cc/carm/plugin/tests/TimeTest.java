package cc.carm.plugin.tests;

import cc.carm.plugin.timereward.data.IntervalType;
import cc.carm.plugin.timereward.data.TimeRecord;
import org.junit.Test;

import java.time.LocalDate;

public class TimeTest {

    LocalDate date = LocalDate.of(2023, 8, 30);

    @Test
    public void test() {

        TimeRecord record1 = new TimeRecord(LocalDate.now().minusMonths(1), 0, 0, 0, 0);
        TimeRecord record2 = new TimeRecord(LocalDate.now().minusWeeks(1), 0, 0, 0, 0);
        TimeRecord record3 = new TimeRecord(LocalDate.now().minusDays(1), 0, 0, 0, 0);

        System.out.println(record1.isDayUpdated());
        System.out.println(record1.isWeekUpdated());
        System.out.println(record1.isMonthUpdated());

        System.out.println(record2.isDayUpdated());
        System.out.println(record2.isWeekUpdated());
        System.out.println(record2.isMonthUpdated());

        System.out.println(record3.isDayUpdated());
        System.out.println(record3.isWeekUpdated());
        System.out.println(record3.isMonthUpdated());

    }

    @Test
    public void durationTest() {
        for (IntervalType value : IntervalType.values()) {
            System.out.println(value.name() + " = <MAX> = " + value.getMaxDuration().getSeconds());
        }
    }

}
