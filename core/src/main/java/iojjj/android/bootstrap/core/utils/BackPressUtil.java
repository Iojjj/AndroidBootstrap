package iojjj.android.bootstrap.core.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * Created by Alexander Vlasov on 10.01.2016.
 */
public class BackPressUtil {

    private long timeout;
    private long prevCheck = -1;

    public BackPressUtil(long timeout) {
        this.timeout = timeout;
    }

    public BackPressUtil(long timeSpan, @NonNull TimeUnit timeUnit) {
        this(TimeUnit.MILLISECONDS.convert(timeSpan, timeUnit));
    }

    public boolean shouldBeFinished() {
        long timestamp = System.currentTimeMillis();
        if (prevCheck < 0 || timestamp - prevCheck > timeout) {
            prevCheck = timestamp;
            return false;
        }
        return true;
    }
}
