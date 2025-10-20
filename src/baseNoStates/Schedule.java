package baseNoStates;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Represents a work schedule for a user group.  A schedule defines a date
 * interval, a set of permitted days of the week and a daily start and end
 * time.  Requests are allowed only when the request timestamp falls
 * within the interval, on one of the permitted weekdays, and between the
 * start and end times (exclusive).
 */
public class Schedule {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final ArrayList<DayOfWeek> workDays;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Schedule(LocalDate startDate,
                    LocalDate endDate,
                    ArrayList<DayOfWeek> workDays,
                    LocalTime startTime,
                    LocalTime endTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.workDays = workDays;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns true if the given timestamp is within this schedule.
     * The date component must fall within [startDate, endDate], the day of week
     * must be one of the work days, and the time must be strictly between the
     * start and end time.
     */
    public boolean isWithinSchedule(LocalDateTime requestDateTime) {
        LocalDate date = requestDateTime.toLocalDate();
        LocalTime time = requestDateTime.toLocalTime();
        DayOfWeek day = date.getDayOfWeek();
        if (date.isBefore(startDate) || date.isAfter(endDate)) {
            return false;
        }
        if (!workDays.contains(day)) {
            return false;
        }
        return time.isAfter(startTime) && time.isBefore(endTime);
    }
}
