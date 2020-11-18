package gazelle.util;

import java.util.Iterator;
import java.util.Optional;

public class FirstOf {
    public static <T> Optional<T> iterable(Iterable<T> iterable) {
        Iterator<T> it = iterable.iterator();
        if (it.hasNext())
            return Optional.of(it.next());
        return Optional.empty();
    }
}
