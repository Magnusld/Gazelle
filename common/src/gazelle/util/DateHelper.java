package gazelle.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateHelper {
    public static Date today() {
        return dateOfLocalDate(LocalDate.now());
    }

    public static Date dateOfLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate localDateOfDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
