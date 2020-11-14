package gazelle.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateHelper {

    @Contract("null -> null")
    public static Date clone(@Nullable Date date) {
        if (date == null)
            return null;
        return new Date(date.getTime());
    }

    public static Date today() {
        return dateOfLocalDate(LocalDate.now());
    }

    @Contract("null -> null")
    public static Date dateOfLocalDate(@Nullable LocalDate localDate) {
        if (localDate == null)
            return null;
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    @Contract("null -> null")
    public static LocalDate localDateOfDate(@Nullable Date date) {
        if (date == null)
            return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
