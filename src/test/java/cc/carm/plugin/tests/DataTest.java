package cc.carm.plugin.tests;

import cc.carm.plugin.timereward.data.IntervalType;
import cc.carm.plugin.timereward.data.TimeRecord;
import cc.carm.plugin.timereward.user.UserRewardData;
import cc.carm.plugin.timereward.util.DateTimeUtils;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class DataTest {

    DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void test() {

        LocalDate date = LocalDate.of(2025, 5, 1);
        LocalDateTime dateTime = LocalDateTime.now().minusHours(10).minusSeconds(600);

        System.out.println("-----------------------------------------");
        System.out.println("   DURATION: " + Duration.between(dateTime, LocalDateTime.now()).getSeconds());
        System.out.println("TODAY START: " + DATETIME.format(DateTimeUtils.todayStartTime()));
        System.out.println("WEEK  START: " + DATETIME.format(DateTimeUtils.currentWeekStartTime()));
        System.out.println("MONTH START: " + DATETIME.format(DateTimeUtils.currentMonthStartTime()));
        System.out.println("-----------------------------------------");
        System.out.println("RECORD DATE: " + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date));
        System.out.println("  JOIN TIME: " + DATETIME.format(dateTime));
        System.out.println("-----------------------------------------");

        UserRewardData data = createData(date, dateTime);
        for (IntervalType value : IntervalType.values()) {
            System.out.println(value.name() + " = " + data.getOnlineDuration(value).getSeconds());
        }

    }

    static UserRewardData createData(LocalDate date, LocalDateTime join) {
        return new UserRewardData(
                UUID.randomUUID(), new HashMap<>(),
                new TimeRecord(date, 0, 0, 0, 0),
                join
        );
    }

}
