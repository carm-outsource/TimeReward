package cc.carm.plugin.tests;

import cc.carm.plugin.timereward.data.IntervalType;
import cc.carm.plugin.timereward.data.TimeRecord;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class IntervalTest {

    @Test
    public void test() {

        System.out.println(IntervalType.DAILY.calculate(
                new TimeRecord(LocalDate.now(), 100, 200, 300, 400),
                LocalDateTime.now()
        ).getSeconds());

    }

}
